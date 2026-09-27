# MEDIA_STORAGE.md — Tuantuan Journal 媒体存储规范

> **本文件定义 Tuantuan Journal 媒体文件的存储规则。**
> **核心原则：数据库存索引，文件系统存内容，两者严格分离。**

---

## 1. 核心原则

### 1.1 绝对禁止

- **禁止** 在 SQLite 中存储 BLOB（照片、视频、音频的二进制数据）
- **禁止** 将媒体文件存储在外部存储（Environment.getExternalStorageDirectory）
- **禁止** 使用 MediaStore API 保存应用数据
- **禁止** 媒体文件路径硬编码

### 1.2 必须遵守

- 媒体文件存储在应用私有目录 `filesDir`
- 数据库只存储文件路径和元数据
- 文件操作必须在 Repository 层
- 文件删除必须先软删除数据库记录，再异步清理文件

---

## 2. 目录结构

```
/data/data/com.tuantuan.journal/
├── files/
│   └── media/
│       ├── photos/               # 照片
│       │   ├── {entryId}/        # 按日记条目分组
│       │   │   ├── {uuid}.jpg
│       │   │   ├── {uuid}.png
│       │   │   └── ...
│       │   └── ...
│       ├── videos/               # 视频
│       │   ├── {entryId}/
│       │   │   ├── {uuid}.mp4
│       │   │   └── ...
│       │   └── ...
│       ├── audio/                # 音频
│       │   ├── {entryId}/
│       │   │   ├── {uuid}.m4a
│       │   │   └── ...
│       │   └── ...
│       └── thumbnails/           # 缩略图
│           ├── {uuid}_thumb.jpg  # 照片缩略图
│           └── {uuid}_thumb.jpg  # 视频缩略图
│
├── shared_prefs/                 # 系统管理
└── databases/                    # Room 数据库
```

---

## 3. 文件命名规则

### 3.1 媒体文件

```
{UUID}.{extension}
```

- 使用 UUID v4 避免冲突
- 扩展名保留原始格式（jpg, png, mp4, m4a, etc.）
- 禁止使用原始文件名（可能含特殊字符或冲突）

### 3.2 缩略图文件

```
{uuid}_thumb.jpg
```

- 统一转为 JPEG 格式
- 后缀固定 `_thumb`
- 缩略图尺寸见第 5 节

---

## 4. 文件操作流程

### 4.1 保存媒体文件

```
用户选择文件 (SAF/Picker)
    ↓
1. 验证文件类型和大小
    ↓
2. 生成 UUID 文件名
    ↓
3. 创建目标目录（如不存在）
    ↓
4. 复制文件到 app-private 目录
    ↓
5. 生成缩略图（照片/视频）
    ↓
6. 写入 MediaItemEntity 到数据库
    ↓
7. 返回 MediaItem Domain Model
```

### 4.2 读取媒体文件

```
UI 请求显示媒体
    ↓
1. 从数据库获取 MediaItem
    ↓
2. 检查 filePath 对应文件是否存在
    ↓
3a. 存在 → 加载文件（Coil/Glide/Media3）
3b. 不存在 → 返回 MediaNotFound 错误
```

### 4.3 删除媒体文件

```
用户请求删除
    ↓
1. 弹出确认对话框
    ↓
2. 设置 MediaItemEntity.isDeleted = true
    ↓
3. 数据库更新
    ↓
4. 异步调度文件清理任务
    ↓
5. 文件清理任务删除物理文件
```

### 4.4 替换媒体文件

```
用户替换照片/视频
    ↓
1. 保存新文件（流程同 4.1）
    ↓
2. 创建新 MediaItemEntity
    ↓
3. 软删除旧 MediaItemEntity
    ↓
4. 异步清理旧文件
```

---

## 5. 缩略图规范

### 5.1 照片缩略图

| 尺寸 | 用途 | 质量 |
|------|------|------|
| 200×200 | 列表/网格 | 70% |
| 400×400 | 详情预览 | 80% |

