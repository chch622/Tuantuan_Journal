package com.tuantuan.journal.domain.exception

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
        DomainException("存储空间不足")

    data class MediaNotFound(val path: String) :
        DomainException("媒体文件未找到: $path")

    data class DatabaseError(val code: Int, val detail: String) :
        DomainException("数据库错误: $code")

    data class FileTooLarge(val maxSize: Long, val actualSize: Long) :
        DomainException("文件过大")

    data class UnsupportedFormat(val mimeType: String) :
        DomainException("不支持的格式: $mimeType")

    data class ValidationError(val field: String, val reason: String) :
        DomainException("验证失败: $field - $reason")

    data class BackupError(val detail: String) :
        DomainException("备份错误: $detail")

    data class NotFound(val entity: String, val id: String) :
        DomainException("$entity 未找到: $id")

    data class Unknown(val original: Throwable) :
        DomainException("未知错误: ${original.message}")
}