package com.tuantuan.journal.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.tuantuan.journal.data.local.db.entity.MediaItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MediaItemDao {

    @Query("SELECT * FROM media_items WHERE entryId = :entryId AND isDeleted = 0 ORDER BY sortOrder ASC")
    fun getMediaByEntry(entryId: String): Flow<List<MediaItemEntity>>

    @Query("SELECT * FROM media_items WHERE id = :id AND isDeleted = 0")
    suspend fun getMediaById(id: String): MediaItemEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(mediaItem: MediaItemEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(mediaItems: List<MediaItemEntity>)

    @Update
    suspend fun update(mediaItem: MediaItemEntity)

    @Query("UPDATE media_items SET isDeleted = 1, updatedAt = :updatedAt WHERE id = :id")
    suspend fun softDelete(id: String, updatedAt: Long)

    @Query("DELETE FROM media_items WHERE entryId = :entryId")
    suspend fun deleteByEntry(entryId: String)
}