# COMPONENTS.md — Tuantuan Journal 组件库

> **本文件定义 Tuantuan Journal 的完整组件体系。**
> **以后 Agent 开发新页面，必须优先搜索已有 Component。**
> **如果不存在，扩展 Component Library，不能直接临时创建几十个局部组件。**

---

## 1. 组件分层

```
Atoms（原子）
↓
Molecules（分子）
↓
Organisms（有机体）
↓
Screens（页面）
```

---

## 2. Atoms — 原子组件

### 2.1 基础原子

| 组件 | 命名 | 圆角 | 说明 |
|------|------|------|------|
| 按钮 | `TtButton` | Medium | 品牌色填充，Primary 操作 |
| 描边按钮 | `TtOutlinedButton` | Medium | 品牌色边框，Secondary 操作 |
| 文字按钮 | `TtTextButton` | Medium | 无边框，Tertiary 操作 |
| 图标按钮 | `TtIconButton` | Full | 圆形，48dp 触控 |
| 文字 | `TtText` | — | 使用 Typography Token |
| 图标 | `TtIcon` | — | 使用 IconSize Token |
| 分割线 | `TtDivider` | — | OutlineVariant 色 |

### 2.2 输入原子

| 组件 | 命名 | 圆角 | 说明 |
|------|------|------|------|
| 输入框 | `TtTextField` | Medium | 带标签和提示 |
| 搜索框 | `TtSearchField` | Medium/Full | 搜索专用 |
| 开关 | `TtSwitch` | Full | 设置项 |

### 2.3 指示原子

| 组件 | 命名 | 说明 |
|------|------|------|
| 进度条 | `TtProgressBar` | 线性进度 |
| 加载圈 | `TtLoadingIndicator` | 圆形加载 |
| 徽章 | `TtBadge` | 小圆点/数字 |

---

## 3. Molecules — 分子组件

### 3.1 信息分子

| 组件 | 命名 | 组成 | 说明 |
|------|------|------|------|
| 日期标签 | `TtDateLabel` | Icon + Text | 日期显示，如"9月27日 星期日" |
| 年龄显示 | `TtAgeDisplay` | Text(数字) + Text(单位) | 数字视觉重量高于说明文字 |
| 心情指示 | `TtMoodIndicator` | Icon + Text | 心情表情 |
| 标签 | `TtTagChip` | Icon? + Text | 可点击筛选 |
| 媒体类型标签 | `TtMediaChip` | Icon + Text | 照片/视频/音频类型标识 |

### 3.2 操作分子

| 组件 | 命名 | 组成 | 说明 |
|------|------|------|------|
| 操作行 | `TtActionRow` | Icon + Text + Chevron | 设置项、菜单项 |
| 筛选条 | `TtFilterBar` | TtTagChip[] | 多选筛选 |
| 媒体添加栏 | `TtMediaAddBar` | TtIconButton[] | 照片/视频/语音快捷添加 |

### 3.3 数据分子

| 组件 | 命名 | 组成 | 说明 |
|------|------|------|------|
| 成长数值 | `TtGrowthValue` | Text(数值) + Text(单位) + Text(日期) | 身高/体重等数据展示 |
| 倒计时 | `TtCountdown` | Text(数字) + Text(说明) | 生日倒计时等 |
| 统计摘要 | `TtStatSummary` | Text(数字) + Text(说明) | "今天已经留下 X 个瞬间" |

---

## 4. Organisms — 有机体组件

### 4.1 卡片有机体

| 组件 | 命名 | 组成 | 说明 |
|------|------|------|------|
| 日记卡片 | `TtDiaryCard` | Image? + Title + Date + Summary + Tags | 时间轴/首页日记展示 |
| 媒体卡片 | `TtMediaCard` | Image/Video + Duration/Count | 照片/视频展示 |
| 成长卡片 | `TtGrowthCard` | TtGrowthValue + TtDateLabel | 成长数据展示 |
| 里程碑卡片 | `TtMilestoneCard` | Icon + Title + Date + Description | 里程碑展示 |
| 今日卡片 | `TtTodayCard` | TtStatSummary / TtText + TtButton | 首页今日状态 |

