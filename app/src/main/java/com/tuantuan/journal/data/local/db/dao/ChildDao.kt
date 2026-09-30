package com.tuantuan.journal.data.local.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.tuantuan.journal.data.local.db.entity.ChildEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChildDao {

    @Query("SELECT * FROM children WHERE isDeleted = 0 ORDER BY sortOrder ASC")
    fun getAllActiveChildren(): Flow<List<ChildEntity>>

    @Query("SELECT * FROM children WHERE id = :id AND isDeleted = 0")
    suspend fun getChildById(id: String): ChildEntity?

    @Query("SELECT * FROM children WHERE id = :id")
    suspend fun getChildByIdIncludeDeleted(id: String): ChildEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(child: ChildEntity)

    @Update
    suspend fun update(child: ChildEntity)

    @Query("UPDATE children SET isDeleted = 1, updatedAt = :updatedAt WHERE id = :id")
    suspend fun softDelete(id: String, updatedAt: Long)

    @Query("SELECT COUNT(*) FROM children WHERE isDeleted = 0")
    suspend fun getActiveChildCount(): Int
}