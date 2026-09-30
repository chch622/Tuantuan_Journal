package com.tuantuan.journal.data.mapper

import com.tuantuan.journal.data.local.db.entity.MediaItemEntity
import com.tuantuan.journal.domain.model.MediaItem

object MediaItemMapper {
    fun toDomain(entity: MediaItemEntity): MediaItem = MediaItem(
        id = entity.id,
        entryId = entity.entryId,
        mediaType = entity.mediaType,
        filePath = entity.filePath,
        fileName = entity.fileName,
        fileSize = entity.fileSize,
        mimeType = entity.mimeType,
        width = entity.width,
        height = entity.height,
        duration = entity.duration,
        thumbnailPath = entity.thumbnailPath,
        sortOrder = entity.sortOrder,
        isDeleted = entity.isDeleted,
        createdAt = entity.createdAt,
        updatedAt = entity.updatedAt
    )

    fun toEntity(domain: MediaItem): MediaItemEntity = MediaItemEntity(
        id = domain.id,
        entryId = domain.entryId,
        mediaType = domain.mediaType,
        filePath = domain.filePath,
        fileName = domain.fileName,
        fileSize = domain.fileSize,
        mimeType = domain.mimeType,
        width = domain.width,
        height = domain.height,
        duration = domain.duration,
        thumbnailPath = domain.thumbnailPath,
        sortOrder = domain.sortOrder,
        isDeleted = domain.isDeleted,
        createdAt = domain.createdAt,
        updatedAt = domain.updatedAt
    )
}