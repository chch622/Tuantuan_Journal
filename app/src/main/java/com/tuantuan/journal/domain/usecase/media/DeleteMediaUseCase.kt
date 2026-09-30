package com.tuantuan.journal.domain.usecase.media

import com.tuantuan.journal.data.local.file.MediaFileManager
import com.tuantuan.journal.domain.repository.MediaRepository
import javax.inject.Inject

/**
 * 删除媒体文件 UseCase。
 *
 * 遵循 DATA_MODEL.md 软删除规范：
 * - 数据库记录软删除（isDeleted = true）
 * - 物理文件延迟清理（由 cleanupDeletedFiles 异步处理）
 *
 * 遵循 ARCHITECTURE.md：Domain层UseCase，协调Data层操作。
 */
class DeleteMediaUseCase @Inject constructor(
    private val mediaRepository: MediaRepository,
    private val mediaFileManager: MediaFileManager
) {
    /**
     * 软删除媒体文件。
     *
     * @param id 媒体项 ID
     */
    suspend operator fun invoke(id: String) {
        // 1. 软删除数据库记录
        mediaRepository.deleteMedia(id)
        // 注意：物理文件由定时清理任务处理（cleanupDeletedFiles），
        // 不在此处立即删除，确保数据可恢复性。
    }
}