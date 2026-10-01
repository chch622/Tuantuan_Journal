package com.tuantuan.journal.domain.usecase.diary

import com.tuantuan.journal.domain.model.DiaryEntry
import com.tuantuan.journal.domain.model.Mood
import com.tuantuan.journal.domain.repository.DiaryRepository
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import com.google.common.truth.Truth.assertThat
import java.time.Instant

class GetDiaryEntryUseCaseTest {
    private lateinit var repository: DiaryRepository
    private lateinit var useCase: GetDiaryEntryUseCase

    private val testEntry = DiaryEntry(
        id = "entry-1",
        childId = "child-1",
        title = "Test",
        content = "Content",
        eventDateTime = Instant.now(),
        mood = Mood.HAPPY,
        weather = null,
        location = null,
        isFavorite = false,
        isDeleted = false,
        createdAt = Instant.now(),
        updatedAt = Instant.now()
    )

    @Before
    fun setup() {
        repository = mock()
        useCase = GetDiaryEntryUseCase(repository)
    }

    @Test
    fun `invoke returns entry when found`() = runTest {
        whenever(repository.getEntryById("entry-1")).thenReturn(testEntry)

        val result = useCase("entry-1")

        assertThat(result).isEqualTo(testEntry)
    }

    @Test
    fun `invoke returns null when not found`() = runTest {
        whenever(repository.getEntryById("nonexistent")).thenReturn(null)

        val result = useCase("nonexistent")

        assertThat(result).isNull()
    }

    @Test
    fun `invoke returns entry with media items and tags`() = runTest {
        val entryWithDetails = testEntry.copy(
            id = "entry-with-details",
            mediaItems = emptyList(),
            tags = emptyList()
        )
        whenever(repository.getEntryById("entry-with-details")).thenReturn(entryWithDetails)

        val result = useCase("entry-with-details")

        assertThat(result).isNotNull()
        assertThat(result!!.mediaItems).isEmpty()
        assertThat(result.tags).isEmpty()
    }
}