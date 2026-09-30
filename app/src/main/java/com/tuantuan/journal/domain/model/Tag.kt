package com.tuantuan.journal.domain.model

import java.time.Instant

data class Tag(
    val id: String,
    val name: String,
    val color: String?,
    val category: String?,
    val usageCount: Int = 0,
    val isDeleted: Boolean = false,
    val createdAt: Instant,
    val updatedAt: Instant
)