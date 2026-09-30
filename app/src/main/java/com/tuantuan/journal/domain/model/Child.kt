package com.tuantuan.journal.domain.model

import java.time.Instant
import java.time.LocalDate

data class Child(
    val id: String,
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