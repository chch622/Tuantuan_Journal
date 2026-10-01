package com.tuantuan.journal.domain.usecase.milestone

import com.tuantuan.journal.domain.repository.MilestoneRepository
import javax.inject.Inject

class DeleteMilestoneUseCase @Inject constructor(
    private val repository: MilestoneRepository
) {
    suspend operator fun invoke(id: String) = repository.deleteMilestone(id)
}