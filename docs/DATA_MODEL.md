# DATA_MODEL.md — Tuantuan Journal 数据模型

> **本文件定义 Tuantuan Journal 所有数据实体和关系。**
> **所有数据库操作必须基于本模型，禁止擅自新增或修改实体。**

---

## 1. 核心原则

### 1.1 媒体数据分离

- **数据库只存索引** — 媒体文件路径、元数据、关联关系
- **文件系统存内容** — 照片、视频、音频的实际二进制数据
- **详见** [MEDIA_STORAGE.md](MEDIA_STORAGE.md)

### 1.2 软删除

- 所有实体包含 `isDeleted` 字段
- 删除操作设置 `isDeleted = true`，不物理删除
- 查询默认过滤 `isDeleted = false`
- 定期清理任务负责物理删除已软删除数据

### 1.3 时间字段

- `createdAt` — 记录创建时间（系统自动）
- `updatedAt` — 记录更新时间（系统自动）
- `eventDateTime` — 事件发生时间（用户指定，允许补记）

---

## 2. 实体定义

### 2.1 Child（儿童档案）

```kotlin
@Entity(tableName = "children")
data class ChildEntity(
    @PrimaryKey val id: String,           // UUID
    val name: String,                      // 姓名
    val nickname: String,                  // 昵称
    val birthDate: LocalDate,              // 出生日期
    val gender: Gender?,                   // 性别（可选）
    val avatarPath: String?,               // 头像路径（文件系统）
    val birthWeight: Double?,              // 出生体重(kg)
    val birthHeight: Double?,              // 出生身高(cm)
    val bloodType: String?,                // 血型
    val birthPlace: String?,               // 出生地
    val notes: String?,                    // 备注
    val sortOrder: Int,                    // 排序
    val isDeleted: Boolean = false,
    val createdAt: Instant,
    val updatedAt: Instant
)

enum class Gender { MALE, FEMALE, OTHER }
```

### 2.2 DiaryEntry（日记条目）

```kotlin
@Entity(tableName = "diary_entries")
data class DiaryEntryEntity(
    @PrimaryKey val id: String,           // UUID
    val childId: String,                   // 关联儿童
    val title: String?,                    // 标题（可选）
    val content: String,                   // 正文内容
    val eventDateTime: Instant,            // 事件发生时间
    val mood: Mood?,                       // 心情
    val weather: Weather?,                 // 天气
    val location: String?,                 // 地点
    val isFavorite: Boolean = false,       // 收藏
    val isDeleted: Boolean = false,
    val createdAt: Instant,
    val updatedAt: Instant
)

enum class Mood { HAPPY, CALM, EXCITED, SAD, ANGRY, SICK, TIRED }
enum class Weather { SUNNY, CLOUDY, RAINY, SNOWY, WINDY, FOGGY }
```

### 2.3 MediaItem（媒体条目）

```kotlin
@Entity(tableName = "media_items")
data class MediaItemEntity(
    @PrimaryKey val id: String,           // UUID
    val entryId: String,                   // 关联日记条目
    val mediaType: MediaType,              // 媒体类型
    val filePath: String,                  // 文件路径（app-private 目录）
    val fileName: String,                  // 原始文件名
    val fileSize: Long,                    // 文件大小(bytes)
    val mimeType: String,                  // MIME 类型
    val width: Int?,                       // 图片/视频宽度
    val height: Int?,                      // 图片/视频高度
    val duration: Long?,                   // 视频/音频时长(ms)
    val thumbnailPath: String?,            // 缩略图路径
    val sortOrder: Int,                    // 排序
    val isDeleted: Boolean = false,
    val createdAt: Instant,
    val updatedAt: Instant
)

enum class MediaType { PHOTO, VIDEO, AUDIO }
```

### 2.4 Tag（标签）

