package com.tuantuan.journal.domain.usecase.tag

import com.tuantuan.journal.domain.model.Tag
import com.tuantuan.journal.domain.repository.TagRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTagsUseCase @Inject constructor(
    private val repository: TagRepository
) {
    operator fun invoke(): Flow<List<Tag>> = repository.getAllTags()
    fun forEntry(entryId: String): Flow<List<Tag>> = repository.getTagsForEntry(entryId)
}

class SaveTagUseCase @Inject constructor(
    private val repository: TagRepository
) {
    suspend fun create(tag: Tag): String = repository.createTag(tag)
}

class ManageEntryTagUseCase @Inject constructor(
    private val repository: TagRepository
) {
    suspend fun add(entryId: String, tagId: String) = repository.addTagToEntry(entryId, tagId)
    suspend fun remove(entryId: String, tagId: String) = repository.removeTagFromEntry(entryId, tagId)
}