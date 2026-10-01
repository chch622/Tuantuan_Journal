package com.tuantuan.journal.data.local.file

import android.net.Uri
import com.tuantuan.journal.domain.model.MediaType

/**
 * 媒体文件服务接口。
 *
 * 定义媒体文件操作的核心契约，便于 Domain 层依赖抽象而非具体实现。
 * 遵循 ARCHITECTURE.md 依赖倒置原则：Domain 层依赖接口，Data 层提供实现。
 */
interface MediaFileService {
    /**
     * 保存媒体文件。
     *
     * @param sourceUri 源文件 URI（来自 Photo Picker 或 SAF）
     * @param entryId 关联的日记 ID
     * @param mediaType 媒体类型
     * @return 保存后的文件信息
     */
    suspend fun saveMedia(
        sourceUri: Uri,
        entryId: String,
        mediaType: MediaType
    ): MediaFileInfo
}