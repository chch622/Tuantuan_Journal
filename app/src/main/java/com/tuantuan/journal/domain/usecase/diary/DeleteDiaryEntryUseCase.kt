package com.tuantuan.journal.domain.usecase.diary

import com.tuantuan.journal.domain.repository.DiaryRepository
import javax.inject.Inject

/**
 * 软删除日记条目 UseCase。
 *
 * 遵循 DATA_MODEL.md 第1.2节：删除操作设置 isDeleted = true，不物理删除。
 * 遵循 ERROR_HANDLING.md 第4.1节：删除为危险操作，UI 层需弹出确认对话框。
 */
class DeleteDiaryEntryUseCase @Inject constructor(
    private val repository: DiaryRepository
) {
    suspend operator fun invoke(id: String) {
        repository.deleteEntry(id)
    }
}