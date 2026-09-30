package com.tuantuan.journal.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.tuantuan.journal.data.local.db.entity.DiaryEntryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DiaryEntryDao {

    @Query("""
        SELECT * FROM diary_entries 
        WHERE childId = :childId AND isDeleted = 0 
        ORDER BY eventDateTime DESC
    """)
    fun getEntriesByChild(childId: String): Flow<List<DiaryEntryEntity>>

    @Query("""
        SELECT * FROM diary_entries 
        WHERE childId = :childId AND isDeleted = 0 
        ORDER BY eventDateTime DESC
        LIMIT :limit OFFSET :offset
    """)
    suspend fun getEntriesByChildPaged(childId: String, limit: Int, offset: Int): List<DiaryEntryEntity>

    @Query("SELECT * FROM diary_entries WHERE id = :id AND isDeleted = 0")
    suspend fun getEntryById(id: String): DiaryEntryEntity?

    @Query("""
        SELECT * FROM diary_entries 
        WHERE childId = :childId AND isDeleted = 0 AND (title LIKE '%' || :query || '%' OR content LIKE '%' || :query || '%')
        ORDER BY eventDateTime DESC
    """)
    fun searchEntries(childId: String, query: String): Flow<List<DiaryEntryEntity>>

    @Query("""
        SELECT * FROM diary_entries 
        WHERE childId = :childId AND isDeleted = 0 AND isFavorite = 1
        ORDER BY eventDateTime DESC
    """)
    fun getFavoriteEntries(childId: String): Flow<List<DiaryEntryEntity>>

    @Query("""
        SELECT * FROM diary_entries 
        WHERE childId = :childId AND isDeleted = 0 
        ORDER BY eventDateTime DESC 
        LIMIT :limit
    """)
    fun getRecentEntries(childId: String, limit: Int = 10): Flow<List<DiaryEntryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: DiaryEntryEntity)

    @Update
    suspend fun update(entry: DiaryEntryEntity)

    @Query("UPDATE diary_entries SET isDeleted = 1, updatedAt = :updatedAt WHERE id = :id")
    suspend fun softDelete(id: String, updatedAt: Long)

    @Query("UPDATE diary_entries SET isFavorite = :isFavorite, updatedAt = :updatedAt WHERE id = :id")
    suspend fun toggleFavorite(id: String, isFavorite: Boolean, updatedAt: Long)

    @Query("SELECT COUNT(*) FROM diary_entries WHERE childId = :childId AND isDeleted = 0")
    suspend fun getEntryCountByChild(childId: String): Int
}