```kotlin
@Entity(tableName = "tags")
data class TagEntity(
    @PrimaryKey val id: String,           // UUID
    val name: String,                      // 标签名（唯一）
    val color: String?,                    // 颜色（HEX）
    val category: String?,                 // 分类
    val usageCount: Int = 0,               // 使用次数
    val isDeleted: Boolean = false,
    val createdAt: Instant,
    val updatedAt: Instant
)
```

### 2.5 EntryTag（日记-标签关联）

```kotlin
@Entity(
    tableName = "entry_tags",
    primaryKeys = ["entryId", "tagId"]
)
data class EntryTagEntity(
    val entryId: String,                   // 关联日记
    val tagId: String,                     // 关联标签
    val createdAt: Instant
)
```

### 2.6 GrowthRecord（成长记录）

```kotlin
@Entity(tableName = "growth_records")
data class GrowthRecordEntity(
    @PrimaryKey val id: String,           // UUID
    val childId: String,                   // 关联儿童
    val recordType: GrowthType,            // 记录类型
    val value: Double,                     // 数值
    val unit: String,                      // 单位
    val measureDate: LocalDate,            // 测量日期
    val notes: String?,                    // 备注
    val isDeleted: Boolean = false,
    val createdAt: Instant,
    val updatedAt: Instant
)

enum class GrowthType { HEIGHT, WEIGHT, HEAD_CIRCUMFERENCE, SHOE_SIZE }
```

### 2.7 Milestone（里程碑）

```kotlin
@Entity(tableName = "milestones")
data class MilestoneEntity(
    @PrimaryKey val id: String,           // UUID
    val childId: String,                   // 关联儿童
    val category: MilestoneCategory,       // 里程碑类别
    val title: String,                     // 标题
    val description: String?,              // 描述
    val achievedDate: LocalDate?,          // 达成日期（null=未达成）
    val isExpected: Boolean = false,       // 是否预期里程碑
    val expectedAgeMonths: Int?,           // 预期月龄
    val notes: String?,                    // 备注
    val isDeleted: Boolean = false,
    val createdAt: Instant,
    val updatedAt: Instant
)

enum class MilestoneCategory {
    MOTOR, LANGUAGE, COGNITIVE, SOCIAL, SELF_CARE
}
```

### 2.8 Reminder（提醒）

```kotlin
@Entity(tableName = "reminders")
data class ReminderEntity(
    @PrimaryKey val id: String,           // UUID
    val childId: String?,                  // 关联儿童（可选）
    val title: String,                     // 提醒标题
    val description: String?,              // 描述
    val reminderDateTime: Instant,         // 提醒时间
    val recurrence: Recurrence?,           // 重复规则
    val isCompleted: Boolean = false,      // 已完成
    val isDeleted: Boolean = false,
    val createdAt: Instant,
    val updatedAt: Instant
)

enum class Recurrence { DAILY, WEEKLY, MONTHLY, YEARLY }
```

### 2.9 FirstEvent（第一次记录）

```kotlin
@Entity(tableName = "first_events")
data class FirstEventEntity(
    @PrimaryKey val id: String,           // UUID
    val childId: String,                   // 关联儿童
    val category: FirstEventCategory,      // 类别
    val title: String,                     // 标题
    val eventDate: LocalDate,              // 日期
    val description: String?,              // 描述
    val mediaItemId: String?,              // 关联媒体
    val isDeleted: Boolean = false,
    val createdAt: Instant,
    val updatedAt: Instant
)

enum class FirstEventCategory {
    FIRST_WORD, FIRST_STEP, FIRST_TOOTH, FIRST_SMILE,
    FIRST_SOLID_FOOD, FIRST_HAIRCUT, FIRST_DAY_SCHOOL, OTHER
}
```

### 2.10 Vaccination（疫苗记录）

```kotlin
@Entity(tableName = "vaccinations")
data class VaccinationEntity(
    @PrimaryKey val id: String,           // UUID
    val childId: String,                   // 关联儿童
    val vaccineName: String,               // 疫苗名称
    val doseNumber: Int?,                  // 第几针
    val administeredDate: LocalDate?,      // 接种日期（null=未接种）
    val scheduledDate: LocalDate?,         // 计划日期
    val location: String?,                 // 接种地点
    val batchNumber: String?,              // 批号
    val notes: String?,                    // 备注
    val isDeleted: Boolean = false,
    val createdAt: Instant,
    val updatedAt: Instant
)
```

