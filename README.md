# 团团日记 / Tuantuan Journal

> 一个纯粹的离线儿童成长记录应用，为家庭保存最珍贵的记忆。

---

## 项目简介

团团日记是一款 Android 原生应用，专注于记录儿童成长中的每一个珍贵瞬间。完全离线运行，不依赖任何网络服务，确保儿童数据的绝对隐私和安全。

### 核心特性

- **完全离线** — 无网络权限，数据永远留在设备上
- **家庭友好** — 支持多个儿童档案，全家共同记录
- **多媒体日记** — 文字、照片、视频、音频，完整记录
- **成长追踪** — 身高、体重、里程碑，量化成长
- **安全备份** — 自定义备份格式，数据永不丢失
- **温暖设计** — Material 3 设计，温暖不幼稚

---

## 技术栈

| 类别 | 技术 |
|------|------|
| 语言 | Kotlin |
| UI | Jetpack Compose + Material 3 |
| 架构 | Clean Architecture |
| 数据库 | Room |
| 依赖注入 | Hilt |
| 图片加载 | Coil |
| 媒体播放 | Media3 |
| 导航 | Navigation Compose |
| 异步 | Coroutines + Flow |
| 偏好 | DataStore |
| 序列化 | Kotlin Serialization |

---

## 项目结构

```
├── AGENTS.md              # Agent 规则（最高优先级）
├── README.md              # 项目说明
├── docs/                  # 规范文档
│   ├── PRODUCT_SPEC.md    # 产品规格
│   ├── ARCHITECTURE.md    # 技术架构
│   ├── DATA_MODEL.md      # 数据模型
│   ├── DESIGN_SYSTEM.md   # 设计系统
│   ├── ROADMAP.md         # 开发路线图
│   └── ...                # 其他规范
├── skills/                # AI 技能集
└── .joycode/              # JoyCode 配置
```

---

## 开发阶段

| 阶段 | 名称 | 状态 |
|------|------|------|
| Phase 0 | 项目基础设施 | ✅ 进行中 |
| Phase 1 | 核心功能 | 未开始 |
| Phase 2 | 成长记录 | 未开始 |
| Phase 3 | 媒体增强 | 未开始 |
| Phase 4 | 数据安全 | 未开始 |
| Phase 5 | 精益打磨 | 未开始 |

详见 [ROADMAP.md](docs/ROADMAP.md)

---

## 核心原则

1. **永久离线** — 不声明 INTERNET 权限，不使用任何网络服务
2. **数据安全** — 儿童数据是最高优先级，绝不泄漏
3. **媒体分离** — 数据库存索引，文件系统存内容
4. **软删除** — 所有删除操作可恢复
5. **用户确认** — 所有危险操作需要确认

详见 [AGENTS.md](AGENTS.md)

---

## 规范文档

| 文档 | 说明 |
|------|------|
| [AGENTS.md](AGENTS.md) | Agent 规则（必读） |
| [PRODUCT_SPEC.md](docs/PRODUCT_SPEC.md) | 产品规格 |
| [ARCHITECTURE.md](docs/ARCHITECTURE.md) | 技术架构 |
| [DATA_MODEL.md](docs/DATA_MODEL.md) | 数据模型 |
| [DESIGN_SYSTEM.md](docs/DESIGN_SYSTEM.md) | 设计系统 |
| [ANIMATION_GUIDELINES.md](docs/ANIMATION_GUIDELINES.md) | 动画规范 |
| [MEDIA_STORAGE.md](docs/MEDIA_STORAGE.md) | 媒体存储 |
| [BACKUP_SPEC.md](docs/BACKUP_SPEC.md) | 备份规范 |
| [SECURITY.md](docs/SECURITY.md) | 安全规范 |
| [PERFORMANCE.md](docs/PERFORMANCE.md) | 性能规范 |
| [TESTING.md](docs/TESTING.md) | 测试规范 |
| [PERMISSIONS.md](docs/PERMISSIONS.md) | 权限规范 |
| [ERROR_HANDLING.md](docs/ERROR_HANDLING.md) | 错误处理 |
| [ACCESSIBILITY.md](docs/ACCESSIBILITY.md) | 无障碍规范 |
| [ROADMAP.md](docs/ROADMAP.md) | 开发路线图 |
| [DEVELOPMENT_WORKFLOW.md](docs/DEVELOPMENT_WORKFLOW.md) | 开发流程 |
| [DECISION_LOG.md](docs/DECISION_LOG.md) | 决策日志 |
| [CHANGELOG.md](docs/CHANGELOG.md) | 变更日志 |

---

## 许可

私有项目，未授权禁止使用。

---

*为团团，记录每一个珍贵的瞬间。*