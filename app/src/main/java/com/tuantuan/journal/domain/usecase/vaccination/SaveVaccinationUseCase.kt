package com.tuantuan.journal.domain.usecase.vaccination

import com.tuantuan.journal.domain.model.Vaccination
import com.tuantuan.journal.domain.repository.VaccinationRepository
import javax.inject.Inject

class SaveVaccinationUseCase @Inject constructor(
    private val repository: VaccinationRepository
) {
    suspend fun create(vaccination: Vaccination): String = repository.createVaccination(vaccination)
    suspend fun update(vaccination: Vaccination) = repository.updateVaccination(vaccination)
}