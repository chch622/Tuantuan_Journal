package com.tuantuan.journal.domain.usecase.vaccination

import com.tuantuan.journal.domain.model.Vaccination
import com.tuantuan.journal.domain.repository.VaccinationRepository
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import com.google.common.truth.Truth.assertThat
import java.time.Instant
import java.time.LocalDate

class SaveVaccinationUseCaseTest {
    private lateinit var repository: VaccinationRepository
    private lateinit var useCase: SaveVaccinationUseCase

    private val testInstant = Instant.parse("2024-06-15T10:30:00Z")
    private val testDate = LocalDate.of(2024, 6, 15)

    private val testVaccination = Vaccination(
        id = "vac-1", childId = "child-1",
        vaccineName = "乙肝疫苗", doseNumber = 1,
        administeredDate = testDate, scheduledDate = testDate.minusDays(1),
        location = null, batchNumber = null, notes = null,
        isDeleted = false, createdAt = testInstant, updatedAt = testInstant
    )

    @Before
    fun setup() {
        repository = mock()
        useCase = SaveVaccinationUseCase(repository)
    }

    @Test
    fun `create delegates to repository and returns id`() = runTest {
        whenever(repository.createVaccination(testVaccination)).thenReturn("vac-1")

        val result = useCase.create(testVaccination)

        verify(repository).createVaccination(testVaccination)
        assertThat(result).isEqualTo("vac-1")
    }

    @Test
    fun `update delegates to repository`() = runTest {
        useCase.update(testVaccination)

        verify(repository).updateVaccination(testVaccination)
    }
}