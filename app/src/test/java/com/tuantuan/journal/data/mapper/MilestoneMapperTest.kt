package com.tuantuan.journal.data.mapper

import com.tuantuan.journal.data.local.db.entity.MilestoneEntity
import com.tuantuan.journal.domain.model.Milestone
import com.tuantuan.journal.domain.model.MilestoneCategory
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.time.Instant
import java.time.LocalDate

class MilestoneMapperTest {

    private val testInstant = Instant.parse("2024-06-15T10:30:00Z")
    private val testDate = LocalDate.of(2024, 6, 15)

    private val testEntity = MilestoneEntity(
        id = "ms-1",
        childId = "child-1",
        category = MilestoneCategory.MOTOR,
        title = "第一次翻身",
        description = "从仰卧翻到俯卧",
        achievedDate = testDate,
        isExpected = false,
        expectedAgeMonths = null,
        notes = "在床上翻身",
        isDeleted = false,
        createdAt = testInstant,
        updatedAt = testInstant
    )

    private val testDomain = Milestone(
        id = "ms-1",
        childId = "child-1",
        category = MilestoneCategory.MOTOR,
        title = "第一次翻身",
        description = "从仰卧翻到俯卧",
        achievedDate = testDate,
        isExpected = false,
        expectedAgeMonths = null,
        notes = "在床上翻身",
        isDeleted = false,
        createdAt = testInstant,
        updatedAt = testInstant
    )

    @Test
    fun `toDomain maps all fields correctly`() {
        val result = MilestoneMapper.toDomain(testEntity)

        assertThat(result.id).isEqualTo("ms-1")
        assertThat(result.childId).isEqualTo("child-1")
        assertThat(result.category).isEqualTo(MilestoneCategory.MOTOR)
        assertThat(result.title).isEqualTo("第一次翻身")
        assertThat(result.description).isEqualTo("从仰卧翻到俯卧")
        assertThat(result.achievedDate).isEqualTo(testDate)
        assertThat(result.isExpected).isFalse()
        assertThat(result.expectedAgeMonths).isNull()
        assertThat(result.notes).isEqualTo("在床上翻身")
    }

    @Test
    fun `toEntity maps all fields correctly`() {
        val result = MilestoneMapper.toEntity(testDomain)

        assertThat(result.id).isEqualTo("ms-1")
        assertThat(result.category).isEqualTo(MilestoneCategory.MOTOR)
        assertThat(result.title).isEqualTo("第一次翻身")
        assertThat(result.achievedDate).isEqualTo(testDate)
    }

    @Test
    fun `toDomain and toEntity are inverse operations`() {
        val result = MilestoneMapper.toDomain(MilestoneMapper.toEntity(testDomain))
        assertThat(result).isEqualTo(testDomain)
    }

    @Test
    fun `toDomain handles null achievedDate`() {
        val entity = testEntity.copy(achievedDate = null)
        val result = MilestoneMapper.toDomain(entity)
        assertThat(result.achievedDate).isNull()
    }

    @Test
    fun `toDomain handles null description and notes`() {
        val entity = testEntity.copy(description = null, notes = null)
        val result = MilestoneMapper.toDomain(entity)
        assertThat(result.description).isNull()
        assertThat(result.notes).isNull()
    }

    @Test
    fun `toDomain handles expected milestone`() {
        val entity = testEntity.copy(isExpected = true, expectedAgeMonths = 6, achievedDate = null)
        val result = MilestoneMapper.toDomain(entity)
        assertThat(result.isExpected).isTrue()
        assertThat(result.expectedAgeMonths).isEqualTo(6)
    }

    @Test
    fun `toDomain handles all milestone categories`() {
        MilestoneCategory.entries.forEach { category ->
            val entity = testEntity.copy(category = category)
            val result = MilestoneMapper.toDomain(entity)
            assertThat(result.category).isEqualTo(category)
        }
    }
}