package com.tuantuan.journal.domain.usecase.milestone

import com.tuantuan.journal.domain.model.Milestone
import com.tuantuan.journal.domain.model.MilestoneCategory
import com.tuantuan.journal.domain.repository.MilestoneRepository
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import com.google.common.truth.Truth.assertThat
import java.time.Instant
import java.time.LocalDate

class SaveMilestoneUseCaseTest {
    private lateinit var repository: MilestoneRepository
    private lateinit var useCase: SaveMilestoneUseCase

    private val testInstant = Instant.parse("2024-06-15T10:30:00Z")

    private val testMilestone = Milestone(
        id = "ms-1", childId = "child-1",
        category = MilestoneCategory.MOTOR, title = "第一次翻身",
        description = null, achievedDate = LocalDate.of(2024, 6, 15),
        isExpected = false, expectedAgeMonths = null,
        notes = null, isDeleted = false,
        createdAt = testInstant, updatedAt = testInstant
    )

    @Before
    fun setup() {
        repository = mock()
        useCase = SaveMilestoneUseCase(repository)
    }

    @Test
    fun `create delegates to repository and returns id`() = runTest {
        whenever(repository.createMilestone(testMilestone)).thenReturn("ms-1")

        val result = useCase.create(testMilestone)

        verify(repository).createMilestone(testMilestone)
        assertThat(result).isEqualTo("ms-1")
    }

    @Test
    fun `update delegates to repository`() = runTest {
        useCase.update(testMilestone)

        verify(repository).updateMilestone(testMilestone)
    }
}