package com.tuantuan.journal.domain.usecase.growth

import com.tuantuan.journal.domain.model.GrowthRecord
import com.tuantuan.journal.domain.model.GrowthType
import com.tuantuan.journal.domain.repository.GrowthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetGrowthRecordsUseCase @Inject constructor(
    private val repository: GrowthRepository
) {
    operator fun invoke(childId: String): Flow<List<GrowthRecord>> =
        repository.getRecordsByChild(childId)

    fun byType(childId: String, type: GrowthType): Flow<List<GrowthRecord>> =
        repository.getRecordsByType(childId, type)

    suspend fun latest(childId: String, type: GrowthType): GrowthRecord? =
        repository.getLatestRecord(childId, type)
}