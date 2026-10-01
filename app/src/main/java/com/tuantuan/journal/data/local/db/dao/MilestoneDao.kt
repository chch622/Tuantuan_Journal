package com.tuantuan.journal.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.tuantuan.journal.data.local.db.entity.MilestoneEntity
import com.tuantuan.journal.domain.model.MilestoneCategory
import kotlinx.coroutines.flow.Flow

/**
 * 里程碑 DAO。
 *
 * 查询规范遵循 DATA_MODEL.md 第7节：
 * - 默认过滤 isDeleted = 0
 * - 排序：按 achievedDate 降序
 */
@Dao
interface MilestoneDao {

    @Query("SELECT * FROM milestones WHERE childId = :childId AND isDeleted = 0 ORDER BY achievedDate DESC")
    fun getActiveMilestonesByChild(childId: String): Flow<List<MilestoneEntity>>

    @Query("SELECT * FROM milestones WHERE childId = :childId AND category = :category AND isDeleted = 0 ORDER BY achievedDate DESC")
    fun getActiveMilestonesByCategory(childId: String, category: MilestoneCategory): Flow<List<MilestoneEntity>>

    @Query("SELECT * FROM milestones WHERE childId = :childId AND achievedDate IS NOT NULL AND isDeleted = 0 ORDER BY achievedDate DESC")
    fun getAchievedMilestones(childId: String): Flow<List<MilestoneEntity>>

    @Query("SELECT * FROM milestones WHERE childId = :childId AND isExpected = 1 AND isDeleted = 0 ORDER BY expectedAgeMonths ASC")
    fun getExpectedMilestones(childId: String): Flow<List<MilestoneEntity>>

    @Query("SELECT * FROM milestones WHERE id = :id AND isDeleted = 0")
    suspend fun getMilestoneById(id: String): MilestoneEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(milestone: MilestoneEntity)

    @Update
    suspend fun update(milestone: MilestoneEntity)

    @Query("UPDATE milestones SET isDeleted = 1, updatedAt = :updatedAt WHERE id = :id")
    suspend fun softDelete(id: String, updatedAt: Long)

    @Query("SELECT COUNT(*) FROM milestones WHERE childId = :childId AND achievedDate IS NOT NULL AND isDeleted = 0")
    suspend fun getAchievedCount(childId: String): Int
}