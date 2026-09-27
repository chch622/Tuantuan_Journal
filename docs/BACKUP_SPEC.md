# BACKUP_SPEC.md — Tuantuan Journal 备份规范

> **本文件定义 Tuantuan Journal 的备份格式和流程。**
> **备份是用户数据安全的最后防线，必须严谨可靠。**

---

## 1. 备份格式

### 1.1 文件格式

- **扩展名：** `.tuanbackup`
- **格式：** ZIP 归档
- **编码：** UTF-8

### 1.2 文件结构

```
{filename}.tuanbackup
├── manifest.json           # 备份清单（必须第一个文件）
├── database/
│   └── tuantuan.db         # Room 数据库文件
├── preferences/
│   └── preferences.json    # DataStore 偏好导出
├── media/
│   ├── photos/
│   │   └── {entryId}/
│   │       └── {uuid}.jpg
│   ├── videos/
│   │   └── {entryId}/
│   │       └── {uuid}.mp4
│   ├── audio/
│   │   └── {entryId}/
│   │       └── {uuid}.m4a
│   └── thumbnails/
│       └── {uuid}_thumb.jpg
└── checksums.sha256        # 文件校验和
```

---

## 2. Manifest 规范

### 2.1 manifest.json 结构

```json
{
  "version": 1,
  "format": "tuanbackup",
  "createdAt": "2026-09-27T10:00:00Z",
  "appVersion": "1.0.0",
  "device": {
    "manufacturer": "Samsung",
    "model": "Galaxy S24",
    "androidVersion": 34
  },
  "content": {
    "database": true,
    "preferences": true,
    "media": {
      "photos": 150,
      "videos": 10,
      "audio": 5,
      "totalSizeBytes": 524288000
    }
  },
  "children": [
    {
      "id": "uuid-1",
      "name": "团团",
      "birthDate": "2023-06-15"
    }
  ],
  "stats": {
    "totalEntries": 200,
    "totalGrowthRecords": 50,
    "totalMilestones": 30,
    "totalTags": 20
  },
  "checksum": "sha256:abc123..."
}
```

### 2.2 字段说明

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| version | Int | 是 | 备份格式版本，当前为 1 |
| format | String | 是 | 固定值 "tuanbackup" |
| createdAt | String | 是 | ISO 8601 时间戳 |
| appVersion | String | 是 | 应用版本号 |
| device | Object | 否 | 设备信息 |
| content | Object | 是 | 备份内容摘要 |
| children | Array | 是 | 儿童档案摘要 |
| stats | Object | 是 | 数据统计 |
| checksum | String | 是 | manifest 自身校验和 |

---

## 3. 备份流程

### 3.1 创建备份

```
用户触发备份
    ↓
1. 检查存储空间（备份大小估算）
    ↓
2. 弹出确认对话框（显示备份大小和位置）
    ↓
3. 用户确认
    ↓
4. 创建临时目录
    ↓
5. 导出 Room 数据库（checkpoint + copy）
    ↓
6. 导出 DataStore 偏好为 JSON
    ↓
7. 复制媒体文件（排除已软删除）
    ↓
8. 生成 manifest.json
    ↓
9. 生成 checksums.sha256
    ↓
10. 打包为 ZIP
    ↓
11. 写入用户指定位置（SAF）
    ↓
12. 清理临时目录
    ↓
13. 显示备份成功通知
```

### 3.2 备份选项

| 选项 | 说明 | 默认 |
|------|------|------|
| 包含媒体 | 是否包含照片/视频/音频 | 是 |
| 包含已软删除 | 是否包含已软删除数据 | 否 |
| 压缩级别 | ZIP 压缩级别 (0-9) | 6 |
| 加密 | 是否加密备份 | 否（未来） |

### 3.3 备份进度

```kotlin
sealed class BackupProgress {
    data class Calculating(val message: String) : BackupProgress()
    data class ExportingDatabase(val progress: Float) : BackupProgress()
    data class ExportingPreferences(val progress: Float) : BackupProgress()
    data class CopyingMedia(val current: Int, val total: Int, val file: String) : BackupProgress()
    data class Packaging(val progress: Float) : BackupProgress()
    data class Writing(val bytesWritten: Long, val totalBytes: Long) : BackupProgress()
    data class Completed(val filePath: String, val sizeBytes: Long) : BackupProgress()
    data class Failed(val error: BackupError) : BackupProgress()
}
```

