package com.tuantuan.journal.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * UseCase 依赖注入模块。
 *
 * 所有 UseCase 均使用 @Inject constructor，Hilt 自动提供依赖。
 * 本模块作为架构文档，记录所有可用的 UseCase 及其职责。
 *
 * 架构规范要求：ARCHITECTURE.md 第4节 — di/ 包结构
 *
 * === Child UseCases ===
 * - GetChildrenUseCase: 获取所有活跃儿童档案
 * - SaveChildUseCase: 创建/更新儿童档案
 * - DeleteChildUseCase: 软删除儿童档案
 *
 * === Diary UseCases ===
 * - GetDiaryEntriesUseCase: 获取日记列表（含收藏/最近）
 * - GetDiaryEntryUseCase: 获取单条日记详情（含媒体和标签）
 * - SaveDiaryEntryUseCase: 创建/更新日记
 * - SearchDiaryEntriesUseCase: 按关键词搜索日记
 * - ToggleFavoriteUseCase: 切换日记收藏状态
 * - DeleteDiaryEntryUseCase: 软删除日记（需UI确认）
 *
 * === Tag UseCases ===
 * - GetTagsUseCase: 获取标签列表（含按日记筛选）
 * - SaveTagUseCase: 创建标签
 * - ManageEntryTagUseCase: 管理日记-标签关联
 *
 * === Media UseCases ===
 * - SaveMediaUseCase: 保存媒体文件（物理文件+数据库索引，含大小/数量验证）
 * - DeleteMediaUseCase: 软删除媒体文件（数据库软删除，物理文件延迟清理）
 */
@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule