package com.tuantuan.journal.domain.repository

import com.tuantuan.journal.domain.model.Milestone
import com.tuantuan.journal.domain.model.MilestoneCategory
import kotlinx.coroutines.flow.Flow

/**
 * 里程碑 Repository 接口。
 *
 * 遵循 ARCHITECTURE.md 第2.2节 — Domain 层定义接口，Data 层实现。
 */
interface MilestoneRepository {

    fun getMilestonesByChild(childId: String): Flow<List<Milestone>>

    fun getMilestonesByCategory(childId: String, category: MilestoneCategory): Flow<List<Milestone>>

    fun getAchievedMilestones(childId: String): Flow<List<Milestone>>

    fun getExpectedMilestones(childId: String): Flow<List<Milestone>>

    suspend fun getMilestoneById(id: String): Milestone?

    suspend fun createMilestone(milestone: Milestone): String

    suspend fun updateMilestone(milestone: Milestone)

    suspend fun deleteMilestone(id: String)
}