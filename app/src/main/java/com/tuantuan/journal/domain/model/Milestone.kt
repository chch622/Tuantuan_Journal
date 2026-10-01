package com.tuantuan.journal.domain.model

import java.time.Instant
import java.time.LocalDate

/**
 * 里程碑 Domain Model。
 *
 * 对应 DATA_MODEL.md 第2.7节 Milestone。
 * 记录第一次走路、第一次说话等成长里程碑。
 */
data class Milestone(
    val id: String,
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