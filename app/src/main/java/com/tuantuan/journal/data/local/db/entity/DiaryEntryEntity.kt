package com.tuantuan.journal.data.local.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.tuantuan.journal.domain.model.Mood
import com.tuantuan.journal.domain.model.Weather
import java.time.Instant

@Entity(
    tableName = "diary_entries",
    foreignKeys = [
        ForeignKey(
            entity = ChildEntity::class,
            parentColumns = ["id"],
            childColumns = ["childId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["childId"]),
        Index(value = ["eventDateTime"]),
        Index(value = ["isDeleted"]),
        Index(value = ["childId", "eventDateTime"]),
        Index(value = ["childId", "isDeleted"])
    ]
)
data class DiaryEntryEntity(
    @PrimaryKey val id: String,
    val childId: String,
    val title: String?,
    val content: String,
    val eventDateTime: Instant,
    val mood: Mood?,
    val weather: Weather?,
    val location: String?,
    val isFavorite: Boolean = false,
    val isDeleted: Boolean = false,
    val createdAt: Instant,
    val updatedAt: Instant
)