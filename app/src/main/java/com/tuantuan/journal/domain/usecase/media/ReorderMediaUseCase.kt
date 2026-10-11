package com.tuantuan.journal.domain.usecase.media

import com.tuantuan.journal.domain.repository.MediaRepository
import javax.inject.Inject

/**
 * 重排媒体排序 UseCase。
 *
 * 遵循 DATA_MODEL.md §7.2：媒体按 sortOrder 升序排列。
 * 当用户调整照片/视频/音频顺序时，更新受影响项的 sortOrder。
 *
 * @param orderedIds 按新顺序排列的媒体 ID 列表（sortOrder = index）
 */
class ReorderMediaUseCase @Inject constructor(
    private val mediaRepository: MediaRepository
) {
    /**
     * 批量更新媒体排序。
     *
     * @param orderedIds 按新顺序排列的媒体 ID 列表
     */
    suspend operator fun invoke(orderedIds: List<String>) {
        orderedIds.forEachIndexed { index, id ->
            mediaRepository.updateSortOrder(id, index)
        }
    }
}