---

## 3. 实体关系

```
Child 1──N DiaryEntry
Child 1──N GrowthRecord
Child 1──N Milestone
Child 1──N Reminder
Child 1──N FirstEvent
Child 1──N Vaccination

DiaryEntry 1──N MediaItem
DiaryEntry N──N Tag (via EntryTag)
```

### 3.1 外键约束

```kotlin
// DiaryEntry → Child
ForeignKey(entity = ChildEntity::class, parentColumns = ["id"], childColumns = ["childId"])

// MediaItem → DiaryEntry
ForeignKey(entity = DiaryEntryEntity::class, parentColumns = ["id"], childColumns = ["entryId"])

// EntryTag → DiaryEntry
ForeignKey(entity = DiaryEntryEntity::class, parentColumns = ["id"], childColumns = ["entryId"])

// EntryTag → Tag
ForeignKey(entity = TagEntity::class, parentColumns = ["id"], childColumns = ["tagId"])

// GrowthRecord → Child
ForeignKey(entity = ChildEntity::class, parentColumns = ["id"], childColumns = ["childId"])

// Milestone → Child
ForeignKey(entity = ChildEntity::class, parentColumns = ["id"], childColumns = ["childId"])
```

---

## 4. 索引策略

```kotlin
// 高频查询索引
indices = [
    Index(value = ["childId"]),                           // 按儿童查询
    Index(value = ["entryId"]),                           // 按条目查询
    Index(value = ["eventDateTime"]),                     // 按事件时间查询
    Index(value = ["isDeleted"]),                         // 软删除过滤
    Index(value = ["childId", "eventDateTime"]),          // 复合查询
    Index(value = ["childId", "isDeleted"]),              // 活跃数据查询
    Index(value = ["tagId"]),                             // 标签查询
    Index(value = ["measureDate"]),                       // 成长记录日期查询
    Index(value = ["achievedDate"]),                      // 里程碑日期查询
]
```

---

## 5. 数据验证规则

### 5.1 通用规则

- `id` 必须为有效 UUID
- `createdAt` ≤ `updatedAt`
- `isDeleted` 默认 false
- 所有字符串字段 trim 后不得为空字符串（允许 null）

### 5.2 Child 验证

- `name` 非空，1-50 字符
- `birthDate` 不得晚于今天
- `avatarPath` 若非 null，文件必须存在

### 5.3 DiaryEntry 验证

- `content` 非空，1-50000 字符
- `childId` 必须引用存在的 Child
- `eventDateTime` 不得晚于当前时间

### 5.4 MediaItem 验证

- `filePath` 非空，文件必须存在
- `fileSize` > 0
- `mimeType` 必须为 image/*、video/* 或 audio/*
- `mediaType` 与 `mimeType` 一致

### 5.5 GrowthRecord 验证

- `value` > 0
- `unit` 非空
- `measureDate` 不得晚于今天

---

## 6. 数据库版本管理

- 初始版本：version 1
- 使用 Room Migration 机制
- 禁止 `fallbackToDestructiveMigration`
- 每次 schema 变更必须提供 Migration
- 导出 schema JSON 用于测试

---

## 7. 查询规范

### 7.1 默认过滤

所有查询默认包含 `WHERE isDeleted = 0`，除非明确需要查询已删除数据。

### 7.2 排序规则

- 日记条目：按 `eventDateTime` 降序
- 成长记录：按 `measureDate` 降序
- 里程碑：按 `achievedDate` 降序
- 标签：按 `usageCount` 降序
- 媒体：按 `sortOrder` 升序

### 7.3 分页

- 使用 Room `PagingSource` 实现
- 默认每页 20 条
- 支持无限滚动加载

---

*本文件最后更新：Phase 0 — 项目初始化*