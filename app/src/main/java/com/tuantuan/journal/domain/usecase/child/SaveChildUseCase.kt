package com.tuantuan.journal.domain.usecase.child

import com.tuantuan.journal.domain.model.Child
import com.tuantuan.journal.domain.repository.ChildRepository
import javax.inject.Inject

class SaveChildUseCase @Inject constructor(
    private val repository: ChildRepository
) {
    suspend fun create(child: Child): String = repository.createChild(child)
    suspend fun update(child: Child) = repository.updateChild(child)
}