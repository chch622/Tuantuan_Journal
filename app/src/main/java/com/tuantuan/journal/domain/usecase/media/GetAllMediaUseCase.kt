package com.tuantuan.journal.domain.usecase.media

import com.tuantuan.journal.domain.model.MediaItem
import com.tuantuan.journal.domain.repository.MediaRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 获取所有媒体 UseCase — Phase 3 P1 媒体管理
 *
 * 返回所有未删除的媒体项，按创建时间降序排列。
 * 用于媒体管理界面展示。
 */
class GetAllMediaUseCase @Inject constructor(
    private val mediaRepository: MediaRepository
) {
    operator fun invoke(): Flow<List<MediaItem>> = mediaRepository.getAllMedia()
}