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
                    _state.value = _state.value.copy(
                        children = children,
                        isLoading = false
                    )
                    // Load recent entries for each child
                    children.forEach { child ->
                        launch {
                            getDiaryEntriesUseCase.recent(child.id, 5)
                                .catch { /* ignore individual failures */ }
                                .collect { entries ->
                                    val current = _state.value.recentEntries.toMutableMap()
                                    current[child.id] = entries
                                    _state.value = _state.value.copy(recentEntries = current)
                                }
                        }
                    }
                }
        }
    }
}