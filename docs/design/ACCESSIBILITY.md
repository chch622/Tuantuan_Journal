# ACCESSIBILITY.md — 无障碍设计

> **本文件是 Tuantuan Journal 的无障碍设计规范。**
> **与 docs/ACCESSIBILITY.md 协调，本文件侧重设计层面。**

---

## 1. 无障碍设计原则

- 不依赖颜色表达信息
- 不依赖手势完成关键操作
- 所有交互元素有替代方式
- 文字对比度满足 WCAG AA
- 触控区域足够大
- 支持系统字体放大
- 尊重"减少动画"设置

---

## 2. 触控区域

所有关键操作必须有合理触控区域：

- 最小触控区域：48×48dp
- 舒适触控区域：56×56dp
- 紧凑触控（仅次要操作）：40×40dp

视觉上可以是小图标，但触控区域必须 ≥ 48dp。

详见 → [DESIGN_TOKENS.md](DESIGN_TOKENS.md) §8

---

## 3. 颜色对比

### 3.1 文字对比度

| 场景 | 最低对比度 | 标准 |
|------|-----------|------|
| 主要文字 | 4.5:1 | WCAG AA |
| 次要文字 | 4.5:1 | WCAG AA |
| 大文字 (≥18sp) | 3:1 | WCAG AA |
| 装饰文字 | 无要求 | — |

### 3.2 不依赖颜色

- 错误状态：颜色 + 图标
- 成功状态：颜色 + 图标
- 选中状态：颜色 + 位置/大小变化
- 媒体类型：图标 + 标签，不能仅靠颜色

---

## 4. Content Description

所有功能性图标必须提供 `contentDescription`：

```kotlin
// ✅ 正确
Icon(Icons.Outlined.Delete, contentDescription = "删除")

// ❌ 错误
Icon(Icons.Outlined.Delete, contentDescription = null)  // 功能性图标
Icon(Icons.Outlined.Delete, contentDescription = "垃圾桶图标")  // 描述图标而非功能
```

装饰性图标：`contentDescription = null`

---

## 5. TalkBack 导航

### 5.1 导航顺序

- 从上到下，从左到右
- 语义分组：使用 `mergeDescendants`
- 跳过装饰性元素

### 5.2 焦点顺序

- 卡片整体作为一个焦点
- 卡片内操作按钮单独焦点
- 列表项按顺序焦点

### 5.3 状态描述

```kotlin
// 选中状态
Modifier.semantics { stateDescription = "已选中" }

// 展开状态
Modifier.semantics { stateDescription = "已展开" }
```

---

## 6. 减少动画

如果系统启用了 Reduce Motion：

- 减少位移动画
- 减少缩放动画
- 减少大型转场
- 减少粒子效果
- 保留必要状态反馈（颜色变化、图标变化）
- 使用 Instant (50ms) 或直接切换

```kotlin
val areAnimationsEnabled = !LocalAccessibilityManager.current.areAnimationsDisabled()
```

详见 → [ANIMATION_BIBLE.md](ANIMATION_BIBLE.md) §14

---

## 7. 字体放大

系统字体变大时：

- 文本容器使用 `wrapContentHeight`
- 不能出现文本截断
- 不能出现按钮文字消失
- 不能出现固定高度卡片溢出
- 最大字号限制在 DisplayLarge 的 1.5 倍

详见 → [TYPOGRAPHY.md](TYPOGRAPHY.md) §8

---

## 8. 设计检查清单

- [ ] 触控区域 ≥ 48dp？
- [ ] 文字对比度 ≥ 4.5:1？
- [ ] 不依赖颜色表达信息？
- [ ] 所有功能图标有 contentDescription？
- [ ] 支持字体放大？
- [ ] 支持减少动画？
- [ ] TalkBack 导航顺序合理？
- [ ] 空状态有文字说明？

---

*本文件版本：v1.0 | 最后更新：2026-09-27*