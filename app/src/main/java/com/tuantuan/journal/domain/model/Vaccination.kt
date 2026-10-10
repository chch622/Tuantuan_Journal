package com.tuantuan.journal.domain.model

import java.time.Instant
import java.time.LocalDate

/**
 * 疫苗记录 Domain Model。
 *
 * 对应 DATA_MODEL.md 第2.10节 Vaccination。
 * 记录疫苗接种情况，支持已接种和计划接种两种状态。
 *
 * 状态判断：
 * - administeredDate != null → 已接种
 * - scheduledDate != null && administeredDate == null → 待接种（可用于提醒）
 */
data class Vaccination(
    val id: String,
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
) {
    /** 是否已接种 */
    val isAdministered: Boolean
        get() = administeredDate != null

    /** 是否待接种（有计划日期但尚未接种） */
    val isPending: Boolean
        get() = administeredDate == null && scheduledDate != null

    /** 是否逾期（计划日期已过但尚未接种） */
    val isOverdue: Boolean
        get() = isPending && scheduledDate!!.isBefore(LocalDate.now())
}