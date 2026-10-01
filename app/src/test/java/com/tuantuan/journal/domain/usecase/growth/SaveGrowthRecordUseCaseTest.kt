package com.tuantuan.journal.domain.usecase.growth

import com.tuantuan.journal.domain.model.GrowthRecord
import com.tuantuan.journal.domain.model.GrowthType
import com.tuantuan.journal.domain.repository.GrowthRepository
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import com.google.common.truth.Truth.assertThat
import java.time.Instant
import java.time.LocalDate

class SaveGrowthRecordUseCaseTest {
    private lateinit var repository: GrowthRepository
    private lateinit var useCase: SaveGrowthRecordUseCase

    private val testInstant = Instant.parse("2024-06-15T10:30:00Z")
    private val testDate = LocalDate.of(2024, 6, 15)

    private val testRecord = GrowthRecord(
        id = "gr-1", childId = "child-1",
        recordType = GrowthType.HEIGHT, value = 75.5, unit = "cm",
        measureDate = testDate, notes = null,
        isDeleted = false, createdAt = testInstant, updatedAt = testInstant
    )

    @Before
    fun setup() {
        repository = mock()
        useCase = SaveGrowthRecordUseCase(repository)
    }

    @Test
    fun `create delegates to repository and returns id`() = runTest {
        whenever(repository.createRecord(testRecord)).thenReturn("gr-1")

        val result = useCase.create(testRecord)

        verify(repository).createRecord(testRecord)
        assertThat(result).isEqualTo("gr-1")
    }

    @Test
    fun `update delegates to repository`() = runTest {
        useCase.update(testRecord)

        verify(repository).updateRecord(testRecord)
    }
}