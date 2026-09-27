# PERFORMANCE_UI.md — UI 性能规范

> **UI 不能为了"漂亮"牺牲性能。**
> **与 docs/PERFORMANCE.md 协调，本文件侧重 UI 层面。**

---

## 1. UI 性能原则

- 流畅优先于炫酷
- 内容优先于装饰
- 静态优先于动画
- 缩略图优先于原图
- 异步优先于同步

---

## 2. 禁止的 UI 行为

- 无限循环动效
- 超重 Blur（模糊）
- 大量实时粒子
- 大量透明图层
- 滚动中大量图片解码
- 主线程执行文件操作
- 主线程执行复杂数据库操作

---

## 3. 列表性能

### 3.1 LazyColumn

- 必须提供 `key`：使用唯一 ID
- 必须提供 `contentType`：区分卡片类型
- 避免在 Item 中创建新对象
- 使用 `remember` 缓存计算结果

```kotlin
LazyColumn {
    items(
        items = diaryEntries,
        key = { it.id },
        contentType = { "diary_card" }
    ) { entry ->
        TtDiaryCard(entry = entry)
    }
}
```

### 3.2 图片加载

- 使用 Coil 加载图片
- 列表中使用缩略图（200×200）
- 全屏时加载原图
- 设置合理的内存缓存
- 使用 `Crossfade` 过渡

---

## 4. 图片性能

### 4.1 解码

- 列表中使用 `size(200)` 限制解码尺寸
- 全屏使用 `size(Size.ORIGINAL)`
- 不在动画中加载原图

### 4.2 缓存

- 内存缓存：可用内存的 1/4
- 磁盘缓存：缩略图 + 原图分别缓存
- 使用 Coil 的默认缓存策略

---

## 5. 动画性能

### 5.1 目标

- 页面切换 < 100ms
- 滚动 60fps
- 动画 60fps

### 5.2 约束

- 动画期间不执行重型操作
- 使用 `remember` 和 `derivedStateOf` 减少重组
- 图片使用缩略图，不在动画中加载原图
- 粒子数量 ≤ 20
- 不使用 Blur 效果

详见 → [ANIMATION_BIBLE.md](ANIMATION_BIBLE.md) §15

---

## 6. Compose 性能

### 6.1 重组

- 使用 `remember` 缓存计算结果
- 使用 `derivedStateOf` 避免不必要重组
- Lambda 使用 `remember` 或 `mutableStateOf`
- 避免在 Composable 中创建新对象

### 6.2 布局

- 避免过深的嵌套
- 使用 `Modifier` 链而非嵌套 Box
- 避免在滚动中使用 `SubcomposeLayout`

---

## 7. 冷启动

- 冷启动 < 500ms
- 首屏显示 < 300ms
- 避免启动时执行重型初始化
- 使用懒加载

---

## 8. 内存

- App 内存占用 < 150MB
- 图片缓存合理释放
- 避免 Activity 泄漏
- 使用 LeakCanary 检测

---

*本文件版本：v1.0 | 最后更新：2026-09-27*