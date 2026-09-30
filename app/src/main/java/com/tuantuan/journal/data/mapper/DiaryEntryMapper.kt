package com.tuantuan.journal.data.mapper

import com.tuantuan.journal.data.local.db.entity.DiaryEntryEntity
import com.tuantuan.journal.domain.model.DiaryEntry
import com.tuantuan.journal.domain.model.MediaItem
import com.tuantuan.journal.domain.model.Tag

object DiaryEntryMapper {
    fun toDomain(
        entity: DiaryEntryEntity,
        mediaItems: List<MediaItem> = emptyList(),
        tags: List<Tag> = emptyList()
    ): DiaryEntry = DiaryEntry(
        id = entity.id,
        childId = entity.childId,
        title = entity.title,
        content = entity.content,
        eventDateTime = entity.eventDateTime,
        mood = entity.mood,
        weather = entity.weather,
        location = entity.location,
        isFavorite = entity.isFavorite,
        isDeleted = entity.isDeleted,
        createdAt = entity.createdAt,
        updatedAt = entity.updatedAt,
        mediaItems = mediaItems,
        tags = tags
    )

    fun toEntity(domain: DiaryEntry): DiaryEntryEntity = DiaryEntryEntity(
        id = domain.id,
        childId = domain.childId,
        title = domain.title,
        content = domain.content,
        eventDateTime = domain.eventDateTime,
        mood = domain.mood,
        weather = domain.weather,
        location = domain.location,
        isFavorite = domain.isFavorite,
        isDeleted = domain.isDeleted,
        createdAt = domain.createdAt,
        updatedAt = domain.updatedAt
    )
}