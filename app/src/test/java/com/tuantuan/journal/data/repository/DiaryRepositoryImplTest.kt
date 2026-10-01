package com.tuantuan.journal.data.repository

import com.tuantuan.journal.data.local.db.dao.DiaryEntryDao
import com.tuantuan.journal.data.local.db.dao.MediaItemDao
import com.tuantuan.journal.data.local.db.dao.TagDao
import com.tuantuan.journal.data.local.db.entity.DiaryEntryEntity
import com.tuantuan.journal.data.local.db.entity.MediaItemEntity
import com.tuantuan.journal.data.local.db.entity.TagEntity
import com.tuantuan.journal.domain.model.DiaryEntry
import com.tuantuan.journal.domain.model.Mood
import com.tuantuan.journal.domain.model.MediaType
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

class DiaryRepositoryImplTest {
    private lateinit var diaryEntryDao: DiaryEntryDao
    private lateinit var mediaItemDao: MediaItemDao
    private lateinit var tagDao: TagDao
    private lateinit var repository: DiaryRepositoryImpl

    private val testInstant = Instant.parse("2024-01-15T10:30:00Z")

    private val testEntity = DiaryEntryEntity(
        id = "entry-1", childId = "child-1", title = "测试日记",
        content = "今天很开心", eventDateTime = testInstant,
        mood = Mood.HAPPY, weather = null, location = null,
        isFavorite = false, isDeleted = false,
        createdAt = testInstant, updatedAt = testInstant
    )

    private val testMediaEntity = MediaItemEntity(
        id = "media-1", entryId = "entry-1", mediaType = MediaType.PHOTO,
        filePath = "/data/photo.jpg", fileName = "photo.jpg",
        fileSize = 1024L, mimeType = "image/jpeg",
        width = 800, height = 600, duration = null,
        thumbnailPath = "/data/thumb.jpg", sortOrder = 0, isDeleted = false,
        createdAt = testInstant, updatedAt = testInstant
    )

    private val testTagEntity = TagEntity(
        id = "tag-1", name = "开心", color = "#FF0000",
        category = null, usageCount = 5, isDeleted = false,
        createdAt = testInstant, updatedAt = testInstant
    )

    @Before
    fun setup() {
        diaryEntryDao = mock()
        mediaItemDao = mock()
        tagDao = mock()
        repository = DiaryRepositoryImpl(diaryEntryDao, mediaItemDao, tagDao)
    }

    @Test
    fun `getEntriesByChild maps entities to domain models`() = runTest {
        whenever(diaryEntryDao.getEntriesByChild("child-1")).thenReturn(flowOf(listOf(testEntity)))

        val result = repository.getEntriesByChild("child-1").first()

        assertThat(result).hasSize(1)
        assertThat(result[0].id).isEqualTo("entry-1")
        assertThat(result[0].title).isEqualTo("测试日记")
    }

    @Test
    fun `getEntriesByChild returns empty list when no entries`() = runTest {
        whenever(diaryEntryDao.getEntriesByChild("child-1")).thenReturn(flowOf(emptyList()))

        val result = repository.getEntriesByChild("child-1").first()

        assertThat(result).isEmpty()
    }

    @Test
    fun `getEntryById returns entry with media items and tags`() = runTest {
        whenever(diaryEntryDao.getEntryById("entry-1")).thenReturn(testEntity)
        whenever(mediaItemDao.getMediaByEntry("entry-1")).thenReturn(flowOf(listOf(testMediaEntity)))
        whenever(tagDao.getTagsByEntry("entry-1")).thenReturn(flowOf(listOf(testTagEntity)))

        val result = repository.getEntryById("entry-1")

        assertThat(result).isNotNull()
        assertThat(result!!.id).isEqualTo("entry-1")
        assertThat(result.mediaItems).hasSize(1)
        assertThat(result.mediaItems[0].id).isEqualTo("media-1")
        assertThat(result.tags).hasSize(1)
        assertThat(result.tags[0].id).isEqualTo("tag-1")
    }

