package com.tuantuan.journal.domain.model

/**
 * 里程碑类别。
 *
 * 对应 DATA_MODEL.md 第2.7节 Milestone.category。
 * 与 GROWTH_PAGE.md 第6.3节图标映射一致。
 */
enum class MilestoneCategory {
    MOTOR,
    LANGUAGE,
    COGNITIVE,
    SOCIAL,
    SELF_CARE,
    OTHER
}