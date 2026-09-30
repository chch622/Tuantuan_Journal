package com.tuantuan.journal.domain.usecase.diary

import com.tuantuan.journal.domain.model.DiaryEntry
import com.tuantuan.journal.domain.repository.DiaryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchDiaryEntriesUseCase @Inject constructor(
    private val repository: DiaryRepository
) {
    operator fun invoke(childId: String, query: String): Flow<List<DiaryEntry>> =
        repository.searchEntries(childId, query)
}