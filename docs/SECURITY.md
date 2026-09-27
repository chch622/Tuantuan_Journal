# SECURITY.md — Tuantuan Journal 安全规范

> **本文件定义 Tuantuan Journal 的安全规则。**
> **儿童数据安全是最高优先级，任何实现不得违反本规范。**

---

## 1. 核心原则

### 1.1 绝对禁止

- **禁止** 网络传输用户数据（无 INTERNET 权限）
- **禁止** 将数据存储在外部存储（其他应用可读）
- **禁止** 在日志中输出用户数据
- **禁止** 使用第三方分析 SDK（Firebase、Umeng 等）
- **禁止** 使用第三方崩溃收集（Sentry、Bugly 等）
- **禁止** 明文存储密码或密钥
- **禁止** 在备份中包含设备标识符

### 1.2 必须遵守

- 所有数据存储在应用私有目录
- 敏感操作需要用户确认
- 文件访问使用 SAF 或 Content URI
- 数据库使用 Room 加密（未来增强）

---

## 2. 权限最小化

### 2.1 声明的权限

| 权限 | 用途 | 必需 |
|------|------|------|
| 无 | 本应用不需要任何权限 | - |

### 2.2 禁止声明的权限

- `INTERNET` — 离线应用不需要
- `READ_EXTERNAL_STORAGE` — 使用 SAF 代替
- `WRITE_EXTERNAL_STORAGE` — 使用 SAF 代替
- `READ_PHONE_STATE` — 不需要设备信息
- `ACCESS_FINE_LOCATION` — 位置由用户手动输入
- `CAMERA` — 使用系统相机 Intent
- `RECORD_AUDIO` — 使用系统录音 Intent
- `READ_CONTACTS` — 不需要联系人
- `READ_CALENDAR` — 不需要日历

### 2.3 运行时权限

- 本应用不请求运行时权限
- 相机/录音通过系统 Intent 间接使用
- 文件选择通过 SAF 间接访问

---

## 3. 数据存储安全

### 3.1 数据库安全

- Room 数据库存储在应用私有目录
- 系统沙箱保护，其他应用无法访问
- Root 设备上可能被访问（无法避免）
- 未来增强：使用 SQLCipher 加密

### 3.2 文件安全

- 媒体文件存储在 `filesDir`（应用私有）
- 缩略图存储在应用私有目录
- 不写入外部存储
- 不写入公共目录

### 3.3 偏好安全

- DataStore 存储在应用私有目录
- 不存储密码或密钥
- 不存储用户隐私数据
- 只存储应用设置和偏好

---

## 4. 日志安全

### 4.1 日志规则

```kotlin
// 禁止
Log.d("Tuantuan", "User data: $child")        // 禁止输出用户数据
Log.d("Tuantuan", "Entry content: $content")  // 禁止输出日记内容
Log.d("Tuantuan", "Photo path: $path")        // 禁止输出文件路径

// 允许
Log.d("Tuantuan", "Entry saved successfully")  // 允许操作结果
Log.d("Tuantuan", "Database query completed")  // 允许技术信息
Log.e("Tuantuan", "Database error: ${e.code}") // 允许错误代码
```

### 4.2 日志级别

| 级别 | 使用场景 | 允许内容 |
|------|---------|---------|
| ERROR | 异常和错误 | 错误类型、错误代码 |
| WARN | 警告 | 警告类型、技术信息 |
| INFO | 重要操作 | 操作类型、结果状态 |
| DEBUG | 调试信息 | 仅技术信息，无用户数据 |

### 4.3 Release 构建

- 禁用 DEBUG 和 VERBOSE 日志
- 只保留 ERROR 和 WARN
- 使用 ProGuard/R8 移除日志调用

---

## 5. 备份安全

### 5.1 备份导出

- 用户通过 SAF 主动选择存储位置
- 不自动上传到任何服务
- 备份文件包含用户所有数据
- 用户负责保管备份文件

### 5.2 备份内容

- 备份包含完整数据库和媒体文件
- 不包含设备标识符
- 不包含应用签名信息
- 未来增强：可选密码加密

### 5.3 Android 备份

- `android:allowBackup="false"` — 禁止 ADB 备份
- `android:fullBackupContent="false"` — 禁止 Auto Backup
- 防止通过 ADB 提取数据

---

## 6. 依赖安全

### 6.1 依赖审查

- 只使用知名开源库
- 定期检查依赖漏洞
- 禁止包含广告 SDK
- 禁止包含追踪 SDK

### 6.2 允许的依赖类别

| 类别 | 库 | 原因 |
|------|---|------|
| UI | Jetpack Compose, Material 3 | Google 官方 |
| 数据库 | Room | Google 官方 |
| DI | Hilt | Google 官方 |
| 图片 | Coil | 开源，无追踪 |
| 媒体 | Media3 | Google 官方 |
| 导航 | Navigation Compose | Google 官方 |
| 序列化 | Kotlin Serialization | JetBrains 官方 |

### 6.3 禁止的依赖

- 任何需要 INTERNET 权限的库
- 任何包含广告的库
- 任何包含追踪的库
- 任何收集用户数据的库

---

## 7. 安全检查清单

每次代码变更时，检查：

- [ ] 是否新增了网络相关代码？
- [ ] 是否新增了权限声明？
- [ ] 日志是否泄露用户数据？
- [ ] 文件是否存储在私有目录？
- [ ] 敏感操作是否需要确认？
- [ ] 依赖是否安全可信？
- [ ] 备份功能是否安全？
- [ ] 是否引入了追踪 SDK？

---

*本文件最后更新：Phase 0 — 项目初始化*