package com.tuantuan.journal.ui.screen.growth

import com.tuantuan.journal.domain.model.Child
import com.tuantuan.journal.domain.model.GrowthRecord
import com.tuantuan.journal.domain.model.GrowthType
import com.tuantuan.journal.domain.model.Milestone
import com.tuantuan.journal.ui.model.UiError

data class GrowthUiState(
    val isLoading: Boolean = false,
    val error: UiError? = null,
    val children: List<Child> = emptyList(),
    val selectedChildId: String? = null,
    val recordsByType: Map<GrowthType, List<GrowthRecord>> = emptyMap(),
    val recentMilestones: List<Milestone> = emptyList(),
    val expandedCardType: GrowthType? = null,
    val showAddSheet: Boolean = false
) {
    /** 获取当前选中的儿童 */
    val selectedChild: Child?
        get() = children.find { it.id == selectedChildId }

    /** 获取指定类型的最新记录 */
    fun latestRecord(type: GrowthType): GrowthRecord? =
        recordsByType[type]?.firstOrNull()

    /** 获取指定类型的所有记录（按日期排序） */
    fun recordsOfType(type: GrowthType): List<GrowthRecord> =
        recordsByType[type] ?: emptyList()

    /** 是否有任何成长数据 */
    val hasAnyData: Boolean
        get() = recordsByType.any { it.value.isNotEmpty() }

    companion object {
        /** 里程碑预览数量（首页只显示3+1） */
        const val MILESTONE_PREVIEW_COUNT = 4
    }
}

data class GrowthFormState(
    val recordType: GrowthType = GrowthType.HEIGHT,
    val value: String = "",
    val unit: String = "",
    val measureDate: String = "",
    val notes: String = "",
    val isSaving: Boolean = false,
    val savedSuccessfully: Boolean = false,
    val error: UiError? = null
) {
    /** 数值是否有效 */
    val isValid: Boolean
        get() = value.isNotBlank() && value.toDoubleOrNull()?.let { it > 0 } == true
}