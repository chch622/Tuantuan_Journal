package com.tuantuan.journal.ui.screen.diary

import com.tuantuan.journal.domain.model.DiaryEntry
import com.tuantuan.journal.domain.model.Tag
import com.tuantuan.journal.ui.model.UiError

data class DiaryListUiState(
    val entries: List<DiaryEntry> = emptyList(),
    val isLoading: Boolean = false,
    val error: UiError? = null
)

data class DiaryDetailUiState(
    val entry: DiaryEntry? = null,
    val isLoading: Boolean = false,
    val error: UiError? = null
)

data class DiaryFormState(
    val title: String = "",
    val content: String = "",
    val eventDateTime: String = "",
    val mood: String = "",
    val weather: String = "",
    val location: String = "",
    val isFavorite: Boolean = false,
    val selectedTagIds: Set<String> = emptySet(),
    val availableTags: List<Tag> = emptyList(),
    val isSaving: Boolean = false,
    val savedSuccessfully: Boolean = false,
    val error: UiError? = null
)