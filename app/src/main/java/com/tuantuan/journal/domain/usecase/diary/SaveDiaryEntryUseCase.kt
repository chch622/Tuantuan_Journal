package com.tuantuan.journal.domain.usecase.diary

import com.tuantuan.journal.domain.model.DiaryEntry
import com.tuantuan.journal.domain.repository.DiaryRepository
import javax.inject.Inject

class SaveDiaryEntryUseCase @Inject constructor(
    private val repository: DiaryRepository
) {
    suspend fun create(entry: DiaryEntry): String = repository.createEntry(entry)
    suspend fun update(entry: DiaryEntry) = repository.updateEntry(entry)
}