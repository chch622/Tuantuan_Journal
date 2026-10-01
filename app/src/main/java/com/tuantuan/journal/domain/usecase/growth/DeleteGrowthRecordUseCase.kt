package com.tuantuan.journal.domain.usecase.growth

import com.tuantuan.journal.domain.repository.GrowthRepository
import javax.inject.Inject

class DeleteGrowthRecordUseCase @Inject constructor(
    private val repository: GrowthRepository
) {
    suspend operator fun invoke(id: String) = repository.deleteRecord(id)
}