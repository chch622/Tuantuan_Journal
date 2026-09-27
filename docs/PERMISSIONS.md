# PERMISSIONS.md — Tuantuan Journal 权限规范

> **本文件定义 Tuantuan Journal 的权限策略。**
> **离线应用不需要网络权限，文件访问通过系统 Intent 间接实现。**

---

## 1. 权限声明

### 1.1 AndroidManifest.xml

```xml
<!-- Tuantuan Journal 不需要任何权限 -->
<!-- 相机、录音、文件选择均通过系统 Intent 实现 -->
```

**本应用声明零权限。**

---

## 2. 功能与权限映射

| 功能 | 实现方式 | 是否需要权限 |
|------|---------|------------|
| 拍照 | MediaStore.ACTION_IMAGE_CAPTURE Intent | 否 |
| 录音 | MediaStore.Audio.Media.RECORD_SOUND_ACTION Intent | 否 |
| 选择照片 | Photo Picker / SAF | 否 |
| 选择文件 | SAF (Storage Access Framework) | 否 |
| 保存备份 | SAF | 否 |
| 恢复备份 | SAF | 否 |
| 位置信息 | 用户手动输入 | 否 |
| 天气信息 | 用户手动选择 | 否 |
| 提醒通知 | AlarmManager + NotificationManager | 否* |

*注：通知渠道需要在 Android 8.0+ 注册，但不需要声明权限。POST_NOTIFICATIONS 在 Android 13+ 需要运行时请求，但这是可选功能，不影响核心使用。

---

## 3. 运行时权限策略

### 3.1 核心原则

- 核心功能不依赖任何运行时权限
- 提醒通知的 POST_NOTIFICATIONS 是唯一可选权限
- 用户拒绝权限不影响核心功能使用

### 3.2 通知权限（Android 13+）

```kotlin
// 可选：仅在用户需要提醒功能时请求
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
    // 检查通知权限
    if (ContextCompat.checkSelfPermission(context, POST_NOTIFICATIONS) 
        != PackageManager.PERMISSION_GRANTED) {
        // 请求权限，但用户拒绝也不影响其他功能
        requestPermissionLauncher.launch(POST_NOTIFICATIONS)
    }
}
```

### 3.3 权限拒绝处理

- 提醒功能：权限拒绝 → 提醒功能不可用，其他功能正常
- 不反复请求已拒绝的权限
- 不在启动时请求权限

---

## 4. 禁止使用的权限

| 权限 | 原因 |
|------|------|
| INTERNET | 离线应用，不需要网络 |
| READ_EXTERNAL_STORAGE | 使用 SAF 代替 |
| WRITE_EXTERNAL_STORAGE | 使用 SAF 代替 |
| READ_PHONE_STATE | 不需要设备信息 |
| ACCESS_FINE_LOCATION | 位置由用户输入 |
| ACCESS_COARSE_LOCATION | 位置由用户输入 |
| READ_CONTACTS | 不需要联系人 |
| WRITE_CONTACTS | 不需要联系人 |
| READ_CALENDAR | 不需要日历 |
| WRITE_CALENDAR | 不需要日历 |
| CAMERA | 使用系统 Intent |
| RECORD_AUDIO | 使用系统 Intent |
| BODY_SENSORS | 不需要传感器 |

---

## 5. 检查清单

每次添加新功能时，检查：

- [ ] 是否需要新权限？
- [ ] 能否通过系统 Intent 实现？
- [ ] 能否通过 SAF 实现？
- [ ] 核心功能是否依赖权限？
- [ ] 权限拒绝是否优雅降级？

---

*本文件最后更新：Phase 0 — 项目初始化*