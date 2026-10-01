package com.tuantuan.journal.data.repository

import com.tuantuan.journal.data.local.db.dao.DiaryEntryDao
import com.tuantuan.journal.data.local.db.dao.MediaItemDao
import com.tuantuan.journal.data.local.db.dao.TagDao
import com.tuantuan.journal.data.mapper.DiaryEntryMapper
import com.tuantuan.journal.data.mapper.MediaItemMapper
import com.tuantuan.journal.data.mapper.TagMapper
import com.tuantuan.journal.domain.model.DiaryEntry
import com.tuantuan.journal.domain.repository.DiaryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DiaryRepositoryImpl @Inject constructor(
    private val diaryEntryDao: DiaryEntryDao,
    private val mediaItemDao: MediaItemDao,
    private val tagDao: TagDao
) : DiaryRepository {

    override fun getEntriesByChild(childId: String): Flow<List<DiaryEntry>> =
        diaryEntryDao.getEntriesByChild(childId).map { entities ->
            entities.map { DiaryEntryMapper.toDomain(it) }
        }

    override suspend fun getEntryById(id: String): DiaryEntry? {
        val entity = diaryEntryDao.getEntryById(id) ?: return null
        val mediaItems = mediaItemDao.getMediaByEntry(id).first()
            .map { MediaItemMapper.toDomain(it) }
        val tags = tagDao.getTagsByEntry(id).first()
            .map { TagMapper.toDomain(it) }
        return DiaryEntryMapper.toDomain(entity, mediaItems, tags)
    }

    override fun searchEntries(childId: String, query: String): Flow<List<DiaryEntry>> =
        diaryEntryDao.searchEntries(childId, query).map { entities ->
            entities.map { DiaryEntryMapper.toDomain(it) }
        }

    override fun getFavoriteEntries(childId: String): Flow<List<DiaryEntry>> =
        diaryEntryDao.getFavoriteEntries(childId).map { entities ->
            entities.map { DiaryEntryMapper.toDomain(it) }
        }

    override fun getRecentEntries(childId: String, limit: Int): Flow<List<DiaryEntry>> =
        diaryEntryDao.getRecentEntries(childId, limit).map { entities ->
            entities.map { DiaryEntryMapper.toDomain(it) }
        }

    override fun getTodayEntries(childId: String): Flow<List<DiaryEntry>> =
        diaryEntryDao.getTodayEntries(childId).map { entities ->
            entities.map { DiaryEntryMapper.toDomain(it) }
        }

    override suspend fun createEntry(entry: DiaryEntry): String {
        diaryEntryDao.insert(DiaryEntryMapper.toEntity(entry))
        return entry.id
    }

    override suspend fun updateEntry(entry: DiaryEntry) {
        diaryEntryDao.update(DiaryEntryMapper.toEntity(entry))
    }

    override suspend fun deleteEntry(id: String) {
        diaryEntryDao.softDelete(id, Instant.now().toEpochMilli())
    }

    override suspend fun toggleFavorite(id: String, isFavorite: Boolean) {
        diaryEntryDao.toggleFavorite(id, isFavorite, Instant.now().toEpochMilli())
    }
}