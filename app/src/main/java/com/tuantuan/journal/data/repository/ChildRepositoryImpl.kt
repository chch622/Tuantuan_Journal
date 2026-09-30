package com.tuantuan.journal.data.repository

import com.tuantuan.journal.data.local.db.dao.ChildDao
import com.tuantuan.journal.data.mapper.ChildMapper
import com.tuantuan.journal.domain.model.Child
import com.tuantuan.journal.domain.repository.ChildRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChildRepositoryImpl @Inject constructor(
    private val childDao: ChildDao
) : ChildRepository {

    override fun getAllActiveChildren(): Flow<List<Child>> =
        childDao.getAllActiveChildren().map { entities ->
            entities.map { ChildMapper.toDomain(it) }
        }

    override suspend fun getChildById(id: String): Child? =
        childDao.getChildById(id)?.let { ChildMapper.toDomain(it) }

    override suspend fun createChild(child: Child): String {
        childDao.insert(ChildMapper.toEntity(child))
        return child.id
    }

    override suspend fun updateChild(child: Child) {
        childDao.update(ChildMapper.toEntity(child))
    }

    override suspend fun deleteChild(id: String) {
        childDao.softDelete(id, Instant.now().toEpochMilli())
    }

    override suspend fun getActiveChildCount(): Int =
        childDao.getActiveChildCount()
}