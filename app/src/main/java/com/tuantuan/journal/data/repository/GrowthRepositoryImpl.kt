package com.tuantuan.journal.data.repository

import com.tuantuan.journal.data.local.db.dao.GrowthRecordDao
import com.tuantuan.journal.data.mapper.GrowthRecordMapper
import com.tuantuan.journal.domain.model.GrowthRecord
import com.tuantuan.journal.domain.model.GrowthType
import com.tuantuan.journal.domain.repository.GrowthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 成长记录 Repository 实现。
 *
 * 遵循 ARCHITECTURE.md 第2.3节 — Data 层实现 Repository 接口，
 * 负责 Entity ↔ Domain Model 映射，不暴露 Entity 给 Domain 层。
 */
@Singleton
class GrowthRepositoryImpl @Inject constructor(
    private val growthRecordDao: GrowthRecordDao
) : GrowthRepository {

    override fun getRecordsByChild(childId: String): Flow<List<GrowthRecord>> =
        growthRecordDao.getActiveRecordsByChild(childId).map { entities ->
            entities.map { GrowthRecordMapper.toDomain(it) }
        }

    override fun getRecordsByType(childId: String, type: GrowthType): Flow<List<GrowthRecord>> =
        growthRecordDao.getActiveRecordsByType(childId, type).map { entities ->
            entities.map { GrowthRecordMapper.toDomain(it) }
        }

    override suspend fun getRecordById(id: String): GrowthRecord? =
        growthRecordDao.getRecordById(id)?.let { GrowthRecordMapper.toDomain(it) }

    override suspend fun createRecord(record: GrowthRecord): String {
        growthRecordDao.insert(GrowthRecordMapper.toEntity(record))
        return record.id
    }

    override suspend fun updateRecord(record: GrowthRecord) {
        growthRecordDao.update(GrowthRecordMapper.toEntity(record))
    }

    override suspend fun deleteRecord(id: String) {
        growthRecordDao.softDelete(id, Instant.now().toEpochMilli())
    }

    override suspend fun getLatestRecord(childId: String, type: GrowthType): GrowthRecord? =
        growthRecordDao.getLatestRecord(childId, type)?.let { GrowthRecordMapper.toDomain(it) }
}