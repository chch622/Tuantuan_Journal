package com.tuantuan.journal.ui.screen.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material.icons.outlined.ChildCare
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tuantuan.journal.R
import com.tuantuan.journal.domain.model.Child
import com.tuantuan.journal.ui.component.AgeDisplay
import com.tuantuan.journal.ui.component.BirthdayCountdown
import com.tuantuan.journal.ui.component.TtDiaryCard
import com.tuantuan.journal.ui.component.TtEmptyState
import com.tuantuan.journal.ui.component.TtGrowthCard
import com.tuantuan.journal.ui.component.TtLoadingIndicator
import com.tuantuan.journal.ui.component.TtTodayCard
import com.tuantuan.journal.ui.theme.TuantuanElevation
import com.tuantuan.journal.ui.theme.TuantuanSpacing

/**
 * 首页 — HOME_PAGE.md
 *
 * 信息架构（自上而下）：
 * 1. 团团主照片 + 名字 + 年龄
 * 2. 今日卡片
 * 3. 最近回忆
 * 4. 成长概览
 * 5. 生日倒计时
 *
 * 多儿童时顶部显示儿童切换器（FilterChip）。
 * 无儿童时显示空状态。
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onChildClick: (String) -> Unit,
    onAddChildClick: () -> Unit,
    onTagManageClick: () -> Unit = {},
    onDiaryClick: (String) -> Unit = {},
    onAddDiaryClick: (String) -> Unit = {},
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.home_title)) },
                actions = {
                    IconButton(onClick = onTagManageClick) {
                        Icon(Icons.Default.Tag, contentDescription = stringResource(R.string.tag_management))
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddChildClick) {
                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.add_child))
            }
        }
    ) { padding ->
        if (state.isLoading) {
            TtLoadingIndicator(
                modifier = Modifier.fillMaxSize().padding(padding)
            )
        } else if (state.children.isEmpty()) {
            // 空状态 — HOME_PAGE.md §10
            TtEmptyState(
                title = stringResource(R.string.welcome_title),
                description = stringResource(R.string.welcome_desc),
                icon = Icons.Outlined.ChildCare,
                actionLabel = stringResource(R.string.welcome_action),
                onAction = onAddChildClick,
                modifier = Modifier.fillMaxSize().padding(padding)
            )
        } else {
            // 有儿童数据 — 显示首页内容
            val selectedChild = state.selectedChild

            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(
                    start = TuantuanSpacing.Base,
                    end = TuantuanSpacing.Base,
                    top = TuantuanSpacing.XXL,
                    bottom = TuantuanSpacing.XXL
                ),
                verticalArrangement = Arrangement.spacedBy(TuantuanSpacing.LG)
            ) {
                // 多儿童切换器
                if (state.children.size > 1) {
                    item {
                        ChildSwitcher(
                            children = state.children,
                            selectedChildId = state.selectedChildId,
                            onChildSelected = { viewModel.selectChild(it) }
                        )
                    }
                }

                // 选中儿童的主内容
                if (selectedChild != null) {
                    // §3 团团主照片 + 名字 + 年龄
                    item {
                        ChildHeroSection(
                            child = selectedChild,
                            onChildClick = { onChildClick(selectedChild.id) }
                        )
                    }

                    // §5 今日卡片
                    item {
                        TtTodayCard(
                            todayEntryCount = state.todayEntryCount,
                            onRecordToday = { onAddDiaryClick(selectedChild.id) },
                            onViewToday = { onChildClick(selectedChild.id) }
                        )
                    }

                    // §6 最近回忆
                    val recentEntries = state.recentEntriesForSelectedChild
                    if (recentEntries.isNotEmpty()) {
                        item {
                            Text(
                                text = stringResource(R.string.recent_memories),
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        items(recentEntries, key = { it.id }) { entry ->
                            TtDiaryCard(
                                entry = entry,
                                onClick = { onDiaryClick(entry.id) }
                            )
                        }
                        item {
                            TextButton(
                                onClick = { onChildClick(selectedChild.id) },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(stringResource(R.string.view_all))
                            }
                        }
                    }

                    // §7 成长概览（Phase 1 占位）
                    item {
                        TtGrowthCard()
                    }

                    // §8 生日倒计时
                    item {
                        BirthdayCountdown(
                            nickname = selectedChild.nickname,
                            birthDate = selectedChild.birthDate
                        )
                    }
                }
            }
        }
    }
}

/**
 * 儿童切换器 — 多儿童时显示
 */
@Composable
private fun ChildSwitcher(
    children: List<Child>,
    selectedChildId: String?,
    onChildSelected: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(TuantuanSpacing.SM)
    ) {
        children.forEach { child ->
            FilterChip(
                selected = child.id == selectedChildId,
                onClick = { onChildSelected(child.id) },
                label = { Text(child.nickname) }
            )
        }
    }
}

/**
 * 团团主照片 + 名字 + 年龄 — HOME_PAGE.md §3-4
 *
 * 主照片：圆角大图（ExtraLarge 24dp），轻微阴影（Level1）
 * 宽度：页面宽度 - 2 × Base (16dp)
 * 高度：宽度 × 0.75（4:3 比例）
 * 名字：TitleLarge
 * 年龄：AgeDisplay 组件
 */
@Composable
private fun ChildHeroSection(
    child: Child,
    onChildClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 主照片（圆角大图）
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .clickable(onClick = onChildClick),
            shape = MaterialTheme.shapes.extraLarge,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = TuantuanElevation.Level1
            )
        ) {
            // 照片区域：4:3 比例占位
            // TODO: Phase 3 - 加载 child.avatarPath 实际照片
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(TuantuanSpacing.XXXL * 5) // 约 240dp，接近 4:3
                    .clip(MaterialTheme.shapes.extraLarge),
                contentAlignment = Alignment.Center
            ) {
                if (child.avatarPath != null) {
                    // TODO: Phase 3 - 使用 Coil 加载 avatarPath
                    // 目前使用占位图标
                    AvatarPlaceholder()
                } else {
                    AvatarPlaceholder()
                }
            }
        }

        Spacer(modifier = Modifier.height(TuantuanSpacing.MD))

        // 名字
        Text(
            text = child.nickname,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(TuantuanSpacing.XS))

        // 年龄 — X岁X个月X天
        AgeDisplay(birthDate = child.birthDate)
    }
}

@Composable
private fun AvatarPlaceholder() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(TuantuanSpacing.SM))
        Text(
            text = stringResource(R.string.click_to_set_photo),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}