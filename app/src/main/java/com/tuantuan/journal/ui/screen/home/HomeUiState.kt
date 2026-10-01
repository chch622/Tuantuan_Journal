package com.tuantuan.journal.ui.screen.home

import com.tuantuan.journal.domain.model.Child
import com.tuantuan.journal.domain.model.DiaryEntry
import com.tuantuan.journal.ui.model.UiError

data class HomeUiState(
    val children: List<Child> = emptyList(),
    val selectedChildId: String? = null,
    val todayEntries: Map<String, List<DiaryEntry>> = emptyMap(),
    val recentEntries: Map<String, List<DiaryEntry>> = emptyMap(),
    val isLoading: Boolean = false,
    val error: UiError? = null
) {
    /** 获取当前选中的儿童 */
    val selectedChild: Child?
        get() = children.find { it.id == selectedChildId }

    /** 获取当前选中儿童的今日日记数 */
    val todayEntryCount: Int
        get() = selectedChildId?.let { todayEntries[it]?.size ?: 0 } ?: 0

    /** 获取当前选中儿童的最近日记 */
    val recentEntriesForSelectedChild: List<DiaryEntry>
        get() = selectedChildId?.let { recentEntries[it] ?: emptyList() } ?: emptyList()
}