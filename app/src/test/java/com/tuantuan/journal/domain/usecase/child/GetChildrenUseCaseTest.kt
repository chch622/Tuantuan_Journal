package com.tuantuan.journal.domain.usecase.child

import com.tuantuan.journal.domain.model.Child
import com.tuantuan.journal.domain.model.Gender
import com.tuantuan.journal.domain.repository.ChildRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import com.google.common.truth.Truth.assertThat
import java.time.Instant
import java.time.LocalDate

class GetChildrenUseCaseTest {
    private lateinit var repository: ChildRepository
    private lateinit var useCase: GetChildrenUseCase

    private val testChildren = listOf(
        Child(
            id = "child-1", name = "Tuantuan", nickname = "Tuan",
            birthDate = LocalDate.of(2023, 6, 15), gender = Gender.FEMALE,
            avatarPath = null, birthWeight = 3.2, birthHeight = 50.0,
            bloodType = null, birthPlace = null, notes = null,
            sortOrder = 0, isDeleted = false,
            createdAt = Instant.now(), updatedAt = Instant.now()
        )
    )

    @Before
    fun setup() {
        repository = mock()
        useCase = GetChildrenUseCase(repository)
    }

    @Test
    fun `returns all active children from repository`() = runTest {
        whenever(repository.getAllActiveChildren()).thenReturn(flowOf(testChildren))

        val result = useCase().first()

        assertThat(result).isEqualTo(testChildren)
    }

    @Test
    fun `returns empty list when no children exist`() = runTest {
        whenever(repository.getAllActiveChildren()).thenReturn(flowOf(emptyList()))

        val result = useCase().first()

        assertThat(result).isEmpty()
    }
}