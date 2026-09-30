package com.tuantuan.journal.domain.model

import java.time.Instant

data class DiaryEntry(
    val id: String,
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
    val updatedAt: Instant,
    val mediaItems: List<MediaItem> = emptyList(),
    val tags: List<Tag> = emptyList()
)