# CHANGELOG.md — Tuantuan Journal 变更日志

> **本文件记录 Tuantuan Journal 的所有版本变更。**
> **遵循 [Keep a Changelog](https://keepachangelog.com/) 格式。**

---

## [Unreleased]

## [0.1.1] - 2026-10-01

### Added — UI/UX Design Bible

- 创建 docs/design/DESIGN_BIBLE.md — 设计圣经总入口（Soft Emotional Minimalism）
- 创建 docs/design/DESIGN_TOKENS.md — 完整 Design Token 系统
- 创建 docs/design/COLOR_SYSTEM.md — 语义色系统与 5 主题色板
- 创建 docs/design/TYPOGRAPHY.md — 字体规范与排版规则
- 创建 docs/design/SPACING.md — 间距系统与布局原则
- 创建 docs/design/SHAPES.md — 圆角与形状系统
- 创建 docs/design/ICONOGRAPHY.md — 图标系统（Material Icons Outlined）
- 创建 docs/design/COMPONENTS.md — 组件库（Atoms/Molecules/Organisms/Screens）
- 创建 docs/design/NAVIGATION.md — 导航设计（底部导航 + 页面层级）
- 创建 docs/design/HOME_PAGE.md — 首页设计（团团主视觉）
- 创建 docs/design/TIMELINE_PAGE.md — 时间轴页面设计
- 创建 docs/design/CALENDAR_PAGE.md — 日历页面设计
- 创建 docs/design/GALLERY_PAGE.md — 照片墙设计（动态排版）
- 创建 docs/design/ENTRY_EDITOR.md — 记录编辑器设计
- 创建 docs/design/GROWTH_PAGE.md — 成长页面设计
- 创建 docs/design/MILESTONE_PAGE.md — 里程碑页面设计
- 创建 docs/design/SETTINGS_PAGE.md — 设置页面设计
- 创建 docs/design/MEDIA_UX.md — 媒体交互设计（照片/视频/音频）
- 创建 docs/design/GESTURES.md — 手势设计
- 创建 docs/design/MICRO_INTERACTIONS.md — 微交互系统
- 创建 docs/design/ANIMATION_BIBLE.md — 动画圣经
- 创建 docs/design/MOTION_TOKENS.md — 动效 Token
- 创建 docs/design/EMPTY_STATES.md — 空状态设计
- 创建 docs/design/ERROR_STATES.md — 错误状态设计
- 创建 docs/design/LOADING_STATES.md — 加载状态设计
- 创建 docs/design/DARK_MODE.md — 深色模式设计
- 创建 docs/design/THEMES.md — 主题系统（5 个主题）
- 创建 docs/design/RESPONSIVE.md — 响应式设计
- 创建 docs/design/ACCESSIBILITY.md — 无障碍设计
- 创建 docs/design/PERFORMANCE_UI.md — UI 性能规范
- 创建 docs/design/DESIGN_DECISIONS.md — 设计决策日志（10 个决策）
- 创建 docs/design/DESIGN_REVIEW_CHECKLIST.md — 设计审查清单

### Added — Phase 1: 核心功能

- 儿童档案管理 — 创建、编辑、删除儿童档案（含删除确认对话框）
- 日记创建 — 创建日记条目（文字+日期+心情+天气+地点+标签+照片）
- 日记列表 — 按日期显示日记列表（含空状态、搜索入口）
- 日记详情 — 查看日记完整内容（含照片展示、全屏查看、收藏）
- 日记编辑 — 编辑已有日记（含表单回填、标签更新、照片添加）
- 日记删除 — 软删除日记（含确认对话框）
- 照片添加 — Photo Picker 选择照片 + SaveMediaUseCase 保存 + MediaFileManager 文件管理
- 照片查看 — TtPhotoGallery 自适应排版 + TtPhotoViewer 全屏查看
- 标签系统 — 创建、管理标签 + 日记关联 FilterChip 选择
- 搜索功能 — 按关键词搜索日记（SearchScreen + SearchViewModel）
- 首页 — 儿童切换器 + 主照片区 + 今日卡片 + 最近回忆 + 成长概览占位 + 生日倒计时
- 5-Tab 底部导航 — Home/Record/Add/Growth/Profile + 子页面路由
- 共享状态组件 — TtEmptyState/TtErrorState(含UiError版)/TtLoadingState(三种模式)
- 确认对话框 — ConfirmDialog + DeleteConfirmDialog 预配置版
- 照片组件 — TtPhotoGallery(1/2/多张自适应)/TtPhotoViewer(全屏)/TtMediaAddBar/TtSelectedPhotosBar
- 首页组件 — TtTodayCard/TtDiaryCard/TtGrowthCard(Phase 1占位)/AgeDisplay/BirthdayCountdown
- 设计系统 Token — TuantuanSpacing/RoundedCorner/Typography/Elevation/Colors
- UiError 分层错误处理链 — Throwable → DomainException → UiError → resolveMessage()
- DomainException 扩展 — MediaCountExceeded 媒体数量限制异常
- 导航系统 — Navigation Compose 5-Tab 底部导航 + 子页面路由（ChildDetail/ChildEdit/ChildAdd/DiaryList/DiaryDetail/DiaryEdit/DiaryAdd/Search/TagManage）
- DI 模块 — DatabaseModule/RepositoryModule/UseCaseModule
- 媒体数据分离 — 数据库存索引，文件系统存内容
- 软删除机制 — isDeleted 标记，查询自动过滤

### Added — Phase 1: 单元测试

- Domain UseCase 单元测试 — 48 个测试全部通过（Child/Diary/Media/Tag 四个模块）
- DomainException 单元测试 — 9 个测试全部通过（消息格式+数据类等价性）
- Data Mapper 单元测试 — 23 个测试全部通过
  - ChildMapperTest — 5 个测试（toDomain/toEntity 字段映射 + nullable 处理 + 双向 round-trip）
  - DiaryEntryMapperTest — 7 个测试（toDomain 含/不含 mediaItems+tags + nullable + toEntity 排除关联数据 + round-trip + 关联数据丢失验证）
  - MediaItemMapperTest — 6 个测试（toDomain/toEntity 字段映射 + nullable + VIDEO duration + round-trip）
  - TagMapperTest — 5 个测试（toDomain/toEntity 字段映射 + nullable + round-trip）

### Changed — Phase 1: 架构改进

- DomainException 消息改为英文技术描述（用户消息由 UiError/stringResource 提供）
- SaveMediaUseCase 媒体数量验证改用 MediaCountExceeded 异常（替代 ValidationError + 硬编码中文）
- MediaFileManager 验证错误消息改为英文
- strings.xml 扩充至 200+ 字符串资源（UI 层全部 stringResource 化）
- SaveMediaUseCaseTest 改用 fake MediaFileService 实现（解决 Mockito + Kotlin 非空 Uri 参数的 null stub 问题）

## [0.1.0] - 2026-09-27

### Added — Phase 0: 项目基础设施

- 建立项目目录结构
- 创建 AGENTS.md — 最高优先级 Agent 规则
- 创建 PRODUCT_SPEC.md — 产品规格说明
- 创建 DESIGN_SYSTEM.md — 设计系统（色彩、间距、字体、组件）
- 创建 ANIMATION_GUIDELINES.md — 动画规范
- 创建 ARCHITECTURE.md — 技术架构（Clean Architecture）
- 创建 DATA_MODEL.md — 数据模型（10 个实体）
- 创建 MEDIA_STORAGE.md — 媒体存储规范
- 创建 BACKUP_SPEC.md — 备份规范（.tuanbackup 格式）
- 创建 SECURITY.md — 安全规范
- 创建 PERFORMANCE.md — 性能规范
- 创建 TESTING.md — 测试规范
- 创建 PERMISSIONS.md — 权限规范（零权限策略）
- 创建 ERROR_HANDLING.md — 错误处理规范
- 创建 ACCESSIBILITY.md — 无障碍规范
- 创建 ROADMAP.md — 开发路线图（6 个阶段）
- 创建 DEVELOPMENT_WORKFLOW.md — 开发流程
- 创建 DECISION_LOG.md — 决策日志（10 个决策）
- 创建 CHANGELOG.md — 变更日志
- 创建 README.md — 项目说明
- 安装 Android 开发技能集（30 个技能文件）

---

[Unreleased]: https://github.com/chch622/Tuantuan_Journal/compare/v0.1.1...HEAD
[0.1.1]: https://github.com/chch622/Tuantuan_Journal/compare/v0.1.0...v0.1.1
[0.1.0]: https://github.com/chch622/Tuantuan_Journal/releases/tag/v0.1.0

*本文件遵循 [Keep a Changelog](https://keepachangelog.com/) 格式*
*本文件遵循 [语义化版本](https://semver.org/) 规范*