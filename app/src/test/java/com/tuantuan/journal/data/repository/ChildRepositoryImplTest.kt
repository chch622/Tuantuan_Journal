package com.tuantuan.journal.data.repository

import com.tuantuan.journal.data.local.db.dao.ChildDao
import com.tuantuan.journal.data.local.db.entity.ChildEntity
import com.tuantuan.journal.domain.model.Child
import com.tuantuan.journal.domain.model.Gender
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
import java.time.LocalDate

class ChildRepositoryImplTest {
    private lateinit var childDao: ChildDao
    private lateinit var repository: ChildRepositoryImpl

    private val testInstant = Instant.parse("2024-01-15T10:30:00Z")
    private val testLocalDate = LocalDate.of(2020, 6, 15)

    private val testEntity = ChildEntity(
        id = "child-1", name = "小明", nickname = "明明",
        birthDate = testLocalDate, gender = Gender.MALE,
        avatarPath = "/data/avatar.png", birthWeight = 3.5, birthHeight = 50.0,
        bloodType = "A", birthPlace = "北京", notes = "测试备注",
        sortOrder = 1, isDeleted = false,
        createdAt = testInstant, updatedAt = testInstant
    )

    private val testDomain = Child(
        id = "child-1", name = "小明", nickname = "明明",
        birthDate = testLocalDate, gender = Gender.MALE,
        avatarPath = "/data/avatar.png", birthWeight = 3.5, birthHeight = 50.0,
        bloodType = "A", birthPlace = "北京", notes = "测试备注",
        sortOrder = 1, isDeleted = false,
        createdAt = testInstant, updatedAt = testInstant
    )

    @Before
    fun setup() {
        childDao = mock()
        repository = ChildRepositoryImpl(childDao)
    }

    @Test
    fun `getAllActiveChildren maps entities to domain models`() = runTest {
        whenever(childDao.getAllActiveChildren()).thenReturn(flowOf(listOf(testEntity)))

        val result = repository.getAllActiveChildren().first()

        assertThat(result).hasSize(1)
        assertThat(result[0].id).isEqualTo("child-1")
        assertThat(result[0].name).isEqualTo("小明")
        assertThat(result[0].gender).isEqualTo(Gender.MALE)
    }

    @Test
    fun `getAllActiveChildren returns empty list when no children`() = runTest {
        whenever(childDao.getAllActiveChildren()).thenReturn(flowOf(emptyList()))

        val result = repository.getAllActiveChildren().first()

        assertThat(result).isEmpty()
    }

    @Test
    fun `getChildById returns mapped domain model when found`() = runTest {
        whenever(childDao.getChildById("child-1")).thenReturn(testEntity)

        val result = repository.getChildById("child-1")

        assertThat(result).isNotNull()
        assertThat(result!!.id).isEqualTo("child-1")
        assertThat(result.name).isEqualTo("小明")
    }

    @Test
    fun `getChildById returns null when not found`() = runTest {
        whenever(childDao.getChildById("nonexistent")).thenReturn(null)

        val result = repository.getChildById("nonexistent")

        assertThat(result).isNull()
    }

    @Test
    fun `createChild inserts entity and returns id`() = runTest {
        val result = repository.createChild(testDomain)

        assertThat(result).isEqualTo("child-1")
        verify(childDao).insert(org.mockito.kotlin.any())
    }

    @Test
    fun `updateChild delegates to dao update`() = runTest {
        repository.updateChild(testDomain)

        verify(childDao).update(org.mockito.kotlin.any())
    }

    @Test
    fun `deleteChild calls softDelete on dao`() = runTest {
        repository.deleteChild("child-1")

        verify(childDao).softDelete(org.mockito.kotlin.eq("child-1"), org.mockito.kotlin.any())
    }

    @Test
    fun `getActiveChildCount delegates to dao`() = runTest {
        whenever(childDao.getActiveChildCount()).thenReturn(3)

        val result = repository.getActiveChildCount()

        assertThat(result).isEqualTo(3)
    }
}