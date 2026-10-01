package com.tuantuan.journal.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.tuantuan.journal.data.local.db.entity.GrowthRecordEntity
import com.tuantuan.journal.domain.model.GrowthType
import kotlinx.coroutines.flow.Flow

/**
 * 成长记录 DAO。
 *
 * 查询规范遵循 DATA_MODEL.md 第7节：
 * - 默认过滤 isDeleted = 0
 * - 排序：按 measureDate 降序
 */
@Dao
interface GrowthRecordDao {

    @Query("SELECT * FROM growth_records WHERE childId = :childId AND isDeleted = 0 ORDER BY measureDate DESC")
    fun getActiveRecordsByChild(childId: String): Flow<List<GrowthRecordEntity>>

    @Query("SELECT * FROM growth_records WHERE childId = :childId AND recordType = :type AND isDeleted = 0 ORDER BY measureDate DESC")
    fun getActiveRecordsByType(childId: String, type: GrowthType): Flow<List<GrowthRecordEntity>>

    @Query("SELECT * FROM growth_records WHERE id = :id AND isDeleted = 0")
    suspend fun getRecordById(id: String): GrowthRecordEntity?

    @Query("SELECT * FROM growth_records WHERE childId = :childId AND recordType = :type AND isDeleted = 0 ORDER BY measureDate DESC LIMIT 1")
    suspend fun getLatestRecord(childId: String, type: GrowthType): GrowthRecordEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(record: GrowthRecordEntity)

    @Update
    suspend fun update(record: GrowthRecordEntity)

    @Query("UPDATE growth_records SET isDeleted = 1, updatedAt = :updatedAt WHERE id = :id")
    suspend fun softDelete(id: String, updatedAt: Long)

    @Query("SELECT COUNT(*) FROM growth_records WHERE childId = :childId AND recordType = :type AND isDeleted = 0")
    suspend fun getRecordCount(childId: String, type: GrowthType): Int
}