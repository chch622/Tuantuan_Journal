package com.tuantuan.journal.domain.usecase.milestone

import com.tuantuan.journal.domain.model.Milestone
import com.tuantuan.journal.domain.model.MilestoneCategory
import com.tuantuan.journal.domain.repository.MilestoneRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMilestonesUseCase @Inject constructor(
    private val repository: MilestoneRepository
) {
    operator fun invoke(childId: String): Flow<List<Milestone>> =
        repository.getMilestonesByChild(childId)

    fun byCategory(childId: String, category: MilestoneCategory): Flow<List<Milestone>> =
        repository.getMilestonesByCategory(childId, category)

    fun achieved(childId: String): Flow<List<Milestone>> =
        repository.getAchievedMilestones(childId)

    fun expected(childId: String): Flow<List<Milestone>> =
        repository.getExpectedMilestones(childId)
}