package com.tuantuan.journal.data.repository

import com.tuantuan.journal.data.local.db.dao.TagDao
import com.tuantuan.journal.data.local.db.entity.EntryTagEntity
import com.tuantuan.journal.data.local.db.entity.TagEntity
import com.tuantuan.journal.domain.model.Tag
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import com.google.common.truth.Truth.assertThat
import java.time.Instant

class TagRepositoryImplTest {
    private lateinit var tagDao: TagDao
    private lateinit var repository: TagRepositoryImpl

    private val testInstant = Instant.parse("2024-01-15T10:30:00Z")

    private val testEntity = TagEntity(
        id = "tag-1", name = "开心", color = "#FF0000",
        category = null, usageCount = 5, isDeleted = false,
        createdAt = testInstant, updatedAt = testInstant
    )

    private val testDomain = Tag(
        id = "tag-1", name = "开心", color = "#FF0000",
        category = null, usageCount = 5, isDeleted = false,
        createdAt = testInstant, updatedAt = testInstant
    )

    @Before
    fun setup() {
        tagDao = mock()
        repository = TagRepositoryImpl(tagDao)
    }

    @Test
    fun `getAllTags maps entities to domain models`() = runTest {
        whenever(tagDao.getAllActiveTags()).thenReturn(flowOf(listOf(testEntity)))

        val result = repository.getAllTags().first()

        assertThat(result).hasSize(1)
        assertThat(result[0].id).isEqualTo("tag-1")
        assertThat(result[0].name).isEqualTo("开心")
    }

    @Test
    fun `getAllTags returns empty list when no tags`() = runTest {
        whenever(tagDao.getAllActiveTags()).thenReturn(flowOf(emptyList()))

        val result = repository.getAllTags().first()

        assertThat(result).isEmpty()
    }

    @Test
    fun `getTagById returns mapped domain model when found`() = runTest {
        whenever(tagDao.getTagById("tag-1")).thenReturn(testEntity)

        val result = repository.getTagById("tag-1")

        assertThat(result).isNotNull()
        assertThat(result!!.id).isEqualTo("tag-1")
        assertThat(result.name).isEqualTo("开心")
    }

    @Test
    fun `getTagById returns null when not found`() = runTest {
        whenever(tagDao.getTagById("nonexistent")).thenReturn(null)

        val result = repository.getTagById("nonexistent")

        assertThat(result).isNull()
    }

    @Test
    fun `createTag inserts entity and returns id`() = runTest {
        val result = repository.createTag(testDomain)

        assertThat(result).isEqualTo("tag-1")
        verify(tagDao).insert(org.mockito.kotlin.any())
    }

    @Test
    fun `deleteTag calls softDelete on dao`() = runTest {
        repository.deleteTag("tag-1")

        verify(tagDao).softDelete(org.mockito.kotlin.eq("tag-1"), org.mockito.kotlin.any())
    }

    @Test
    fun `addTagToEntry inserts entry tag and increments usage count`() = runTest {
        repository.addTagToEntry("entry-1", "tag-1")

        verify(tagDao).insertEntryTag(org.mockito.kotlin.any())
        verify(tagDao).incrementUsageCount(org.mockito.kotlin.eq("tag-1"), org.mockito.kotlin.any())
    }

    @Test
    fun `removeTagFromEntry delegates to dao`() = runTest {
        repository.removeTagFromEntry("entry-1", "tag-1")

        verify(tagDao).deleteEntryTag("entry-1", "tag-1")
    }

    @Test
    fun `getTagsForEntry maps entities to domain models`() = runTest {
        whenever(tagDao.getTagsByEntry("entry-1")).thenReturn(flowOf(listOf(testEntity)))

        val result = repository.getTagsForEntry("entry-1").first()

        assertThat(result).hasSize(1)
        assertThat(result[0].id).isEqualTo("tag-1")
    }

    @Test
    fun `getTagsForEntry returns empty list when no tags`() = runTest {
        whenever(tagDao.getTagsByEntry("entry-1")).thenReturn(flowOf(emptyList()))

        val result = repository.getTagsForEntry("entry-1").first()

        assertThat(result).isEmpty()
    }
}