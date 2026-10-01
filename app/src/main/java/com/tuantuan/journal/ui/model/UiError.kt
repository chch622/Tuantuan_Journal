package com.tuantuan.journal.ui.model

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.tuantuan.journal.R
import com.tuantuan.journal.domain.exception.DomainException
import com.tuantuan.journal.domain.model.MediaType

/**
 * Presentation 层用户友好错误类型。
 *
 * 将 DomainException 转为用户可理解的错误消息，
 * 用于 UI 层展示 Snackbar / Dialog 等提示。
 *
 * 使用 @StringRes messageResId 支持 i18n，
 * 在 UI 层通过 resolveMessage() 扩展函数解析为本地化字符串。
 *
 * @see ARCHITECTURE.md 第7节
 * @see ERROR_HANDLING.md 第1节
 */
sealed class UiError {
    @get:StringRes
    abstract val messageResId: Int
    open val formatArgs: Array<Any>? = null

    data class StorageFull(
        @StringRes override val messageResId: Int = R.string.error_storage_full
    ) : UiError()

    data class MediaNotFound(
        @StringRes override val messageResId: Int = R.string.error_media_not_found
    ) : UiError()

    data class BackupCorrupted(
        @StringRes override val messageResId: Int = R.string.error_backup_corrupted
    ) : UiError()

    data class DatabaseError(
        @StringRes override val messageResId: Int = R.string.error_database
    ) : UiError()

    data class FileTooLarge(
        @StringRes override val messageResId: Int = R.string.error_file_too_large
    ) : UiError()

    data class UnsupportedFormat(
        @StringRes override val messageResId: Int = R.string.error_unsupported_format
    ) : UiError()

    data class ValidationError(
        @StringRes override val messageResId: Int
    ) : UiError()

    data class MediaCountExceeded(
        @StringRes override val messageResId: Int,
        override val formatArgs: Array<Any>? = null
    ) : UiError()

    data class GenericError(
        @StringRes override val messageResId: Int = R.string.error_unknown,
        override val formatArgs: Array<Any>? = null
    ) : UiError()
}

/**
 * 在 @Composable 上下文中将 UiError 解析为本地化字符串。
 */
@Composable
fun UiError.resolveMessage(): String {
    return stringResource(messageResId, *(formatArgs ?: emptyArray()))
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
        "name" -> R.string.error_validation_name
        "content" -> R.string.error_validation_content
        "date" -> R.string.error_validation_date
        "tagName" -> R.string.error_validation_tag
        else -> R.string.error_validation_generic
    })
    is DomainException.MediaCountExceeded -> UiError.MediaCountExceeded(
        messageResId = when (mediaType) {
            MediaType.PHOTO -> R.string.error_media_count_exceeded_photo
            MediaType.VIDEO -> R.string.error_media_count_exceeded_video
            MediaType.AUDIO -> R.string.error_media_count_exceeded_audio
        },
        formatArgs = arrayOf(maxCount)
    )
    is DomainException.BackupError -> UiError.BackupCorrupted()
    is DomainException.NotFound -> UiError.GenericError(R.string.error_not_found)
    is DomainException.Unknown -> UiError.GenericError(R.string.error_unknown)
}

/**
 * 将任意 Throwable 转为 UiError。
 * 若异常已是 DomainException 则直接转换，否则包装为 GenericError。
 */
fun Throwable.toUiError(): UiError =
    (this as? DomainException)?.toUiError()
        ?: UiError.GenericError()