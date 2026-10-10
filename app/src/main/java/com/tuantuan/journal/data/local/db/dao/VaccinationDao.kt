package com.tuantuan.journal.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.tuantuan.journal.data.local.db.entity.VaccinationEntity
import kotlinx.coroutines.flow.Flow

/**
 * 疫苗记录 DAO。
 *
 * 查询规范遵循 DATA_MODEL.md 第7节：
 * - 默认过滤 isDeleted = 0
 * - 已接种按 administeredDate 降序
 * - 待接种按 scheduledDate 升序
 */
@Dao
interface VaccinationDao {

    /** 获取指定儿童的所有疫苗记录（已接种在前，待接种在后） */
    @Query("""
        SELECT * FROM vaccinations
        WHERE childId = :childId AND isDeleted = 0
        ORDER BY administeredDate DESC, scheduledDate ASC
    """)
    fun getActiveVaccinationsByChild(childId: String): Flow<List<VaccinationEntity>>

    /** 获取已接种记录 */
    @Query("""
        SELECT * FROM vaccinations
        WHERE childId = :childId AND administeredDate IS NOT NULL AND isDeleted = 0
        ORDER BY administeredDate DESC
    """)
    fun getAdministeredVaccinations(childId: String): Flow<List<VaccinationEntity>>

    /** 获取待接种记录（有计划日期但尚未接种） */
    @Query("""
        SELECT * FROM vaccinations
        WHERE childId = :childId AND administeredDate IS NULL AND scheduledDate IS NOT NULL AND isDeleted = 0
        ORDER BY scheduledDate ASC
    """)
    fun getPendingVaccinations(childId: String): Flow<List<VaccinationEntity>>

    /** 获取逾期未接种记录（计划日期已过但尚未接种） */
    @Query("""
        SELECT * FROM vaccinations
        WHERE childId = :childId AND administeredDate IS NULL AND scheduledDate < :today AND isDeleted = 0
        ORDER BY scheduledDate ASC
    """)
    fun getOverdueVaccinations(childId: String, today: String): Flow<List<VaccinationEntity>>

    /** 获取单条疫苗记录 */
    @Query("SELECT * FROM vaccinations WHERE id = :id AND isDeleted = 0")
    suspend fun getVaccinationById(id: String): VaccinationEntity?

    /** 插入或替换疫苗记录 */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vaccination: VaccinationEntity)

    /** 更新疫苗记录 */
    @Update
    suspend fun update(vaccination: VaccinationEntity)

    /** 软删除疫苗记录 */
    @Query("UPDATE vaccinations SET isDeleted = 1, updatedAt = :updatedAt WHERE id = :id")
    suspend fun softDelete(id: String, updatedAt: Long)

    /** 获取已接种数量 */
    @Query("SELECT COUNT(*) FROM vaccinations WHERE childId = :childId AND administeredDate IS NOT NULL AND isDeleted = 0")
    suspend fun getAdministeredCount(childId: String): Int

    /** 获取待接种数量 */
    @Query("SELECT COUNT(*) FROM vaccinations WHERE childId = :childId AND administeredDate IS NULL AND scheduledDate IS NOT NULL AND isDeleted = 0")
    suspend fun getPendingCount(childId: String): Int
}