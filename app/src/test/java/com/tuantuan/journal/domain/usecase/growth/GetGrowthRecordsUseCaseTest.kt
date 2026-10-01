package com.tuantuan.journal.domain.usecase.growth

import com.tuantuan.journal.domain.model.GrowthRecord
import com.tuantuan.journal.domain.model.GrowthType
import com.tuantuan.journal.domain.repository.GrowthRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import com.google.common.truth.Truth.assertThat
import java.time.Instant
import java.time.LocalDate

class GetGrowthRecordsUseCaseTest {
    private lateinit var repository: GrowthRepository
    private lateinit var useCase: GetGrowthRecordsUseCase

    private val testInstant = Instant.parse("2024-06-15T10:30:00Z")
    private val testDate = LocalDate.of(2024, 6, 15)

    private val testRecords = listOf(
        GrowthRecord(
            id = "gr-1", childId = "child-1",
            recordType = GrowthType.HEIGHT, value = 75.5, unit = "cm",
            measureDate = testDate, notes = null,
            isDeleted = false, createdAt = testInstant, updatedAt = testInstant
        ),
        GrowthRecord(
            id = "gr-2", childId = "child-1",
            recordType = GrowthType.WEIGHT, value = 10.0, unit = "kg",
            measureDate = testDate, notes = null,
            isDeleted = false, createdAt = testInstant, updatedAt = testInstant
        )
    )

    @Before
    fun setup() {
        repository = mock()
        useCase = GetGrowthRecordsUseCase(repository)
    }

    @Test
    fun `invoke returns all records for child`() = runTest {
        whenever(repository.getRecordsByChild("child-1")).thenReturn(flowOf(testRecords))

        val result = useCase("child-1").first()

        assertThat(result).hasSize(2)
        assertThat(result).isEqualTo(testRecords)
    }

    @Test
    fun `byType returns records filtered by type`() = runTest {
        val heightRecords = testRecords.filter { it.recordType == GrowthType.HEIGHT }
        whenever(repository.getRecordsByType("child-1", GrowthType.HEIGHT)).thenReturn(flowOf(heightRecords))

        val result = useCase.byType("child-1", GrowthType.HEIGHT).first()

        assertThat(result).hasSize(1)
        assertThat(result[0].recordType).isEqualTo(GrowthType.HEIGHT)
    }

    @Test
    fun `latest returns most recent record of type`() = runTest {
        whenever(repository.getLatestRecord("child-1", GrowthType.HEIGHT)).thenReturn(testRecords[0])

        val result = useCase.latest("child-1", GrowthType.HEIGHT)

        assertThat(result).isNotNull()
        assertThat(result!!.recordType).isEqualTo(GrowthType.HEIGHT)
    }

    @Test
    fun `latest returns null when no records exist`() = runTest {
        whenever(repository.getLatestRecord("child-1", GrowthType.HEIGHT)).thenReturn(null)

        val result = useCase.latest("child-1", GrowthType.HEIGHT)

        assertThat(result).isNull()
    }

    @Test
    fun `invoke returns empty list when no records exist`() = runTest {
        whenever(repository.getRecordsByChild("child-1")).thenReturn(flowOf(emptyList()))

        val result = useCase("child-1").first()

        assertThat(result).isEmpty()
    }
}