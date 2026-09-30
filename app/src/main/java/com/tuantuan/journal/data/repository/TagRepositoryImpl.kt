package com.tuantuan.journal.data.repository

import com.tuantuan.journal.data.local.db.dao.TagDao
import com.tuantuan.journal.data.local.db.entity.EntryTagEntity
import com.tuantuan.journal.data.mapper.TagMapper
import com.tuantuan.journal.domain.model.Tag
import com.tuantuan.journal.domain.repository.TagRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TagRepositoryImpl @Inject constructor(
    private val tagDao: TagDao
) : TagRepository {

    override fun getAllTags(): Flow<List<Tag>> =
        tagDao.getAllActiveTags().map { entities ->
            entities.map { TagMapper.toDomain(it) }
        }

    override suspend fun getTagById(id: String): Tag? =
        tagDao.getTagById(id)?.let { TagMapper.toDomain(it) }

    override suspend fun createTag(tag: Tag): String {
        tagDao.insert(TagMapper.toEntity(tag))
        return tag.id
    }

    override suspend fun deleteTag(id: String) {
        tagDao.softDelete(id, Instant.now().toEpochMilli())
    }

    override suspend fun addTagToEntry(entryId: String, tagId: String) {
        tagDao.insertEntryTag(EntryTagEntity(entryId, tagId, Instant.now()))
        tagDao.incrementUsageCount(tagId, Instant.now().toEpochMilli())
    }

    override suspend fun removeTagFromEntry(entryId: String, tagId: String) {
        tagDao.deleteEntryTag(entryId, tagId)
    }

    override fun getTagsForEntry(entryId: String): Flow<List<Tag>> =
        tagDao.getTagsByEntry(entryId).map { entities ->
            entities.map { TagMapper.toDomain(it) }
        }
}