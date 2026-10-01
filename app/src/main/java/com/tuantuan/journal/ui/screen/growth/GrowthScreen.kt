package com.tuantuan.journal.ui.screen.growth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Straighten
import androidx.compose.material.icons.outlined.MonitorWeight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tuantuan.journal.R
import com.tuantuan.journal.domain.model.GrowthType
import com.tuantuan.journal.domain.model.Milestone
import com.tuantuan.journal.domain.model.MilestoneCategory
import com.tuantuan.journal.ui.component.AgeDisplay
import com.tuantuan.journal.ui.component.TtEmptyState
import com.tuantuan.journal.ui.component.TtLoadingIndicator
import com.tuantuan.journal.ui.theme.TuantuanSpacing
import java.time.format.DateTimeFormatter

/**
 * 成长页面 — GROWTH_PAGE.md
 *
 * 信息架构：
 * 1. 年龄概览（"团团正在慢慢长大"）
 * 2. 成长数据卡片组（身高/体重/头围/鞋码）
 * 3. 成长里程碑预览
 * 4. FAB 添加记录
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GrowthScreen(
    viewModel: GrowthViewModel = hiltViewModel(),
    onMilestoneViewAll: ((String) -> Unit)? = null
) {
    val state by viewModel.state.collectAsState()
    val child = state.selectedChild

    if (state.isLoading && state.children.isEmpty()) {
        TtLoadingIndicator(modifier = Modifier.fillMaxSize())
        return
    }

    if (state.children.isEmpty()) {
        TtEmptyState(
            title = stringResource(R.string.growth_empty_title),
            description = stringResource(R.string.growth_empty_desc),
            icon = Icons.Outlined.Straighten,
            actionLabel = stringResource(R.string.growth_empty_action),
            onAction = { /* 导航到添加儿童 */ },
            modifier = Modifier.fillMaxSize()
        )
        return
    }

    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Scaffold(
        floatingActionButton = {
            if (child != null) {
                FloatingActionButton(
                    onClick = { showBottomSheet = true },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Icon(Icons.Filled.Add, contentDescription = stringResource(R.string.growth_add_record))
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = TuantuanSpacing.Base),
            contentPadding = PaddingValues(vertical = TuantuanSpacing.XXL),
            verticalArrangement = Arrangement.spacedBy(TuantuanSpacing.LG)
        ) {
            // §3 年龄概览
            if (child != null) {
                item(key = "age_overview") {
                    AgeOverviewSection(birthDate = child.birthDate)
                }
            }

            // §4 成长数据卡片组
            if (child != null) {
                item(key = "growth_cards") {
                    GrowthDataCardsSection(
                        state = state,
                        onCardClick = { type -> viewModel.toggleCardExpansion(type) },
                        onStartRecordClick = { type ->
                            viewModel.resetForm(type)
                            showBottomSheet = true
                        }
                    )
                }
            }

            // §6 成长里程碑预览
            if (child != null) {
                item(key = "milestone_preview") {
                    MilestonePreviewSection(
                        milestones = state.recentMilestones,
                        onViewAllClick = { onMilestoneViewAll?.invoke(child.id) },
                        onAchieveClick = { id -> viewModel.achieveMilestone(id) }
                    )
                }
            }
        }
    }

    // 添加记录 BottomSheet
    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState
        ) {
            AddRecordSheetContent(
                onRecordTypeSelected = { type ->
                    viewModel.resetForm(type)
                    showBottomSheet = false
                },
                onMilestoneSelected = {
                    viewModel.resetMilestoneForm()
                    showBottomSheet = false
                }
            )
        }
    }
}

/** §3 年龄概览 — "团团正在慢慢长大" */
@Composable
private fun AgeOverviewSection(birthDate: java.time.LocalDate) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.growth_growing_up),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(TuantuanSpacing.SM))
        AgeDisplay(birthDate = birthDate)
    }
}