### 4.2 列表有机体

| 组件 | 命名 | 组成 | 说明 |
|------|------|------|------|
| 时间轴列表 | `TtTimelineList` | TtDateLabel + TtDiaryCard[] | 按日期聚合的时间轴 |
| 照片网格 | `TtPhotoGrid` | TtMediaCard[] | 大图+小图组合排版 |
| 成长列表 | `TtGrowthList` | TtGrowthCard[] | 成长数据列表 |

### 4.3 容器有机体

| 组件 | 命名 | 组成 | 说明 |
|------|------|------|------|
| 空状态 | `TtEmptyState` | TtIcon + TtText + TtButton? | 各页面空状态 |
| 错误状态 | `TtErrorState` | TtIcon + TtText + TtButton | 各页面错误状态 |
| 加载状态 | `TtLoadingState` | TtLoadingIndicator / Skeleton | 各页面加载状态 |

---

## 5. Screens — 页面

| 页面 | 命名 | 说明 |
|------|------|------|
| 首页 | `HomeScreen` | 详见 [HOME_PAGE.md](HOME_PAGE.md) |
| 时间轴 | `TimelineScreen` | 详见 [TIMELINE_PAGE.md](TIMELINE_PAGE.md) |
| 日历 | `CalendarScreen` | 详见 [CALENDAR_PAGE.md](CALENDAR_PAGE.md) |
| 照片墙 | `GalleryScreen` | 详见 [GALLERY_PAGE.md](GALLERY_PAGE.md) |
| 记录编辑 | `EntryEditorScreen` | 详见 [ENTRY_EDITOR.md](ENTRY_EDITOR.md) |
| 成长 | `GrowthScreen` | 详见 [GROWTH_PAGE.md](GROWTH_PAGE.md) |
| 里程碑 | `MilestoneScreen` | 详见 [MILESTONE_PAGE.md](MILESTONE_PAGE.md) |
| 设置 | `SettingsScreen` | 详见 [SETTINGS_PAGE.md](SETTINGS_PAGE.md) |

---

## 6. 组件命名规范

统一使用 `Tt` 前缀（Tuantuan 缩写）：

```kotlin
TtButton
TtCard
TtMediaCard
TtTimelineCard
TtGrowthCard
TtEmptyState
TtDateLabel
TtAgeDisplay
TtGrowthValue
TtTagChip
TtMediaChip
TtMoodIndicator
TtTodayCard
TtDiaryCard
TtMilestoneCard
TtPhotoGrid
TtTimelineList
TtGrowthList
TtFilterBar
TtMediaAddBar
TtActionRow
TtCountdown
TtStatSummary
TtBadge
TtProgressBar
TtLoadingIndicator
TtSearchField
TtTextField
TtSwitch
TtDivider
TtText
TtIcon
TtIconButton
TtOutlinedButton
TtTextButton
```

如果项目已有更合适命名规则，优先遵循现有架构。

---

## 7. 组件设计规则

- 所有组件必须从设计系统定义
- 禁止在页面中临时创建未定义的组件
- 新组件必须先在此注册，说明用途和规格
- 组件内部间距使用 spacing token
- 组件圆角使用 rounded corner token
- 组件颜色使用 semantic color token
- 组件必须支持深色模式
- 组件必须提供 contentDescription

---

## 8. 组件扩展流程

1. 确认现有组件无法满足需求
2. 在本文件中定义新组件（名称、组成、圆角、说明）
3. 在 DESIGN_DECISIONS.md 中记录新增原因
4. 实现组件代码
5. 在页面中使用

---

*本文件版本：v1.0 | 最后更新：2026-09-27*