package com.tuantuan.journal.ui.screen.home

import com.tuantuan.journal.domain.model.Child
import com.tuantuan.journal.domain.model.DiaryEntry
import com.tuantuan.journal.ui.model.UiError

data class HomeUiState(
    val children: List<Child> = emptyList(),
    val recentEntries: Map<String, List<DiaryEntry>> = emptyMap(),
    val isLoading: Boolean = false,
    val error: UiError? = null
)