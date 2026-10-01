package com.tuantuan.journal.domain.usecase.growth

import com.tuantuan.journal.domain.model.GrowthRecord
import com.tuantuan.journal.domain.repository.GrowthRepository
import javax.inject.Inject

class SaveGrowthRecordUseCase @Inject constructor(
    private val repository: GrowthRepository
) {
    suspend fun create(record: GrowthRecord): String = repository.createRecord(record)
    suspend fun update(record: GrowthRecord) = repository.updateRecord(record)
}