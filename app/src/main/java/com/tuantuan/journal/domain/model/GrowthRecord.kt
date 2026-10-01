package com.tuantuan.journal.domain.model

import java.time.Instant
import java.time.LocalDate

/**
 * 成长记录 Domain Model。
 *
 * 对应 DATA_MODEL.md 第2.6节 GrowthRecord。
 * 记录身高、体重、头围、鞋码等成长数据。
 */
data class GrowthRecord(
    val id: String,
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