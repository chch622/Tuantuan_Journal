package com.tuantuan.journal.domain.usecase.child

import com.tuantuan.journal.domain.model.Child
import com.tuantuan.journal.domain.model.Gender
import com.tuantuan.journal.domain.repository.ChildRepository
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import com.google.common.truth.Truth.assertThat
import java.time.Instant
import java.time.LocalDate

class GetChildByIdUseCaseTest {
    private lateinit var repository: ChildRepository
    private lateinit var useCase: GetChildByIdUseCase

    private val testChild = Child(
        id = "child-1", name = "Tuantuan", nickname = "Tuan",
        birthDate = LocalDate.of(2023, 6, 15), gender = Gender.FEMALE,
        avatarPath = null, birthWeight = 3.2, birthHeight = 50.0,
        bloodType = null, birthPlace = null, notes = null,
        sortOrder = 0, isDeleted = false,
        createdAt = Instant.now(), updatedAt = Instant.now()
    )

    @Before
    fun setup() {
        repository = mock()
        useCase = GetChildByIdUseCase(repository)
    }

    @Test
    fun `returns child when found`() = runTest {
        whenever(repository.getChildById("child-1")).thenReturn(testChild)

        val result = useCase("child-1")

        assertThat(result).isEqualTo(testChild)
    }

    @Test
    fun `returns null when child not found`() = runTest {
        whenever(repository.getChildById("nonexistent")).thenReturn(null)

        val result = useCase("nonexistent")

        assertThat(result).isNull()
    }
}