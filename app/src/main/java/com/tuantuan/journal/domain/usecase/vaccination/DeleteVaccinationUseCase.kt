package com.tuantuan.journal.domain.usecase.vaccination

import com.tuantuan.journal.domain.repository.VaccinationRepository
import javax.inject.Inject

class DeleteVaccinationUseCase @Inject constructor(
    private val repository: VaccinationRepository
) {
    suspend operator fun invoke(id: String) = repository.deleteVaccination(id)
}