package com.tuantuan.journal.ui.screen.diary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tuantuan.journal.domain.model.DiaryEntry
import com.tuantuan.journal.domain.model.Mood
import com.tuantuan.journal.domain.model.Tag
import com.tuantuan.journal.domain.model.Weather
import com.tuantuan.journal.domain.usecase.diary.DeleteDiaryEntryUseCase
import com.tuantuan.journal.domain.usecase.diary.GetDiaryEntryUseCase
import com.tuantuan.journal.domain.usecase.diary.GetDiaryEntriesUseCase
import com.tuantuan.journal.domain.usecase.diary.SaveDiaryEntryUseCase
import com.tuantuan.journal.domain.usecase.diary.SearchDiaryEntriesUseCase
import com.tuantuan.journal.domain.usecase.diary.ToggleFavoriteUseCase
import com.tuantuan.journal.domain.usecase.tag.GetTagsUseCase
import com.tuantuan.journal.domain.usecase.tag.ManageEntryTagUseCase
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
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class DiaryViewModel @Inject constructor(
    private val getDiaryEntriesUseCase: GetDiaryEntriesUseCase,
    private val getDiaryEntryUseCase: GetDiaryEntryUseCase,
    private val saveDiaryEntryUseCase: SaveDiaryEntryUseCase,
    private val searchDiaryEntriesUseCase: SearchDiaryEntriesUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val deleteDiaryEntryUseCase: DeleteDiaryEntryUseCase,
    private val getTagsUseCase: GetTagsUseCase,
    private val manageEntryTagUseCase: ManageEntryTagUseCase
) : ViewModel() {

    private val _listState = MutableStateFlow(DiaryListUiState())
    val listState: StateFlow<DiaryListUiState> = _listState.asStateFlow()

    private val _detailState = MutableStateFlow(DiaryDetailUiState())
    val detailState: StateFlow<DiaryDetailUiState> = _detailState.asStateFlow()

    private val _formState = MutableStateFlow(DiaryFormState())
    val formState: StateFlow<DiaryFormState> = _formState.asStateFlow()

    fun loadEntries(childId: String) {
        viewModelScope.launch {
            _listState.value = _listState.value.copy(isLoading = true)
            getDiaryEntriesUseCase(childId)
                .catch { e ->
                    _listState.value = _listState.value.copy(
                        isLoading = false,
                        error = e.toUiError()
                    )
                }
                .collect { entries ->
                    _listState.value = DiaryListUiState(
                        entries = entries,
                        isLoading = false
                    )
                }
        }
    }

    fun loadEntry(entryId: String) {
        viewModelScope.launch {
            _detailState.value = _detailState.value.copy(isLoading = true)
            try {
                val entry = getDiaryEntryUseCase(entryId)
                _detailState.value = DiaryDetailUiState(entry = entry, isLoading = false)
            } catch (e: Exception) {
                _detailState.value = _detailState.value.copy(
                    isLoading = false,
                    error = e.toUiError()
                )
            }
        }
    }

    fun searchEntries(childId: String, query: String) {
        viewModelScope.launch {
            _listState.value = _listState.value.copy(isLoading = true)
            searchDiaryEntriesUseCase(childId, query)
                .catch { e ->
                    _listState.value = _listState.value.copy(
                        isLoading = false,
                        error = e.toUiError()
                    )
                }
                .collect { entries ->
                    _listState.value = DiaryListUiState(
                        entries = entries,
                        isLoading = false
                    )
                }
        }
    }

    fun createEntry(childId: String) {
        val state = _formState.value
        if (state.content.isBlank()) {
            _formState.value = state.copy(error = UiError.ValidationError("请输入日记内容"))
            return
        }
        viewModelScope.launch {
            _formState.value = state.copy(isSaving = true)
            try {
                val now = Instant.now()
                val eventDateTime = try {
                    LocalDate.parse(state.eventDateTime)
                        .atStartOfDay(ZoneId.systemDefault())
                        .toInstant()
                } catch (_: Exception) {
                    now
                }
                val entry = DiaryEntry(
                    id = UUID.randomUUID().toString(),
                    childId = childId,
                    title = state.title.ifBlank { null },
                    content = state.content,
                    eventDateTime = eventDateTime,
                    mood = try { Mood.valueOf(state.mood) } catch (_: Exception) { null },
                    weather = try { Weather.valueOf(state.weather) } catch (_: Exception) { null },
                    location = state.location.ifBlank { null },
                    isFavorite = state.isFavorite,
                    createdAt = now,
                    updatedAt = now
                )
                saveDiaryEntryUseCase.create(entry)
                // 保存标签关联
                state.selectedTagIds.forEach { tagId ->
                    manageEntryTagUseCase.add(entry.id, tagId)
                }
                _formState.value = state.copy(isSaving = false, savedSuccessfully = true)
            } catch (e: Exception) {
                _formState.value = state.copy(isSaving = false, error = e.toUiError())
            }
        }
    }

    fun updateEntry(entryId: String) {
        val state = _formState.value
        viewModelScope.launch {
            _formState.value = state.copy(isSaving = true)
            try {
                val existing = getDiaryEntryUseCase(entryId) ?: return@launch
                val eventDateTime = try {
                    LocalDate.parse(state.eventDateTime)
                        .atStartOfDay(ZoneId.systemDefault())
                        .toInstant()
                } catch (_: Exception) {
                    existing.eventDateTime
                }
                val updated = existing.copy(
                    title = state.title.ifBlank { null },
                    content = state.content,
                    eventDateTime = eventDateTime,
                    mood = try { Mood.valueOf(state.mood) } catch (_: Exception) { null },
                    weather = try { Weather.valueOf(state.weather) } catch (_: Exception) { null },
                    location = state.location.ifBlank { null },
                    isFavorite = state.isFavorite,
                    updatedAt = Instant.now()
                )
                saveDiaryEntryUseCase.update(updated)
                // 更新标签关联：先清除旧的，再添加新的
                existing.tags.forEach { tag ->
                    manageEntryTagUseCase.remove(updated.id, tag.id)
                }
                state.selectedTagIds.forEach { tagId ->
                    manageEntryTagUseCase.add(updated.id, tagId)
                }
                _formState.value = state.copy(isSaving = false, savedSuccessfully = true)
            } catch (e: Exception) {
                _formState.value = state.copy(isSaving = false, error = e.toUiError())
            }
        }
    }

    fun deleteEntry(id: String) {
        viewModelScope.launch {
            deleteDiaryEntryUseCase(id)
        }
    }

    fun toggleFavorite(id: String, isFavorite: Boolean) {
        viewModelScope.launch {
            toggleFavoriteUseCase(id, isFavorite)
        }
    }

    fun updateFormState(update: (DiaryFormState) -> DiaryFormState) {
        _formState.value = update(_formState.value)
    }

    /** 加载所有可用标签到表单状态 */
    fun loadAvailableTags() {
        viewModelScope.launch {
            getTagsUseCase().catch { /* 静默失败 */ }.collect { tags ->
                _formState.value = _formState.value.copy(availableTags = tags)
            }
        }
    }

    /** 切换标签选中状态 */
    fun toggleTagSelection(tagId: String) {
        val current = _formState.value.selectedTagIds
        _formState.value = _formState.value.copy(
            selectedTagIds = if (tagId in current) current - tagId else current + tagId
        )
    }

    fun populateForm(entry: DiaryEntry) {
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        _formState.value = DiaryFormState(
            title = entry.title ?: "",
            content = entry.content,
            eventDateTime = dateFormatter.format(
                entry.eventDateTime.atZone(ZoneId.systemDefault()).toLocalDate()
            ),
            mood = entry.mood?.name ?: "",
            weather = entry.weather?.name ?: "",
            location = entry.location ?: "",
            isFavorite = entry.isFavorite,
            selectedTagIds = entry.tags.map { it.id }.toSet()
        )
    }

    fun clearError() {
        _formState.value = _formState.value.copy(error = null)
        _listState.value = _listState.value.copy(error = null)
        _detailState.value = _detailState.value.copy(error = null)
    }
}