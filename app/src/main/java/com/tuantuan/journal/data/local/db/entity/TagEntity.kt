package com.tuantuan.journal.data.local.db.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.Instant

@Entity(
    tableName = "tags",
    indices = [
        Index(value = ["name"], unique = true),
        Index(value = ["isDeleted"]),
        Index(value = ["usageCount"])
    ]
)
data class TagEntity(
    @PrimaryKey val id: String,
    val name: String,
    val color: String?,
    val category: String?,
    val usageCount: Int = 0,
    val isDeleted: Boolean = false,
    val createdAt: Instant,
    val updatedAt: Instant
)