package com.tuantuan.journal.ui.screen.vaccination

import com.tuantuan.journal.domain.model.Vaccination
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.time.Instant
import java.time.LocalDate

class VaccinationUiStateTest {

    private val testInstant = Instant.parse("2024-06-15T10:30:00Z")
    private val today = LocalDate.now()

    private val administeredVaccination = Vaccination(
        id = "vac-1", childId = "child-1",
        vaccineName = "乙肝疫苗", doseNumber = 1,
        administeredDate = today, scheduledDate = today.minusDays(1),
        location = null, batchNumber = null, notes = null,
        isDeleted = false, createdAt = testInstant, updatedAt = testInstant
    )

    private val pendingVaccination = Vaccination(
        id = "vac-2", childId = "child-1",
        vaccineName = "卡介苗", doseNumber = null,
        administeredDate = null, scheduledDate = today.plusDays(7),
        location = null, batchNumber = null, notes = null,
        isDeleted = false, createdAt = testInstant, updatedAt = testInstant
    )

    private val overdueVaccination = Vaccination(
        id = "vac-3", childId = "child-1",
        vaccineName = "脊灰疫苗", doseNumber = 1,
        administeredDate = null, scheduledDate = today.minusDays(3),
        location = null, batchNumber = null, notes = null,
        isDeleted = false, createdAt = testInstant, updatedAt = testInstant
    )

    @Test
    fun `administeredVaccinations returns only administered`() {
        val state = VaccinationUiState(vaccinations = listOf(administeredVaccination, pendingVaccination))

        assertThat(state.administeredVaccinations).hasSize(1)
        assertThat(state.administeredVaccinations[0].isAdministered).isTrue()
    }

    @Test
    fun `pendingVaccinations returns only pending`() {
        val state = VaccinationUiState(vaccinations = listOf(administeredVaccination, pendingVaccination, overdueVaccination))

        assertThat(state.pendingVaccinations).hasSize(2)
        assertThat(state.pendingVaccinations.all { it.isPending }).isTrue()
    }

    @Test
    fun `overdueVaccinations returns only overdue`() {
        val state = VaccinationUiState(vaccinations = listOf(administeredVaccination, pendingVaccination, overdueVaccination))

        assertThat(state.overdueVaccinations).hasSize(1)
        assertThat(state.overdueVaccinations[0].isOverdue).isTrue()
    }

    @Test
    fun `hasVaccinations is true when data exists`() {
        val state = VaccinationUiState(vaccinations = listOf(administeredVaccination))
        assertThat(state.hasVaccinations).isTrue()
    }

    @Test
    fun `hasVaccinations is false when empty`() {
        val state = VaccinationUiState(vaccinations = emptyList())
        assertThat(state.hasVaccinations).isFalse()
    }

    @Test
    fun `pendingCount includes overdue vaccinations`() {
        val state = VaccinationUiState(vaccinations = listOf(pendingVaccination, overdueVaccination))
        assertThat(state.pendingCount).isEqualTo(2)
    }

    @Test
    fun `overdueCount returns only overdue count`() {
        val state = VaccinationUiState(vaccinations = listOf(pendingVaccination, overdueVaccination))
        assertThat(state.overdueCount).isEqualTo(1)
    }

    @Test
    fun `VaccinationFormState isValid when vaccineName is not blank`() {
        val form = VaccinationFormState(vaccineName = "乙肝疫苗")
        assertThat(form.isValid).isTrue()
    }

    @Test
    fun `VaccinationFormState isInvalid when vaccineName is blank`() {
        val form = VaccinationFormState(vaccineName = "")
        assertThat(form.isValid).isFalse()
    }

    @Test
    fun `VaccinationFormState isAdministered when administeredDate is not blank`() {
        val form = VaccinationFormState(administeredDate = "2024-06-15")
        assertThat(form.isAdministered).isTrue()
    }
}