    @Test
    fun `getEntryById returns null when not found`() = runTest {
        whenever(diaryEntryDao.getEntryById("nonexistent")).thenReturn(null)

        val result = repository.getEntryById("nonexistent")

        assertThat(result).isNull()
    }

    @Test
    fun `getEntryById returns entry with empty media and tags when none exist`() = runTest {
        whenever(diaryEntryDao.getEntryById("entry-1")).thenReturn(testEntity)
        whenever(mediaItemDao.getMediaByEntry("entry-1")).thenReturn(flowOf(emptyList()))
        whenever(tagDao.getTagsByEntry("entry-1")).thenReturn(flowOf(emptyList()))

        val result = repository.getEntryById("entry-1")

        assertThat(result).isNotNull()
        assertThat(result!!.mediaItems).isEmpty()
        assertThat(result.tags).isEmpty()
    }

    @Test
    fun `searchEntries maps entities to domain models`() = runTest {
        whenever(diaryEntryDao.searchEntries("child-1", "开心")).thenReturn(flowOf(listOf(testEntity)))

        val result = repository.searchEntries("child-1", "开心").first()

        assertThat(result).hasSize(1)
        assertThat(result[0].id).isEqualTo("entry-1")
    }

    @Test
    fun `getFavoriteEntries maps entities to domain models`() = runTest {
        whenever(diaryEntryDao.getFavoriteEntries("child-1")).thenReturn(flowOf(listOf(testEntity)))

        val result = repository.getFavoriteEntries("child-1").first()

        assertThat(result).hasSize(1)
    }

    @Test
    fun `getRecentEntries maps entities to domain models`() = runTest {
        whenever(diaryEntryDao.getRecentEntries("child-1", 5)).thenReturn(flowOf(listOf(testEntity)))

        val result = repository.getRecentEntries("child-1", 5).first()

        assertThat(result).hasSize(1)
    }

    @Test
    fun `getTodayEntries maps entities to domain models`() = runTest {
        whenever(diaryEntryDao.getTodayEntries("child-1")).thenReturn(flowOf(listOf(testEntity)))

        val result = repository.getTodayEntries("child-1").first()

        assertThat(result).hasSize(1)
    }

    @Test
    fun `createEntry inserts entity and returns id`() = runTest {
        val domain = DiaryEntry(
            id = "entry-1", childId = "child-1", title = "测试日记",
            content = "今天很开心", eventDateTime = testInstant,
            mood = Mood.HAPPY, weather = null, location = null,
            isFavorite = false, isDeleted = false,
            mediaItems = emptyList(), tags = emptyList(),
            createdAt = testInstant, updatedAt = testInstant
        )

        val result = repository.createEntry(domain)

        assertThat(result).isEqualTo("entry-1")
        verify(diaryEntryDao).insert(org.mockito.kotlin.any())
    }

    @Test
    fun `updateEntry delegates to dao update`() = runTest {
        val domain = DiaryEntry(
            id = "entry-1", childId = "child-1", title = "更新日记",
            content = "更新内容", eventDateTime = testInstant,
            mood = Mood.HAPPY, weather = null, location = null,
            isFavorite = false, isDeleted = false,
            mediaItems = emptyList(), tags = emptyList(),
            createdAt = testInstant, updatedAt = testInstant
        )

        repository.updateEntry(domain)

        verify(diaryEntryDao).update(org.mockito.kotlin.any())
    }

    @Test
    fun `deleteEntry calls softDelete on dao`() = runTest {
        repository.deleteEntry("entry-1")

        verify(diaryEntryDao).softDelete(org.mockito.kotlin.eq("entry-1"), org.mockito.kotlin.any())
    }

    @Test
    fun `toggleFavorite delegates to dao`() = runTest {
        repository.toggleFavorite("entry-1", true)

        verify(diaryEntryDao).toggleFavorite(
            org.mockito.kotlin.eq("entry-1"),
            org.mockito.kotlin.eq(true),
            org.mockito.kotlin.any()
        )
    }
}