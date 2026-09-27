# LOADING_STATES.md — 加载状态设计

> **不要只放一个无限转圈。**
> **根据内容选择合适的加载方式。**

---

## 1. 加载状态原则

- 加载状态应该给用户信心：正在处理
- 根据内容类型选择合适的加载方式
- 加载时间 < 1 秒：可以不显示加载状态
- 加载时间 1-3 秒：显示轻量加载指示
- 加载时间 > 3 秒：显示 Skeleton 或进度

---

## 2. 加载方式

### 2.1 Skeleton（骨架屏）

适用于：列表、卡片、已知布局的内容

```
[灰色矩形]  ← 标题位置
[灰色矩形]  ← 正文位置
[灰色矩形]  ← 图片位置
```

- 使用 PrimaryContainer 色，TuantuanOpacity.Disabled (0.12f)
- 轻微闪烁动画
- 布局与实际内容一致
- 时长：Normal (300ms) 淡入

### 2.2 淡入

适用于：单张图片、简单内容

```
占位色 → 内容 Crossfade
```

- 时长：Normal (300ms)
- 缓动：Standard

### 2.3 进度条

适用于：备份、恢复、批量操作

```
[━━━━━━━━░░░░░░░░] 60%
```

- 使用 TtProgressBar
- Primary 色
- 显示百分比（可选）
- 不确定进度时使用 indeterminate 模式

### 2.4 占位卡片

适用于：首页、数据概览

```
[Surface 色 Card]
  [PrimaryContainer 色 矩形]
  [PrimaryContainer 色 矩形]
```

- 与实际卡片布局一致
- 轻微脉冲动画

---

## 3. 加载状态组件

使用 `TtLoadingState` 组件：

- Skeleton 模式：显示骨架屏
- Indicator 模式：显示加载圈
- Progress 模式：显示进度条

---

## 4. 加载完成过渡

```
Skeleton/Loading 淡出 → 内容淡入
```

- 时长：Normal (300ms)
- 缓动：Standard
- 避免内容突然出现

---

## 5. 加载失败过渡

```
内容/Loading 淡出 → ErrorState 淡入
```

- 时长：Normal (300ms)
- 缓动：Standard

详见 → [ERROR_STATES.md](ERROR_STATES.md)

---

## 6. 禁止

- 不要在所有地方都使用无限转圈
- 不要使用大面积 Blur 作为加载效果
- 不要在加载时阻塞整个页面（除非必要）
- 不要使用过于花哨的加载动画

---

*本文件版本：v1.0 | 最后更新：2026-09-27*