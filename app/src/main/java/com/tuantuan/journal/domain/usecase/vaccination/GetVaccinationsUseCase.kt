package com.tuantuan.journal.domain.usecase.vaccination

import com.tuantuan.journal.domain.model.Vaccination
import com.tuantuan.journal.domain.repository.VaccinationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetVaccinationsUseCase @Inject constructor(
    private val repository: VaccinationRepository
) {
    /** 获取指定儿童的所有疫苗记录 */
    operator fun invoke(childId: String): Flow<List<Vaccination>> =
        repository.getVaccinationsByChild(childId)

    /** 获取已接种记录 */
    fun administered(childId: String): Flow<List<Vaccination>> =
        repository.getAdministeredVaccinations(childId)

    /** 获取待接种记录 */
    fun pending(childId: String): Flow<List<Vaccination>> =
        repository.getPendingVaccinations(childId)

    /** 获取逾期未接种记录 */
    fun overdue(childId: String): Flow<List<Vaccination>> =
        repository.getOverdueVaccinations(childId)
}