package com.tuantuan.journal.domain.usecase.media

import android.net.Uri
import com.tuantuan.journal.data.local.file.MediaFileManager
import com.tuantuan.journal.data.local.file.MediaFileInfo
import com.tuantuan.journal.domain.exception.DomainException
import com.tuantuan.journal.domain.model.MediaItem
import com.tuantuan.journal.domain.model.MediaType
import com.tuantuan.journal.domain.repository.MediaRepository
import java.time.Instant
import java.util.UUID
import javax.inject.Inject

/**
 * 保存媒体文件 UseCase。
 *
 * 遵循 MEDIA_STORAGE.md 规范：
 * 1. 通过 MediaFileManager 保存物理文件（验证大小/类型、生成UUID文件名、复制到app-private、生成缩略图）
 * 2. 通过 MediaRepository 保存数据库索引记录
 * 3. 检查每条日记的媒体数量限制
 *
 * 遵循 ARCHITECTURE.md：Domain层UseCase，协调Data层操作。
 */
class SaveMediaUseCase @Inject constructor(
    private val mediaFileManager: MediaFileManager,
    private val mediaRepository: MediaRepository
) {
    /**
     * 保存媒体文件。
     *
     * @param sourceUri 源文件 URI（来自 Photo Picker 或 SAF）
     * @param entryId 关联的日记 ID
     * @param mediaType 媒体类型
     * @return 保存后的 MediaItem
     * @throws DomainException.FileTooLarge 文件超过大小限制
     * @throws DomainException.UnsupportedFormat 不支持的文件格式
     * @throws DomainException.ValidationError 超过每条日记的媒体数量限制
     */
    suspend operator fun invoke(
        sourceUri: Uri,
        entryId: String,
        mediaType: MediaType
    ): MediaItem {
        // 1. 检查媒体数量限制
        validateMediaCount(entryId, mediaType)

        // 2. 保存物理文件
        val fileInfo: MediaFileInfo = mediaFileManager.saveMedia(sourceUri, entryId, mediaType)

        // 3. 构建数据库记录
        val now = Instant.now()
        val mediaItem = MediaItem(
            id = UUID.randomUUID().toString(),
            entryId = entryId,
            mediaType = mediaType,
            filePath = fileInfo.filePath,
            fileName = fileInfo.fileName,
            fileSize = fileInfo.fileSize,
            mimeType = fileInfo.mimeType,
            width = fileInfo.width,
            height = fileInfo.height,
            duration = null, // 视频时长需额外提取，后续迭代补充
            thumbnailPath = fileInfo.thumbnailPath,
            sortOrder = 0, // 排序由 UI 层设置
            isDeleted = false,
            createdAt = now,
            updatedAt = now
        )

        // 4. 保存数据库索引
        mediaRepository.addMedia(mediaItem)

        return mediaItem
    }

    private suspend fun validateMediaCount(entryId: String, mediaType: MediaType) {
        val currentCount = mediaRepository.getMediaCount(entryId)
        val maxCount = when (mediaType) {
            MediaType.PHOTO -> MAX_PHOTOS_PER_ENTRY
            MediaType.VIDEO -> MAX_VIDEOS_PER_ENTRY
            MediaType.AUDIO -> MAX_AUDIOS_PER_ENTRY
        }
        if (currentCount >= maxCount) {
            throw DomainException.ValidationError(
                "mediaCount",
                "每条日记最多添加 $maxCount 个${mediaType.label}"
            )
        }
    }

    companion object {
        private const val MAX_PHOTOS_PER_ENTRY = 10
        private const val MAX_VIDEOS_PER_ENTRY = 3
        private const val MAX_AUDIOS_PER_ENTRY = 5
    }
}

private val MediaType.label: String
    get() = when (this) {
        MediaType.PHOTO -> "照片"
        MediaType.VIDEO -> "视频"
        MediaType.AUDIO -> "音频"
    }