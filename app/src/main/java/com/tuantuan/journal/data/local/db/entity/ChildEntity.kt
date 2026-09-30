package com.tuantuan.journal.data.local.db.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.tuantuan.journal.domain.model.Gender
import java.time.Instant
import java.time.LocalDate

@Entity(
    tableName = "children",
    indices = [
        Index(value = ["isDeleted"]),
        Index(value = ["sortOrder"])
    ]
)
data class ChildEntity(
    @PrimaryKey val id: String,
    val name: String,
    val nickname: String,
    val birthDate: LocalDate,
    val gender: Gender?,
    val avatarPath: String?,
    val birthWeight: Double?,
    val birthHeight: Double?,
    val bloodType: String?,
    val birthPlace: String?,
    val notes: String?,
    val sortOrder: Int,
    val isDeleted: Boolean = false,
    val createdAt: Instant,
    val updatedAt: Instant
)