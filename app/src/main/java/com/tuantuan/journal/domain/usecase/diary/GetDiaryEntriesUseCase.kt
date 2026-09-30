package com.tuantuan.journal.domain.usecase.diary

import com.tuantuan.journal.domain.model.DiaryEntry
import com.tuantuan.journal.domain.repository.DiaryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDiaryEntriesUseCase @Inject constructor(
    private val repository: DiaryRepository
) {
    operator fun invoke(childId: String): Flow<List<DiaryEntry>> =
        repository.getEntriesByChild(childId)

    fun favorites(childId: String): Flow<List<DiaryEntry>> =
        repository.getFavoriteEntries(childId)

    fun recent(childId: String, limit: Int = 10): Flow<List<DiaryEntry>> =
        repository.getRecentEntries(childId, limit)
}