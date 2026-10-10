package com.tuantuan.journal.ui.screen.vaccination

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tuantuan.journal.R
import com.tuantuan.journal.domain.model.Vaccination
import com.tuantuan.journal.domain.usecase.vaccination.DeleteVaccinationUseCase
import com.tuantuan.journal.domain.usecase.vaccination.GetVaccinationsUseCase
import com.tuantuan.journal.domain.usecase.vaccination.SaveVaccinationUseCase
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
class VaccinationViewModel @Inject constructor(
    private val getVaccinationsUseCase: GetVaccinationsUseCase,
    private val saveVaccinationUseCase: SaveVaccinationUseCase,
    private val deleteVaccinationUseCase: DeleteVaccinationUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(VaccinationUiState())
    val state: StateFlow<VaccinationUiState> = _state.asStateFlow()

    private val _formState = MutableStateFlow(VaccinationFormState())
    val formState: StateFlow<VaccinationFormState> = _formState.asStateFlow()

    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    /** 加载指定儿童的疫苗记录 */
    fun loadVaccinations(childId: String) {
        _state.value = _state.value.copy(selectedChildId = childId, isLoading = true)
        viewModelScope.launch {
            getVaccinationsUseCase(childId)
                .catch { e ->
                    _state.value = _state.value.copy(isLoading = false, error = e.toUiError())
                }
                .collect { vaccinations ->
                    _state.value = _state.value.copy(
                        vaccinations = vaccinations,
                        isLoading = false
                    )
                }
        }
    }

    /** 显示/隐藏添加记录 BottomSheet */
    fun showAddSheet(show: Boolean) {
        _state.value = _state.value.copy(showAddSheet = show)
    }

    /** 保存疫苗记录 */
    fun saveVaccination(childId: String) {
        val form = _formState.value
        if (!form.isValid) {
            _formState.value = form.copy(error = UiError.ValidationError(R.string.error_validation_title))
            return
        }
        viewModelScope.launch {
            _formState.value = form.copy(isSaving = true)
            try {
                val now = Instant.now()
                val scheduledDate = parseDate(form.scheduledDate)
                val administeredDate = parseDate(form.administeredDate)
                val doseNum = form.doseNumber.toIntOrNull()

                val vaccination = Vaccination(
                    id = UUID.randomUUID().toString(),
                    childId = childId,
                    vaccineName = form.vaccineName.trim(),
                    doseNumber = doseNum,
                    administeredDate = administeredDate,
                    scheduledDate = scheduledDate,
                    location = form.location.ifBlank { null },
                    batchNumber = form.batchNumber.ifBlank { null },
                    notes = form.notes.ifBlank { null },
                    createdAt = now,
                    updatedAt = now
                )
                saveVaccinationUseCase.create(vaccination)
                _formState.value = form.copy(isSaving = false, savedSuccessfully = true)
                showAddSheet(false)
            } catch (e: Exception) {
                _formState.value = form.copy(isSaving = false, error = e.toUiError())
            }
        }
    }

    /** 标记为已接种 */
    fun markAdministered(vaccinationId: String, date: LocalDate = LocalDate.now()) {
        viewModelScope.launch {
            val vaccination = _state.value.vaccinations.find { it.id == vaccinationId } ?: return@launch
            val updated = vaccination.copy(
                administeredDate = date,
                updatedAt = Instant.now()
            )
            saveVaccinationUseCase.update(updated)
        }
    }

    /** 删除疫苗记录 */
    fun deleteVaccination(id: String) {
        viewModelScope.launch {
            deleteVaccinationUseCase(id)
        }
    }

    /** 更新表单状态 */
    fun updateFormState(update: (VaccinationFormState) -> VaccinationFormState) {
        _formState.value = update(_formState.value)
    }

    /** 重置表单 */
    fun resetForm() {
        _formState.value = VaccinationFormState(
            scheduledDate = LocalDate.now().format(dateFormatter)
        )
    }

    fun clearError() {
        _state.value = _state.value.copy(error = null)
        _formState.value = _formState.value.copy(error = null)
    }

    private fun parseDate(dateStr: String): LocalDate? {
        return try {
            LocalDate.parse(dateStr)
        } catch (_: Exception) {
            null
        }
    }
}