package com.tuantuan.journal.ui.model

import com.tuantuan.journal.domain.exception.DomainException

/**
 * Presentation 层用户友好错误类型。
 *
 * 将 DomainException 转为用户可理解的错误消息，
 * 用于 UI 层展示 Snackbar / Dialog 等提示。
 *
 * @see ARCHITECTURE.md 第7节
 * @see ERROR_HANDLING.md 第1节
 */
sealed class UiError {
    abstract val displayMessage: String

    data class StorageFull(override val displayMessage: String = "存储空间不足，请清理后重试") : UiError()
    data class MediaNotFound(override val displayMessage: String = "文件未找到，可能已被移动") : UiError()
    data class BackupCorrupted(override val displayMessage: String = "备份文件已损坏，无法恢复") : UiError()
    data class DatabaseError(override val displayMessage: String = "数据异常，请稍后重试") : UiError()
    data class FileTooLarge(override val displayMessage: String = "文件超过大小限制（20MB）") : UiError()
    data class UnsupportedFormat(override val displayMessage: String = "不支持此文件格式") : UiError()
    data class ValidationError(override val displayMessage: String) : UiError()
    data class GenericError(override val displayMessage: String) : UiError()
}

/**
 * 将 DomainException 转为 UiError
 */
fun DomainException.toUiError(): UiError = when (this) {
    is DomainException.StorageFull -> UiError.StorageFull()
    is DomainException.MediaNotFound -> UiError.MediaNotFound()
    is DomainException.DatabaseError -> UiError.DatabaseError()
    is DomainException.FileTooLarge -> UiError.FileTooLarge()
    is DomainException.UnsupportedFormat -> UiError.UnsupportedFormat()
    is DomainException.ValidationError -> UiError.ValidationError(when (field) {
        "name" -> "请输入儿童姓名"
        "content" -> "请输入日记内容"
        "date" -> "日期不能晚于今天"
        "tagName" -> "标签名已存在"
        else -> message ?: "输入有误，请检查"
    })
    is DomainException.BackupError -> UiError.BackupCorrupted()
    is DomainException.NotFound -> UiError.GenericError(message ?: "数据未找到")
    is DomainException.Unknown -> UiError.GenericError(message ?: "发生未知错误")
}

/**
 * 将任意 Throwable 转为 UiError。
 * 若异常已是 DomainException 则直接转换，否则包装为 GenericError。
 */
fun Throwable.toUiError(): UiError =
    (this as? DomainException)?.toUiError()
        ?: UiError.GenericError(message ?: "发生未知错误")