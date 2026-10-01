package com.tuantuan.journal.data.repository

import com.tuantuan.journal.data.local.db.dao.MilestoneDao
import com.tuantuan.journal.data.mapper.MilestoneMapper
import com.tuantuan.journal.domain.model.Milestone
import com.tuantuan.journal.domain.model.MilestoneCategory
import com.tuantuan.journal.domain.repository.MilestoneRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 里程碑 Repository 实现。
 *
 * 遵循 ARCHITECTURE.md 第2.3节 — Data 层实现 Repository 接口，
 * 负责 Entity ↔ Domain Model 映射，不暴露 Entity 给 Domain 层。
 */
@Singleton
class MilestoneRepositoryImpl @Inject constructor(
    private val milestoneDao: MilestoneDao
) : MilestoneRepository {

    override fun getMilestonesByChild(childId: String): Flow<List<Milestone>> =
        milestoneDao.getActiveMilestonesByChild(childId).map { entities ->
            entities.map { MilestoneMapper.toDomain(it) }
        }

    override fun getMilestonesByCategory(childId: String, category: MilestoneCategory): Flow<List<Milestone>> =
        milestoneDao.getActiveMilestonesByCategory(childId, category).map { entities ->
            entities.map { MilestoneMapper.toDomain(it) }
        }

    override fun getAchievedMilestones(childId: String): Flow<List<Milestone>> =
        milestoneDao.getAchievedMilestones(childId).map { entities ->
            entities.map { MilestoneMapper.toDomain(it) }
        }

    override fun getExpectedMilestones(childId: String): Flow<List<Milestone>> =
        milestoneDao.getExpectedMilestones(childId).map { entities ->
            entities.map { MilestoneMapper.toDomain(it) }
        }

    override suspend fun getMilestoneById(id: String): Milestone? =
        milestoneDao.getMilestoneById(id)?.let { MilestoneMapper.toDomain(it) }

    override suspend fun createMilestone(milestone: Milestone): String {
        milestoneDao.insert(MilestoneMapper.toEntity(milestone))
        return milestone.id
    }

    override suspend fun updateMilestone(milestone: Milestone) {
        milestoneDao.update(MilestoneMapper.toEntity(milestone))
    }

    override suspend fun deleteMilestone(id: String) {
        milestoneDao.softDelete(id, Instant.now().toEpochMilli())
    }
}