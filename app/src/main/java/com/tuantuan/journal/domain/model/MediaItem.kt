package com.tuantuan.journal.domain.model

import java.time.Instant

data class MediaItem(
    val id: String,
    val entryId: String,
    val mediaType: MediaType,
    val filePath: String,
    val fileName: String,
    val fileSize: Long,
    val mimeType: String,
    val width: Int?,
    val height: Int?,
    val duration: Long?,
    val thumbnailPath: String?,
    val sortOrder: Int,
    val isDeleted: Boolean = false,
    val createdAt: Instant,
    val updatedAt: Instant
)