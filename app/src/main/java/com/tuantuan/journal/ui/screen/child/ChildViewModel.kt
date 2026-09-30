package com.tuantuan.journal.ui.screen.child

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tuantuan.journal.domain.model.Child
import com.tuantuan.journal.domain.model.Gender
import com.tuantuan.journal.domain.usecase.child.DeleteChildUseCase
import com.tuantuan.journal.domain.usecase.child.GetChildByIdUseCase
import com.tuantuan.journal.domain.usecase.child.GetChildrenUseCase
import com.tuantuan.journal.domain.usecase.child.SaveChildUseCase
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
class ChildViewModel @Inject constructor(
    private val getChildrenUseCase: GetChildrenUseCase,
    private val getChildByIdUseCase: GetChildByIdUseCase,
    private val saveChildUseCase: SaveChildUseCase,
    private val deleteChildUseCase: DeleteChildUseCase
) : ViewModel() {

    private val _listState = MutableStateFlow(ChildListUiState())
    val listState: StateFlow<ChildListUiState> = _listState.asStateFlow()

    private val _detailState = MutableStateFlow(ChildDetailUiState())
    val detailState: StateFlow<ChildDetailUiState> = _detailState.asStateFlow()

    private val _formState = MutableStateFlow(ChildFormState())
    val formState: StateFlow<ChildFormState> = _formState.asStateFlow()

    init {
        loadChildren()
    }

    fun loadChildren() {
        viewModelScope.launch {
            _listState.value = _listState.value.copy(isLoading = true)
            getChildrenUseCase()
                .catch { e ->
                    _listState.value = _listState.value.copy(
                        isLoading = false,
                        error = e.toUiError()
                    )
                }
                .collect { children ->
                    _listState.value = ChildListUiState(
                        children = children,
                        isLoading = false
                    )
                }
        }
    }

    fun loadChild(childId: String) {
        viewModelScope.launch {
            _detailState.value = _detailState.value.copy(isLoading = true)
            try {
                val child = getChildByIdUseCase(childId)
                _detailState.value = ChildDetailUiState(child = child, isLoading = false)
            } catch (e: Exception) {
                _detailState.value = _detailState.value.copy(
                    isLoading = false,
                    error = e.toUiError()
                )
            }
        }
    }

    fun createChild() {
        val state = _formState.value
        if (state.name.isBlank()) {
            _formState.value = state.copy(error = UiError.ValidationError("请输入姓名"))
            return
        }
        viewModelScope.launch {
            _formState.value = state.copy(isSaving = true)
            try {
                val now = Instant.now()
                val birthDate = try {
                    LocalDate.parse(state.birthDate)
                } catch (_: Exception) {
                    LocalDate.now()
                }
                val child = Child(
                    id = UUID.randomUUID().toString(),
                    name = state.name,
                    nickname = state.nickname.ifBlank { state.name },
                    birthDate = birthDate,
                    gender = try { Gender.valueOf(state.gender) } catch (_: Exception) { null },
                    avatarPath = state.avatarPath,
                    birthWeight = state.birthWeight.toDoubleOrNull(),
                    birthHeight = state.birthHeight.toDoubleOrNull(),
                    bloodType = state.bloodType.ifBlank { null },
                    birthPlace = state.birthPlace.ifBlank { null },
                    notes = state.notes.ifBlank { null },
                    sortOrder = 0,
                    createdAt = now,
                    updatedAt = now
                )
                saveChildUseCase.create(child)
                _formState.value = state.copy(isSaving = false, savedSuccessfully = true)
            } catch (e: Exception) {
                _formState.value = state.copy(isSaving = false, error = e.toUiError())
            }
        }
    }

    fun updateChild(childId: String) {
        val state = _formState.value
        viewModelScope.launch {
            _formState.value = state.copy(isSaving = true)
            try {
                val existing = getChildByIdUseCase(childId) ?: return@launch
                val birthDate = try {
                    LocalDate.parse(state.birthDate)
                } catch (_: Exception) {
                    existing.birthDate
                }
                val updated = existing.copy(
                    name = state.name,
                    nickname = state.nickname.ifBlank { state.name },
                    birthDate = birthDate,
                    gender = try { Gender.valueOf(state.gender) } catch (_: Exception) { null },
                    avatarPath = state.avatarPath,
                    birthWeight = state.birthWeight.toDoubleOrNull(),
                    birthHeight = state.birthHeight.toDoubleOrNull(),
                    bloodType = state.bloodType.ifBlank { null },
                    birthPlace = state.birthPlace.ifBlank { null },
                    notes = state.notes.ifBlank { null },
                    updatedAt = Instant.now()
                )
                saveChildUseCase.update(updated)
                _formState.value = state.copy(isSaving = false, savedSuccessfully = true)
            } catch (e: Exception) {
                _formState.value = state.copy(isSaving = false, error = e.toUiError())
            }
        }
    }

    fun deleteChild(id: String) {
        viewModelScope.launch {
            deleteChildUseCase(id)
        }
    }

    fun updateFormState(update: (ChildFormState) -> ChildFormState) {
        _formState.value = update(_formState.value)
    }

    fun populateForm(child: Child) {
        _formState.value = ChildFormState(
            name = child.name,
            nickname = child.nickname,
            birthDate = child.birthDate.format(DateTimeFormatter.ISO_LOCAL_DATE),
            gender = child.gender?.name ?: "",
            avatarPath = child.avatarPath,
            birthWeight = child.birthWeight?.toString() ?: "",
            birthHeight = child.birthHeight?.toString() ?: "",
            bloodType = child.bloodType ?: "",
            birthPlace = child.birthPlace ?: "",
            notes = child.notes ?: ""
        )
    }

    fun clearError() {
        _formState.value = _formState.value.copy(error = null)
        _listState.value = _listState.value.copy(error = null)
        _detailState.value = _detailState.value.copy(error = null)
    }
}