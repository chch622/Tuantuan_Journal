package com.tuantuan.journal.data.repository

import com.tuantuan.journal.data.local.db.dao.VaccinationDao
import com.tuantuan.journal.data.mapper.VaccinationMapper
import com.tuantuan.journal.domain.model.Vaccination
import com.tuantuan.journal.domain.repository.VaccinationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Instant
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 疫苗记录 Repository 实现。
 *
 * 遵循 ARCHITECTURE.md 第2.3节 — Data 层实现 Repository 接口，
 * 负责 Entity ↔ Domain Model 映射，不暴露 Entity 给 Domain 层。
 */
@Singleton
class VaccinationRepositoryImpl @Inject constructor(
    private val vaccinationDao: VaccinationDao
) : VaccinationRepository {

    override fun getVaccinationsByChild(childId: String): Flow<List<Vaccination>> =
        vaccinationDao.getActiveVaccinationsByChild(childId).map { entities ->
            entities.map { VaccinationMapper.toDomain(it) }
        }

    override fun getAdministeredVaccinations(childId: String): Flow<List<Vaccination>> =
        vaccinationDao.getAdministeredVaccinations(childId).map { entities ->
            entities.map { VaccinationMapper.toDomain(it) }
        }

    override fun getPendingVaccinations(childId: String): Flow<List<Vaccination>> =
        vaccinationDao.getPendingVaccinations(childId).map { entities ->
            entities.map { VaccinationMapper.toDomain(it) }
        }

    override fun getOverdueVaccinations(childId: String): Flow<List<Vaccination>> =
        vaccinationDao.getOverdueVaccinations(childId, LocalDate.now().toString()).map { entities ->
            entities.map { VaccinationMapper.toDomain(it) }
        }

    override suspend fun getVaccinationById(id: String): Vaccination? =
        vaccinationDao.getVaccinationById(id)?.let { VaccinationMapper.toDomain(it) }

    override suspend fun createVaccination(vaccination: Vaccination): String {
        vaccinationDao.insert(VaccinationMapper.toEntity(vaccination))
        return vaccination.id
    }

    override suspend fun updateVaccination(vaccination: Vaccination) {
        vaccinationDao.update(VaccinationMapper.toEntity(vaccination))
    }

    override suspend fun deleteVaccination(id: String) {
        vaccinationDao.softDelete(id, Instant.now().toEpochMilli())
    }
}