package com.tuantuan.journal.data.repository

import com.tuantuan.journal.data.local.db.dao.GrowthRecordDao
import com.tuantuan.journal.data.local.db.entity.GrowthRecordEntity
import com.tuantuan.journal.domain.model.GrowthType
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

class GrowthRepositoryImplTest {
    private lateinit var dao: GrowthRecordDao
    private lateinit var repository: GrowthRepositoryImpl

    private val testInstant = Instant.parse("2024-06-15T10:30:00Z")
    private val testDate = LocalDate.of(2024, 6, 15)

    private val testEntity = GrowthRecordEntity(
        id = "gr-1", childId = "child-1",
        recordType = GrowthType.HEIGHT, value = 75.5, unit = "cm",
        measureDate = testDate, notes = "早晨测量",
        isDeleted = false, createdAt = testInstant, updatedAt = testInstant
    )

    private val testEntityWeight = GrowthRecordEntity(
        id = "gr-2", childId = "child-1",
        recordType = GrowthType.WEIGHT, value = 10.0, unit = "kg",
        measureDate = testDate, notes = null,
        isDeleted = false, createdAt = testInstant, updatedAt = testInstant
    )

    @Before
    fun setup() {
        dao = mock()
        repository = GrowthRepositoryImpl(dao)
    }

    @Test
    fun `getRecordsByChild maps entities to domain models`() = runTest {
        whenever(dao.getActiveRecordsByChild("child-1")).thenReturn(flowOf(listOf(testEntity, testEntityWeight)))

        val result = repository.getRecordsByChild("child-1").first()

        assertThat(result).hasSize(2)
        assertThat(result[0].id).isEqualTo("gr-1")
        assertThat(result[0].recordType).isEqualTo(GrowthType.HEIGHT)
        assertThat(result[1].recordType).isEqualTo(GrowthType.WEIGHT)
    }

    @Test
    fun `getRecordsByType filters by growth type`() = runTest {
        whenever(dao.getActiveRecordsByType("child-1", GrowthType.HEIGHT)).thenReturn(flowOf(listOf(testEntity)))

        val result = repository.getRecordsByType("child-1", GrowthType.HEIGHT).first()

        assertThat(result).hasSize(1)
        assertThat(result[0].recordType).isEqualTo(GrowthType.HEIGHT)
    }

    @Test
    fun `getRecordById returns mapped domain model`() = runTest {
        whenever(dao.getRecordById("gr-1")).thenReturn(testEntity)

        val result = repository.getRecordById("gr-1")

        assertThat(result).isNotNull()
        assertThat(result!!.id).isEqualTo("gr-1")
        assertThat(result.value).isEqualTo(75.5)
    }

    @Test
    fun `getRecordById returns null for non-existent record`() = runTest {
        whenever(dao.getRecordById("non-existent")).thenReturn(null)

        val result = repository.getRecordById("non-existent")

        assertThat(result).isNull()
    }

    @Test
    fun `createRecord inserts entity and returns id`() = runTest {
        val domain = GrowthRecordMapperTestHelper.toDomain(testEntity)

        val result = repository.createRecord(domain)

        verify(dao).insert(org.mockito.kotlin.any())
        assertThat(result).isEqualTo("gr-1")
    }

    @Test
    fun `updateRecord updates entity via dao`() = runTest {
        val domain = GrowthRecordMapperTestHelper.toDomain(testEntity)

        repository.updateRecord(domain)

        verify(dao).update(org.mockito.kotlin.any())
    }

    @Test
    fun `deleteRecord calls soft delete on dao`() = runTest {
        repository.deleteRecord("gr-1")

        verify(dao).softDelete(org.mockito.kotlin.eq("gr-1"), org.mockito.kotlin.any())
    }

    @Test
    fun `getLatestRecord returns mapped domain model`() = runTest {
        whenever(dao.getLatestRecord("child-1", GrowthType.HEIGHT)).thenReturn(testEntity)

        val result = repository.getLatestRecord("child-1", GrowthType.HEIGHT)

        assertThat(result).isNotNull()
        assertThat(result!!.id).isEqualTo("gr-1")
    }

    @Test
    fun `getLatestRecord returns null when no records exist`() = runTest {
        whenever(dao.getLatestRecord("child-1", GrowthType.HEIGHT)).thenReturn(null)

        val result = repository.getLatestRecord("child-1", GrowthType.HEIGHT)

        assertThat(result).isNull()
    }

    /** Helper to avoid direct mapper dependency in test setup */
    private object GrowthRecordMapperTestHelper {
        fun toDomain(entity: GrowthRecordEntity) = com.tuantuan.journal.data.mapper.GrowthRecordMapper.toDomain(entity)
    }
}