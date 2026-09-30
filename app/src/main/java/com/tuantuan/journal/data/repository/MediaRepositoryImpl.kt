package com.tuantuan.journal.data.repository

import com.tuantuan.journal.data.local.db.dao.MediaItemDao
import com.tuantuan.journal.data.local.file.MediaFileManager
import com.tuantuan.journal.data.mapper.MediaItemMapper
import com.tuantuan.journal.domain.model.MediaItem
import com.tuantuan.journal.domain.repository.MediaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MediaRepositoryImpl @Inject constructor(
    private val mediaItemDao: MediaItemDao,
    private val mediaFileManager: MediaFileManager
) : MediaRepository {

    override fun getMediaByEntry(entryId: String): Flow<List<MediaItem>> =
        mediaItemDao.getMediaByEntry(entryId).map { entities ->
            entities.map { MediaItemMapper.toDomain(it) }
        }

    override suspend fun addMedia(mediaItem: MediaItem): String {
        mediaItemDao.insert(MediaItemMapper.toEntity(mediaItem))
        return mediaItem.id
    }

    override suspend fun deleteMedia(id: String) {
        mediaItemDao.softDelete(id, Instant.now().toEpochMilli())
        // 物理文件延迟清理，不在此处立即删除
    }

    override suspend fun deleteMediaByEntry(entryId: String) {
        // 软删除所有关联媒体记录
        val mediaItems = mediaItemDao.getMediaByEntry(entryId).first()
        val now = Instant.now().toEpochMilli()
        mediaItems.forEach { entity ->
            mediaItemDao.softDelete(entity.id, now)
        }
    }

    override suspend fun getMediaCount(entryId: String): Int =
        mediaItemDao.getMediaByEntry(entryId).first().size
}