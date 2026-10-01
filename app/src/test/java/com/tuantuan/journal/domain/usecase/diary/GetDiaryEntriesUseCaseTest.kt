package com.tuantuan.journal.domain.usecase.diary

import com.tuantuan.journal.domain.model.DiaryEntry
import com.tuantuan.journal.domain.model.Mood
import com.tuantuan.journal.domain.model.Weather
import com.tuantuan.journal.domain.repository.DiaryRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import com.google.common.truth.Truth.assertThat
import java.time.Instant

class GetDiaryEntriesUseCaseTest {
    private lateinit var repository: DiaryRepository
    private lateinit var useCase: GetDiaryEntriesUseCase

    private val testEntry = DiaryEntry(
        id = "entry-1", childId = "child-1", title = "Test",
        content = "Test content", eventDateTime = Instant.now(),
        mood = Mood.HAPPY, weather = Weather.SUNNY, location = null,
        isFavorite = false, isDeleted = false,
        createdAt = Instant.now(), updatedAt = Instant.now()
    )

    @Before
    fun setup() {
        repository = mock()
        useCase = GetDiaryEntriesUseCase(repository)
    }

    @Test
    fun `returns entries by child`() = runTest {
        whenever(repository.getEntriesByChild("child-1")).thenReturn(flowOf(listOf(testEntry)))

        val result = useCase("child-1").first()

        assertThat(result).containsExactly(testEntry)
    }

    @Test
    fun `returns favorite entries`() = runTest {
        whenever(repository.getFavoriteEntries("child-1")).thenReturn(flowOf(listOf(testEntry)))

        val result = useCase.favorites("child-1").first()

        assertThat(result).containsExactly(testEntry)
    }

    @Test
    fun `returns recent entries`() = runTest {
        whenever(repository.getRecentEntries("child-1", 5)).thenReturn(flowOf(listOf(testEntry)))

        val result = useCase.recent("child-1", 5).first()

        assertThat(result).containsExactly(testEntry)
    }

    @Test
    fun `returns today entries`() = runTest {
        whenever(repository.getTodayEntries("child-1")).thenReturn(flowOf(listOf(testEntry)))

        val result = useCase.today("child-1").first()

        assertThat(result).containsExactly(testEntry)
    }
}