---

## 4. 恢复流程

### 4.1 恢复前验证

```
用户选择 .tuanbackup 文件
    ↓
1. 验证 ZIP 格式
    ↓
2. 读取 manifest.json
    ↓
3. 验证 manifest 格式和版本
    ↓
4. 校验 manifest checksum
    ↓
5. 验证 checksums.sha256
    ↓
6. 检查磁盘空间
    ↓
7. 显示恢复摘要（数据量、儿童信息）
    ↓
8. 弹出确认对话框（⚠️ 警告：将替换现有数据）
    ↓
9. 用户确认
    ↓
10. 执行恢复
```

### 4.2 恢复执行

```
用户确认恢复
    ↓
1. 备份当前数据（安全回退）
    ↓
2. 清空当前数据库
    ↓
3. 恢复数据库文件
    ↓
4. 恢复偏好设置
    ↓
5. 恢复媒体文件
    ↓
6. 校验恢复完整性
    ↓
7. 清理安全回退备份
    ↓
8. 显示恢复结果
```

### 4.3 恢复冲突处理

| 场景 | 处理方式 |
|------|---------|
| 备份版本高于当前应用 | 拒绝恢复，提示升级应用 |
| 备份版本低于当前应用 | 尝试迁移，失败则拒绝 |
| 磁盘空间不足 | 拒绝恢复，提示空间不足 |
| 媒体文件损坏 | 跳过并记录，继续恢复其他 |
| manifest 损坏 | 拒绝恢复 |

---

## 5. 校验和规范

### 5.1 checksums.sha256 格式

```
sha256:abc123def456...  database/tuantuan.db
sha256:789abc123def...  preferences/preferences.json
sha256:123456789abc...  media/photos/{entryId}/{uuid}.jpg
...
```

### 5.2 校验规则

- 每个文件一行
- 格式：`sha256:{hash}  {relative_path}`
- 两个空格分隔
- 相对路径使用 `/` 分隔
- manifest.json 自身包含 checksum 字段

### 5.3 校验时机

- 备份完成后立即校验
- 恢复前校验所有文件
- 恢复后校验关键文件

---

## 6. 安全考虑

### 6.1 当前阶段

- 备份不加密（Phase 0-1）
- 依赖 Android 沙箱安全
- 用户通过 SAF 选择存储位置

### 6.2 未来增强

- Phase 2+: 可选密码加密
- 使用 AES-256-GCM 加密
- 密码不存储，用户自行保管
- 加密后 manifest 仍可读（不加密）

---

## 7. 自动备份

### 7.1 当前阶段

- 不实现自动备份（Phase 0-1）
- 用户手动触发

### 7.2 未来增强

- Phase 2+: 提醒用户定期备份
- 可设置备份提醒频率（每周/每月）
- 不自动执行备份（需要用户确认）

---

## 8. 错误处理

### 8.1 BackupError 定义

```kotlin
sealed class BackupError {
    data class StorageFull(val required: Long, val available: Long) : BackupError()
    data class MediaFileNotFound(val path: String) : BackupError()
    data class DatabaseExportFailed(val reason: String) : BackupError()
    data class PackagingFailed(val reason: String) : BackupError()
    data class WriteFailed(val reason: String) : BackupError()
    data class RestoreVersionMismatch(val backupVersion: Int, val appVersion: Int) : BackupError()
    data class ManifestCorrupted(val reason: String) : BackupError()
    data class ChecksumMismatch(val file: String) : BackupError()
    data class RestoreFailed(val reason: String) : BackupError()
}
```

### 8.2 错误恢复

- 备份失败：清理临时文件，显示错误原因
- 恢复失败：尝试回退到安全备份
- 部分恢复：记录失败项，允许用户查看

---

## 9. 检查清单

每次涉及备份功能时，检查：

- [ ] manifest.json 是否完整正确？
- [ ] 校验和是否正确生成？
- [ ] 恢复前是否验证了备份完整性？
- [ ] 恢复前是否创建了安全回退？
- [ ] 是否处理了磁盘空间不足？
- [ ] 是否处理了版本不匹配？
- [ ] 媒体文件是否正确包含/排除？
- [ ] 已软删除数据是否按选项处理？
- [ ] 临时文件是否正确清理？
- [ ] 进度是否正确反馈给用户？

---

*本文件最后更新：Phase 0 — 项目初始化*