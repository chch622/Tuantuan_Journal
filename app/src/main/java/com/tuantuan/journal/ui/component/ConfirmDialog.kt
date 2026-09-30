package com.tuantuan.journal.ui.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

/**
 * 确认对话框组件 — ERROR_HANDLING.md §4.2
 *
 * 用于所有需要用户确认的危险操作（删除、恢复备份等）。
 *
 * 规范：
 * - 不可逆操作使用红色确认按钮（isDestructive = true）
 * - 认文案清晰说明后果
 * - 默认焦点在取消按钮上
 * - 圆角使用 ExtraLarge（Dialog 规范）
 *
 * @param title 对话框标题
 * @param message 对话框内容描述
 * @param confirmText 认按钮文案，默认 "确认"
 * @param dismissText 取消按钮文案，默认 "取消"
 * @param isDestructive 是否为破坏性操作（红色确认按钮）
 * @param onConfirm 认回调
 * @param onDismiss 取消回调
 */
@Composable
fun ConfirmDialog(
    title: String,
    message: String,
    confirmText: String = "确认",
    dismissText: String = "取消",
    isDestructive: Boolean = false,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
        },
        text = {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                colors = if (isDestructive) {
                    androidx.compose.material3.ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                } else {
                    androidx.compose.material3.ButtonDefaults.textButtonColors()
                }
            ) {
                Text(text = confirmText)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = dismissText)
            }
        }
    )
}

/**
 * 删除确认对话框 — 预配置的 ConfirmDialog
 *
 * 专门用于删除操作，自动设置破坏性样式。
 *
 * @param itemName 被删除项目的名称，用于提示文案
 * @param isPermanent 是否为永久删除（不可恢复），影响提示文案
 * @param onConfirm 硠除确认回调
 * @param onDismiss 取消回调
 */
@Composable
fun DeleteConfirmDialog(
    itemName: String,
    isPermanent: Boolean = false,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    ConfirmDialog(
        title = "确认删除",
        message = if (isPermanent) {
            "删除 $itemName 后将无法恢复，确定要删除吗？"
        } else {
            "确定删除 $itemName 吗？"
        },
        confirmText = "删除",
        dismissText = "取消",
        isDestructive = true,
        onConfirm = onConfirm,
        onDismiss = onDismiss
    )
}