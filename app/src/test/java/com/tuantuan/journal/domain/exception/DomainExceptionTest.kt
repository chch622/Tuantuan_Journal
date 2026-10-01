package com.tuantuan.journal.domain.exception

import com.tuantuan.journal.domain.model.MediaType
import org.junit.Test
import com.google.common.truth.Truth.assertThat

class DomainExceptionTest {

    @Test
    fun `StorageFull contains required and available in message`() {
        val ex = DomainException.StorageFull(required = 100L, available = 50L)
        assertThat(ex.message).contains("required=100")
        assertThat(ex.message).contains("available=50")
    }

    @Test
    fun `MediaNotFound contains path in message`() {
        val ex = DomainException.MediaNotFound(path = "/data/photo.jpg")
        assertThat(ex.message).contains("/data/photo.jpg")
    }

    @Test
    fun `FileTooLarge contains sizes in message`() {
        val ex = DomainException.FileTooLarge(maxSize = 10L, actualSize = 20L)
        assertThat(ex.message).contains("max=10")
        assertThat(ex.message).contains("actual=20")
    }

    @Test
    fun `UnsupportedFormat contains mime type in message`() {
        val ex = DomainException.UnsupportedFormat(mimeType = "image/bmp")
        assertThat(ex.message).contains("image/bmp")
    }

    @Test
    fun `ValidationError contains field and reason in message`() {
        val ex = DomainException.ValidationError(field = "name", reason = "empty")
        assertThat(ex.message).contains("name")
        assertThat(ex.message).contains("empty")
    }

    @Test
    fun `MediaCountExceeded contains type and max in message`() {
        val ex = DomainException.MediaCountExceeded(mediaType = MediaType.PHOTO, maxCount = 10)
        assertThat(ex.message).contains("PHOTO")
        assertThat(ex.message).contains("max=10")
    }

    @Test
    fun `MediaCountExceeded data class equality`() {
        val ex1 = DomainException.MediaCountExceeded(MediaType.VIDEO, 3)
        val ex2 = DomainException.MediaCountExceeded(MediaType.VIDEO, 3)
        assertThat(ex1).isEqualTo(ex2)
    }

    @Test
    fun `NotFound contains entity and id in message`() {
        val ex = DomainException.NotFound(entity = "Child", id = "abc")
        assertThat(ex.message).contains("Child")
        assertThat(ex.message).contains("abc")
    }
}