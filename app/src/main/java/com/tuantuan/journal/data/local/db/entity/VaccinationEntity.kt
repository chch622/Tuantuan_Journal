package com.tuantuan.journal.data.local.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.Instant
import java.time.LocalDate

/**
 * 疫苗记录 Room Entity。
 *
 * 对应 DATA_MODEL.md 第2.10节 Vaccination。
 * 外键关联 ChildEntity，索引优化查询。
 *
 * 状态判断：
 * - administeredDate != null → 已接种
 * - scheduledDate != null && administeredDate == null → 待接种
 */
@Entity(
    tableName = "vaccinations",
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
        Index(value = ["administeredDate"]),
        Index(value = ["scheduledDate"]),
        Index(value = ["isDeleted"]),
        Index(value = ["childId", "isDeleted"]),
        Index(value = ["childId", "administeredDate", "isDeleted"]),
        Index(value = ["childId", "scheduledDate", "isDeleted"])
    ]
)
data class VaccinationEntity(
    @PrimaryKey val id: String,
    val childId: String,
    val vaccineName: String,
    val doseNumber: Int?,
    val administeredDate: LocalDate?,
    val scheduledDate: LocalDate?,
    val location: String?,
    val batchNumber: String?,
    val notes: String?,
    val isDeleted: Boolean = false,
    val createdAt: Instant,
    val updatedAt: Instant
)