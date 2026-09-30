package com.tuantuan.journal.domain.usecase.child

import com.tuantuan.journal.domain.repository.ChildRepository
import javax.inject.Inject

class DeleteChildUseCase @Inject constructor(
    private val repository: ChildRepository
) {
    suspend operator fun invoke(id: String) = repository.deleteChild(id)
}