package com.tuantuan.journal.domain.usecase.milestone

import com.tuantuan.journal.domain.model.Milestone
import com.tuantuan.journal.domain.model.MilestoneCategory
import com.tuantuan.journal.domain.repository.MilestoneRepository
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import com.google.common.truth.Truth.assertThat
import java.time.Instant
import java.time.LocalDate

class AchieveMilestoneUseCaseTest {
    private lateinit var repository: MilestoneRepository
    private lateinit var useCase: AchieveMilestoneUseCase

    private val testInstant = Instant.parse("2024-06-15T10:30:00Z")

    private val pendingMilestone = Milestone(
        id = "ms-1", childId = "child-1",
        category = MilestoneCategory.MOTOR, title = "第一次翻身",
        description = null, achievedDate = null,
        isExpected = true, expectedAgeMonths = 6,
        notes = null, isDeleted = false,
        createdAt = testInstant, updatedAt = testInstant
    )

    @Before
    fun setup() {
        repository = mock()
        useCase = AchieveMilestoneUseCase(repository)
    }

    @Test
    fun `invoke sets achievedDate and updates milestone`() = runTest {
        whenever(repository.getMilestoneById("ms-1")).thenReturn(pendingMilestone)

        useCase("ms-1")

        val captor = argumentCaptor<Milestone>()
        verify(repository).updateMilestone(captor.capture())
        assertThat(captor.firstValue.achievedDate).isNotNull()
        assertThat(captor.firstValue.id).isEqualTo("ms-1")
    }

    @Test
    fun `invoke with custom date uses provided date`() = runTest {
        val customDate = LocalDate.of(2024, 5, 1)
        whenever(repository.getMilestoneById("ms-1")).thenReturn(pendingMilestone)

        useCase("ms-1", customDate)

        val captor = argumentCaptor<Milestone>()
        verify(repository).updateMilestone(captor.capture())
        assertThat(captor.firstValue.achievedDate).isEqualTo(customDate)
    }

    @Test
    fun `invoke does nothing when milestone not found`() = runTest {
        whenever(repository.getMilestoneById("non-existent")).thenReturn(null)

        useCase("non-existent")

        verify(repository, org.mockito.kotlin.never()).updateMilestone(org.mockito.kotlin.any())
    }

    @Test
    fun `invoke updates updatedAt timestamp`() = runTest {
        whenever(repository.getMilestoneById("ms-1")).thenReturn(pendingMilestone)

        useCase("ms-1")

        val captor = argumentCaptor<Milestone>()
        verify(repository).updateMilestone(captor.capture())
        assertThat(captor.firstValue.updatedAt).isNotNull()
    }
}