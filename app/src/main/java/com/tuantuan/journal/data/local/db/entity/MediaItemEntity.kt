package com.tuantuan.journal.data.local.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.tuantuan.journal.domain.model.MediaType
import java.time.Instant

@Entity(
    tableName = "media_items",
    foreignKeys = [
        ForeignKey(
            entity = DiaryEntryEntity::class,
            parentColumns = ["id"],
            childColumns = ["entryId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["entryId"]),
        Index(value = ["isDeleted"]),
        Index(value = ["sortOrder"])
    ]
)
data class MediaItemEntity(
    @PrimaryKey val id: String,
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