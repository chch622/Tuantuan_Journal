package com.tuantuan.journal.data.mapper

import com.tuantuan.journal.data.local.db.entity.TagEntity
import com.tuantuan.journal.domain.model.Tag
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.time.Instant

class TagMapperTest {

    private val testInstant = Instant.parse("2024-01-15T10:30:00Z")

    private val testEntity = TagEntity(
        id = "tag-1",
        name = "里程碑",
        color = "#FF5722",
        category = "成长",
        usageCount = 5,
        isDeleted = false,
        createdAt = testInstant,
        updatedAt = testInstant
    )

    private val testDomain = Tag(
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
    fun `toDomain maps all fields correctly`() {
        val result = TagMapper.toDomain(testEntity)

        assertThat(result.id).isEqualTo("tag-1")
        assertThat(result.name).isEqualTo("里程碑")
        assertThat(result.color).isEqualTo("#FF5722")
        assertThat(result.category).isEqualTo("成长")
        assertThat(result.usageCount).isEqualTo(5)
        assertThat(result.isDeleted).isEqualTo(false)
        assertThat(result.createdAt).isEqualTo(testInstant)
        assertThat(result.updatedAt).isEqualTo(testInstant)
    }

    @Test
    fun `toEntity maps all fields correctly`() {
        val result = TagMapper.toEntity(testDomain)

        assertThat(result.id).isEqualTo("tag-1")
        assertThat(result.name).isEqualTo("里程碑")
        assertThat(result.color).isEqualTo("#FF5722")
        assertThat(result.category).isEqualTo("成长")
        assertThat(result.usageCount).isEqualTo(5)
        assertThat(result.isDeleted).isEqualTo(false)
        assertThat(result.createdAt).isEqualTo(testInstant)
        assertThat(result.updatedAt).isEqualTo(testInstant)
    }

    @Test
    fun `toDomain handles nullable fields as null`() {
        val entityWithNulls = testEntity.copy(
            color = null,
            category = null
        )

        val result = TagMapper.toDomain(entityWithNulls)

        assertThat(result.color).isNull()
        assertThat(result.category).isNull()
    }

    @Test
    fun `round trip entity to domain to entity preserves all fields`() {
        val result = TagMapper.toEntity(TagMapper.toDomain(testEntity))
        assertThat(result).isEqualTo(testEntity)
    }

    @Test
    fun `round trip domain to entity to domain preserves all fields`() {
        val result = TagMapper.toDomain(TagMapper.toEntity(testDomain))
        assertThat(result).isEqualTo(testDomain)
    }
}