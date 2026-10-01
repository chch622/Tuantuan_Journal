package com.tuantuan.journal.ui.model

import com.tuantuan.journal.domain.exception.DomainException
import com.tuantuan.journal.domain.model.MediaType
import com.google.common.truth.Truth.assertThat
import org.junit.Test

/**
 * 测试 DomainException → UiError 和 Throwable → UiError 的转换链。
 *
 * 在 JVM 单元测试中 R.string 常量值为 0（unitTests.isReturnDefaultValues = true），
 * 因此仅验证 UiError 子类型和关键属性（formatArgs），不验证 messageResId 的具体数值。
 */
class UiErrorMappingTest {

    // ---- DomainException.toUiError() ----

    @Test
    fun `StorageFull maps to UiError StorageFull`() {
        val ex = DomainException.StorageFull(required = 100L, available = 50L)
        val error = ex.toUiError()
        assertThat(error).isInstanceOf(UiError.StorageFull::class.java)
    }

    @Test
    fun `MediaNotFound maps to UiError MediaNotFound`() {
        val ex = DomainException.MediaNotFound(path = "/data/photo.jpg")
        val error = ex.toUiError()
        assertThat(error).isInstanceOf(UiError.MediaNotFound::class.java)
    }

    @Test
    fun `DatabaseError maps to UiError DatabaseError`() {
        val ex = DomainException.DatabaseError(code = 1, detail = "constraint")
        val error = ex.toUiError()
        assertThat(error).isInstanceOf(UiError.DatabaseError::class.java)
    }

    @Test
    fun `FileTooLarge maps to UiError FileTooLarge`() {
        val ex = DomainException.FileTooLarge(maxSize = 10L, actualSize = 20L)
        val error = ex.toUiError()
        assertThat(error).isInstanceOf(UiError.FileTooLarge::class.java)
    }

    @Test
    fun `UnsupportedFormat maps to UiError UnsupportedFormat`() {
        val ex = DomainException.UnsupportedFormat(mimeType = "image/bmp")
        val error = ex.toUiError()
        assertThat(error).isInstanceOf(UiError.UnsupportedFormat::class.java)
    }

    @Test
    fun `ValidationError with name field maps to UiError ValidationError`() {
        val ex = DomainException.ValidationError(field = "name", reason = "empty")
        val error = ex.toUiError()
        assertThat(error).isInstanceOf(UiError.ValidationError::class.java)
    }

    @Test
    fun `ValidationError with content field maps to UiError ValidationError`() {
        val ex = DomainException.ValidationError(field = "content", reason = "blank")
        val error = ex.toUiError()
        assertThat(error).isInstanceOf(UiError.ValidationError::class.java)
    }

    @Test
    fun `ValidationError with unknown field maps to UiError ValidationError`() {
        val ex = DomainException.ValidationError(field = "unknown", reason = "invalid")
        val error = ex.toUiError()
        assertThat(error).isInstanceOf(UiError.ValidationError::class.java)
    }

    @Test
    fun `MediaCountExceeded with PHOTO maps to UiError MediaCountExceeded with formatArgs`() {
        val ex = DomainException.MediaCountExceeded(mediaType = MediaType.PHOTO, maxCount = 9)
        val error = ex.toUiError()
        assertThat(error).isInstanceOf(UiError.MediaCountExceeded::class.java)
        val mediaError = error as UiError.MediaCountExceeded
        assertThat(mediaError.formatArgs).isNotNull()
        assertThat(mediaError.formatArgs!![0]).isEqualTo(9)
    }

    @Test
    fun `MediaCountExceeded with VIDEO maps to UiError MediaCountExceeded`() {
        val ex = DomainException.MediaCountExceeded(mediaType = MediaType.VIDEO, maxCount = 3)
        val error = ex.toUiError()
        assertThat(error).isInstanceOf(UiError.MediaCountExceeded::class.java)
    }

    @Test
    fun `MediaCountExceeded with AUDIO maps to UiError MediaCountExceeded`() {
        val ex = DomainException.MediaCountExceeded(mediaType = MediaType.AUDIO, maxCount = 5)
        val error = ex.toUiError()
        assertThat(error).isInstanceOf(UiError.MediaCountExceeded::class.java)
    }

    @Test
    fun `BackupError maps to UiError BackupCorrupted`() {
        val ex = DomainException.BackupError(detail = "checksum mismatch")
        val error = ex.toUiError()
        assertThat(error).isInstanceOf(UiError.BackupCorrupted::class.java)
    }

    @Test
    fun `NotFound maps to UiError GenericError`() {
        val ex = DomainException.NotFound(entity = "Child", id = "abc")
        val error = ex.toUiError()
        assertThat(error).isInstanceOf(UiError.GenericError::class.java)
    }

    @Test
    fun `Unknown maps to UiError GenericError`() {
        val ex = DomainException.Unknown(original = RuntimeException("oops"))
        val error = ex.toUiError()
        assertThat(error).isInstanceOf(UiError.GenericError::class.java)
    }

    // ---- Throwable.toUiError() ----

    @Test
    fun `Throwable DomainException delegates to DomainException toUiError`() {
        val ex = DomainException.StorageFull(required = 100L, available = 50L)
        val error = (ex as Throwable).toUiError()
        assertThat(error).isInstanceOf(UiError.StorageFull::class.java)
    }

    @Test
    fun `Throwable non DomainException maps to GenericError`() {
        val ex = RuntimeException("some error")
        val error = ex.toUiError()
        assertThat(error).isInstanceOf(UiError.GenericError::class.java)
    }

    @Test
    fun `Throwable IllegalStateException maps to GenericError`() {
        val ex = IllegalStateException("invalid state")
        val error = ex.toUiError()
        assertThat(error).isInstanceOf(UiError.GenericError::class.java)
    }
}