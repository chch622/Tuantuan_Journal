package com.tuantuan.journal.domain.usecase.tag

import com.tuantuan.journal.domain.model.Tag
import com.tuantuan.journal.domain.repository.TagRepository
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import com.google.common.truth.Truth.assertThat
import java.time.Instant

class SaveTagUseCaseTest {
    private lateinit var repository: TagRepository
    private lateinit var useCase: SaveTagUseCase

    private val testTag = Tag(
        id = "tag-1", name = "里程碑", color = "#FF5722",
        category = null, usageCount = 0, isDeleted = false,
        createdAt = Instant.now(), updatedAt = Instant.now()
    )

    @Before
    fun setup() {
        repository = mock()
        useCase = SaveTagUseCase(repository)
    }

    @Test
    fun `create tag delegates to repository`() = runTest {
        whenever(repository.createTag(testTag)).thenReturn("tag-1")

        val result = useCase.create(testTag)

        assertThat(result).isEqualTo("tag-1")
        verify(repository).createTag(testTag)
    }

    @Test
    fun `create tag returns generated id`() = runTest {
        val expectedId = "new-tag-id"
        whenever(repository.createTag(testTag)).thenReturn(expectedId)

        val result = useCase.create(testTag)

        assertThat(result).isEqualTo(expectedId)
    }
}