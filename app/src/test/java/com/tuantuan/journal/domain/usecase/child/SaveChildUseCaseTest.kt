package com.tuantuan.journal.domain.usecase.child

import com.tuantuan.journal.domain.model.Child
import com.tuantuan.journal.domain.model.Gender
import com.tuantuan.journal.domain.repository.ChildRepository
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import com.google.common.truth.Truth.assertThat
import java.time.Instant
import java.time.LocalDate

class SaveChildUseCaseTest {
    private lateinit var repository: ChildRepository
    private lateinit var useCase: SaveChildUseCase

    private val testChild = Child(
        id = "child-1",
        name = "Tuantuan",
        nickname = "Tuan",
        birthDate = LocalDate.of(2023, 6, 15),
        gender = Gender.FEMALE,
        avatarPath = null,
        birthWeight = 3.2,
        birthHeight = 50.0,
        bloodType = null,
        birthPlace = null,
        notes = null,
        sortOrder = 0,
        isDeleted = false,
        createdAt = Instant.now(),
        updatedAt = Instant.now()
    )

    @Before
    fun setup() {
        repository = mock()
        useCase = SaveChildUseCase(repository)
    }

    @Test
    fun `create child delegates to repository`() = runTest {
        whenever(repository.createChild(testChild)).thenReturn("child-1")

        val result = useCase.create(testChild)

        assertThat(result).isEqualTo("child-1")
        verify(repository).createChild(testChild)
    }

    @Test
    fun `update child delegates to repository`() = runTest {
        useCase.update(testChild)

        verify(repository).updateChild(testChild)
    }

    @Test
    fun `create child returns generated id`() = runTest {
        val expectedId = "new-child-id"
        whenever(repository.createChild(testChild)).thenReturn(expectedId)

        val result = useCase.create(testChild)

        assertThat(result).isEqualTo(expectedId)
    }
}