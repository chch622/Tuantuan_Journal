package com.tuantuan.journal.domain.usecase.child

import com.tuantuan.journal.domain.repository.ChildRepository
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class DeleteChildUseCaseTest {
    private lateinit var repository: ChildRepository
    private lateinit var useCase: DeleteChildUseCase

    @Before
    fun setup() {
        repository = mock()
        useCase = DeleteChildUseCase(repository)
    }

    @Test
    fun `deletes child by id`() = runTest {
        useCase("child-1")

        verify(repository).deleteChild("child-1")
    }

    @Test
    fun `deletes different child by id`() = runTest {
        useCase("child-2")

        verify(repository).deleteChild("child-2")
    }
}