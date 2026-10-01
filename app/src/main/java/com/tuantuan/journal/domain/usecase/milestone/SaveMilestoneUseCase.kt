package com.tuantuan.journal.domain.usecase.milestone

import com.tuantuan.journal.domain.model.Milestone
import com.tuantuan.journal.domain.repository.MilestoneRepository
import javax.inject.Inject

class SaveMilestoneUseCase @Inject constructor(
    private val repository: MilestoneRepository
) {
    suspend fun create(milestone: Milestone): String = repository.createMilestone(milestone)
    suspend fun update(milestone: Milestone) = repository.updateMilestone(milestone)
}