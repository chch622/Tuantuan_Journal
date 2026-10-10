package com.tuantuan.journal.domain.usecase.vaccination

import com.tuantuan.journal.domain.repository.VaccinationRepository
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class DeleteVaccinationUseCaseTest {
    private lateinit var repository: VaccinationRepository
    private lateinit var useCase: DeleteVaccinationUseCase

    @Before
    fun setup() {
        repository = mock()
        useCase = DeleteVaccinationUseCase(repository)
    }

    @Test
    fun `invoke delegates to repository`() = runTest {
        useCase("vac-1")

        verify(repository).deleteVaccination("vac-1")
    }
}