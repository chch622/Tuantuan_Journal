package com.tuantuan.journal.data.mapper

import com.tuantuan.journal.data.local.db.entity.VaccinationEntity
import com.tuantuan.journal.domain.model.Vaccination
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.time.Instant
import java.time.LocalDate

class VaccinationMapperTest {

    private val testInstant = Instant.parse("2024-06-15T10:30:00Z")
    private val testDate = LocalDate.of(2024, 6, 15)

    private val testEntity = VaccinationEntity(
        id = "vac-1",
        childId = "child-1",
        vaccineName = "乙肝疫苗",
        doseNumber = 1,
        administeredDate = testDate,
        scheduledDate = testDate.minusDays(1),
        location = "社区医院",
        batchNumber = "B20240601",
        notes = "第一针",
        isDeleted = false,
        createdAt = testInstant,
        updatedAt = testInstant
    )

    private val testDomain = Vaccination(
        id = "vac-1",
        childId = "child-1",
        vaccineName = "乙肝疫苗",
        doseNumber = 1,
        administeredDate = testDate,
        scheduledDate = testDate.minusDays(1),
        location = "社区医院",
        batchNumber = "B20240601",
        notes = "第一针",
        isDeleted = false,
        createdAt = testInstant,
        updatedAt = testInstant
    )

    @Test
    fun `toDomain maps all fields correctly`() {
        val result = VaccinationMapper.toDomain(testEntity)

        assertThat(result.id).isEqualTo("vac-1")
        assertThat(result.childId).isEqualTo("child-1")
        assertThat(result.vaccineName).isEqualTo("乙肝疫苗")
        assertThat(result.doseNumber).isEqualTo(1)
        assertThat(result.administeredDate).isEqualTo(testDate)
        assertThat(result.scheduledDate).isEqualTo(testDate.minusDays(1))
        assertThat(result.location).isEqualTo("社区医院")
        assertThat(result.batchNumber).isEqualTo("B20240601")
        assertThat(result.notes).isEqualTo("第一针")
    }

    @Test
    fun `toEntity maps all fields correctly`() {
        val result = VaccinationMapper.toEntity(testDomain)

        assertThat(result.id).isEqualTo("vac-1")
        assertThat(result.vaccineName).isEqualTo("乙肝疫苗")
        assertThat(result.doseNumber).isEqualTo(1)
        assertThat(result.administeredDate).isEqualTo(testDate)
    }

    @Test
    fun `toDomain and toEntity are inverse operations`() {
        val result = VaccinationMapper.toDomain(VaccinationMapper.toEntity(testDomain))
        assertThat(result).isEqualTo(testDomain)
    }

    @Test
    fun `toDomain handles pending vaccination with null administeredDate`() {
        val entity = testEntity.copy(administeredDate = null)
        val result = VaccinationMapper.toDomain(entity)
        assertThat(result.administeredDate).isNull()
        assertThat(result.isAdministered).isFalse()
        assertThat(result.isPending).isTrue()
    }

    @Test
    fun `toDomain handles null optional fields`() {
        val entity = testEntity.copy(
            doseNumber = null,
            location = null,
            batchNumber = null,
            notes = null
        )
        val result = VaccinationMapper.toDomain(entity)
        assertThat(result.doseNumber).isNull()
        assertThat(result.location).isNull()
        assertThat(result.batchNumber).isNull()
        assertThat(result.notes).isNull()
    }
}