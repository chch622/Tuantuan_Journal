package com.tuantuan.journal.ui.screen.child

import com.tuantuan.journal.domain.model.Child
import com.tuantuan.journal.ui.model.UiError

data class ChildListUiState(
    val children: List<Child> = emptyList(),
    val isLoading: Boolean = false,
    val error: UiError? = null
)

data class ChildDetailUiState(
    val child: Child? = null,
    val isLoading: Boolean = false,
    val error: UiError? = null
)

data class ChildFormState(
    val name: String = "",
    val nickname: String = "",
    val birthDate: String = "",
    val gender: String = "",
    val avatarPath: String? = null,
    val birthWeight: String = "",
    val birthHeight: String = "",
    val bloodType: String = "",
    val birthPlace: String = "",
    val notes: String = "",
    val isSaving: Boolean = false,
    val savedSuccessfully: Boolean = false,
    val error: UiError? = null
)