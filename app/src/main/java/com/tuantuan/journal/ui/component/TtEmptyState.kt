package com.tuantuan.journal.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Inbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.tuantuan.journal.ui.theme.TuantuanSpacing

/**
 * 空状态组件 — EMPTY_STATES.md / COMPONENTS.md §4.3
 *
 * 用于各页面数据为空时的展示。
 * 文案应该温柔、鼓励、有引导，提供明确的下一步操作。
 *
 * 视觉规格：
 * - 图标：OutlineVariant 色，弱化
 * - 标题：OnSurface 色，TitleMedium
 * - 说明：OnSurfaceVariant 色，BodyMedium
 * - 按钮：Primary 色，TtTextButton
 * - 图标与标题间距：MD (12dp)
 * - 标题与说明间距：SM (8dp)
 * - 说明与按钮间距：LG (20dp)
 * - 整体垂直居中
 *
 * @param icon 顶部图标，默认 Inbox
 * @param title 标题文案
 * @param description 说明文案
 * @param actionLabel 操作按钮文案，null 则不显示按钮
 * @param onAction 操作按钮回调
 * @param modifier Modifier
 */
@Composable
fun TtEmptyState(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.Outlined.Inbox,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // 图标 — OutlineVariant 色，弱化
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(TuantuanSpacing.MD))

        // 标题 — OnSurface 色，TitleMedium
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(TuantuanSpacing.SM))

        // 说明 — OnSurfaceVariant 色，BodyMedium
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        // 操作按钮 — Primary 色
        if (actionLabel != null && onAction != null) {
            Spacer(modifier = Modifier.height(TuantuanSpacing.LG))

            TextButton(onClick = onAction) {
                Text(text = actionLabel)
            }
        }
    }
}