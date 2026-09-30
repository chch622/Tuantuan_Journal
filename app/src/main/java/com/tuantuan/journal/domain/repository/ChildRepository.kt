package com.tuantuan.journal.domain.repository

import com.tuantuan.journal.domain.model.Child
import kotlinx.coroutines.flow.Flow

interface ChildRepository {
    fun getAllActiveChildren(): Flow<List<Child>>
    suspend fun getChildById(id: String): Child?
    suspend fun createChild(child: Child): String
    suspend fun updateChild(child: Child)
    suspend fun deleteChild(id: String)
    suspend fun getActiveChildCount(): Int
}