---
name: project_tuantuan_journal
description: Tuantuan Journal 项目概况和核心约束
type: project
---

Tuantuan Journal / 团团日记 — 纯离线儿童成长记录 Android 应用

**核心约束：**
- 永久离线：不声明 INTERNET 权限，不使用任何网络服务
- 数据安全：儿童数据是最高优先级
- 媒体分离：数据库存索引，文件系统存内容
- 软删除：所有实体包含 isDeleted 字段
- 危险操作需确认：删除、恢复备份等

**技术栈：** Kotlin, Jetpack Compose, Material 3, Room, Hilt, Coil, Media3, Coroutines/Flow, DataStore

**架构：** Clean Architecture (Presentation → Domain → Data)

**当前阶段：** Phase 0 — 项目基础设施（治理体系已建立）

**Why:** 用户明确要求先建立"项目宪法"约束未来 AI Agent，不开发任何业务功能
**How to apply:** 所有开发必须先读 AGENTS.md → PRODUCT_SPEC.md → ROADMAP.md，严格遵循规范文档