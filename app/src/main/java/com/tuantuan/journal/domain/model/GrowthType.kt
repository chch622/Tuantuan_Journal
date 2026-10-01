package com.tuantuan.journal.domain.model

/**
 * 成长记录类型。
 *
 * 对应 DATA_MODEL.md 第2.6节 GrowthRecord.recordType。
 * Phase 2 P0: HEIGHT, WEIGHT；P1: HEAD_CIRCUMFERENCE, SHOE_SIZE。
 */
enum class GrowthType {
    HEIGHT,
    WEIGHT,
    HEAD_CIRCUMFERENCE,
    SHOE_SIZE
}