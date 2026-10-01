package com.tuantuan.journal.ui.screen.growth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tuantuan.journal.R
import com.tuantuan.journal.domain.model.GrowthRecord
import com.tuantuan.journal.domain.model.GrowthType
import com.tuantuan.journal.domain.model.Milestone
import com.tuantuan.journal.domain.usecase.child.GetChildrenUseCase
import com.tuantuan.journal.domain.usecase.growth.DeleteGrowthRecordUseCase
import com.tuantuan.journal.domain.usecase.growth.GetGrowthRecordsUseCase
import com.tuantuan.journal.domain.usecase.growth.SaveGrowthRecordUseCase
import com.tuantuan.journal.domain.usecase.milestone.AchieveMilestoneUseCase
import com.tuantuan.journal.domain.usecase.milestone.GetMilestonesUseCase
import com.tuantuan.journal.ui.model.UiError
import com.tuantuan.journal.ui.model.toUiError
import com.tuantuan.journal.ui.screen.milestone.MilestoneFormState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class GrowthViewModel @Inject constructor(
    private val getChildrenUseCase: GetChildrenUseCase,
    private val getGrowthRecordsUseCase: GetGrowthRecordsUseCase,
    private val saveGrowthRecordUseCase: SaveGrowthRecordUseCase,
    private val deleteGrowthRecordUseCase: DeleteGrowthRecordUseCase,
    private val getMilestonesUseCase: GetMilestonesUseCase,
    private val achieveMilestoneUseCase: AchieveMilestoneUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(GrowthUiState())
    val state: StateFlow<GrowthUiState> = _state.asStateFlow()

    private val _formState = MutableStateFlow(GrowthFormState())
    val formState: StateFlow<GrowthFormState> = _formState.asStateFlow()

    private val _milestoneFormState = MutableStateFlow(MilestoneFormState())
    val milestoneFormState: StateFlow<MilestoneFormState> = _milestoneFormState.asStateFlow()

    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    init {
        loadChildren()
    }

    /** 加载儿童列表并自动选中第一个 */
    fun loadChildren() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            getChildrenUseCase()
                .catch { e -> handleError(e) }
                .collect { children ->
                    val currentSelectedId = _state.value.selectedChildId
                    val autoSelectedId = currentSelectedId ?: children.firstOrNull()?.id
                    _state.value = _state.value.copy(
                        children = children,
                        selectedChildId = autoSelectedId,
                        isLoading = false
                    )
                    autoSelectedId?.let { loadGrowthData(it) }
                }
        }
    }

    /** 选择儿童 */
    fun selectChild(childId: String) {
        _state.value = _state.value.copy(selectedChildId = childId)
        loadGrowthData(childId)
    }

    /** 加载指定儿童的成长数据 */
    fun loadGrowthData(childId: String) {
        GrowthType.entries.forEach { type ->
            viewModelScope.launch {
                getGrowthRecordsUseCase.byType(childId, type)
                    .catch { /* 单类型加载失败不阻塞 */ }
                    .collect { records ->
                        val current = _state.value.recordsByType.toMutableMap()
                        current[type] = records
                        _state.value = _state.value.copy(recordsByType = current)
                    }
            }
        }
        loadMilestones(childId)
    }

    /** 加载里程碑 */
    private fun loadMilestones(childId: String) {
        viewModelScope.launch {
            getMilestonesUseCase(childId)
                .catch { /* 里程碑加载失败不阻塞主流程 */ }
                .collect { milestones ->
                    _state.value = _state.value.copy(
                        recentMilestones = milestones.take(GrowthUiState.MILESTONE_PREVIEW_COUNT)
                    )
                }
        }
    }

    /** 展开/收起成长卡片 */
    fun toggleCardExpansion(type: GrowthType) {
        val current = _state.value.expandedCardType
        _state.value = _state.value.copy(
            expandedCardType = if (current == type) null else type
        )
    }

    /** 显示/隐藏添加记录 BottomSheet */
    fun showAddSheet(show: Boolean) {
        _state.value = _state.value.copy(showAddSheet = show)
    }

    /** 保存成长记录 */
    fun saveRecord(childId: String) {
        val form = _formState.value
        if (!form.isValid) {
            _formState.value = form.copy(error = UiError.ValidationError(R.string.error_validation_value))
            return
        }
        viewModelScope.launch {
            _formState.value = form.copy(isSaving = true)
            try {
                val now = Instant.now()
                val measureDate = try {
                    LocalDate.parse(form.measureDate)
                } catch (_: Exception) {
                    LocalDate.now()
                }
                val record = GrowthRecord(
                    id = UUID.randomUUID().toString(),
                    childId = childId,
                    recordType = form.recordType,
                    value = form.value.toDouble(),
                    unit = form.unit,
                    measureDate = measureDate,
                    notes = form.notes.ifBlank { null },
                    createdAt = now,
                    updatedAt = now
                )
                saveGrowthRecordUseCase.create(record)
                _formState.value = form.copy(isSaving = false, savedSuccessfully = true)
                showAddSheet(false)
            } catch (e: Exception) {
                _formState.value = form.copy(isSaving = false, error = e.toUiError())
            }
        }
    }

    /** 删除成长记录 */
    fun deleteRecord(id: String) {
        viewModelScope.launch {
            deleteGrowthRecordUseCase(id)
        }
    }

    /** 达成里程碑 */
    fun achieveMilestone(milestoneId: String, date: LocalDate = LocalDate.now()) {
        viewModelScope.launch {
            achieveMilestoneUseCase(milestoneId, date)
        }
    }

    /** 更新表单状态 */
    fun updateFormState(update: (GrowthFormState) -> GrowthFormState) {
        _formState.value = update(_formState.value)
    }

    /** 更新里程碑表单状态 */
    fun updateMilestoneFormState(update: (MilestoneFormState) -> MilestoneFormState) {
        _milestoneFormState.value = update(_milestoneFormState.value)
    }

    /** 重置成长记录表单 */
    fun resetForm(type: GrowthType) {
        _formState.value = GrowthFormState(
            recordType = type,
            unit = type.defaultUnit,
            measureDate = LocalDate.now().format(dateFormatter)
        )
    }

    /** 重置里程碑表单 */
    fun resetMilestoneForm() {
        _milestoneFormState.value = MilestoneFormState(
            achievedDate = LocalDate.now().format(dateFormatter)
        )
    }

    fun clearError() {
        _state.value = _state.value.copy(error = null)
        _formState.value = _formState.value.copy(error = null)
        _milestoneFormState.value = _milestoneFormState.value.copy(error = null)
    }

    private fun handleError(e: Throwable) {
        _state.value = _state.value.copy(isLoading = false, error = e.toUiError())
    }
}

/** GrowthType 默认单位映射 */
private val GrowthType.defaultUnit: String
    get() = when (this) {
        GrowthType.HEIGHT -> "cm"
        GrowthType.WEIGHT -> "kg"
        GrowthType.HEAD_CIRCUMFERENCE -> "cm"
        GrowthType.SHOE_SIZE -> "码"
    }