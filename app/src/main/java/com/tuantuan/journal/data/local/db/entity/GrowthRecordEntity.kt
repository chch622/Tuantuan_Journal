package com.tuantuan.journal.data.local.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.tuantuan.journal.domain.model.GrowthType
import java.time.Instant
import java.time.LocalDate

/**
 * 成长记录 Room Entity。
 *
 * 对应 DATA_MODEL.md 第2.6节。
 * 外键关联 ChildEntity，索引优化高频查询。
 */
@Entity(
    tableName = "growth_records",
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
        Index(value = ["recordType"]),
        Index(value = ["measureDate"]),
        Index(value = ["isDeleted"]),
        Index(value = ["childId", "recordType"]),
        Index(value = ["childId", "isDeleted"]),
        Index(value = ["childId", "recordType", "isDeleted"])
    ]
)
data class GrowthRecordEntity(
    @PrimaryKey val id: String,
    val childId: String,
    val recordType: GrowthType,
    val value: Double,
    val unit: String,
    val measureDate: LocalDate,
    val notes: String?,
    val isDeleted: Boolean = false,
    val createdAt: Instant,
    val updatedAt: Instant
)