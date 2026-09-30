package com.tuantuan.journal.domain.repository

import com.tuantuan.journal.domain.model.MediaItem
import kotlinx.coroutines.flow.Flow

interface MediaRepository {
    fun getMediaByEntry(entryId: String): Flow<List<MediaItem>>
    suspend fun addMedia(mediaItem: MediaItem): String
    suspend fun deleteMedia(id: String)
    suspend fun deleteMediaByEntry(entryId: String)
    suspend fun getMediaCount(entryId: String): Int
}