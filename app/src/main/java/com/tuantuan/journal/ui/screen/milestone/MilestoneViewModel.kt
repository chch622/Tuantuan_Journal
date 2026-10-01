package com.tuantuan.journal.ui.screen.milestone

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tuantuan.journal.domain.model.Milestone
import com.tuantuan.journal.domain.model.MilestoneCategory
import com.tuantuan.journal.R
import com.tuantuan.journal.domain.usecase.milestone.AchieveMilestoneUseCase
import com.tuantuan.journal.domain.usecase.milestone.DeleteMilestoneUseCase
import com.tuantuan.journal.domain.usecase.milestone.GetMilestonesUseCase
import com.tuantuan.journal.domain.usecase.milestone.SaveMilestoneUseCase
import com.tuantuan.journal.ui.model.UiError
import com.tuantuan.journal.ui.model.toUiError
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
class MilestoneViewModel @Inject constructor(
    private val getMilestonesUseCase: GetMilestonesUseCase,
    private val saveMilestoneUseCase: SaveMilestoneUseCase,
    private val achieveMilestoneUseCase: AchieveMilestoneUseCase,
    private val deleteMilestoneUseCase: DeleteMilestoneUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(MilestoneUiState())
    val state: StateFlow<MilestoneUiState> = _state.asStateFlow()

    private val _formState = MutableStateFlow(MilestoneFormState())
    val formState: StateFlow<MilestoneFormState> = _formState.asStateFlow()

    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    /** 加载指定儿童的里程碑 */
    fun loadMilestones(childId: String) {
        _state.value = _state.value.copy(selectedChildId = childId, isLoading = true)
        viewModelScope.launch {
            getMilestonesUseCase(childId)
                .catch { e ->
                    _state.value = _state.value.copy(isLoading = false, error = e.toUiError())
                }
                .collect { milestones ->
                    _state.value = _state.value.copy(
                        milestones = milestones,
                        isLoading = false
                    )
                }
        }
    }

    /** 切换分类筛选 */
    fun selectCategory(category: MilestoneCategory?) {
        _state.value = _state.value.copy(selectedCategory = category)
    }

    /** 达成里程碑 */
    fun achieveMilestone(milestoneId: String, date: LocalDate = LocalDate.now()) {
        viewModelScope.launch {
            achieveMilestoneUseCase(milestoneId, date)
        }
    }

    /** 保存里程碑 */
    fun saveMilestone(childId: String) {
        val form = _formState.value
        if (!form.isValid) {
            _formState.value = form.copy(error = UiError.ValidationError(R.string.error_validation_title))
            return
        }
        viewModelScope.launch {
            _formState.value = form.copy(isSaving = true)
            try {
                val now = Instant.now()
                val achievedDate = try {
                    LocalDate.parse(form.achievedDate)
                } catch (_: Exception) {
                    null
                }
                val category = try {
                    MilestoneCategory.valueOf(form.category)
                } catch (_: Exception) {
                    MilestoneCategory.OTHER
                }
                val milestone = Milestone(
                    id = UUID.randomUUID().toString(),
                    childId = childId,
                    category = category,
                    title = form.title,
                    description = form.description.ifBlank { null },
                    achievedDate = achievedDate,
                    isExpected = false,
                    expectedAgeMonths = null,
                    notes = form.notes.ifBlank { null },
                    createdAt = now,
                    updatedAt = now
                )
                saveMilestoneUseCase.create(milestone)
                _formState.value = form.copy(isSaving = false, savedSuccessfully = true)
            } catch (e: Exception) {
                _formState.value = form.copy(isSaving = false, error = e.toUiError())
            }
        }
    }

    /** 删除里程碑 */
    fun deleteMilestone(id: String) {
        viewModelScope.launch {
            deleteMilestoneUseCase(id)
        }
    }

    /** 更新表单状态 */
    fun updateFormState(update: (MilestoneFormState) -> MilestoneFormState) {
        _formState.value = update(_formState.value)
    }

    /** 重置表单 */
    fun resetForm() {
        _formState.value = MilestoneFormState(
            achievedDate = LocalDate.now().format(dateFormatter)
        )
    }

    fun clearError() {
        _state.value = _state.value.copy(error = null)
        _formState.value = _formState.value.copy(error = null)
    }
}