# DEVELOPMENT_WORKFLOW.md — Tuantuan Journal 开发流程

> **本文件定义 Tuantuan Journal 的开发工作流程。**
> **所有开发必须遵循本流程，确保质量和一致性。**

---

## 1. Agent 启动协议

### 1.1 必读文件（按顺序）

1. **AGENTS.md** — 最高优先级规则
2. **PRODUCT_SPEC.md** — 产品规格
3. **ROADMAP.md** — 当前阶段
4. **相关专项文档** — 根据任务类型选择

### 1.2 任务类型与文档映射

| 任务类型 | 必读文档 |
|---------|---------|
| UI 开发 | DESIGN_SYSTEM.md, ANIMATION_GUIDELINES.md, ACCESSIBILITY.md |
| 数据层开发 | DATA_MODEL.md, MEDIA_STORAGE.md, ARCHITECTURE.md |
| 备份功能 | BACKUP_SPEC.md, SECURITY.md |
| 性能优化 | PERFORMANCE.md |
| 测试编写 | TESTING.md |
| 错误处理 | ERROR_HANDLING.md |

---

## 2. 开发流程

### 2.1 功能开发流程

```
1. 确认任务属于当前阶段（ROADMAP.md）
    ↓
2. 阅读相关规范文档
    ↓
3. 设计方案（遵循 ARCHITECTURE.md 分层）
    ↓
4. 实现代码
    ↓
5. 编写测试
    ↓
6. 自检（AGENTS.md 工作检查清单）
    ↓
7. 更新 CHANGELOG.md
    ↓
8. 提交代码
```

### 2.2 代码提交规范

#### 提交信息格式

```
<type>(<scope>): <subject>

<body>

<footer>
```

#### Type

| Type | 说明 |
|------|------|
| feat | 新功能 |
| fix | 修复缺陷 |
| docs | 文档变更 |
| style | 代码格式（不影响逻辑） |
| refactor | 重构（不新增功能） |
| test | 测试 |
| chore | 构建/工具变更 |

#### Scope

| Scope | 说明 |
|-------|------|
| ui | UI 相关 |
| data | 数据层 |
| domain | 业务逻辑层 |
| media | 媒体处理 |
| backup | 备份恢复 |
| db | 数据库 |
| nav | 导航 |
| theme | 主题 |

#### 示例

```
feat(ui): 添加日记列表页面

- 实现 DiaryListScreen Composable
- 实现 DiaryListItem 组件
- 添加分页加载
- 添加空状态展示

Closes #12
```

---

## 3. 分层开发规则

### 3.1 新增功能开发顺序

```
1. Domain 层
   - 定义 Domain Model
   - 定义 Repository 接口
   - 实现 UseCase

2. Data 层
   - 定义 Entity
   - 定义 DAO
   - 实现 Repository
   - 实现 Mapper

3. Presentation 层
   - 实现 ViewModel
   - 实现 UI State
   - 实现 Compose UI
   - 实现导航
```

### 3.2 修改现有功能

```
1. 确认影响范围
2. 修改 Domain 层（如需要）
3. 修改 Data 层（如需要）
4. 修改 Presentation 层
5. 更新测试
6. 验证所有层级
```

---

## 4. 代码审查清单

### 4.1 架构审查

- [ ] 分层是否正确？
- [ ] 依赖方向是否正确？
- [ ] 是否有跨层直接访问？
- [ ] 新增类是否在正确的包中？

### 4.2 数据安全审查

- [ ] 是否遵循离线原则？
- [ ] 媒体文件是否存储在私有目录？
- [ ] 数据库是否只存索引？
- [ ] 危险操作是否有确认？
- [ ] 日志是否泄露用户数据？

### 4.3 UI 审查

- [ ] 是否遵循设计系统？
- [ ] 是否遵循动画规范？
- [ ] 是否遵循无障碍规范？
- [ ] 是否处理了 Loading/Error 状态？

### 4.4 测试审查

- [ ] UseCase 是否有测试？
- [ ] Mapper 是否有测试？
- [ ] 关键路径是否有 UI 测试？
- [ ] 边界值是否覆盖？

---

## 5. 版本管理

### 5.1 版本号

遵循语义化版本（SemVer）：

```
MAJOR.MINOR.PATCH

MAJOR: 不兼容的 API 变更
MINOR: 向后兼容的功能新增
PATCH: 向后兼容的缺陷修复
```

### 5.2 分支策略

```
main        — 稳定发布
develop     — 开发集成
feature/*   — 功能分支
fix/*       — 修复分支
```

### 5.3 发布流程

```
1. 完成 Phase 所有功能
2. 所有测试通过
3. 更新 CHANGELOG.md
4. 合并到 main
5. 打 tag (vX.Y.Z)
6. 构建 Release APK
```

---

## 6. 决策记录

所有技术决策记录在 [DECISION_LOG.md](DECISION_LOG.md)，包括：

- 决策内容
- 决策原因
- 替代方案
- 影响范围

---

*本文件最后更新：Phase 0 — 项目初始化*