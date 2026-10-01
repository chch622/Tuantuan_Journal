package com.tuantuan.journal.ui.screen.milestone

import com.tuantuan.journal.domain.model.Milestone
import com.tuantuan.journal.domain.model.MilestoneCategory
import com.tuantuan.journal.ui.model.UiError

data class MilestoneUiState(
    val isLoading: Boolean = false,
    val error: UiError? = null,
    val selectedChildId: String? = null,
    val selectedCategory: MilestoneCategory? = null,
    val milestones: List<Milestone> = emptyList()
) {
    /** 按当前筛选分类过滤里程碑 */
    val filteredMilestones: List<Milestone>
        get() = selectedCategory?.let { cat ->
            milestones.filter { it.category == cat }
        } ?: milestones

    /** 已达成的里程碑 */
    val achievedMilestones: List<Milestone>
        get() = filteredMilestones.filter { it.achievedDate != null }

    /** 未达成的里程碑 */
    val pendingMilestones: List<Milestone>
        get() = filteredMilestones.filter { it.achievedDate == null }

    /** 是否有里程碑数据 */
    val hasMilestones: Boolean
        get() = milestones.isNotEmpty()
}

data class MilestoneFormState(
    val category: String = "",
    val title: String = "",
    val description: String = "",
    val achievedDate: String = "",
    val notes: String = "",
    val isSaving: Boolean = false,
    val savedSuccessfully: Boolean = false,
    val error: UiError? = null
) {
    /** 标题是否有效 */
    val isValid: Boolean
        get() = title.isNotBlank()
}