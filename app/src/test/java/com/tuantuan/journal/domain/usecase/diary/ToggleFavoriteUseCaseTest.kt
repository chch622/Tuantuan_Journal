package com.tuantuan.journal.domain.usecase.diary

import com.tuantuan.journal.domain.repository.DiaryRepository
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class ToggleFavoriteUseCaseTest {
    private lateinit var repository: DiaryRepository
    private lateinit var useCase: ToggleFavoriteUseCase

    @Before
    fun setup() {
        repository = mock()
        useCase = ToggleFavoriteUseCase(repository)
    }

    @Test
    fun `invoke sets favorite to true`() = runTest {
        useCase("entry-1", true)

        verify(repository).toggleFavorite("entry-1", true)
    }

    @Test
    fun `invoke sets favorite to false`() = runTest {
        useCase("entry-1", false)

        verify(repository).toggleFavorite("entry-1", false)
    }

    @Test
    fun `invoke toggles favorite for different entries`() = runTest {
        useCase("entry-1", true)
        useCase("entry-2", false)

        verify(repository).toggleFavorite("entry-1", true)
        verify(repository).toggleFavorite("entry-2", false)
    }
}