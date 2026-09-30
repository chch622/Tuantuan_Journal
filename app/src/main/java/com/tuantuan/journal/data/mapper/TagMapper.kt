package com.tuantuan.journal.data.mapper

import com.tuantuan.journal.data.local.db.entity.TagEntity
import com.tuantuan.journal.domain.model.Tag

object TagMapper {
    fun toDomain(entity: TagEntity): Tag = Tag(
        id = entity.id,
        name = entity.name,
        color = entity.color,
        category = entity.category,
        usageCount = entity.usageCount,
        isDeleted = entity.isDeleted,
        createdAt = entity.createdAt,
        updatedAt = entity.updatedAt
    )

    fun toEntity(domain: Tag): TagEntity = TagEntity(
        id = domain.id,
        name = domain.name,
        color = domain.color,
        category = domain.category,
        usageCount = domain.usageCount,
        isDeleted = domain.isDeleted,
        createdAt = domain.createdAt,
        updatedAt = domain.updatedAt
    )
}