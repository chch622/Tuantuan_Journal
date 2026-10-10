package com.tuantuan.journal.data.repository

import com.tuantuan.journal.data.local.db.dao.VaccinationDao
import com.tuantuan.journal.data.local.db.entity.VaccinationEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import com.google.common.truth.Truth.assertThat
import java.time.Instant
import java.time.LocalDate

class VaccinationRepositoryImplTest {
    private lateinit var dao: VaccinationDao
    private lateinit var repository: VaccinationRepositoryImpl

    private val testInstant = Instant.parse("2024-06-15T10:30:00Z")
    private val testDate = LocalDate.of(2024, 6, 15)

    private val administeredEntity = VaccinationEntity(
        id = "vac-1", childId = "child-1",
        vaccineName = "乙肝疫苗", doseNumber = 1,
        administeredDate = testDate, scheduledDate = testDate.minusDays(1),
        location = "社区医院", batchNumber = null, notes = null,
        isDeleted = false, createdAt = testInstant, updatedAt = testInstant
    )

    private val pendingEntity = VaccinationEntity(
        id = "vac-2", childId = "child-1",
        vaccineName = "卡介苗", doseNumber = null,
        administeredDate = null, scheduledDate = testDate.plusDays(7),
        location = null, batchNumber = null, notes = null,
        isDeleted = false, createdAt = testInstant, updatedAt = testInstant
    )

    @Before
    fun setup() {
        dao = mock()
        repository = VaccinationRepositoryImpl(dao)
    }

    @Test
    fun `getVaccinationsByChild maps entities to domain models`() = runTest {
        whenever(dao.getActiveVaccinationsByChild("child-1"))
            .thenReturn(flowOf(listOf(administeredEntity, pendingEntity)))

        val result = repository.getVaccinationsByChild("child-1").first()

        assertThat(result).hasSize(2)
        assertThat(result[0].id).isEqualTo("vac-1")
        assertThat(result[1].vaccineName).isEqualTo("卡介苗")
    }

    @Test
    fun `getAdministeredVaccinations returns only administered`() = runTest {
        whenever(dao.getAdministeredVaccinations("child-1"))
            .thenReturn(flowOf(listOf(administeredEntity)))

        val result = repository.getAdministeredVaccinations("child-1").first()

        assertThat(result).hasSize(1)
        assertThat(result[0].administeredDate).isNotNull()
    }

    @Test
    fun `getPendingVaccinations returns only pending`() = runTest {
        whenever(dao.getPendingVaccinations("child-1"))
            .thenReturn(flowOf(listOf(pendingEntity)))

        val result = repository.getPendingVaccinations("child-1").first()

        assertThat(result).hasSize(1)
        assertThat(result[0].administeredDate).isNull()
    }

    @Test
    fun `getOverdueVaccinations passes today date to dao`() = runTest {
        whenever(dao.getOverdueVaccinations(org.mockito.kotlin.any(), org.mockito.kotlin.any()))
            .thenReturn(flowOf(emptyList()))

        repository.getOverdueVaccinations("child-1").first()

        verify(dao).getOverdueVaccinations(org.mockito.kotlin.eq("child-1"), org.mockito.kotlin.any())
    }

    @Test
    fun `getVaccinationById returns mapped domain model`() = runTest {
        whenever(dao.getVaccinationById("vac-1")).thenReturn(administeredEntity)

        val result = repository.getVaccinationById("vac-1")

        assertThat(result).isNotNull()
        assertThat(result!!.id).isEqualTo("vac-1")
        assertThat(result.vaccineName).isEqualTo("乙肝疫苗")
    }

    @Test
    fun `getVaccinationById returns null for non-existent`() = runTest {
        whenever(dao.getVaccinationById("non-existent")).thenReturn(null)

        val result = repository.getVaccinationById("non-existent")

        assertThat(result).isNull()
    }

    @Test
    fun `createVaccination inserts entity and returns id`() = runTest {
        val domain = com.tuantuan.journal.data.mapper.VaccinationMapper.toDomain(administeredEntity)

        val result = repository.createVaccination(domain)

        verify(dao).insert(org.mockito.kotlin.any())
        assertThat(result).isEqualTo("vac-1")
    }

    @Test
    fun `deleteVaccination calls softDelete on dao`() = runTest {
        repository.deleteVaccination("vac-1")

        verify(dao).softDelete(org.mockito.kotlin.eq("vac-1"), org.mockito.kotlin.any())
    }
}