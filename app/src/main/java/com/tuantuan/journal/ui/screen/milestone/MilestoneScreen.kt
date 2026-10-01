package com.tuantuan.journal.ui.screen.milestone

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tuantuan.journal.R
import com.tuantuan.journal.domain.model.Milestone
import com.tuantuan.journal.domain.model.MilestoneCategory
import com.tuantuan.journal.ui.component.TtEmptyState
import com.tuantuan.journal.ui.component.TtLoadingIndicator
import com.tuantuan.journal.ui.theme.TuantuanSpacing
import java.time.format.DateTimeFormatter

/**
 * 里程碑页面 — MILESTONE_PAGE.md
 *
 * 信息架构：
 * 1. 分类筛选 Tab（动作/语言/认知/社交/自理/其他）
 * 2. 已达成里程碑列表
 * 3. 未达成里程碑列表
 * 4. FAB 添加里程碑
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MilestoneScreen(
    viewModel: MilestoneViewModel = hiltViewModel(),
    onAddMilestone: (() -> Unit)? = null
) {
    val state by viewModel.state.collectAsState()

    if (state.isLoading) {
        TtLoadingIndicator(modifier = Modifier.fillMaxSize())
        return
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onAddMilestone?.invoke() },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Filled.Add, contentDescription = stringResource(R.string.milestone_add))
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = TuantuanSpacing.Base)
        ) {
            // §5 分类筛选
            CategoryFilterRow(
                selectedCategory = state.selectedCategory,
                onCategorySelected = { viewModel.selectCategory(it) }
            )

            Spacer(modifier = Modifier.height(TuantuanSpacing.MD))

            // 内容区域
            if (!state.hasMilestones) {
                // 全局空状态
                TtEmptyState(
                    title = stringResource(R.string.milestone_empty_title),
                    description = stringResource(R.string.milestone_empty_desc),
                    icon = Icons.Outlined.EmojiEvents,
                    actionLabel = stringResource(R.string.milestone_empty_action),
                    onAction = { onAddMilestone?.invoke() },
                    modifier = Modifier.fillMaxSize()
                )
            } else if (state.filteredMilestones.isEmpty()) {
                // 当前分类空状态
                TtEmptyState(
                    title = stringResource(R.string.milestone_category_empty_title),
                    description = stringResource(R.string.milestone_category_empty_desc),
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                MilestoneListContent(
                    achievedMilestones = state.achievedMilestones,
                    pendingMilestones = state.pendingMilestones,
                    onAchieveClick = { id -> viewModel.achieveMilestone(id) }
                )
            }
        }
    }
}

/** §5 分类筛选 Row */
@Composable
private fun CategoryFilterRow(
    selectedCategory: MilestoneCategory?,
    onCategorySelected: (MilestoneCategory?) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(TuantuanSpacing.XS)
    ) {
        // "全部"选项
        CategoryChip(
            label = stringResource(R.string.milestone_category_all),
            isSelected = selectedCategory == null,
            onClick = { onCategorySelected(null) }
        )

        // 各分类
        MilestoneCategory.entries.forEach { category ->
            CategoryChip(
                label = milestoneCategoryLabel(category),
                emoji = milestoneCategoryIcon(category),
                isSelected = selectedCategory == category,
                onClick = { onCategorySelected(category) }
            )
        }
    }
}

/** 分类筛选 Chip */
@Composable
private fun CategoryChip(
    label: String,
    emoji: String? = null,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        shape = MaterialTheme.shapes.small,
        color = if (isSelected) MaterialTheme.colorScheme.primaryContainer
        else MaterialTheme.colorScheme.surfaceVariant,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.padding(horizontal = TuantuanSpacing.SM, vertical = TuantuanSpacing.XS),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(TuantuanSpacing.XS)
        ) {
            if (emoji != null) {
                Text(text = emoji, style = MaterialTheme.typography.bodySmall)
            }
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer
                else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/** 里程碑列表内容 */
@Composable
private fun MilestoneListContent(
    achievedMilestones: List<Milestone>,
    pendingMilestones: List<Milestone>,
    onAchieveClick: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = TuantuanSpacing.SM),
        verticalArrangement = Arrangement.spacedBy(TuantuanSpacing.SM)
    ) {
        // 未达成里程碑（优先展示）
        if (pendingMilestones.isNotEmpty()) {
            item(key = "pending_header") {
                SectionHeader(
                    title = stringResource(R.string.milestone_pending_title),
                    count = pendingMilestones.size
                )
            }

            items(
                items = pendingMilestones,
                key = { "pending_${it.id}" }
            ) { milestone ->
                MilestoneCard(
                    milestone = milestone,
                    isAchieved = false,
                    onAchieveClick = { onAchieveClick(milestone.id) }
                )
            }
        }

        // 已达成里程碑
        if (achievedMilestones.isNotEmpty()) {
            item(key = "achieved_header") {
                Spacer(modifier = Modifier.height(TuantuanSpacing.SM))
                SectionHeader(
                    title = stringResource(R.string.milestone_achieved_title),
                    count = achievedMilestones.size
                )
            }

            items(
                items = achievedMilestones,
                key = { "achieved_${it.id}" }
            ) { milestone ->
                MilestoneCard(
                    milestone = milestone,
                    isAchieved = true,
                    onAchieveClick = null
                )
            }
        }
    }
}

/** 区段标题 */
@Composable
private fun SectionHeader(title: String, count: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(TuantuanSpacing.XS)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = "($count)",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/** 单条里程碑卡片 */
@Composable
private fun MilestoneCard(
    milestone: Milestone,
    isAchieved: Boolean,
    onAchieveClick: (() -> Unit)?,
    modifier: Modifier = Modifier
) {
    val categoryIcon = milestoneCategoryIcon(milestone.category)

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        color = if (isAchieved) MaterialTheme.colorScheme.surfaceVariant
        else MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier.padding(TuantuanSpacing.Base),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(TuantuanSpacing.SM)
        ) {
            // 分类图标
            Text(
                text = categoryIcon,
                style = MaterialTheme.typography.titleMedium
            )

            // 内容
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = milestone.title,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                if (milestone.description != null) {
                    Text(
                        text = milestone.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                if (isAchieved && milestone.achievedDate != null) {
                    val dateFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")
                    Text(
                        text = dateFormatter.format(milestone.achievedDate),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // 达成按钮
            if (onAchieveClick != null) {
                TextButton(onClick = onAchieveClick) {
                    Text(text = stringResource(R.string.milestone_record_this))
                }
            }
        }
    }
}

/** 里程碑分类标签映射 */
private fun milestoneCategoryLabel(category: MilestoneCategory): String = when (category) {
    MilestoneCategory.MOTOR -> "动作"
    MilestoneCategory.LANGUAGE -> "语言"
    MilestoneCategory.COGNITIVE -> "认知"
    MilestoneCategory.SOCIAL -> "社交"
    MilestoneCategory.SELF_CARE -> "自理"
    MilestoneCategory.OTHER -> "其他"
}

/** 里程碑分类图标映射 */
private fun milestoneCategoryIcon(category: MilestoneCategory): String = when (category) {
    MilestoneCategory.MOTOR -> "🌱"
    MilestoneCategory.LANGUAGE -> "🗣"
    MilestoneCategory.COGNITIVE -> "💡"
    MilestoneCategory.SOCIAL -> "🤝"
    MilestoneCategory.SELF_CARE -> "🧸"
    MilestoneCategory.OTHER -> "⭐"
}