package com.tuantuan.journal.domain.usecase.milestone

import com.tuantuan.journal.domain.model.Milestone
import com.tuantuan.journal.domain.repository.MilestoneRepository
import java.time.LocalDate
import javax.inject.Inject

class AchieveMilestoneUseCase @Inject constructor(
    private val repository: MilestoneRepository
) {
    suspend operator fun invoke(milestoneId: String, date: LocalDate = LocalDate.now()) {
        val milestone = repository.getMilestoneById(milestoneId) ?: return
        val updated = milestone.copy(
            achievedDate = date,
            updatedAt = java.time.Instant.now()
        )
        repository.updateMilestone(updated)
    }
}