package com.tuantuan.journal.domain.usecase.child

import com.tuantuan.journal.domain.model.Child
import com.tuantuan.journal.domain.repository.ChildRepository
import javax.inject.Inject

class GetChildByIdUseCase @Inject constructor(
    private val repository: ChildRepository
) {
    suspend operator fun invoke(id: String): Child? = repository.getChildById(id)
}