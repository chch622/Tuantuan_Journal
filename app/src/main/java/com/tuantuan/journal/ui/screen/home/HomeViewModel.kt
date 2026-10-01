package com.tuantuan.journal.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tuantuan.journal.domain.usecase.child.GetChildrenUseCase
import com.tuantuan.journal.domain.usecase.diary.GetDiaryEntriesUseCase
import com.tuantuan.journal.ui.model.toUiError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getChildrenUseCase: GetChildrenUseCase,
    private val getDiaryEntriesUseCase: GetDiaryEntriesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(HomeUiState())
    val state: StateFlow<HomeUiState> = _state.asStateFlow()

    init {
        loadChildren()
    }

    fun loadChildren() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            getChildrenUseCase()
                .catch { e ->
                    _state.value = _state.value.copy(isLoading = false, error = e.toUiError())
                }
                .collect { children ->
                    val currentSelectedId = _state.value.selectedChildId
                    // 自动选中第一个儿童（如果没有选中的话）
                    val autoSelectedId = currentSelectedId
                        ?: children.firstOrNull()?.id

                    _state.value = _state.value.copy(
                        children = children,
                        selectedChildId = autoSelectedId,
                        isLoading = false
                    )

                    // 为每个儿童加载今日和最近日记
                    children.forEach { child ->
                        launch { loadTodayEntries(child.id) }
                        launch { loadRecentEntries(child.id) }
                    }
                }
        }
    }

    fun selectChild(childId: String) {
        _state.value = _state.value.copy(selectedChildId = childId)
    }

    private suspend fun loadTodayEntries(childId: String) {
        getDiaryEntriesUseCase.today(childId)
            .catch { /* ignore individual failures */ }
            .collect { entries ->
                val current = _state.value.todayEntries.toMutableMap()
                current[childId] = entries
                _state.value = _state.value.copy(todayEntries = current)
            }
    }

    private suspend fun loadRecentEntries(childId: String) {
        getDiaryEntriesUseCase.recent(childId, 5)
            .catch { /* ignore individual failures */ }
            .collect { entries ->
                val current = _state.value.recentEntries.toMutableMap()
                current[childId] = entries
                _state.value = _state.value.copy(recentEntries = current)
            }
    }
}