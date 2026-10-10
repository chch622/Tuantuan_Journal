package com.tuantuan.journal.ui.screen.vaccination

import com.tuantuan.journal.domain.model.Vaccination
import com.tuantuan.journal.ui.model.UiError

data class VaccinationUiState(
    val isLoading: Boolean = false,
    val error: UiError? = null,
    val selectedChildId: String? = null,
    val vaccinations: List<Vaccination> = emptyList(),
    val showAddSheet: Boolean = false
) {
    /** 已接种记录 */
    val administeredVaccinations: List<Vaccination>
        get() = vaccinations.filter { it.isAdministered }

    /** 待接种记录 */
    val pendingVaccinations: List<Vaccination>
        get() = vaccinations.filter { it.isPending }

    /** 逾期未接种记录 */
    val overdueVaccinations: List<Vaccination>
        get() = vaccinations.filter { it.isOverdue }

    /** 是否有任何疫苗数据 */
    val hasVaccinations: Boolean
        get() = vaccinations.isNotEmpty()

    /** 待接种数量（含逾期） */
    val pendingCount: Int
        get() = vaccinations.count { it.isPending }

    /** 逾期数量 */
    val overdueCount: Int
        get() = vaccinations.count { it.isOverdue }
}

data class VaccinationFormState(
    val vaccineName: String = "",
    val doseNumber: String = "",
    val scheduledDate: String = "",
    val administeredDate: String = "",
    val location: String = "",
    val batchNumber: String = "",
    val notes: String = "",
    val isSaving: Boolean = false,
    val savedSuccessfully: Boolean = false,
    val error: UiError? = null
) {
    /** 疫苗名称是否有效 */
    val isValid: Boolean
        get() = vaccineName.isNotBlank()

    /** 是否为已接种记录（有接种日期） */
    val isAdministered: Boolean
        get() = administeredDate.isNotBlank()
}