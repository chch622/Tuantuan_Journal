package com.tuantuan.journal.data.mapper

import com.tuantuan.journal.data.local.db.entity.GrowthRecordEntity
import com.tuantuan.journal.domain.model.GrowthRecord

/**
 * GrowthRecord Entity ↔ Domain Model 映射器。
 *
 * 遵循 ARCHITECTURE.md 第2.3节 — Data 层负责 Entity ↔ Domain Model 转换。
 */
object GrowthRecordMapper {

    fun toDomain(entity: GrowthRecordEntity): GrowthRecord = GrowthRecord(
        id = entity.id,
        childId = entity.childId,
        recordType = entity.recordType,
        value = entity.value,
        unit = entity.unit,
        measureDate = entity.measureDate,
        notes = entity.notes,
        isDeleted = entity.isDeleted,
        createdAt = entity.createdAt,
        updatedAt = entity.updatedAt
    )

    fun toEntity(domain: GrowthRecord): GrowthRecordEntity = GrowthRecordEntity(
        id = domain.id,
        childId = domain.childId,
        recordType = domain.recordType,
        value = domain.value,
        unit = domain.unit,
        measureDate = domain.measureDate,
        notes = domain.notes,
        isDeleted = domain.isDeleted,
        createdAt = domain.createdAt,
        updatedAt = domain.updatedAt
    )
}