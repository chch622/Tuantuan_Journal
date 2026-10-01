package com.tuantuan.journal.ui.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.tuantuan.journal.R

/**
 * 确认对话框组件 — ERROR_HANDLING.md §4.2
 *
 * 用于所有需要用户确认的危险操作（删除、恢复备份等）。
 *
 * 规范：
 * - 不可逆操作使用红色确认按钮（isDestructive = true）
 * - 确认文案清晰说明后果
 * - 默认焦点在取消按钮上
 * - 圆角使用 ExtraLarge（Dialog 规范）
 *
 * @param title 对话框标题
 * @param message 对话框内容描述
 * @param confirmText 确认按钮文案，默认 "确认"
 * @param dismissText 取消按钮文案，默认 "取消"
 * @param isDestructive 是否为破坏性操作（红色确认按钮）
 * @param onConfirm 确认回调
 * @param onDismiss 取消回调
 */
@Composable
fun ConfirmDialog(
    title: String,
    message: String,
    confirmText: String = stringResource(R.string.confirm),
    dismissText: String = stringResource(R.string.cancel),
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
 * @param onConfirm 删除确认回调
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
        title = stringResource(R.string.confirm_delete_title),
        message = if (isPermanent) {
            stringResource(R.string.confirm_delete_permanent, itemName)
        } else {
            stringResource(R.string.confirm_delete_temp, itemName)
        },
        confirmText = stringResource(R.string.delete),
        dismissText = stringResource(R.string.cancel),
        isDestructive = true,
        onConfirm = onConfirm,
        onDismiss = onDismiss
    )
}