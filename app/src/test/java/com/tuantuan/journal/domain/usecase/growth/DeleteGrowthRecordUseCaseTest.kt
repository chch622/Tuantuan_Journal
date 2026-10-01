package com.tuantuan.journal.domain.usecase.growth

import com.tuantuan.journal.domain.repository.GrowthRepository
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class DeleteGrowthRecordUseCaseTest {
    private lateinit var repository: GrowthRepository
    private lateinit var useCase: DeleteGrowthRecordUseCase

    @Before
    fun setup() {
        repository = mock()
        useCase = DeleteGrowthRecordUseCase(repository)
    }

    @Test
    fun `invoke delegates to repository`() = runTest {
        useCase("gr-1")

        verify(repository).deleteRecord("gr-1")
    }
}