/** §4 成长数据卡片组 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun GrowthDataCardsSection(
    state: GrowthUiState,
    onCardClick: (GrowthType) -> Unit,
    onStartRecordClick: (GrowthType) -> Unit
) {
    val cardTypes = listOf(
        GrowthType.HEIGHT,
        GrowthType.WEIGHT,
        GrowthType.HEAD_CIRCUMFERENCE,
        GrowthType.SHOE_SIZE
    )

    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(TuantuanSpacing.SM),
        verticalArrangement = Arrangement.spacedBy(TuantuanSpacing.SM)
    ) {
        cardTypes.forEach { type ->
            GrowthDataCard(
                type = type,
                latestRecord = state.latestRecord(type),
                isExpanded = state.expandedCardType == type,
                onClick = { onCardClick(type) },
                onStartRecordClick = { onStartRecordClick(type) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

/** 单个成长数据卡片 */
@Composable
private fun GrowthDataCard(
    type: GrowthType,
    latestRecord: com.tuantuan.journal.domain.model.GrowthRecord?,
    isExpanded: Boolean,
    onClick: () -> Unit,
    onStartRecordClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val (icon, label, unit) = when (type) {
        GrowthType.HEIGHT -> Triple(Icons.Outlined.Straighten, stringResource(R.string.growth_height), "cm")
        GrowthType.WEIGHT -> Triple(Icons.Outlined.MonitorWeight, stringResource(R.string.growth_weight), "kg")
        GrowthType.HEAD_CIRCUMFERENCE -> Triple(Icons.Outlined.Straighten, stringResource(R.string.growth_head_circumference), "cm")
        GrowthType.SHOE_SIZE -> Triple(Icons.Outlined.Straighten, stringResource(R.string.growth_shoe_size), "码")
    }

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surfaceVariant,
        onClick = onClick
    ) {
        Column(modifier = Modifier.padding(TuantuanSpacing.Base)) {
            // 图标 + 标题
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(TuantuanSpacing.XS)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = label,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(TuantuanSpacing.SM))

            // 数值
            if (latestRecord != null) {
                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.spacedBy(TuantuanSpacing.XS)
                ) {
                    Text(
                        text = formatGrowthValue(latestRecord.value),
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = latestRecord.unit,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(TuantuanSpacing.XS))

                // 最近一次日期
                val dateFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")
                Text(
                    text = stringResource(
                        R.string.growth_latest_record,
                        dateFormatter.format(latestRecord.measureDate)
                    ),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(TuantuanSpacing.XS))

                // 查看完整曲线
                TextButton(onClick = onClick) {
                    Text(text = stringResource(R.string.growth_view_curve))
                }
            } else {
                // 未记录
                Text(
                    text = stringResource(R.string.growth_not_recorded),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(TuantuanSpacing.XS))

                TextButton(onClick = onStartRecordClick) {
                    Text(text = stringResource(R.string.growth_start_recording))
                }
            }

            // 展开曲线区域（Phase 2 基础实现，曲线绘制将在后续完善）
            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                if (latestRecord != null) {
                    Text(
                        text = stringResource(R.string.growth_curve_hint),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = TuantuanSpacing.SM)
                    )
                }
            }
        }
    }
}

/** §6 成长里程碑预览 */
@Composable
private fun MilestonePreviewSection(
    milestones: List<Milestone>,
    onViewAllClick: () -> Unit,
    onAchieveClick: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        // 标题行
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.milestone_title),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            if (milestones.isNotEmpty()) {
                TextButton(onClick = onViewAllClick) {
                    Text(text = stringResource(R.string.milestone_view_all))
                }
            }
        }

        Spacer(modifier = Modifier.height(TuantuanSpacing.SM))

        if (milestones.isEmpty()) {
            // 空状态
            TtEmptyState(
                title = stringResource(R.string.milestone_empty_title),
                description = stringResource(R.string.milestone_empty_desc),
                actionLabel = stringResource(R.string.milestone_empty_action),
                onAction = { /* 触发添加里程碑 */ },
                modifier = Modifier.fillMaxWidth()
            )
        } else {
            // 里程碑列表
            milestones.forEach { milestone ->
                MilestonePreviewItem(
                    milestone = milestone,
                    onAchieveClick = { onAchieveClick(milestone.id) }
                )
                Spacer(modifier = Modifier.height(TuantuanSpacing.SM))
            }
        }
    }
}

