# DESIGN_TOKENS.md — Tuantuan Journal Design Token 系统

> **本文件定义 Tuantuan Journal 的完整 Design Token 体系。**
> **所有未来组件必须优先使用 Token。禁止页面里大量出现完全随意的值。**
> **如果需要一个新值：优先扩展 Token。**

---

## 1. Token 总览

| 类别 | Token 对象 | 详见 |
|------|-----------|------|
| 颜色 | `TuantuanColors` | [COLOR_SYSTEM.md](COLOR_SYSTEM.md) |
| 字体 | `TuantuanTypography` | [TYPOGRAPHY.md](TYPOGRAPHY.md) |
| 间距 | `TuantuanSpacing` | [SPACING.md](SPACING.md) |
| 圆角 | `TuantuanRoundedCorner` | [SHAPES.md](SHAPES.md) |
| 阴影 | `TuantuanElevation` | 本文件 §6 |
| 透明度 | `TuantuanOpacity` | 本文件 §7 |
| 图标尺寸 | `TuantuanIconSize` | [ICONOGRAPHY.md](ICONOGRAPHY.md) |
| 触控区域 | `TuantuanTouchTarget` | 本文件 §8 |
| 动画时长 | `TuantuanDuration` | [MOTION_TOKENS.md](MOTION_TOKENS.md) |
| 动画缓动 | `TuantuanEasing` | [MOTION_TOKENS.md](MOTION_TOKENS.md) |

---

## 2. Token 使用原则

### 2.1 优先使用 Token

```kotlin
// ✅ 正确：使用 Token
Spacer(modifier = Modifier.height(TuantuanSpacing.Base))

// ❌ 错误：硬编码值
Spacer(modifier = Modifier.height(16.dp))
```

### 2.2 扩展 Token 而非硬编码

如果现有 Token 不满足需求：

1. 先在本文件或对应专项文档中添加新 Token
2. 说明 Token 名称、值、用途
3. 然后在代码中使用新 Token

```kotlin
// ✅ 正确：扩展 Token
// 在 TuantuanSpacing 中添加：
val SectionGap: Dp = 36.dp  // 大区块间距，介于 XL 和 XXL 之间

// ❌ 错误：直接硬编码
Spacer(modifier = Modifier.height(36.dp))
```

### 2.3 禁止的随意值

```text
24.dp    → 使用 TuantuanSpacing.XL
17.dp    → 不存在此 Token，评估是否真的需要
13.dp    → 不存在此 Token，评估是否真的需要
#xxxxxx  → 必须使用语义色 Token
```

---

## 3. 颜色 Token

详见 → [COLOR_SYSTEM.md](COLOR_SYSTEM.md)

```kotlin
object TuantuanColors {
    // 品牌色
    val Primary: Color
    val OnPrimary: Color
    val PrimaryContainer: Color
    val OnPrimaryContainer: Color

    // 辅助色
    val Secondary: Color
    val OnSecondary: Color
    val SecondaryContainer: Color
    val OnSecondaryContainer: Color

    // 第三色
    val Tertiary: Color
    val OnTertiary: Color
    val TertiaryContainer: Color
    val OnTertiaryContainer: Color

    // 背景色
    val Background: Color
    val OnBackground: Color

    // 表面色
    val Surface: Color
    val OnSurface: Color
    val SurfaceVariant: Color
    val OnSurfaceVariant: Color

    // 轮廓
    val Outline: Color
    val OutlineVariant: Color

    // 功能色
    val Error: Color
    val OnError: Color
    val Success: Color
    val Warning: Color
    val Info: Color

    // 反转
    val InverseSurface: Color
    val InverseOnSurface: Color
    val Scrim: Color
}
```

---

## 4. 字体 Token

详见 → [TYPOGRAPHY.md](TYPOGRAPHY.md)

```kotlin
object TuantuanTypography {
    val DisplayLarge: TextStyle
    val DisplayMedium: TextStyle
    val DisplaySmall: TextStyle
    val HeadlineLarge: TextStyle
    val HeadlineMedium: TextStyle
    val HeadlineSmall: TextStyle
    val TitleLarge: TextStyle
    val TitleMedium: TextStyle
    val TitleSmall: TextStyle
    val BodyLarge: TextStyle
    val BodyMedium: TextStyle
    val BodySmall: TextStyle
    val LabelLarge: TextStyle
    val LabelMedium: TextStyle
    val LabelSmall: TextStyle
    val Caption: TextStyle
}
```

