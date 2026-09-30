package com.tuantuan.journal.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.vector.ImageVector
import com.tuantuan.journal.ui.model.UiError
import com.tuantuan.journal.ui.theme.TuantuanSpacing

/**
 * 错误状态组件 — ERROR_STATES.md / COMPONENTS.md §4.3
 *
 * 用于页面级错误（数据加载失败等）。
 * 错误信息必须清晰、诚实、可恢复。
 *
 * 视觉规格：
 * - 图标：Error 色，Large
 * - 标题：OnSurface 色，TitleMedium
 * - 说明：OnSurfaceVariant 色，BodyMedium
 * - 按钮：TtOutlinedButton "重试"
 * - 整体垂直居中
 *
 * @param error UiError 实例
 * @param onRetry 重试回调
 * @param modifier Modifier
 * @param icon 错误图标，默认 ErrorOutline
 * @param retryLabel 重试按钮文案，默认 "重试"
 * @param detail 补充说明，null 则不显示第二行
 */
@Composable
fun TtErrorState(
    error: UiError,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.Outlined.ErrorOutline,
    retryLabel: String = "重试",
    detail: String? = null
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // 错误图标 — Error 色
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.error
        )

        Spacer(modifier = Modifier.height(TuantuanSpacing.MD))

        // 错误标题 — OnSurface 色，TitleMedium
        Text(
            text = error.displayMessage,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )

        // 补充说明 — OnSurfaceVariant 色，BodyMedium
        if (detail != null) {
            Spacer(modifier = Modifier.height(TuantuanSpacing.SM))
            Text(
                text = detail,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(TuantuanSpacing.LG))

        // 重试按钮 — OutlinedButton
        OutlinedButton(onClick = onRetry) {
            Text(text = retryLabel)
        }
    }
}

/**
 * 简化版错误状态 — 仅显示错误消息和可选重试按钮。
 * 适用于 Snackbar 无法覆盖的轻量级错误展示。
 */
@Composable
fun TtErrorState(
    message: String,
    onRetry: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    retryLabel: String = "重试"
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Outlined.ErrorOutline,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.error
        )

        Spacer(modifier = Modifier.height(TuantuanSpacing.MD))

        Text(
            text = message,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )

        if (onRetry != null) {
            Spacer(modifier = Modifier.height(TuantuanSpacing.LG))
            OutlinedButton(onClick = onRetry) {
                Text(text = retryLabel)
            }
        }
    }
}