/** 单条里程碑预览 */
@Composable
private fun MilestonePreviewItem(
    milestone: Milestone,
    onAchieveClick: () -> Unit
) {
    val categoryIcon = milestoneCategoryIcon(milestone.category)

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(TuantuanSpacing.SM)
    ) {
        // 图标
        Text(
            text = categoryIcon,
            style = MaterialTheme.typography.bodyMedium
        )

        // 标题 + 日期
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = milestone.title,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (milestone.achievedDate != null) {
                val dateFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")
                Text(
                    text = dateFormatter.format(milestone.achievedDate),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                Text(
                    text = stringResource(R.string.milestone_not_achieved),
                    style = MaterialTheme.typography.bodySmall.copy(fontStyle = FontStyle.Italic),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // 未达成时显示"记录"按钮
        if (milestone.achievedDate == null) {
            TextButton(onClick = onAchieveClick) {
                Text(text = stringResource(R.string.milestone_record_this))
            }
        }
    }
}

/** 添加记录 BottomSheet 内容 */
@Composable
private fun AddRecordSheetContent(
    onRecordTypeSelected: (GrowthType) -> Unit,
    onMilestoneSelected: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(TuantuanSpacing.Base),
        verticalArrangement = Arrangement.spacedBy(TuantuanSpacing.SM)
    ) {
        Text(
            text = stringResource(R.string.growth_add_record),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(TuantuanSpacing.XS))

        // 记录身高
        AddRecordOption(
            icon = "📏",
            label = stringResource(R.string.growth_record_height),
            onClick = { onRecordTypeSelected(GrowthType.HEIGHT) }
        )

        // 记录体重
        AddRecordOption(
            icon = "⚖️",
            label = stringResource(R.string.growth_record_weight),
            onClick = { onRecordTypeSelected(GrowthType.WEIGHT) }
        )

        // 记录头围
        AddRecordOption(
            icon = "🔵",
            label = stringResource(R.string.growth_record_head),
            onClick = { onRecordTypeSelected(GrowthType.HEAD_CIRCUMFERENCE) }
        )

        // 记录鞋码
        AddRecordOption(
            icon = "👟",
            label = stringResource(R.string.growth_record_shoe),
            onClick = { onRecordTypeSelected(GrowthType.SHOE_SIZE) }
        )

        // 记录里程碑
        AddRecordOption(
            icon = "⭐",
            label = stringResource(R.string.growth_record_milestone),
            onClick = onMilestoneSelected
        )
    }
}

/** BottomSheet 选项行 */
@Composable
private fun AddRecordOption(
    icon: String,
    label: String,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surface,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.padding(TuantuanSpacing.Base),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(TuantuanSpacing.SM)
        ) {
            Text(text = icon, style = MaterialTheme.typography.titleMedium)
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

/** 里程碑类别图标映射 — GROWTH_PAGE.md §6.3 */
private fun milestoneCategoryIcon(category: MilestoneCategory): String = when (category) {
    MilestoneCategory.MOTOR -> "🌱"
    MilestoneCategory.LANGUAGE -> "🗣"
    MilestoneCategory.COGNITIVE -> "💡"
    MilestoneCategory.SOCIAL -> "🤝"
    MilestoneCategory.SELF_CARE -> "🧸"
    MilestoneCategory.OTHER -> "⭐"
}

/** 格式化成长数值（保留一位小数） */
private fun formatGrowthValue(value: Double): String {
    return if (value == value.toLong().toDouble()) {
        value.toLong().toString()
    } else {
        String.format("%.1f", value)
    }
}