package com.tuantuan.journal.data.mapper

import com.tuantuan.journal.data.local.db.entity.ChildEntity
import com.tuantuan.journal.domain.model.Child
import com.tuantuan.journal.domain.model.Gender
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.time.Instant
import java.time.LocalDate

class ChildMapperTest {

    private val testInstant = Instant.parse("2024-01-15T10:30:00Z")
    private val testLocalDate = LocalDate.of(2020, 6, 15)

    private val testEntity = ChildEntity(
        id = "child-1",
        name = "小明",
        nickname = "明明",
        birthDate = testLocalDate,
        gender = Gender.MALE,
        avatarPath = "/data/avatar/child1.png",
        birthWeight = 3.5,
        birthHeight = 50.0,
        bloodType = "A",
        birthPlace = "北京",
        notes = "测试备注",
        sortOrder = 1,
        isDeleted = false,
        createdAt = testInstant,
        updatedAt = testInstant
    )

    private val testDomain = Child(
        id = "child-1",
        name = "小明",
        nickname = "明明",
        birthDate = testLocalDate,
        gender = Gender.MALE,
        avatarPath = "/data/avatar/child1.png",
        birthWeight = 3.5,
        birthHeight = 50.0,
        bloodType = "A",
        birthPlace = "北京",
        notes = "测试备注",
        sortOrder = 1,
        isDeleted = false,
        createdAt = testInstant,
        updatedAt = testInstant
    )

    @Test
    fun `toDomain maps all fields correctly`() {
        val result = ChildMapper.toDomain(testEntity)

        assertThat(result.id).isEqualTo("child-1")
        assertThat(result.name).isEqualTo("小明")
        assertThat(result.nickname).isEqualTo("明明")
        assertThat(result.birthDate).isEqualTo(testLocalDate)
        assertThat(result.gender).isEqualTo(Gender.MALE)
        assertThat(result.avatarPath).isEqualTo("/data/avatar/child1.png")
        assertThat(result.birthWeight).isEqualTo(3.5)
        assertThat(result.birthHeight).isEqualTo(50.0)
        assertThat(result.bloodType).isEqualTo("A")
        assertThat(result.birthPlace).isEqualTo("北京")
        assertThat(result.notes).isEqualTo("测试备注")
        assertThat(result.sortOrder).isEqualTo(1)
        assertThat(result.isDeleted).isEqualTo(false)
        assertThat(result.createdAt).isEqualTo(testInstant)
        assertThat(result.updatedAt).isEqualTo(testInstant)
    }

    @Test
    fun `toEntity maps all fields correctly`() {
        val result = ChildMapper.toEntity(testDomain)

        assertThat(result.id).isEqualTo("child-1")
        assertThat(result.name).isEqualTo("小明")
        assertThat(result.nickname).isEqualTo("明明")
        assertThat(result.birthDate).isEqualTo(testLocalDate)
        assertThat(result.gender).isEqualTo(Gender.MALE)
        assertThat(result.avatarPath).isEqualTo("/data/avatar/child1.png")
        assertThat(result.birthWeight).isEqualTo(3.5)
        assertThat(result.birthHeight).isEqualTo(50.0)
        assertThat(result.bloodType).isEqualTo("A")
        assertThat(result.birthPlace).isEqualTo("北京")
        assertThat(result.notes).isEqualTo("测试备注")
        assertThat(result.sortOrder).isEqualTo(1)
        assertThat(result.isDeleted).isEqualTo(false)
        assertThat(result.createdAt).isEqualTo(testInstant)
        assertThat(result.updatedAt).isEqualTo(testInstant)
    }

    @Test
    fun `toDomain handles nullable fields as null`() {
        val entityWithNulls = testEntity.copy(
            gender = null,
            avatarPath = null,
            birthWeight = null,
            birthHeight = null,
            bloodType = null,
            birthPlace = null,
            notes = null
        )

        val result = ChildMapper.toDomain(entityWithNulls)

        assertThat(result.gender).isNull()
        assertThat(result.avatarPath).isNull()
        assertThat(result.birthWeight).isNull()
        assertThat(result.birthHeight).isNull()
        assertThat(result.bloodType).isNull()
        assertThat(result.birthPlace).isNull()
        assertThat(result.notes).isNull()
    }

    @Test
    fun `round trip entity to domain to entity preserves all fields`() {
        val result = ChildMapper.toEntity(ChildMapper.toDomain(testEntity))
        assertThat(result).isEqualTo(testEntity)
    }

    @Test
    fun `round trip domain to entity to domain preserves all fields`() {
        val result = ChildMapper.toDomain(ChildMapper.toEntity(testDomain))
        assertThat(result).isEqualTo(testDomain)
    }
}