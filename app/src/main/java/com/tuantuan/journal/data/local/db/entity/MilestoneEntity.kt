package com.tuantuan.journal.data.local.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.tuantuan.journal.domain.model.MilestoneCategory
import java.time.Instant
import java.time.LocalDate

/**
 * 里程碑 Room Entity。
 *
 * 对应 DATA_MODEL.md 第2.7节。
 * 外键关联 ChildEntity，索引优化查询。
 */
@Entity(
    tableName = "milestones",
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
        Index(value = ["category"]),
        Index(value = ["achievedDate"]),
        Index(value = ["isDeleted"]),
        Index(value = ["childId", "isDeleted"]),
        Index(value = ["childId", "category", "isDeleted"])
    ]
)
data class MilestoneEntity(
    @PrimaryKey val id: String,
    val childId: String,
    val category: MilestoneCategory,
    val title: String,
    val description: String?,
    val achievedDate: LocalDate?,
    val isExpected: Boolean = false,
    val expectedAgeMonths: Int?,
    val notes: String?,
    val isDeleted: Boolean = false,
    val createdAt: Instant,
    val updatedAt: Instant
)