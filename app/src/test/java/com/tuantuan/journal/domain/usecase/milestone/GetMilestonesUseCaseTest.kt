package com.tuantuan.journal.domain.usecase.milestone

import com.tuantuan.journal.domain.model.Milestone
import com.tuantuan.journal.domain.model.MilestoneCategory
import com.tuantuan.journal.domain.repository.MilestoneRepository
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

class GetMilestonesUseCaseTest {
    private lateinit var repository: MilestoneRepository
    private lateinit var useCase: GetMilestonesUseCase

    private val testInstant = Instant.parse("2024-06-15T10:30:00Z")
    private val testDate = LocalDate.of(2024, 6, 15)

    private val testMilestones = listOf(
        Milestone(
            id = "ms-1", childId = "child-1",
            category = MilestoneCategory.MOTOR, title = "第一次翻身",
            description = null, achievedDate = testDate,
            isExpected = false, expectedAgeMonths = null,
            notes = null, isDeleted = false,
            createdAt = testInstant, updatedAt = testInstant
        ),
        Milestone(
            id = "ms-2", childId = "child-1",
            category = MilestoneCategory.LANGUAGE, title = "第一次叫妈妈",
            description = null, achievedDate = null,
            isExpected = true, expectedAgeMonths = 12,
            notes = null, isDeleted = false,
            createdAt = testInstant, updatedAt = testInstant
        )
    )

    @Before
    fun setup() {
        repository = mock()
        useCase = GetMilestonesUseCase(repository)
    }

    @Test
    fun `invoke returns all milestones for child`() = runTest {
        whenever(repository.getMilestonesByChild("child-1")).thenReturn(flowOf(testMilestones))

        val result = useCase("child-1").first()

        assertThat(result).hasSize(2)
        assertThat(result).isEqualTo(testMilestones)
    }

    @Test
    fun `byCategory returns milestones filtered by category`() = runTest {
        val motorMilestones = testMilestones.filter { it.category == MilestoneCategory.MOTOR }
        whenever(repository.getMilestonesByCategory("child-1", MilestoneCategory.MOTOR)).thenReturn(flowOf(motorMilestones))

        val result = useCase.byCategory("child-1", MilestoneCategory.MOTOR).first()

        assertThat(result).hasSize(1)
        assertThat(result[0].category).isEqualTo(MilestoneCategory.MOTOR)
    }

    @Test
    fun `achieved returns only achieved milestones`() = runTest {
        val achieved = testMilestones.filter { it.achievedDate != null }
        whenever(repository.getAchievedMilestones("child-1")).thenReturn(flowOf(achieved))

        val result = useCase.achieved("child-1").first()

        assertThat(result).hasSize(1)
        assertThat(result[0].achievedDate).isNotNull()
    }

    @Test
    fun `expected returns only expected milestones`() = runTest {
        val expected = testMilestones.filter { it.isExpected }
        whenever(repository.getExpectedMilestones("child-1")).thenReturn(flowOf(expected))

        val result = useCase.expected("child-1").first()

        assertThat(result).hasSize(1)
        assertThat(result[0].isExpected).isTrue()
    }

    @Test
    fun `invoke returns empty list when no milestones exist`() = runTest {
        whenever(repository.getMilestonesByChild("child-1")).thenReturn(flowOf(emptyList()))

        val result = useCase("child-1").first()

        assertThat(result).isEmpty()
    }
}