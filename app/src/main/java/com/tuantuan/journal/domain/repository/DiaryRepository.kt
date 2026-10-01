package com.tuantuan.journal.domain.repository

import com.tuantuan.journal.domain.model.DiaryEntry
import kotlinx.coroutines.flow.Flow

interface DiaryRepository {
    fun getEntriesByChild(childId: String): Flow<List<DiaryEntry>>
    suspend fun getEntryById(id: String): DiaryEntry?
    fun searchEntries(childId: String, query: String): Flow<List<DiaryEntry>>
    fun getFavoriteEntries(childId: String): Flow<List<DiaryEntry>>
    fun getRecentEntries(childId: String, limit: Int = 10): Flow<List<DiaryEntry>>
    fun getTodayEntries(childId: String): Flow<List<DiaryEntry>>
    suspend fun createEntry(entry: DiaryEntry): String
    suspend fun updateEntry(entry: DiaryEntry)
    suspend fun deleteEntry(id: String)
    suspend fun toggleFavorite(id: String, isFavorite: Boolean)
}