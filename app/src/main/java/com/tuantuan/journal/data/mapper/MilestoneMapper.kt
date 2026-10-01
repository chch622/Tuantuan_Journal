package com.tuantuan.journal.data.mapper

import com.tuantuan.journal.data.local.db.entity.MilestoneEntity
import com.tuantuan.journal.domain.model.Milestone

/**
 * Milestone Entity ↔ Domain Model 映射器。
 *
 * 遵循 ARCHITECTURE.md 第2.3节 — Data 层负责 Entity ↔ Domain Model 转换。
 */
object MilestoneMapper {

    fun toDomain(entity: MilestoneEntity): Milestone = Milestone(
        id = entity.id,
        childId = entity.childId,
        category = entity.category,
        title = entity.title,
        description = entity.description,
        achievedDate = entity.achievedDate,
        isExpected = entity.isExpected,
        expectedAgeMonths = entity.expectedAgeMonths,
        notes = entity.notes,
        isDeleted = entity.isDeleted,
        createdAt = entity.createdAt,
        updatedAt = entity.updatedAt
    )

    fun toEntity(domain: Milestone): MilestoneEntity = MilestoneEntity(
        id = domain.id,
        childId = domain.childId,
        category = domain.category,
        title = domain.title,
        description = domain.description,
        achievedDate = domain.achievedDate,
        isExpected = domain.isExpected,
        expectedAgeMonths = domain.expectedAgeMonths,
        notes = domain.notes,
        isDeleted = domain.isDeleted,
        createdAt = domain.createdAt,
        updatedAt = domain.updatedAt
    )
}