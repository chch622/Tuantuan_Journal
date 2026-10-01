package com.tuantuan.journal.ui.screen.milestone

import com.tuantuan.journal.domain.model.Milestone
import com.tuantuan.journal.domain.model.MilestoneCategory
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.time.Instant
import java.time.LocalDate

class MilestoneUiStateTest {

    private val testInstant = Instant.parse("2024-06-15T10:30:00Z")
    private val testDate = LocalDate.of(2024, 6, 15)

    private val achievedMilestone = Milestone(
        id = "ms-1", childId = "child-1",
        category = MilestoneCategory.MOTOR, title = "第一次翻身",
        description = null, achievedDate = testDate,
        isExpected = false, expectedAgeMonths = null,
        notes = null, isDeleted = false,
        createdAt = testInstant, updatedAt = testInstant
    )

    private val pendingMilestone = Milestone(
        id = "ms-2", childId = "child-1",
        category = MilestoneCategory.LANGUAGE, title = "第一次叫妈妈",
        description = null, achievedDate = null,
        isExpected = true, expectedAgeMonths = 12,
        notes = null, isDeleted = false,
        createdAt = testInstant, updatedAt = testInstant
    )

    @Test
    fun `filteredMilestones returns all when no category selected`() {
        val milestones = listOf(achievedMilestone, pendingMilestone)
        val state = MilestoneUiState(milestones = milestones, selectedCategory = null)

        assertThat(state.filteredMilestones).hasSize(2)
    }

    @Test
    fun `filteredMilestones filters by category`() {
        val milestones = listOf(achievedMilestone, pendingMilestone)
        val state = MilestoneUiState(milestones = milestones, selectedCategory = MilestoneCategory.MOTOR)

        assertThat(state.filteredMilestones).hasSize(1)
        assertThat(state.filteredMilestones[0].category).isEqualTo(MilestoneCategory.MOTOR)
    }

    @Test
    fun `filteredMilestones returns empty when no match`() {
        val milestones = listOf(achievedMilestone) // MOTOR
        val state = MilestoneUiState(milestones = milestones, selectedCategory = MilestoneCategory.SOCIAL)

        assertThat(state.filteredMilestones).isEmpty()
    }

    @Test
    fun `achievedMilestones returns only achieved`() {
        val milestones = listOf(achievedMilestone, pendingMilestone)
        val state = MilestoneUiState(milestones = milestones)

        assertThat(state.achievedMilestones).hasSize(1)
        assertThat(state.achievedMilestones[0].achievedDate).isNotNull()
    }

    @Test
    fun `pendingMilestones returns only pending`() {
        val milestones = listOf(achievedMilestone, pendingMilestone)
        val state = MilestoneUiState(milestones = milestones)

        assertThat(state.pendingMilestones).hasSize(1)
        assertThat(state.pendingMilestones[0].achievedDate).isNull()
    }

    @Test
    fun `hasMilestones is true when data exists`() {
        val state = MilestoneUiState(milestones = listOf(achievedMilestone))
        assertThat(state.hasMilestones).isTrue()
    }

    @Test
    fun `hasMilestones is false when empty`() {
        val state = MilestoneUiState(milestones = emptyList())
        assertThat(state.hasMilestones).isFalse()
    }

    @Test
    fun `achievedMilestones respects category filter`() {
        val milestones = listOf(achievedMilestone, pendingMilestone)
        val state = MilestoneUiState(
            milestones = milestones,
            selectedCategory = MilestoneCategory.LANGUAGE
        )

        // LANGUAGE category only has pendingMilestone (not achieved)
        assertThat(state.achievedMilestones).isEmpty()
        assertThat(state.pendingMilestones).hasSize(1)
    }
}

class MilestoneFormStateTest {

    @Test
    fun `isValid is true for non-blank title`() {
        val state = MilestoneFormState(title = "第一次翻身")
        assertThat(state.isValid).isTrue()
    }

    @Test
    fun `isValid is false for empty title`() {
        val state = MilestoneFormState(title = "")
        assertThat(state.isValid).isFalse()
    }

    @Test
    fun `isValid is false for blank title`() {
        val state = MilestoneFormState(title = "   ")
        assertThat(state.isValid).isFalse()
    }
}