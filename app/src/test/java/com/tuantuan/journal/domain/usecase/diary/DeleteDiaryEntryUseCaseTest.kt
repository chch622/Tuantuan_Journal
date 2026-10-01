package com.tuantuan.journal.domain.usecase.diary

import com.tuantuan.journal.domain.repository.DiaryRepository
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class DeleteDiaryEntryUseCaseTest {
    private lateinit var repository: DiaryRepository
    private lateinit var useCase: DeleteDiaryEntryUseCase

    @Before
    fun setup() {
        repository = mock()
        useCase = DeleteDiaryEntryUseCase(repository)
    }

    @Test
    fun `invoke delegates to repository deleteEntry`() = runTest {
        val entryId = "entry-123"

        useCase(entryId)

        verify(repository).deleteEntry(entryId)
    }

    @Test
    fun `invoke with different ids delegates correctly`() = runTest {
        val entryId1 = "entry-1"
        val entryId2 = "entry-2"

        useCase(entryId1)
        useCase(entryId2)

        verify(repository).deleteEntry(entryId1)
        verify(repository).deleteEntry(entryId2)
    }
}