### 5.2 视频缩略图

| 尺寸 | 用途 | 来源 |
|------|------|------|
| 200×200 | 列表/网格 | 首帧 |
| 400×400 | 详情预览 | 首帧 |

### 5.3 音频缩略图

- 音频不生成缩略图
- 使用默认音频图标
- `thumbnailPath` 为 null

---

## 6. 文件大小限制

| 类型 | 单文件上限 | 单条日记上限 |
|------|-----------|------------|
| 照片 | 20 MB | 10 张 |
| 视频 | 500 MB | 3 个 |
| 音频 | 100 MB | 5 个 |

### 6.1 超限处理

- 选择文件时检查大小
- 超限弹出提示，不允许继续
- 不自动压缩（保持原始质量）
- 可在设置中调整限制

---

## 7. 存储空间管理

### 7.1 空间监控

```kotlin
data class StorageInfo(
    val totalSpace: Long,        // 总空间
    val usedSpace: Long,         // 已用空间
    val mediaSpace: Long,        // 媒体占用
    val databaseSpace: Long,     // 数据库占用
    val thumbnailSpace: Long,    // 缩略图占用
    val availableSpace: Long     // 可用空间
)
```

### 7.2 清理策略

1. **软删除文件清理** — 已软删除超过 30 天的媒体文件
2. **缩略图重建** — 缩略图丢失时自动重建
3. **孤立文件清理** — 数据库无记录的文件
4. **空间不足警告** — 可用空间 < 500MB 时提醒

### 7.3 清理时机

- 应用启动时检查（异步）
- 设置页面手动触发
- 存储空间不足时自动触发
- 不在用户操作过程中清理（避免卡顿）

---

## 8. 文件安全

### 8.1 私有目录

- 所有文件存储在 `filesDir`（应用私有）
- 卸载应用时系统自动删除
- 其他应用无法直接访问
- 不需要 READ/WRITE_EXTERNAL_STORAGE 权限

### 8.2 文件验证

- 保存前验证 MIME 类型
- 保存前验证文件完整性（读取成功）
- 定期校验 filePath 与实际文件一致性
- 发现不一致时标记为 MediaNotFound

---

## 9. 备份中的媒体处理

### 9.1 备份

- 备份包含所有媒体文件
- 按目录结构打包
- 详见 [BACKUP_SPEC.md](BACKUP_SPEC.md)

### 9.2 恢复

- 恢复时先恢复数据库记录
- 再恢复媒体文件到对应目录
- 恢复后校验文件完整性
- 缺失文件标记为 MediaNotFound

---

## 10. MediaFileManager 接口

```kotlin
interface MediaFileManager {
    suspend fun saveMedia(sourceUri: Uri, entryId: String, mediaType: MediaType): Result<MediaItem>
    suspend fun deleteMedia(mediaItemId: String): Result<Unit>
    suspend fun getMediaFile(mediaItemId: String): Result<File?>
    suspend fun generateThumbnail(file: File, mediaType: MediaType): Result<File?>
    suspend fun getStorageInfo(): StorageInfo
    suspend fun cleanupDeletedFiles(olderThanDays: Int = 30): Result<Int>
    suspend fun validateIntegrity(): Result<List<String>>
    suspend fun cleanupOrphanedFiles(): Result<Int>
}
```

---

## 11. 检查清单

每次涉及媒体操作时，检查：

- [ ] 是否在 Repository 层操作文件？
- [ ] 是否使用 UUID 文件名？
- [ ] 是否存储在 app-private 目录？
- [ ] 是否生成了缩略图？
- [ ] 删除是否使用软删除？
- [ ] 是否验证了文件类型和大小？
- [ ] 文件操作是否在 IO 线程？
- [ ] 是否处理了文件不存在的情况？

---

*本文件最后更新：Phase 0 — 项目初始化*