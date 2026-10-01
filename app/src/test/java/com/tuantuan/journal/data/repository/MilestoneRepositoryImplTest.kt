package com.tuantuan.journal.data.repository

import com.tuantuan.journal.data.local.db.dao.MilestoneDao
import com.tuantuan.journal.data.local.db.entity.MilestoneEntity
import com.tuantuan.journal.domain.model.MilestoneCategory
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

class MilestoneRepositoryImplTest {
    private lateinit var dao: MilestoneDao
    private lateinit var repository: MilestoneRepositoryImpl

    private val testInstant = Instant.parse("2024-06-15T10:30:00Z")
    private val testDate = LocalDate.of(2024, 6, 15)

    private val testEntity = MilestoneEntity(
        id = "ms-1", childId = "child-1",
        category = MilestoneCategory.MOTOR, title = "第一次翻身",
        description = "从仰卧翻到俯卧", achievedDate = testDate,
        isExpected = false, expectedAgeMonths = null,
        notes = "在床上翻身", isDeleted = false,
        createdAt = testInstant, updatedAt = testInstant
    )

    private val testEntityPending = MilestoneEntity(
        id = "ms-2", childId = "child-1",
        category = MilestoneCategory.LANGUAGE, title = "第一次叫妈妈",
        description = null, achievedDate = null,
        isExpected = true, expectedAgeMonths = 12,
        notes = null, isDeleted = false,
        createdAt = testInstant, updatedAt = testInstant
    )

    @Before
    fun setup() {
        dao = mock()
        repository = MilestoneRepositoryImpl(dao)
    }

    @Test
    fun `getMilestonesByChild maps entities to domain models`() = runTest {
        whenever(dao.getActiveMilestonesByChild("child-1")).thenReturn(flowOf(listOf(testEntity, testEntityPending)))

        val result = repository.getMilestonesByChild("child-1").first()

        assertThat(result).hasSize(2)
        assertThat(result[0].id).isEqualTo("ms-1")
        assertThat(result[1].category).isEqualTo(MilestoneCategory.LANGUAGE)
    }

    @Test
    fun `getMilestonesByCategory filters by category`() = runTest {
        whenever(dao.getActiveMilestonesByCategory("child-1", MilestoneCategory.MOTOR)).thenReturn(flowOf(listOf(testEntity)))

        val result = repository.getMilestonesByCategory("child-1", MilestoneCategory.MOTOR).first()

        assertThat(result).hasSize(1)
        assertThat(result[0].category).isEqualTo(MilestoneCategory.MOTOR)
    }

    @Test
    fun `getAchievedMilestones returns only achieved`() = runTest {
        whenever(dao.getAchievedMilestones("child-1")).thenReturn(flowOf(listOf(testEntity)))

        val result = repository.getAchievedMilestones("child-1").first()

        assertThat(result).hasSize(1)
        assertThat(result[0].achievedDate).isNotNull()
    }

    @Test
    fun `getExpectedMilestones returns only expected`() = runTest {
        whenever(dao.getExpectedMilestones("child-1")).thenReturn(flowOf(listOf(testEntityPending)))

        val result = repository.getExpectedMilestones("child-1").first()

        assertThat(result).hasSize(1)
        assertThat(result[0].isExpected).isTrue()
    }

    @Test
    fun `getMilestoneById returns mapped domain model`() = runTest {
        whenever(dao.getMilestoneById("ms-1")).thenReturn(testEntity)

        val result = repository.getMilestoneById("ms-1")

        assertThat(result).isNotNull()
        assertThat(result!!.id).isEqualTo("ms-1")
        assertThat(result.title).isEqualTo("第一次翻身")
    }

    @Test
    fun `getMilestoneById returns null for non-existent`() = runTest {
        whenever(dao.getMilestoneById("non-existent")).thenReturn(null)

        val result = repository.getMilestoneById("non-existent")

        assertThat(result).isNull()
    }

    @Test
    fun `createMilestone inserts entity and returns id`() = runTest {
        val domain = MilestoneMapperTestHelper.toDomain(testEntity)

        val result = repository.createMilestone(domain)

        verify(dao).insert(org.mockito.kotlin.any())
        assertThat(result).isEqualTo("ms-1")
    }

    @Test
    fun `updateMilestone updates entity via dao`() = runTest {
        val domain = MilestoneMapperTestHelper.toDomain(testEntity)

        repository.updateMilestone(domain)

        verify(dao).update(org.mockito.kotlin.any())
    }

    @Test
    fun `deleteMilestone calls soft delete on dao`() = runTest {
        repository.deleteMilestone("ms-1")

        verify(dao).softDelete(org.mockito.kotlin.eq("ms-1"), org.mockito.kotlin.any())
    }

    private object MilestoneMapperTestHelper {
        fun toDomain(entity: MilestoneEntity) = com.tuantuan.journal.data.mapper.MilestoneMapper.toDomain(entity)
    }
}