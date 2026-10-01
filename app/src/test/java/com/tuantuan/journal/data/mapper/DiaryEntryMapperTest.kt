package com.tuantuan.journal.data.mapper

import com.tuantuan.journal.data.local.db.entity.DiaryEntryEntity
import com.tuantuan.journal.domain.model.DiaryEntry
import com.tuantuan.journal.domain.model.MediaItem
import com.tuantuan.journal.domain.model.Mood
import com.tuantuan.journal.domain.model.Tag
import com.tuantuan.journal.domain.model.Weather
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.time.Instant

class DiaryEntryMapperTest {

    private val testInstant = Instant.parse("2024-01-15T10:30:00Z")

    private val testEntity = DiaryEntryEntity(
        id = "entry-1",
        childId = "child-1",
        title = "今天很开心",
        content = "宝宝今天第一次翻身了！",
        eventDateTime = testInstant,
        mood = Mood.HAPPY,
        weather = Weather.SUNNY,
        location = "北京",
        isFavorite = true,
        isDeleted = false,
        createdAt = testInstant,
        updatedAt = testInstant
    )

    private val testDomain = DiaryEntry(
        id = "entry-1",
        childId = "child-1",
        title = "今天很开心",
        content = "宝宝今天第一次翻身了！",
        eventDateTime = testInstant,
        mood = Mood.HAPPY,
        weather = Weather.SUNNY,
        location = "北京",
        isFavorite = true,
        isDeleted = false,
        createdAt = testInstant,
        updatedAt = testInstant,
        mediaItems = emptyList(),
        tags = emptyList()
    )

    private val testMediaItem = MediaItem(
        id = "media-1",
        entryId = "entry-1",
        mediaType = com.tuantuan.journal.domain.model.MediaType.PHOTO,
        filePath = "/data/media/photo.jpg",
        fileName = "photo.jpg",
        fileSize = 2048L,
        mimeType = "image/jpeg",
        width = 1920,
        height = 1080,
        duration = null,
        thumbnailPath = "/data/media/photo_thumb.jpg",
        sortOrder = 0,
        isDeleted = false,
        createdAt = testInstant,
        updatedAt = testInstant
    )

    private val testTag = Tag(
        id = "tag-1",
        name = "里程碑",
        color = "#FF5722",
        category = "成长",
        usageCount = 5,
        isDeleted = false,
        createdAt = testInstant,
        updatedAt = testInstant
    )

    @Test
    fun `toDomain maps all entity fields correctly without mediaItems and tags`() {
        val result = DiaryEntryMapper.toDomain(testEntity)

        assertThat(result.id).isEqualTo("entry-1")
        assertThat(result.childId).isEqualTo("child-1")
        assertThat(result.title).isEqualTo("今天很开心")
        assertThat(result.content).isEqualTo("宝宝今天第一次翻身了！")
        assertThat(result.eventDateTime).isEqualTo(testInstant)
        assertThat(result.mood).isEqualTo(Mood.HAPPY)
        assertThat(result.weather).isEqualTo(Weather.SUNNY)
        assertThat(result.location).isEqualTo("北京")
        assertThat(result.isFavorite).isEqualTo(true)
        assertThat(result.isDeleted).isEqualTo(false)
        assertThat(result.createdAt).isEqualTo(testInstant)
        assertThat(result.updatedAt).isEqualTo(testInstant)
        assertThat(result.mediaItems).isEmpty()
        assertThat(result.tags).isEmpty()
    }

    @Test
    fun `toDomain maps all entity fields correctly with mediaItems and tags`() {
        val result = DiaryEntryMapper.toDomain(
            testEntity,
            mediaItems = listOf(testMediaItem),
            tags = listOf(testTag)
        )

        assertThat(result.id).isEqualTo("entry-1")
        assertThat(result.childId).isEqualTo("child-1")
        assertThat(result.title).isEqualTo("今天很开心")
        assertThat(result.content).isEqualTo("宝宝今天第一次翻身了！")
        assertThat(result.eventDateTime).isEqualTo(testInstant)
        assertThat(result.mood).isEqualTo(Mood.HAPPY)
        assertThat(result.weather).isEqualTo(Weather.SUNNY)
        assertThat(result.location).isEqualTo("北京")
        assertThat(result.isFavorite).isEqualTo(true)
        assertThat(result.isDeleted).isEqualTo(false)
        assertThat(result.createdAt).isEqualTo(testInstant)
        assertThat(result.updatedAt).isEqualTo(testInstant)
        assertThat(result.mediaItems).hasSize(1)
        assertThat(result.mediaItems[0].id).isEqualTo("media-1")
        assertThat(result.tags).hasSize(1)
        assertThat(result.tags[0].id).isEqualTo("tag-1")
    }

    @Test
    fun `toDomain handles nullable fields as null`() {
        val entityWithNulls = testEntity.copy(
            title = null,
            mood = null,
            weather = null,
            location = null
        )

        val result = DiaryEntryMapper.toDomain(entityWithNulls)

        assertThat(result.title).isNull()
        assertThat(result.mood).isNull()
        assertThat(result.weather).isNull()
        assertThat(result.location).isNull()
    }

    @Test
    fun `toEntity maps all domain fields correctly excluding mediaItems and tags`() {
        val domainWithMediaAndTags = testDomain.copy(
            mediaItems = listOf(testMediaItem),
            tags = listOf(testTag)
        )

        val result = DiaryEntryMapper.toEntity(domainWithMediaAndTags)

        assertThat(result.id).isEqualTo("entry-1")
        assertThat(result.childId).isEqualTo("child-1")
        assertThat(result.title).isEqualTo("今天很开心")
        assertThat(result.content).isEqualTo("宝宝今天第一次翻身了！")
        assertThat(result.eventDateTime).isEqualTo(testInstant)
        assertThat(result.mood).isEqualTo(Mood.HAPPY)
        assertThat(result.weather).isEqualTo(Weather.SUNNY)
        assertThat(result.location).isEqualTo("北京")
        assertThat(result.isFavorite).isEqualTo(true)
        assertThat(result.isDeleted).isEqualTo(false)
        assertThat(result.createdAt).isEqualTo(testInstant)
        assertThat(result.updatedAt).isEqualTo(testInstant)
    }

    @Test
    fun `toEntity does not include mediaItems or tags fields`() {
        // DiaryEntryEntity has 12 fields, none of which are mediaItems or tags
        val result = DiaryEntryMapper.toEntity(testDomain)
        // Verify the entity is created correctly - mediaItems/tags are not entity fields
        assertThat(result.id).isEqualTo("entry-1")
        assertThat(result.childId).isEqualTo("child-1")
    }

    @Test
    fun `round trip entity to domain to entity preserves entity fields`() {
        val domain = DiaryEntryMapper.toDomain(testEntity)
        val result = DiaryEntryMapper.toEntity(domain)
        assertThat(result).isEqualTo(testEntity)
    }

    @Test
    fun `round trip domain to entity to domain loses mediaItems and tags`() {
        val domainWithMedia = testDomain.copy(
            mediaItems = listOf(testMediaItem),
            tags = listOf(testTag)
        )
        val entity = DiaryEntryMapper.toEntity(domainWithMedia)
        val result = DiaryEntryMapper.toDomain(entity)

        // mediaItems and tags are lost in toEntity conversion
        assertThat(result.mediaItems).isEmpty()
        assertThat(result.tags).isEmpty()
        // but other fields are preserved
        assertThat(result.id).isEqualTo("entry-1")
        assertThat(result.childId).isEqualTo("child-1")
    }
}