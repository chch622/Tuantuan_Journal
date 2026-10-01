package com.tuantuan.journal.data.mapper

import com.tuantuan.journal.data.local.db.entity.MediaItemEntity
import com.tuantuan.journal.domain.model.MediaItem
import com.tuantuan.journal.domain.model.MediaType
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.time.Instant

class MediaItemMapperTest {

    private val testInstant = Instant.parse("2024-01-15T10:30:00Z")

    private val testEntity = MediaItemEntity(
        id = "media-1",
        entryId = "entry-1",
        mediaType = MediaType.PHOTO,
        filePath = "/data/media/entry-1/photo.jpg",
        fileName = "photo.jpg",
        fileSize = 2048L,
        mimeType = "image/jpeg",
        width = 1920,
        height = 1080,
        duration = null,
        thumbnailPath = "/data/media/entry-1/photo_thumb.jpg",
        sortOrder = 0,
        isDeleted = false,
        createdAt = testInstant,
        updatedAt = testInstant
    )

    private val testDomain = MediaItem(
        id = "media-1",
        entryId = "entry-1",
        mediaType = MediaType.PHOTO,
        filePath = "/data/media/entry-1/photo.jpg",
        fileName = "photo.jpg",
        fileSize = 2048L,
        mimeType = "image/jpeg",
        width = 1920,
        height = 1080,
        duration = null,
        thumbnailPath = "/data/media/entry-1/photo_thumb.jpg",
        sortOrder = 0,
        isDeleted = false,
        createdAt = testInstant,
        updatedAt = testInstant
    )

    @Test
    fun `toDomain maps all fields correctly`() {
        val result = MediaItemMapper.toDomain(testEntity)

        assertThat(result.id).isEqualTo("media-1")
        assertThat(result.entryId).isEqualTo("entry-1")
        assertThat(result.mediaType).isEqualTo(MediaType.PHOTO)
        assertThat(result.filePath).isEqualTo("/data/media/entry-1/photo.jpg")
        assertThat(result.fileName).isEqualTo("photo.jpg")
        assertThat(result.fileSize).isEqualTo(2048L)
        assertThat(result.mimeType).isEqualTo("image/jpeg")
        assertThat(result.width).isEqualTo(1920)
        assertThat(result.height).isEqualTo(1080)
        assertThat(result.duration).isNull()
        assertThat(result.thumbnailPath).isEqualTo("/data/media/entry-1/photo_thumb.jpg")
        assertThat(result.sortOrder).isEqualTo(0)
        assertThat(result.isDeleted).isEqualTo(false)
        assertThat(result.createdAt).isEqualTo(testInstant)
        assertThat(result.updatedAt).isEqualTo(testInstant)
    }

    @Test
    fun `toEntity maps all fields correctly`() {
        val result = MediaItemMapper.toEntity(testDomain)

        assertThat(result.id).isEqualTo("media-1")
        assertThat(result.entryId).isEqualTo("entry-1")
        assertThat(result.mediaType).isEqualTo(MediaType.PHOTO)
        assertThat(result.filePath).isEqualTo("/data/media/entry-1/photo.jpg")
        assertThat(result.fileName).isEqualTo("photo.jpg")
        assertThat(result.fileSize).isEqualTo(2048L)
        assertThat(result.mimeType).isEqualTo("image/jpeg")
        assertThat(result.width).isEqualTo(1920)
        assertThat(result.height).isEqualTo(1080)
        assertThat(result.duration).isNull()
        assertThat(result.thumbnailPath).isEqualTo("/data/media/entry-1/photo_thumb.jpg")
        assertThat(result.sortOrder).isEqualTo(0)
        assertThat(result.isDeleted).isEqualTo(false)
        assertThat(result.createdAt).isEqualTo(testInstant)
        assertThat(result.updatedAt).isEqualTo(testInstant)
    }

    @Test
    fun `toDomain handles nullable fields as null`() {
        val entityWithNulls = testEntity.copy(
            width = null,
            height = null,
            duration = null,
            thumbnailPath = null
        )

        val result = MediaItemMapper.toDomain(entityWithNulls)

        assertThat(result.width).isNull()
        assertThat(result.height).isNull()
        assertThat(result.duration).isNull()
        assertThat(result.thumbnailPath).isNull()
    }

    @Test
    fun `toDomain maps video type with duration correctly`() {
        val videoEntity = testEntity.copy(
            mediaType = MediaType.VIDEO,
            duration = 30000L
        )

        val result = MediaItemMapper.toDomain(videoEntity)

        assertThat(result.mediaType).isEqualTo(MediaType.VIDEO)
        assertThat(result.duration).isEqualTo(30000L)
    }

    @Test
    fun `round trip entity to domain to entity preserves all fields`() {
        val result = MediaItemMapper.toEntity(MediaItemMapper.toDomain(testEntity))
        assertThat(result).isEqualTo(testEntity)
    }

    @Test
    fun `round trip domain to entity to domain preserves all fields`() {
        val result = MediaItemMapper.toDomain(MediaItemMapper.toEntity(testDomain))
        assertThat(result).isEqualTo(testDomain)
    }
}