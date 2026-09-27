# PERFORMANCE.md — Tuantuan Journal 性能规范

> **本文件定义 Tuantuan Journal 的性能标准和约束。**
> **流畅体验是基础要求，性能问题必须在开发阶段解决。**

---

## 1. 性能目标

### 1.1 响应时间

| 操作 | 目标 | 上限 |
|------|------|------|
| 页面切换 | < 100ms | 200ms |
| 列表滚动 | 60fps | 55fps |
| 数据库写入 | < 50ms | 100ms |
| 数据库查询 | < 30ms | 80ms |
| 图片缩略图加载 | < 200ms | 500ms |
| 图片全尺寸加载 | < 500ms | 1000ms |
| 备份创建 | 渐进进度 | - |
| 备份恢复 | 渐进进度 | - |

### 1.2 内存

| 指标 | 目标 | 上限 |
|------|------|------|
| 应用内存占用 | < 150MB | 250MB |
| 图片缓存 | < 50MB | 80MB |
| 单张图片解码 | < 5MB | 10MB |

### 1.3 存储

| 指标 | 说明 |
|------|------|
| APK 大小 | < 20MB（不含媒体） |
| 数据库大小 | 取决于数据量，定期优化 |
| 缩略图缓存 | 自动管理，LRU 策略 |

---

## 2. 列表性能

### 2.1 LazyColumn/LazyVerticalGrid 规范

- 使用 `key` 参数标识项目
- 避免在 item 中创建新对象
- 项目布局扁平化（减少嵌套）
- 使用 `contentType` 区分项目类型
- 分页加载，不一次加载全部

```kotlin
// 正确
LazyColumn {
    items(
        count = items.size,
        key = { index -> items[index].id },
        contentType = { index -> "diary_entry" }
    ) { index ->
        DiaryEntryItem(entry = items[index])
    }
}

// 禁止
LazyColumn {
    items(items) { entry ->  // 缺少 key
        DiaryEntryItem(entry = entry)
    }
}
```

### 2.2 图片加载

- 使用 Coil 加载图片
- 列表中只加载缩略图（200×200）
- 设置合适的 `size` 参数避免过度解码
- 使用 `crossfade` 过渡

```kotlin
AsyncImage(
    model = ImageRequest.Builder(context)
        .data(thumbnailPath)
        .size(200)  // 缩略图尺寸
        .crossfade(true)
        .build(),
    contentDescription = null
)
```

---

## 3. 数据库性能

### 3.1 Room 优化

- 使用索引加速高频查询
- 避免在主线程操作数据库
- 使用 Flow 实现增量更新
- 批量操作使用事务

```kotlin
// 批量插入
@Transaction
suspend fun insertAll(entries: List<DiaryEntryEntity>) {
    diaryEntryDao().insertAll(entries)
}
```

### 3.2 查询优化

- 避免 `SELECT *`，只查需要的列
- 复杂查询使用 `@RawQuery`
- 分页使用 `PagingSource`
- 全文搜索使用 `Fts4`

### 3.3 数据库维护

- 定期执行 `PRAGMA optimize`
- 定期执行 `VACUUM`（大数据量后）
- 监控数据库大小
- 软删除数据定期清理

---

## 4. 图片性能

### 4.1 图片解码

- 根据显示尺寸解码（不加载原图到内存）
- 使用 Coil 的自动尺寸适配
- 长图使用 BitmapRegionDecoder
- 大图使用 SubsamplingScaleImageView

### 4.2 图片缓存

- Coil 自动管理内存缓存
- 磁盘缓存使用 Coil 默认配置
- 缩略图不重复生成（缓存到文件）
- 缓存策略：LRU，最大 50MB

### 4.3 相机照片

- 现代相机照片可能 > 10MB
- 保存时保留原图（不压缩）
- 显示时使用缩略图
- 全屏查看时按需解码

---

## 5. 启动性能

### 5.1 冷启动

- 目标：< 500ms 到可交互
- 避免在 Application.onCreate 做重操作
- 延迟初始化非必要组件
- 使用 Hilt 延迟注入

### 5.2 热启动

- 目标：< 100ms 恢复
- 保存 UI 状态
- 使用 `savedStateHandle`
- 避免重新加载数据

### 5.3 启动优化

```kotlin
// Application 中只初始化必要组件
class TuantuanApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // 只做最少的初始化
        // 数据库初始化延迟到首次访问
    }
}
```

---

## 6. 动画性能

### 6.1 帧率目标

- 动画期间保持 60fps
- 避免在动画期间做 IO 操作
- 使用硬件加速
- 避免过度绘制

### 6.2 动画约束

- 详见 [ANIMATION_GUIDELINES.md](ANIMATION_GUIDELINES.md)
- 不在动画回调中做重计算
- 使用 `animate*AsState` 而非手动动画
- 限制同时运行的动画数量

---

## 7. 内存管理

### 7.1 内存泄漏防护

- ViewModel 不持有 View/Context 引用
- 使用 WeakReference 持有大对象
- 协程在 ViewModel scope 中启动
- 注册/反注册成对出现

### 7.2 大对象处理

- 图片使用 Coil 管理
- 视频使用 Media3 管理
- 大列表使用分页
- 避免在内存中持有多个大图

### 7.3 GC 优化

- 避免在热路径创建短命对象
- 使用对象池复用
- 使用 `sequence` 替代 `list` 做中间操作
- 避免频繁的字符串拼接

---

## 8. 协程性能

### 8.1 调度器选择

| 操作类型 | 调度器 | 说明 |
|---------|--------|------|
| UI 操作 | Dispatchers.Main | Compose 状态更新 |
| 数据库 | Dispatchers.IO | Room 操作 |
| 文件操作 | Dispatchers.IO | 媒体文件读写 |
| 计算 | Dispatchers.Default | 图片处理、数据转换 |

### 8.2 协程最佳实践

- 不在 `withContext(Dispatchers.IO)` 中更新 UI
- 使用 `flowOn` 指定 Flow 的执行调度器
- 避免嵌套 `withContext`
- 使用 `supervisorScope` 隔离子协程失败

---

## 9. 性能监控

### 9.1 开发阶段

- 使用 Android Profiler 监控
- 使用 `Debug.startMethodTracing` 定位瓶颈
- 使用 Compose Layout Inspector 检查重组
- 使用 `StrictMode` 检测主线程 IO

### 9.2 发布阶段

- 不集成第三方 APM SDK
- 通过用户反馈发现性能问题
- 可选：自建简单性能日志（不收集用户数据）

---

## 10. 性能检查清单

每次代码变更时，检查：

- [ ] 列表是否使用 key？
- [ ] 图片是否使用缩略图？
- [ ] 数据库操作是否在 IO 线程？
- [ ] 是否有潜在的内存泄漏？
- [ ] 动画是否流畅？
- [ ] 启动时间是否合理？
- [ ] 是否有不必要的重新组合？
- [ ] 大文件操作是否异步？

---

*本文件最后更新：Phase 0 — 项目初始化*