package com.tuantuan.journal.domain.exception

import com.tuantuan.journal.domain.model.MediaType

/**
 * 领域层统一异常类型。
 *
 * Data 层捕获技术异常后转为 DomainException，
 * Domain 层处理业务错误或向上传播，
 * Presentation 层转为 UiError 显示给用户。
 *
 * @see ERROR_HANDLING.md 第2节
 */
sealed class DomainException(message: String) : Exception(message) {
    data class StorageFull(val required: Long, val available: Long) :
        DomainException("Storage full: required=$required, available=$available")

    data class MediaNotFound(val path: String) :
        DomainException("Media not found: $path")

    data class DatabaseError(val code: Int, val detail: String) :
        DomainException("Database error: code=$code")

    data class FileTooLarge(val maxSize: Long, val actualSize: Long) :
        DomainException("File too large: max=$maxSize, actual=$actualSize")

    data class UnsupportedFormat(val mimeType: String) :
        DomainException("Unsupported format: $mimeType")

    data class ValidationError(val field: String, val reason: String) :
        DomainException("Validation failed: $field - $reason")

    data class MediaCountExceeded(val mediaType: MediaType, val maxCount: Int) :
        DomainException("Media count exceeded: type=$mediaType, max=$maxCount")

    data class BackupError(val detail: String) :
        DomainException("Backup error: $detail")

    data class NotFound(val entity: String, val id: String) :
        DomainException("Not found: $entity/$id")

    data class Unknown(val original: Throwable) :
        DomainException("Unknown error: ${original.message}")
}