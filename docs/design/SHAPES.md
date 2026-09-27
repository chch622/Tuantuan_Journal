# SHAPES.md — Tuantuan Journal 形状系统

> **本文件定义 Tuantuan Journal 的圆角与形状系统。**
> **整体偏圆润，但不等于所有东西都做成药丸。**

---

## 1. 圆角 Token

```kotlin
object TuantuanRoundedCorner {
    val Small: Dp      = 8.dp    // 小型组件：Chip、Tag、Badge
    val Medium: Dp     = 12.dp   // 中型组件：Button、TextField、Switch
    val Large: Dp      = 16.dp   // 大型组件：Card、Dialog、Banner
    val ExtraLarge: Dp = 24.dp   // 特大组件：BottomSheet、Modal、ImageCard
    val Full: Dp       = 50.dp   // 圆形：Avatar、FAB、IconButton
}
```

---

## 2. 圆角使用规则

| 组件类型 | Token | 值 | 示例 |
|----------|-------|-----|------|
| Chip / Tag / Badge | Small | 8dp | 标签、筛选器 |
| Button / TextField | Medium | 12dp | 操作按钮、输入框 |
| Card / Dialog | Large | 16dp | 内容卡片、对话框 |
| BottomSheet / Modal | ExtraLarge | 24dp | 底部面板、全屏弹窗 |
| Avatar / FAB | Full | 50dp | 头像、浮动按钮 |
| ImageCard | ExtraLarge | 24dp | 照片卡片（大圆角增加柔和感） |
| Divider | 无 | 0dp | 分割线无圆角 |
| TopBar | 无 | 0dp | 顶部栏无圆角 |
| NavigationBar | 无 | 0dp | 底部导航无圆角 |

---

## 3. 圆角原则

### 3.1 整体偏圆润

Tuantuan Journal 的视觉风格要求组件偏圆润，营造温柔感。

### 3.2 不等于所有东西都做成药丸

- 按钮使用 Medium (12dp)，不是 Full
- 卡片使用 Large (16dp)，不是 Full
- 只有 Avatar 和 FAB 使用 Full

### 3.3 一致性

- 禁止每个组件自己随意决定圆角
- 新组件必须从以上 Token 中选择
- 如果现有 Token 不满足需求，先在此添加新 Token

---

## 4. 形状层级

```
Level 0  无圆角    — 页面级容器、TopBar、NavigationBar
Level 1  Small     — 小型内联组件
Level 2  Medium    — 中型交互组件
Level 3  Large     — 大型内容容器
Level 4  ExtraLarge — 弹出层、特殊容器
Level 5  Full      — 圆形元素
```

层级越高，圆角越大，组件越"浮"在页面上。

---

## 5. 特殊形状

### 5.1 照片裁剪

照片使用 ExtraLarge (24dp) 圆角裁剪，增加柔和感。

允许：
- 圆角大图
- 柔和裁剪
- 轻微阴影

不允许：
- 过度装饰相框
- 圆形裁剪照片（仅头像使用圆形）

### 5.2 头像

头像使用 Full (50dp) 圆形裁剪。

### 5.3 浮动按钮

中心 "+" 按钮使用 Full 圆形，但不做成夸张的悬浮按钮。

---

## 6. 禁止事项

- 禁止每个页面使用不同的圆角体系
- 禁止所有组件都使用 Full 圆角
- 禁止硬编码圆角值
- 新增圆角需求优先使用现有 Token

---

*本文件版本：v1.0 | 最后更新：2026-09-27*