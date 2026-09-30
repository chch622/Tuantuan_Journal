package com.tuantuan.journal.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.tuantuan.journal.data.local.db.entity.EntryTagEntity
import com.tuantuan.journal.data.local.db.entity.TagEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TagDao {

    @Query("SELECT * FROM tags WHERE isDeleted = 0 ORDER BY usageCount DESC")
    fun getAllActiveTags(): Flow<List<TagEntity>>

    @Query("SELECT * FROM tags WHERE id = :id AND isDeleted = 0")
    suspend fun getTagById(id: String): TagEntity?

    @Query("SELECT * FROM tags WHERE name = :name AND isDeleted = 0")
    suspend fun getTagByName(name: String): TagEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(tag: TagEntity)

    @Update
    suspend fun update(tag: TagEntity)

    @Query("UPDATE tags SET isDeleted = 1, updatedAt = :updatedAt WHERE id = :id")
    suspend fun softDelete(id: String, updatedAt: Long)

    @Query("UPDATE tags SET usageCount = usageCount + 1, updatedAt = :updatedAt WHERE id = :id")
    suspend fun incrementUsageCount(id: String, updatedAt: Long)

    // EntryTag operations
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntryTag(entryTag: EntryTagEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntryTags(entryTags: List<EntryTagEntity>)

    @Query("DELETE FROM entry_tags WHERE entryId = :entryId")
    suspend fun deleteEntryTagsByEntry(entryId: String)

    @Query("DELETE FROM entry_tags WHERE entryId = :entryId AND tagId = :tagId")
    suspend fun deleteEntryTag(entryId: String, tagId: String)

    @Query("""
        SELECT t.* FROM tags t 
        INNER JOIN entry_tags et ON t.id = et.tagId 
        WHERE et.entryId = :entryId AND t.isDeleted = 0
    """)
    fun getTagsByEntry(entryId: String): Flow<List<TagEntity>>
}