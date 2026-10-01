package com.tuantuan.journal.domain.usecase.tag

import com.tuantuan.journal.domain.repository.TagRepository
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class ManageEntryTagUseCaseTest {
    private lateinit var repository: TagRepository
    private lateinit var useCase: ManageEntryTagUseCase

    @Before
    fun setup() {
        repository = mock()
        useCase = ManageEntryTagUseCase(repository)
    }

    @Test
    fun `add tag to entry delegates to repository`() = runTest {
        useCase.add("entry-1", "tag-1")

        verify(repository).addTagToEntry("entry-1", "tag-1")
    }

    @Test
    fun `remove tag from entry delegates to repository`() = runTest {
        useCase.remove("entry-1", "tag-1")

        verify(repository).removeTagFromEntry("entry-1", "tag-1")
    }

    @Test
    fun `add different tag to entry`() = runTest {
        useCase.add("entry-2", "tag-2")

        verify(repository).addTagToEntry("entry-2", "tag-2")
    }
}