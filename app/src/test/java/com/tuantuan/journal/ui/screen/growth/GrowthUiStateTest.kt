package com.tuantuan.journal.ui.screen.growth

import com.tuantuan.journal.domain.model.Child
import com.tuantuan.journal.domain.model.GrowthRecord
import com.tuantuan.journal.domain.model.GrowthType
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.time.Instant
import java.time.LocalDate

class GrowthUiStateTest {

    private val testInstant = Instant.parse("2024-06-15T10:30:00Z")
    private val testDate = LocalDate.of(2023, 6, 15)

    private val testChild = Child(
        id = "child-1", name = "团团", nickname = "小团",
        birthDate = testDate, gender = null,
        avatarPath = null, birthWeight = null, birthHeight = null,
        bloodType = null, birthPlace = null, notes = null,
        sortOrder = 0, isDeleted = false,
        createdAt = testInstant, updatedAt = testInstant
    )

    private val testRecord = GrowthRecord(
        id = "gr-1", childId = "child-1",
        recordType = GrowthType.HEIGHT, value = 75.5, unit = "cm",
        measureDate = LocalDate.of(2024, 6, 15), notes = null,
        isDeleted = false, createdAt = testInstant, updatedAt = testInstant
    )

    @Test
    fun `latestRecord returns first record by type`() {
        val state = GrowthUiState(
            recordsByType = mapOf(GrowthType.HEIGHT to listOf(testRecord))
        )

        assertThat(state.latestRecord(GrowthType.HEIGHT)).isEqualTo(testRecord)
        assertThat(state.latestRecord(GrowthType.WEIGHT)).isNull()
    }

    @Test
    fun `latestRecord returns null for empty type`() {
        val state = GrowthUiState(recordsByType = emptyMap())

        assertThat(state.latestRecord(GrowthType.HEIGHT)).isNull()
    }

    @Test
    fun `hasAnyData is true when records exist`() {
        val stateWithData = GrowthUiState(
            recordsByType = mapOf(GrowthType.HEIGHT to listOf(testRecord))
        )
        assertThat(stateWithData.hasAnyData).isTrue()
    }

    @Test
    fun `hasAnyData is false when no records`() {
        val stateEmpty = GrowthUiState(recordsByType = emptyMap())
        assertThat(stateEmpty.hasAnyData).isFalse()
    }

    @Test
    fun `hasAnyData is false when all type lists are empty`() {
        val state = GrowthUiState(
            recordsByType = mapOf(
                GrowthType.HEIGHT to emptyList(),
                GrowthType.WEIGHT to emptyList()
            )
        )
        assertThat(state.hasAnyData).isFalse()
    }

    @Test
    fun `selectedChild returns matching child`() {
        val state = GrowthUiState(
            children = listOf(testChild),
            selectedChildId = "child-1"
        )
        assertThat(state.selectedChild).isEqualTo(testChild)
    }

    @Test
    fun `selectedChild returns null for unknown id`() {
        val state = GrowthUiState(
            children = listOf(testChild),
            selectedChildId = "unknown"
        )
        assertThat(state.selectedChild).isNull()
    }

    @Test
    fun `selectedChild returns null when no children`() {
        val state = GrowthUiState(children = emptyList(), selectedChildId = "child-1")
        assertThat(state.selectedChild).isNull()
    }

    @Test
    fun `MILESTONE_PREVIEW_COUNT is 4`() {
        assertThat(GrowthUiState.MILESTONE_PREVIEW_COUNT).isEqualTo(4)
    }
}

class GrowthFormStateTest {

    @Test
    fun `isValid is true for positive number`() {
        val state = GrowthFormState(value = "75.5")
        assertThat(state.isValid).isTrue()
    }

    @Test
    fun `isValid is false for empty value`() {
        val state = GrowthFormState(value = "")
        assertThat(state.isValid).isFalse()
    }

    @Test
    fun `isValid is false for negative number`() {
        val state = GrowthFormState(value = "-5")
        assertThat(state.isValid).isFalse()
    }

    @Test
    fun `isValid is false for zero`() {
        val state = GrowthFormState(value = "0")
        assertThat(state.isValid).isFalse()
    }

    @Test
    fun `isValid is false for non-numeric string`() {
        val state = GrowthFormState(value = "abc")
        assertThat(state.isValid).isFalse()
    }

    @Test
    fun `isValid is true for integer value`() {
        val state = GrowthFormState(value = "75")
        assertThat(state.isValid).isTrue()
    }
}