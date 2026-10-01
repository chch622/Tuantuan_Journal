package com.tuantuan.journal.domain.usecase.media

import com.tuantuan.journal.domain.repository.MediaRepository
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class DeleteMediaUseCaseTest {
    private lateinit var repository: MediaRepository
    private lateinit var useCase: DeleteMediaUseCase

    @Before
    fun setup() {
        repository = mock()
        useCase = DeleteMediaUseCase(repository)
    }

    @Test
    fun `soft deletes media by id`() = runTest {
        useCase("media-1")

        verify(repository).deleteMedia("media-1")
    }

    @Test
    fun `soft deletes different media by id`() = runTest {
        useCase("media-2")

        verify(repository).deleteMedia("media-2")
    }
}