---

## 5. 间距 Token

详见 → [SPACING.md](SPACING.md)

```kotlin
object TuantuanSpacing {
    val XS: Dp   = 4.dp
    val SM: Dp   = 8.dp
    val MD: Dp   = 12.dp
    val Base: Dp = 16.dp
    val LG: Dp   = 20.dp
    val XL: Dp   = 24.dp
    val XXL: Dp  = 32.dp
    val XXXL: Dp = 48.dp
}
```

---

## 6. 阴影 Token

```kotlin
object TuantuanElevation {
    val Level0: Dp = 0.dp     // 页面背景
    val Level1: Dp = 1.dp     // 卡片（resting）
    val Level2: Dp = 3.dp     // 卡片（hovered）
    val Level3: Dp = 6.dp     // BottomSheet、Dialog
    val Level4: Dp = 8.dp     // Modal、Snackbar
    val Level5: Dp = 12.dp    // 最高层级
}
```

### 使用规则

- 页面背景无阴影（Level0）
- 普通卡片优先使用 Surface/Tone 区分层级，而非阴影
- 真正需要时才使用轻微 Elevation
- 禁止随意使用未定义的阴影值

---

## 7. 透明度 Token

```kotlin
object TuantuanOpacity {
    val Full: Float     = 1.0f   // 完全不透明
    val High: Float     = 0.87f  // 主要文字
    val Medium: Float   = 0.60f  // 次要文字
    val Low: Float      = 0.38f  // 提示文字、禁用状态
    val Disabled: Float = 0.12f  // 禁用背景
    val Scrim: Float    = 0.32f  // 遮罩层
}
```

### 使用规则

- 文字透明度使用 High/Medium/Low
- 禁用状态使用 Disabled
- Dialog/BottomSheet 遮罩使用 Scrim
- 禁止随意使用未定义的透明度值

---

## 8. 触控区域 Token

```kotlin
object TuantuanTouchTarget {
    val Minimum: Dp = 48.dp    // 最小触控区域（WCAG 推荐）
    val Comfortable: Dp = 56.dp // 舒适触控区域
    val Compact: Dp = 40.dp    // 紧凑触控（仅限次要操作）
}
```

### 使用规则

- 所有关键操作必须有合理触控区域
- 视觉上可以是小图标，但触控区域必须 ≥ Minimum (48dp)
- 紧凑触控仅用于次要操作，且需确保不误触
- 禁止出现只能精准点 12dp 小位置的操作

---

## 9. 圆角 Token

详见 → [SHAPES.md](SHAPES.md)

```kotlin
object TuantuanRoundedCorner {
    val Small: Dp      = 8.dp    // Chip、Tag
    val Medium: Dp     = 12.dp   // Button、TextField
    val Large: Dp      = 16.dp   // Card、Dialog
    val ExtraLarge: Dp = 24.dp   // BottomSheet、Modal
    val Full: Dp       = 50.dp   // Avatar、FAB
}
```

---

## 10. 图标尺寸 Token

详见 → [ICONOGRAPHY.md](ICONOGRAPHY.md)

```kotlin
object TuantuanIconSize {
    val Small: Dp  = 16.dp   // 辅助图标
    val Medium: Dp = 24.dp   // 标准图标
    val Large: Dp  = 32.dp   // 强调图标
    val XLarge: Dp = 48.dp   // 空状态插图
}
```

---

## 11. 动画 Token

详见 → [MOTION_TOKENS.md](MOTION_TOKENS.md)

```kotlin
object TuantuanDuration {
    val Instant: Int  = 50     // 即时反馈
    val Quick: Int    = 150    // 快速过渡
    val Normal: Int   = 300    // 标准过渡
    val Slow: Int     = 500    // 慢速过渡
    val Emphasis: Int = 700    // 强调动画
}
```

---

## 12. Token 扩展流程

当现有 Token 不满足需求时：

1. **评估**：是否真的需要新值？能否用现有 Token 组合？
2. **命名**：使用语义化命名，不使用数值命名
3. **定义**：在对应专项文档中添加定义
4. **记录**：在 DESIGN_DECISIONS.md 中记录新增原因
5. **使用**：在代码中引用新 Token

---

*本文件版本：v1.0 | 最后更新：2026-09-27*