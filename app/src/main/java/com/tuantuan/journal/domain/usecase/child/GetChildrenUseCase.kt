package com.tuantuan.journal.domain.usecase.child

import com.tuantuan.journal.domain.model.Child
import com.tuantuan.journal.domain.repository.ChildRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetChildrenUseCase @Inject constructor(
    private val repository: ChildRepository
) {
    operator fun invoke(): Flow<List<Child>> = repository.getAllActiveChildren()
}