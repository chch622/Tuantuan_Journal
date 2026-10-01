package com.tuantuan.journal.domain.usecase.tag

import com.tuantuan.journal.domain.model.Tag
import com.tuantuan.journal.domain.repository.TagRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import com.google.common.truth.Truth.assertThat
import java.time.Instant

class GetTagsUseCaseTest {
    private lateinit var repository: TagRepository
    private lateinit var useCase: GetTagsUseCase

    private val testTags = listOf(
        Tag(
            id = "tag-1", name = "里程碑", color = "#FF5722",
            category = null, usageCount = 5, isDeleted = false,
            createdAt = Instant.now(), updatedAt = Instant.now()
        )
    )

    @Before
    fun setup() {
        repository = mock()
        useCase = GetTagsUseCase(repository)
    }

    @Test
    fun `returns all tags from repository`() = runTest {
        whenever(repository.getAllTags()).thenReturn(flowOf(testTags))

        val result = useCase().first()

        assertThat(result).isEqualTo(testTags)
    }

    @Test
    fun `returns tags for specific entry`() = runTest {
        whenever(repository.getTagsForEntry("entry-1")).thenReturn(flowOf(testTags))

        val result = useCase.forEntry("entry-1").first()

        assertThat(result).isEqualTo(testTags)
    }

    @Test
    fun `returns empty list when no tags exist`() = runTest {
        whenever(repository.getAllTags()).thenReturn(flowOf(emptyList()))

        val result = useCase().first()

        assertThat(result).isEmpty()
    }
}