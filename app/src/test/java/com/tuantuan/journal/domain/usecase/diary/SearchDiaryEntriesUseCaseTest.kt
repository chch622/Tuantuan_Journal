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

class SearchDiaryEntriesUseCaseTest {
    private lateinit var repository: DiaryRepository
    private lateinit var useCase: SearchDiaryEntriesUseCase

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
        useCase = SearchDiaryEntriesUseCase(repository)
    }

    @Test
    fun `search returns matching entries`() = runTest {
        whenever(repository.searchEntries("child-1", "test")).thenReturn(flowOf(listOf(testEntry)))

        val result = useCase("child-1", "test").first()

        assertThat(result).containsExactly(testEntry)
    }

    @Test
    fun `search returns empty list for no matches`() = runTest {
        whenever(repository.searchEntries("child-1", "nonexistent")).thenReturn(flowOf(emptyList()))

        val result = useCase("child-1", "nonexistent").first()

        assertThat(result).isEmpty()
    }
}