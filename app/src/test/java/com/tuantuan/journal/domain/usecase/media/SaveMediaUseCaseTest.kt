package com.tuantuan.journal.domain.usecase.media

import android.net.Uri
import com.tuantuan.journal.data.local.file.MediaFileService
import com.tuantuan.journal.data.local.file.MediaFileInfo
import com.tuantuan.journal.domain.exception.DomainException
import com.tuantuan.journal.domain.model.MediaType
import com.tuantuan.journal.domain.repository.MediaRepository
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import com.google.common.truth.Truth.assertThat

class SaveMediaUseCaseTest {
    private lateinit var repository: MediaRepository

    private val testFileInfo = MediaFileInfo(
        filePath = "/data/media/entry-1/uuid.jpg",
        fileName = "uuid.jpg",
        fileSize = 1024L,
        mimeType = "image/jpeg",
        width = 1920,
        height = 1080,
        thumbnailPath = "/data/media/entry-1/uuid_thumb.jpg"
    )

    /** Fake实现：直接返回testFileInfo，避免Mockito对suspend函数+非空Uri参数的null stub问题 */
    private val fakeMediaFileService = object : MediaFileService {
        override suspend fun saveMedia(sourceUri: Uri, entryId: String, mediaType: MediaType): MediaFileInfo {
            return testFileInfo
        }
    }

    @Before
    fun setup() {
        repository = mock()
    }

    @Test
    fun `save media delegates to file service and repository`() = runTest {
        whenever(repository.getMediaCount("entry-1")).thenReturn(0)
        whenever(repository.addMedia(any())).thenReturn("media-1")
        val useCase = SaveMediaUseCase(fakeMediaFileService, repository)

        val result = useCase(TestUriHelper.nullUri(), "entry-1", MediaType.PHOTO)

        assertThat(result.entryId).isEqualTo("entry-1")
        assertThat(result.mediaType).isEqualTo(MediaType.PHOTO)
        assertThat(result.filePath).isEqualTo(testFileInfo.filePath)
        verify(repository).addMedia(any())
    }

    @Test
    fun `throws MediaCountExceeded when photo limit reached`() = runTest {
        whenever(repository.getMediaCount("entry-1")).thenReturn(10)
        val useCase = SaveMediaUseCase(fakeMediaFileService, repository)

        try {
            useCase(TestUriHelper.nullUri(), "entry-1", MediaType.PHOTO)
            assert(false) { "Should have thrown MediaCountExceeded" }
        } catch (e: DomainException.MediaCountExceeded) {
            assertThat(e.mediaType).isEqualTo(MediaType.PHOTO)
            assertThat(e.maxCount).isEqualTo(10)
        }
    }

    @Test
    fun `throws MediaCountExceeded when video limit reached`() = runTest {
        whenever(repository.getMediaCount("entry-1")).thenReturn(3)
        val useCase = SaveMediaUseCase(fakeMediaFileService, repository)

        try {
            useCase(TestUriHelper.nullUri(), "entry-1", MediaType.VIDEO)
            assert(false) { "Should have thrown MediaCountExceeded" }
        } catch (e: DomainException.MediaCountExceeded) {
            assertThat(e.mediaType).isEqualTo(MediaType.VIDEO)
            assertThat(e.maxCount).isEqualTo(3)
        }
    }

    @Test
    fun `throws MediaCountExceeded when audio limit reached`() = runTest {
        whenever(repository.getMediaCount("entry-1")).thenReturn(5)
        val useCase = SaveMediaUseCase(fakeMediaFileService, repository)

        try {
            useCase(TestUriHelper.nullUri(), "entry-1", MediaType.AUDIO)
            assert(false) { "Should have thrown MediaCountExceeded" }
        } catch (e: DomainException.MediaCountExceeded) {
            assertThat(e.mediaType).isEqualTo(MediaType.AUDIO)
            assertThat(e.maxCount).isEqualTo(5)
        }
    }
}