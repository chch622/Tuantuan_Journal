package com.tuantuan.journal.domain.usecase.vaccination

import com.tuantuan.journal.domain.model.Vaccination
import com.tuantuan.journal.domain.repository.VaccinationRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import com.google.common.truth.Truth.assertThat
import java.time.Instant
import java.time.LocalDate

class GetVaccinationsUseCaseTest {
    private lateinit var repository: VaccinationRepository
    private lateinit var useCase: GetVaccinationsUseCase

    private val testInstant = Instant.parse("2024-06-15T10:30:00Z")
    private val testDate = LocalDate.of(2024, 6, 15)

    private val administeredVaccination = Vaccination(
        id = "vac-1", childId = "child-1",
        vaccineName = "乙肝疫苗", doseNumber = 1,
        administeredDate = testDate, scheduledDate = testDate.minusDays(1),
        location = null, batchNumber = null, notes = null,
        isDeleted = false, createdAt = testInstant, updatedAt = testInstant
    )

    private val pendingVaccination = Vaccination(
        id = "vac-2", childId = "child-1",
        vaccineName = "卡介苗", doseNumber = null,
        administeredDate = null, scheduledDate = testDate.plusDays(7),
        location = null, batchNumber = null, notes = null,
        isDeleted = false, createdAt = testInstant, updatedAt = testInstant
    )

    @Before
    fun setup() {
        repository = mock()
        useCase = GetVaccinationsUseCase(repository)
    }

    @Test
    fun `invoke returns all vaccinations for child`() = runTest {
        whenever(repository.getVaccinationsByChild("child-1"))
            .thenReturn(flowOf(listOf(administeredVaccination, pendingVaccination)))

        val result = useCase("child-1").first()

        assertThat(result).hasSize(2)
    }

    @Test
    fun `administered returns only administered vaccinations`() = runTest {
        whenever(repository.getAdministeredVaccinations("child-1"))
            .thenReturn(flowOf(listOf(administeredVaccination)))

        val result = useCase.administered("child-1").first()

        assertThat(result).hasSize(1)
        assertThat(result[0].isAdministered).isTrue()
    }

    @Test
    fun `pending returns only pending vaccinations`() = runTest {
        whenever(repository.getPendingVaccinations("child-1"))
            .thenReturn(flowOf(listOf(pendingVaccination)))

        val result = useCase.pending("child-1").first()

        assertThat(result).hasSize(1)
        assertThat(result[0].isPending).isTrue()
    }

    @Test
    fun `overdue returns only overdue vaccinations`() = runTest {
        whenever(repository.getOverdueVaccinations("child-1"))
            .thenReturn(flowOf(listOf(pendingVaccination)))

        val result = useCase.overdue("child-1").first()

        assertThat(result).hasSize(1)
    }
}