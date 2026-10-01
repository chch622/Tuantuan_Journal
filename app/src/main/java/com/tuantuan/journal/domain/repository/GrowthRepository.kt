package com.tuantuan.journal.domain.repository

import com.tuantuan.journal.domain.model.GrowthRecord
import com.tuantuan.journal.domain.model.GrowthType
import kotlinx.coroutines.flow.Flow

/**
 * 成长记录 Repository 接口。
 *
 * 遵循 ARCHITECTURE.md 第2.2节 — Domain 层定义接口，Data 层实现。
 */
interface GrowthRepository {

    fun getRecordsByChild(childId: String): Flow<List<GrowthRecord>>

    fun getRecordsByType(childId: String, type: GrowthType): Flow<List<GrowthRecord>>

    suspend fun getRecordById(id: String): GrowthRecord?

    suspend fun createRecord(record: GrowthRecord): String

    suspend fun updateRecord(record: GrowthRecord)

    suspend fun deleteRecord(id: String)

    suspend fun getLatestRecord(childId: String, type: GrowthType): GrowthRecord?
}