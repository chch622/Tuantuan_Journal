package com.tuantuan.journal.ui.screen.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tuantuan.journal.domain.model.DiaryEntry
import com.tuantuan.journal.domain.usecase.diary.SearchDiaryEntriesUseCase
import com.tuantuan.journal.ui.model.UiError
import com.tuantuan.journal.ui.model.toUiError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SearchUiState(
    val query: String = "",
    val results: List<DiaryEntry> = emptyList(),
    val isSearching: Boolean = false,
    val error: UiError? = null
)

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchDiaryEntriesUseCase: SearchDiaryEntriesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(SearchUiState())
    val state: StateFlow<SearchUiState> = _state.asStateFlow()

    fun updateQuery(query: String) {
        _state.value = _state.value.copy(query = query)
    }

    fun search(childId: String, query: String) {
        if (query.isBlank()) {
            _state.value = _state.value.copy(results = emptyList(), isSearching = false)
            return
        }
        viewModelScope.launch {
            _state.value = _state.value.copy(isSearching = true)
            searchDiaryEntriesUseCase(childId, query)
                .catch { e ->
                    _state.value = _state.value.copy(isSearching = false, error = e.toUiError())
                }
                .collect { results ->
                    _state.value = _state.value.copy(
                        results = results,
                        isSearching = false
                    )
                }
        }
    }
}