package com.tuantuan.journal.domain.usecase.milestone

import com.tuantuan.journal.domain.repository.MilestoneRepository
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class DeleteMilestoneUseCaseTest {
    private lateinit var repository: MilestoneRepository
    private lateinit var useCase: DeleteMilestoneUseCase

    @Before
    fun setup() {
        repository = mock()
        useCase = DeleteMilestoneUseCase(repository)
    }

    @Test
    fun `invoke delegates to repository`() = runTest {
        useCase("ms-1")

        verify(repository).deleteMilestone("ms-1")
    }
}