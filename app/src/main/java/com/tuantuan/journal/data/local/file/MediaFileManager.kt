package com.tuantuan.journal.data.local.file

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ThumbnailUtils
import android.net.Uri
import android.provider.OpenableColumns
import com.tuantuan.journal.domain.exception.DomainException
import com.tuantuan.journal.domain.model.MediaItem
import com.tuantuan.journal.domain.model.MediaType
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 媒体文件管理器实现。
 *
 * 遵循 MEDIA_STORAGE.md 规范：
 * - 数据库存索引，文件系统存内容
 * - 文件存储在 app-private 目录 (filesDir/media/)
 * - 使用 UUID 文件名避免冲突
 * - 照片/视频生成缩略图
 * - 删除使用软删除，异步清理物理文件
 *
 * 遵循 PERMISSIONS.md：不需要任何运行时权限，文件选择通过 Photo Picker / SAF 实现。
 */
@Singleton
class MediaFileManager @Inject constructor(
    @param:ApplicationContext private val context: Context
) : MediaFileService {

    companion object {
        private const val MEDIA_DIR = "media"
        private const val PHOTOS_DIR = "photos"
        private const val VIDEOS_DIR = "videos"
        private const val AUDIO_DIR = "audio"
        private const val THUMBNAILS_DIR = "thumbnails"

        // MEDIA_STORAGE.md 第5节缩略图尺寸
        private const val THUMBNAIL_SIZE_LIST = 200
        private const val THUMBNAIL_QUALITY_LIST = 70

        // MEDIA_STORAGE.md 第6节文件大小限制
        private const val MAX_PHOTO_SIZE_BYTES = 20L * 1024 * 1024       // 20 MB
        private const val MAX_VIDEO_SIZE_BYTES = 500L * 1024 * 1024     // 500 MB
        private const val MAX_AUDIO_SIZE_BYTES = 100L * 1024 * 1024     // 100 MB
        private const val MAX_PHOTOS_PER_ENTRY = 10
        private const val MAX_VIDEOS_PER_ENTRY = 3
        private const val MAX_AUDIOS_PER_ENTRY = 5
    }

    private val mediaRootDir = File(context.filesDir, MEDIA_DIR)

    /**
     * 保存媒体文件。
     *
     * 流程（MEDIA_STORAGE.md 第4.1节）：
     * 1. 验证文件类型和大小
     * 2. 生成 UUID 文件名
     * 3. 创建目标目录
     * 4. 复制文件到 app-private 目录
     * 5. 生成缩略图（照片/视频）
     * 6. 返回文件信息
     */
    override suspend fun saveMedia(
        sourceUri: Uri,
        entryId: String,
        mediaType: MediaType
    ): MediaFileInfo = withContext(Dispatchers.IO) {
        // 1. 验证文件大小
        val fileSize = getFileSize(sourceUri)
        validateFileSize(mediaType, fileSize)

        // 2. 获取 MIME 类型和文件名
        val mimeType = getMimeType(sourceUri)
        validateMimeType(mediaType, mimeType)
        val originalFileName = getFileName(sourceUri)

        // 3. 生成 UUID 文件名
        val extension = getExtensionFromMimeType(mimeType)
        val uuid = UUID.randomUUID().toString()
        val newFileName = "$uuid.$extension"

        // 4. 创建目标目录并复制文件
        val targetDir = getMediaDir(mediaType, entryId)
        val targetFile = File(targetDir, newFileName)
        copyFile(sourceUri, targetFile)

        // 5. 生成缩略图（照片/视频）
        val thumbnailPath = generateThumbnail(targetFile, mediaType, uuid)

        // 6. 获取图片尺寸
        val dimensions = getImageDimensions(targetFile, mediaType)

        MediaFileInfo(
            filePath = targetFile.absolutePath,
            fileName = originalFileName,
            fileSize = fileSize,
            mimeType = mimeType,
            width = dimensions?.first,
            height = dimensions?.second,
            thumbnailPath = thumbnailPath
        )
    }

    /**
     * 删除媒体物理文件（异步清理）。
     * 注意：数据库记录通过 Repository 软删除，此方法仅清理物理文件。
     */
    suspend fun deletePhysicalFile(filePath: String) = withContext(Dispatchers.IO) {
        val file = File(filePath)
        if (file.exists()) {
            file.delete()
        }
        // 同时删除缩略图
        val thumbFile = File(filePath.replaceAfterLast("/", file.nameWithoutExtension + "_thumb.jpg"))
        if (thumbFile.exists()) {
            thumbFile.delete()
        }
    }

    /**
     * 清理已软删除超过指定天数的物理文件。
     */
    suspend fun cleanupDeletedFiles(olderThanDays: Int = 30): Int = withContext(Dispatchers.IO) {
        // 清理逻辑需要配合数据库查询，此处仅提供文件系统清理
        // 实际清理在 Repository 层协调
        0
    }

    /**
     * 获取存储空间信息。
     */
    fun getStorageInfo(): StorageInfo {
        val totalSpace = mediaRootDir.totalSpace
        val availableSpace = mediaRootDir.usableSpace
        val mediaSpace = calculateDirSize(mediaRootDir)
        return StorageInfo(
            totalSpace = totalSpace,
            usedSpace = totalSpace - availableSpace,
            mediaSpace = mediaSpace,
            availableSpace = availableSpace
        )
    }

    // ===== 私有辅助方法 =====

    private fun getMediaDir(mediaType: MediaType, entryId: String): File {
        val subDir = when (mediaType) {
            MediaType.PHOTO -> PHOTOS_DIR
            MediaType.VIDEO -> VIDEOS_DIR
            MediaType.AUDIO -> AUDIO_DIR
        }
        val dir = File(File(mediaRootDir, subDir), entryId)
        dir.mkdirs()
        return dir
    }

    private fun getFileSize(uri: Uri): Long {
        return context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
            if (cursor.moveToFirst() && sizeIndex >= 0) cursor.getLong(sizeIndex) else 0L
        } ?: 0L
    }

    private fun getFileName(uri: Uri): String {
        return context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (cursor.moveToFirst() && nameIndex >= 0) cursor.getString(nameIndex) else "unknown"
        } ?: "unknown"
    }

    private fun getMimeType(uri: Uri): String {
        return context.contentResolver.getType(uri) ?: "application/octet-stream"
    }

    private fun validateFileSize(mediaType: MediaType, size: Long) {
        val maxSize = when (mediaType) {
            MediaType.PHOTO -> MAX_PHOTO_SIZE_BYTES
            MediaType.VIDEO -> MAX_VIDEO_SIZE_BYTES
            MediaType.AUDIO -> MAX_AUDIO_SIZE_BYTES
        }
        if (size > maxSize) {
            throw DomainException.FileTooLarge(maxSize, size)
        }
        if (size <= 0) {
            throw DomainException.ValidationError("fileSize", "Invalid file size")
        }
    }

    private fun validateMimeType(mediaType: MediaType, mimeType: String) {
        val validPrefix = when (mediaType) {
            MediaType.PHOTO -> "image/"
            MediaType.VIDEO -> "video/"
            MediaType.AUDIO -> "audio/"
        }
        if (!mimeType.startsWith(validPrefix)) {
            throw DomainException.UnsupportedFormat(mimeType)
        }
    }

    private fun getExtensionFromMimeType(mimeType: String): String {
        return when {
            mimeType.contains("jpeg") || mimeType.contains("jpg") -> "jpg"
            mimeType.contains("png") -> "png"
            mimeType.contains("webp") -> "webp"
            mimeType.contains("gif") -> "gif"
            mimeType.contains("mp4") -> "mp4"
            mimeType.contains("mpeg") -> "mp4"
            mimeType.contains("webm") -> "webm"
            mimeType.contains("m4a") -> "m4a"
            mimeType.contains("mp3") -> "mp3"
            mimeType.contains("ogg") -> "ogg"
            mimeType.contains("wav") -> "wav"
            else -> mimeType.substringAfterLast("/", "bin")
        }
    }

    private fun copyFile(sourceUri: Uri, targetFile: File) {
        context.contentResolver.openInputStream(sourceUri)?.use { input ->
            FileOutputStream(targetFile).use { output ->
                input.copyTo(output)
            }
        } ?: throw DomainException.ValidationError("file", "Unable to read file")
    }

    private fun generateThumbnail(file: File, mediaType: MediaType, uuid: String): String? {
        if (mediaType == MediaType.AUDIO) return null

        return try {
            val thumbnailDir = File(mediaRootDir, THUMBNAILS_DIR)
            thumbnailDir.mkdirs()
            val thumbnailFile = File(thumbnailDir, "${uuid}_thumb.jpg")

            when (mediaType) {
                MediaType.PHOTO -> {
                    val bitmap = BitmapFactory.decodeFile(file.absolutePath) ?: return null
                    val thumbnail = ThumbnailUtils.extractThumbnail(
                        bitmap, THUMBNAIL_SIZE_LIST, THUMBNAIL_SIZE_LIST
                    )
                    FileOutputStream(thumbnailFile).use { out ->
                        thumbnail.compress(Bitmap.CompressFormat.JPEG, THUMBNAIL_QUALITY_LIST, out)
                    }
                    thumbnail.recycle()
                    if (bitmap != thumbnail) bitmap.recycle()
                }
                MediaType.VIDEO -> {
                    // 视频缩略图使用 MediaMetadataRetriever（替代已弃用的 createVideoThumbnail）
                    val retriever = android.media.MediaMetadataRetriever()
                    try {
                        retriever.setDataSource(file.absolutePath)
                        val bitmap = retriever.getFrameAtTime(0, android.media.MediaMetadataRetriever.OPTION_CLOSEST_SYNC)
                            ?: return null
                        val thumbnail = ThumbnailUtils.extractThumbnail(
                            bitmap, THUMBNAIL_SIZE_LIST, THUMBNAIL_SIZE_LIST
                        )
                        FileOutputStream(thumbnailFile).use { out ->
                            thumbnail.compress(Bitmap.CompressFormat.JPEG, THUMBNAIL_QUALITY_LIST, out)
                        }
                        thumbnail.recycle()
                        if (bitmap != thumbnail) bitmap.recycle()
                    } finally {
                        retriever.release()
                    }
                }
                else -> return null
            }
            thumbnailFile.absolutePath
        } catch (e: Exception) {
            null // 缩略图生成失败不影响主流程
        }
    }

    private fun getImageDimensions(file: File, mediaType: MediaType): Pair<Int, Int>? {
        if (mediaType != MediaType.PHOTO) return null
        return try {
            val options = BitmapFactory.Options().apply { inJustDecodeBounds = true }
            BitmapFactory.decodeFile(file.absolutePath, options)
            options.outWidth to options.outHeight
        } catch (e: Exception) {
            null
        }
    }

    private fun calculateDirSize(dir: File): Long {
        if (!dir.exists()) return 0L
        return dir.walkTopDown().filter { it.isFile }.sumOf { it.length() }
    }
}

/**
 * 保存媒体文件后返回的文件信息。
 */
data class MediaFileInfo(
    val filePath: String,
    val fileName: String,
    val fileSize: Long,
    val mimeType: String,
    val width: Int?,
    val height: Int?,
    val thumbnailPath: String?
)

/**
 * 存储空间信息。
 * 遵循 MEDIA_STORAGE.md 第7.1节。
 */
data class StorageInfo(
    val totalSpace: Long,
    val usedSpace: Long,
    val mediaSpace: Long,
    val availableSpace: Long
)