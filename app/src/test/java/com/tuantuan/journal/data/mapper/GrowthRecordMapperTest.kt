package com.tuantuan.journal.data.mapper

import com.tuantuan.journal.data.local.db.entity.GrowthRecordEntity
import com.tuantuan.journal.domain.model.GrowthRecord
import com.tuantuan.journal.domain.model.GrowthType
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.time.Instant
import java.time.LocalDate

class GrowthRecordMapperTest {

    private val testInstant = Instant.parse("2024-06-15T10:30:00Z")
    private val testDate = LocalDate.of(2024, 6, 15)

    private val testEntity = GrowthRecordEntity(
        id = "gr-1",
        childId = "child-1",
        recordType = GrowthType.HEIGHT,
        value = 75.5,
        unit = "cm",
        measureDate = testDate,
        notes = "早晨测量",
        isDeleted = false,
        createdAt = testInstant,
        updatedAt = testInstant
    )

    private val testDomain = GrowthRecord(
        id = "gr-1",
        childId = "child-1",
        recordType = GrowthType.HEIGHT,
        value = 75.5,
        unit = "cm",
        measureDate = testDate,
        notes = "早晨测量",
        isDeleted = false,
        createdAt = testInstant,
        updatedAt = testInstant
    )

    @Test
    fun `toDomain maps all fields correctly`() {
        val result = GrowthRecordMapper.toDomain(testEntity)

        assertThat(result.id).isEqualTo("gr-1")
        assertThat(result.childId).isEqualTo("child-1")
        assertThat(result.recordType).isEqualTo(GrowthType.HEIGHT)
        assertThat(result.value).isEqualTo(75.5)
        assertThat(result.unit).isEqualTo("cm")
        assertThat(result.measureDate).isEqualTo(testDate)
        assertThat(result.notes).isEqualTo("早晨测量")
        assertThat(result.isDeleted).isFalse()
        assertThat(result.createdAt).isEqualTo(testInstant)
        assertThat(result.updatedAt).isEqualTo(testInstant)
    }

    @Test
    fun `toEntity maps all fields correctly`() {
        val result = GrowthRecordMapper.toEntity(testDomain)

        assertThat(result.id).isEqualTo("gr-1")
        assertThat(result.childId).isEqualTo("child-1")
        assertThat(result.recordType).isEqualTo(GrowthType.HEIGHT)
        assertThat(result.value).isEqualTo(75.5)
        assertThat(result.unit).isEqualTo("cm")
        assertThat(result.measureDate).isEqualTo(testDate)
        assertThat(result.notes).isEqualTo("早晨测量")
        assertThat(result.isDeleted).isFalse()
        assertThat(result.createdAt).isEqualTo(testInstant)
        assertThat(result.updatedAt).isEqualTo(testInstant)
    }

    @Test
    fun `toDomain and toEntity are inverse operations`() {
        val result = GrowthRecordMapper.toDomain(GrowthRecordMapper.toEntity(testDomain))
        assertThat(result).isEqualTo(testDomain)
    }

    @Test
    fun `toDomain handles null notes`() {
        val entity = testEntity.copy(notes = null)
        val result = GrowthRecordMapper.toDomain(entity)
        assertThat(result.notes).isNull()
    }

    @Test
    fun `toDomain handles different growth types`() {
        GrowthType.entries.forEach { type ->
            val entity = testEntity.copy(recordType = type)
            val result = GrowthRecordMapper.toDomain(entity)
            assertThat(result.recordType).isEqualTo(type)
        }
    }
}