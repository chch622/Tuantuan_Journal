# ERROR_HANDLING.md — Tuantuan Journal 错误处理规范

> **本文件定义 Tuantuan Journal 的错误处理策略。**
> **错误处理必须用户友好，绝不丢失数据。**

---

## 1. 错误分类

### 1.1 可恢复错误

用户可以采取行动解决的错误。

| 错误 | 用户提示 | 恢复操作 |
|------|---------|---------|
| 存储空间不足 | "存储空间不足，请清理后重试" | 清理空间或删除旧数据 |
| 文件不存在 | "文件未找到，可能已被移动" | 重新选择文件 |
| 文件格式不支持 | "不支持此文件格式" | 选择其他格式 |
| 文件过大 | "文件超过大小限制（20MB）" | 选择更小的文件 |
| 备份文件损坏 | "备份文件已损坏，无法恢复" | 使用其他备份 |
| 数据库迁移失败 | "数据升级失败，请联系支持" | 重新安装 |

### 1.2 不可恢复错误

应用无法自动恢复的错误，需要用户干预。

| 错误 | 用户提示 | 处理方式 |
|------|---------|---------|
| 数据库严重损坏 | "数据异常，建议重新安装" | 引导备份和重装 |
| 关键文件缺失 | "应用数据异常" | 引导重装 |
| 版本不兼容 | "应用版本过低，请升级" | 引导升级 |

### 1.3 预期错误

业务逻辑中的正常错误路径。

| 错误 | 用户提示 | 处理方式 |
|------|---------|---------|
| 儿童名称为空 | "请输入儿童姓名" | 表单验证 |
| 日记内容为空 | "请输入日记内容" | 表单验证 |
| 日期在未来 | "日期不能晚于今天" | 表单验证 |
| 标签名重复 | "标签名已存在" | 提示修改 |

---

## 2. 错误处理架构

### 2.1 分层错误处理

```
Data 层: 捕获技术异常 → 转为 DomainException
    ↓
Domain 层: 处理业务错误 → 传播或处理
    ↓
Presentation 层: 转为 UiError → 显示给用户
```

### 2.2 DomainException

```kotlin
sealed class DomainException(message: String) : Exception(message) {
    data class StorageFull(val required: Long, val available: Long) 
        : DomainException("存储空间不足")
    data class MediaNotFound(val path: String) 
        : DomainException("媒体文件未找到")
    data class DatabaseError(val code: Int, val detail: String) 
        : DomainException("数据库错误: $code")
    data class FileTooLarge(val maxSize: Long, val actualSize: Long) 
        : DomainException("文件过大")
    data class UnsupportedFormat(val mimeType: String) 
        : DomainException("不支持的格式: $mimeType")
    data class ValidationError(val field: String, val reason: String) 
        : DomainException("验证失败: $field - $reason")
    data class BackupError(val detail: String) 
        : DomainException("备份错误: $detail")
    data class Unknown(val original: Throwable) 
        : DomainException("未知错误")
}
```

### 2.3 UiError

```kotlin
sealed class UiError {
    data class StorageFull(val message: String) : UiError()
    data class MediaNotFound(val message: String) : UiError()
    data class BackupCorrupted(val message: String) : UiError()
    data class DatabaseError(val message: String) : UiError()
    data class ValidationError(val field: String, val message: String) : UiError()
    data class FileTooLarge(val message: String) : UiError()
    data class UnsupportedFormat(val message: String) : UiError()
    data class GenericError(val message: String) : UiError()
}

fun DomainException.toUiError(): UiError = when (this) {
    is DomainException.StorageFull -> UiError.StorageFull("存储空间不足，请清理后重试")
    is DomainException.MediaNotFound -> UiError.MediaNotFound("文件未找到")
    is DomainException.DatabaseError -> UiError.DatabaseError("数据操作失败，请重试")
    is DomainException.FileTooLarge -> UiError.FileTooLarge("文件超过大小限制")
    is DomainException.UnsupportedFormat -> UiError.UnsupportedFormat("不支持此文件格式")
    is DomainException.ValidationError -> UiError.ValidationError(field, reason)
    is DomainException.BackupError -> UiError.BackupCorrupted("备份操作失败")
    is DomainException.Unknown -> UiError.GenericError("发生未知错误")
}
```

---

## 3. UI 错误展示

### 3.1 错误展示方式

| 错误类型 | 展示方式 | 持续时间 |
|---------|---------|---------|
| 表单验证 | 字段下方红色文字 | 持续直到修正 |
| 操作失败 | Snackbar | 5秒或手动关闭 |
| 严重错误 | 全屏错误页面 | 持续直到操作 |
| 网络无关 | Toast（极少使用） | 3秒 |

### 3.2 Snackbar 模式

```kotlin
@Composable
fun ErrorSnackbar(
    error: UiError?,
    onDismiss: () -> Unit,
    onAction: (() -> Unit)? = null
) {
    error?.let {
        Snackbar(
            action = onAction?.let { { it() } },
            dismissAction = { onDismiss() }
        ) {
            Text(text = it.toUserMessage())
        }
    }
}
```

### 3.3 全屏错误

```kotlin
@Composable
fun FullScreenError(
    error: UiError,
    onRetry: () -> Unit,
    onGoBack: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(Icons.Outlined.ErrorOutline, contentDescription = null)
        Text(text = error.toUserMessage())
        Button(onClick = onRetry) { Text("重试") }
        TextButton(onClick = onGoBack) { Text("返回") }
    }
}
```

---

## 4. 危险操作确认

### 4.1 需要确认的操作

| 操作 | 确认文案 | 不可逆 |
|------|---------|--------|
| 删除日记 | "确定删除这条日记吗？" | 否（软删除） |
| 删除儿童档案 | "确定删除 {name} 的所有数据吗？此操作不可恢复" | 是 |
| 清空已删除数据 | "确定永久删除所有已删除的数据吗？" | 是 |
| 恢复备份 | "恢复将替换当前所有数据，确定继续吗？" | 是 |
| 清除应用数据 | "确定清除所有数据吗？此操作不可恢复" | 是 |

### 4.2 确认对话框规范

```kotlin
@Composable
fun ConfirmDialog(
    title: String,
    message: String,
    confirmText: String = "确认",
    dismissText: String = "取消",
    isDestructive: Boolean = false,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
)
```

- 不可逆操作使用红色确认按钮
- 确认文案清晰说明后果
- 默认焦点在取消按钮上

---

## 5. 错误日志

### 5.1 日志记录

```kotlin
// 记录错误但不暴露用户数据
fun logError(tag: String, error: Throwable) {
    Log.e(tag, "Error: ${error.javaClass.simpleName}")
    // 不记录 error.message（可能含用户数据）
    // 不记录 stack trace 中的用户数据
}
```

### 5.2 错误统计

- 记录错误类型和频率
- 不记录用户数据
- 不上传到服务器
- 仅用于本地调试

---

## 6. 检查清单

每次错误处理相关代码时，检查：

- [ ] 错误是否转为 DomainException？
- [ ] 用户是否看到友好提示？
- [ ] 危险操作是否有确认？
- [ ] 不可逆操作是否有警告？
- [ ] 错误日志是否泄露用户数据？
- [ ] 错误恢复路径是否完整？

---

*本文件最后更新：Phase 0 — 项目初始化*