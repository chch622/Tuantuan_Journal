package com.tuantuan.journal.domain.repository

import com.tuantuan.journal.domain.model.Vaccination
import kotlinx.coroutines.flow.Flow

/**
 * 疫苗记录 Repository 接口。
 *
 * 遵循 ARCHITECTURE.md 第2.2节 — Domain 层定义接口，Data 层实现。
 */
interface VaccinationRepository {

    fun getVaccinationsByChild(childId: String): Flow<List<Vaccination>>

    fun getAdministeredVaccinations(childId: String): Flow<List<Vaccination>>

    fun getPendingVaccinations(childId: String): Flow<List<Vaccination>>

    fun getOverdueVaccinations(childId: String): Flow<List<Vaccination>>

    suspend fun getVaccinationById(id: String): Vaccination?

    suspend fun createVaccination(vaccination: Vaccination): String

    suspend fun updateVaccination(vaccination: Vaccination)

    suspend fun deleteVaccination(id: String)
}