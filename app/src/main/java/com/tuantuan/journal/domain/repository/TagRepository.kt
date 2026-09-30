package com.tuantuan.journal.domain.repository

import com.tuantuan.journal.domain.model.Tag
import kotlinx.coroutines.flow.Flow

interface TagRepository {
    fun getAllTags(): Flow<List<Tag>>
    suspend fun getTagById(id: String): Tag?
    suspend fun createTag(tag: Tag): String
    suspend fun deleteTag(id: String)
    suspend fun addTagToEntry(entryId: String, tagId: String)
    suspend fun removeTagFromEntry(entryId: String, tagId: String)
    fun getTagsForEntry(entryId: String): Flow<List<Tag>>
}