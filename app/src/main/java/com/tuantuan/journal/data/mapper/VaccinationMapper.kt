package com.tuantuan.journal.data.mapper

import com.tuantuan.journal.data.local.db.entity.VaccinationEntity
import com.tuantuan.journal.domain.model.Vaccination

/**
 * Vaccination Entity ↔ Domain Model 映射器。
 *
 * 遵循 ARCHITECTURE.md 第2.3节 — Data 层负责 Entity ↔ Domain Model 转换。
 */
object VaccinationMapper {

    fun toDomain(entity: VaccinationEntity): Vaccination = Vaccination(
        id = entity.id,
        childId = entity.childId,
        vaccineName = entity.vaccineName,
        doseNumber = entity.doseNumber,
        administeredDate = entity.administeredDate,
        scheduledDate = entity.scheduledDate,
        location = entity.location,
        batchNumber = entity.batchNumber,
        notes = entity.notes,
        isDeleted = entity.isDeleted,
        createdAt = entity.createdAt,
        updatedAt = entity.updatedAt
    )

    fun toEntity(domain: Vaccination): VaccinationEntity = VaccinationEntity(
        id = domain.id,
        childId = domain.childId,
        vaccineName = domain.vaccineName,
        doseNumber = domain.doseNumber,
        administeredDate = domain.administeredDate,
        scheduledDate = domain.scheduledDate,
        location = domain.location,
        batchNumber = domain.batchNumber,
        notes = domain.notes,
        isDeleted = domain.isDeleted,
        createdAt = domain.createdAt,
        updatedAt = domain.updatedAt
    )
}