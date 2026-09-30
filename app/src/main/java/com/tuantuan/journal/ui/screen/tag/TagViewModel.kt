package com.tuantuan.journal.ui.screen.tag

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tuantuan.journal.domain.model.Tag
import com.tuantuan.journal.domain.usecase.tag.GetTagsUseCase
import com.tuantuan.journal.domain.usecase.tag.SaveTagUseCase
import com.tuantuan.journal.ui.model.UiError
import com.tuantuan.journal.ui.model.toUiError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.UUID
import javax.inject.Inject

data class TagUiState(
    val tags: List<Tag> = emptyList(),
    val isLoading: Boolean = false,
    val error: UiError? = null
)

@HiltViewModel
class TagViewModel @Inject constructor(
    private val getTagsUseCase: GetTagsUseCase,
    private val saveTagUseCase: SaveTagUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(TagUiState())
    val state: StateFlow<TagUiState> = _state.asStateFlow()

    init {
        loadTags()
    }

    fun loadTags() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            getTagsUseCase()
                .catch { e ->
                    _state.value = _state.value.copy(isLoading = false, error = e.toUiError())
                }
                .collect { tags ->
                    _state.value = TagUiState(tags = tags, isLoading = false)
                }
        }
    }

    fun createTag(name: String, color: String? = null, category: String? = null) {
        viewModelScope.launch {
            val now = Instant.now()
            val tag = Tag(
                id = UUID.randomUUID().toString(),
                name = name,
                color = color,
                category = category,
                createdAt = now,
                updatedAt = now
            )
            saveTagUseCase.create(tag)
        }
    }
}