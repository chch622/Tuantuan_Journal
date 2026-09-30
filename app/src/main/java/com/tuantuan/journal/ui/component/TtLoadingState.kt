package com.tuantuan.journal.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.tuantuan.journal.ui.theme.TuantuanSpacing

/**
 * 加载状态组件 — LOADING_STATES.md / COMPONENTS.md §4.3
 *
 * 支持三种模式：
 * - Indicator：圆形加载指示器（默认，适用于不确定时长）
 * - Skeleton：骨架屏占位（适用于列表/卡片等已知布局）
 * - Progress：线性进度条（适用于备份/恢复等批量操作）
 *
 * 视觉规格：
 * - Indicator 模式：CircularProgressIndicator，Primary 色
 * - Skeleton 模式：PrimaryContainer 色，圆角占位矩形
 * - Progress 模式：LinearProgressIndicator，Primary 色
 */

/**
 * 圆形加载指示器模式
 *
 * @param message 加载提示文案，null 则不显示
 */
@Composable
fun TtLoadingIndicator(
    modifier: Modifier = Modifier,
    message: String? = null
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(48.dp),
            color = MaterialTheme.colorScheme.primary,
            strokeWidth = 4.dp
        )

        if (message != null) {
            Spacer(modifier = Modifier.height(TuantuanSpacing.MD))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * 骨架屏模式 — 单个卡片占位
 *
 * @param lines 占位行数
 */
@Composable
fun TtLoadingSkeleton(
    modifier: Modifier = Modifier,
    lines: Int = 3
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(com.tuantuan.journal.ui.theme.TuantuanRoundedCorner.Large),
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier.padding(TuantuanSpacing.Base),
            verticalArrangement = Arrangement.spacedBy(TuantuanSpacing.SM)
        ) {
            // 标题占位
            Surface(
                modifier = Modifier.fillMaxWidth(0.6f),
                shape = RoundedCornerShape(com.tuantuan.journal.ui.theme.TuantuanRoundedCorner.Small),
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Box(modifier = Modifier.height(20.dp))
            }

            // 内容行占位
            repeat(lines) { index ->
                val widthFraction = if (index == lines - 1) 0.4f else 0.8f
                Surface(
                    modifier = Modifier.fillMaxWidth(widthFraction),
                    shape = RoundedCornerShape(com.tuantuan.journal.ui.theme.TuantuanRoundedCorner.Small),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Box(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

/**
 * 线性进度条模式
 *
 * @param progress 进度 0f-1f，null 为不确定进度
 * @param message 进度提示文案
 */
@Composable
fun TtLoadingProgress(
    modifier: Modifier = Modifier,
    progress: Float? = null,
    message: String? = null
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (progress != null) {
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primary
            )
        } else {
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primary
            )
        }

        if (message != null) {
            Spacer(modifier = Modifier.height(TuantuanSpacing.SM))
            Text(
                text = message,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}