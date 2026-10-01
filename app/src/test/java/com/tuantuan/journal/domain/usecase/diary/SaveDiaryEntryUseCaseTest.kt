package com.tuantuan.journal.domain.usecase.diary

import com.tuantuan.journal.domain.model.DiaryEntry
import com.tuantuan.journal.domain.model.Mood
import com.tuantuan.journal.domain.repository.DiaryRepository
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.time.Instant
import com.google.common.truth.Truth.assertThat

class SaveDiaryEntryUseCaseTest {
    private lateinit var repository: DiaryRepository
    private lateinit var useCase: SaveDiaryEntryUseCase

    private val testEntry = DiaryEntry(
        id = "test-id",
        childId = "child-1",
        title = "Test Title",
        content = "Test content",
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
        useCase = SaveDiaryEntryUseCase(repository)
    }

    @Test
    fun `create entry delegates to repository`() = runTest {
        whenever(repository.createEntry(testEntry)).thenReturn("test-id")

        val result = useCase.create(testEntry)

        assertThat(result).isEqualTo("test-id")
        verify(repository).createEntry(testEntry)
    }

    @Test
    fun `update entry delegates to repository`() = runTest {
        useCase.update(testEntry)

        verify(repository).updateEntry(testEntry)
    }

    @Test
    fun `create entry returns generated id`() = runTest {
        val expectedId = "generated-id-123"
        whenever(repository.createEntry(testEntry)).thenReturn(expectedId)

        val result = useCase.create(testEntry)

        assertThat(result).isEqualTo(expectedId)
    }
}