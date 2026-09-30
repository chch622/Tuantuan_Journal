package com.tuantuan.journal.domain.usecase.diary

import com.tuantuan.journal.domain.model.DiaryEntry
import com.tuantuan.journal.domain.repository.DiaryRepository
import javax.inject.Inject

/**
 * 获取单条日记详情 UseCase。
 *
 * 返回包含媒体项和标签的完整日记数据，
 * 用于日记详情页展示。
 */
class GetDiaryEntryUseCase @Inject constructor(
    private val repository: DiaryRepository
) {
    suspend operator fun invoke(id: String): DiaryEntry? =
        repository.getEntryById(id)
}