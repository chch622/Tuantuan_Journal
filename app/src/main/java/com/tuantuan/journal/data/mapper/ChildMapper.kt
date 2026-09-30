package com.tuantuan.journal.data.mapper

import com.tuantuan.journal.data.local.db.entity.ChildEntity
import com.tuantuan.journal.domain.model.Child

object ChildMapper {
    fun toDomain(entity: ChildEntity): Child = Child(
        id = entity.id,
        name = entity.name,
        nickname = entity.nickname,
        birthDate = entity.birthDate,
        gender = entity.gender,
        avatarPath = entity.avatarPath,
        birthWeight = entity.birthWeight,
        birthHeight = entity.birthHeight,
        bloodType = entity.bloodType,
        birthPlace = entity.birthPlace,
        notes = entity.notes,
        sortOrder = entity.sortOrder,
        isDeleted = entity.isDeleted,
        createdAt = entity.createdAt,
        updatedAt = entity.updatedAt
    )

    fun toEntity(domain: Child): ChildEntity = ChildEntity(
        id = domain.id,
        name = domain.name,
        nickname = domain.nickname,
        birthDate = domain.birthDate,
        gender = domain.gender,
        avatarPath = domain.avatarPath,
        birthWeight = domain.birthWeight,
        birthHeight = domain.birthHeight,
        bloodType = domain.bloodType,
        birthPlace = domain.birthPlace,
        notes = domain.notes,
        sortOrder = domain.sortOrder,
        isDeleted = domain.isDeleted,
        createdAt = domain.createdAt,
        updatedAt = domain.updatedAt
    )
}