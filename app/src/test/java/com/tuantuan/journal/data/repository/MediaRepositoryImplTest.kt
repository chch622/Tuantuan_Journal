package com.tuantuan.journal.data.repository

import com.tuantuan.journal.data.local.db.dao.MediaItemDao
import com.tuantuan.journal.data.local.db.entity.MediaItemEntity
import com.tuantuan.journal.data.local.file.MediaFileService
import com.tuantuan.journal.domain.model.MediaItem
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

class MediaRepositoryImplTest {
    private lateinit var mediaItemDao: MediaItemDao
    private lateinit var mediaFileManager: MediaFileService
    private lateinit var repository: MediaRepositoryImpl

    private val testInstant = Instant.parse("2024-01-15T10:30:00Z")

    private val testEntity = MediaItemEntity(
        id = "media-1", entryId = "entry-1", mediaType = MediaType.PHOTO,
        filePath = "/data/photo.jpg", fileName = "photo.jpg",
        fileSize = 1024L, mimeType = "image/jpeg",
        width = 800, height = 600, duration = null,
        thumbnailPath = "/data/thumb.jpg", sortOrder = 0, isDeleted = false,
        createdAt = testInstant, updatedAt = testInstant
    )

    private val testDomain = MediaItem(
        id = "media-1", entryId = "entry-1", mediaType = MediaType.PHOTO,
        filePath = "/data/photo.jpg", fileName = "photo.jpg",
        fileSize = 1024L, mimeType = "image/jpeg",
        width = 800, height = 600, duration = null,
        thumbnailPath = "/data/thumb.jpg", sortOrder = 0, isDeleted = false,
        createdAt = testInstant, updatedAt = testInstant
    )

    @Before
    fun setup() {
        mediaItemDao = mock()
        mediaFileManager = mock()
        repository = MediaRepositoryImpl(mediaItemDao, mediaFileManager)
    }

    @Test
    fun `getMediaByEntry maps entities to domain models`() = runTest {
        whenever(mediaItemDao.getMediaByEntry("entry-1")).thenReturn(flowOf(listOf(testEntity)))

        val result = repository.getMediaByEntry("entry-1").first()

        assertThat(result).hasSize(1)
        assertThat(result[0].id).isEqualTo("media-1")
        assertThat(result[0].mediaType).isEqualTo(MediaType.PHOTO)
    }

    @Test
    fun `getMediaByEntry returns empty list when no media`() = runTest {
        whenever(mediaItemDao.getMediaByEntry("entry-1")).thenReturn(flowOf(emptyList()))

        val result = repository.getMediaByEntry("entry-1").first()

        assertThat(result).isEmpty()
    }

    @Test
    fun `addMedia inserts entity and returns id`() = runTest {
        val result = repository.addMedia(testDomain)

        assertThat(result).isEqualTo("media-1")
        verify(mediaItemDao).insert(org.mockito.kotlin.any())
    }

    @Test
    fun `deleteMedia calls softDelete on dao`() = runTest {
        repository.deleteMedia("media-1")

        verify(mediaItemDao).softDelete(org.mockito.kotlin.eq("media-1"), org.mockito.kotlin.any())
    }

    @Test
    fun `deleteMediaByEntry soft deletes all media for entry`() = runTest {
        val entity1 = testEntity.copy(id = "media-1")
        val entity2 = testEntity.copy(id = "media-2")
        whenever(mediaItemDao.getMediaByEntry("entry-1")).thenReturn(flowOf(listOf(entity1, entity2)))

        repository.deleteMediaByEntry("entry-1")

        verify(mediaItemDao).softDelete(org.mockito.kotlin.eq("media-1"), org.mockito.kotlin.any())
        verify(mediaItemDao).softDelete(org.mockito.kotlin.eq("media-2"), org.mockito.kotlin.any())
    }

    @Test
    fun `deleteMediaByEntry with no media does nothing`() = runTest {
        whenever(mediaItemDao.getMediaByEntry("entry-1")).thenReturn(flowOf(emptyList()))

        repository.deleteMediaByEntry("entry-1")

        // No softDelete calls should be made
        verify(mediaItemDao, org.mockito.kotlin.never()).softDelete(org.mockito.kotlin.any(), org.mockito.kotlin.any())
    }

    @Test
    fun `getMediaCount returns count from dao`() = runTest {
        whenever(mediaItemDao.getMediaByEntry("entry-1")).thenReturn(flowOf(listOf(testEntity, testEntity.copy(id = "media-2"))))

        val result = repository.getMediaCount("entry-1")

        assertThat(result).isEqualTo(2)
    }

    @Test
    fun `getMediaCount returns zero when no media`() = runTest {
        whenever(mediaItemDao.getMediaByEntry("entry-1")).thenReturn(flowOf(emptyList()))

        val result = repository.getMediaCount("entry-1")

        assertThat(result).isEqualTo(0)
    }
}