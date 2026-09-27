# DESIGN_SYSTEM.md — Tuantuan Journal 设计系统

> **本文件定义 Tuantuan Journal 的统一视觉语言。**
> **所有 UI 开发必须遵循本设计系统，禁止页面自行随意定义样式。**

---

## 1. 设计哲学

**关键词：** 温柔、可爱、简洁、干净、轻盈、有生活感

**核心原则：**

- 照片是视觉核心，UI 不应抢夺照片的注意力
- 留白比装饰更重要
- 一致性比创意更重要
- 功能性比视觉炫技更重要
- 可爱但不幼稚

---

## 2. 色彩系统

### 2.1 设计基础色

| 色彩名 | 描述 | 情感 |
|--------|------|------|
| 奶油白 | 温暖的白色基底 | 干净、温暖 |
| 柔和粉 | 淡粉色调 | 温柔、可爱 |
| 暖灰 | 带暖调的中性灰 | 沉稳、舒适 |
| 柔和强调色 | 柔和的点缀色 | 活泼、愉悦 |

### 2.2 语义化颜色 Token

```kotlin
object TuantuanColors {
    // 品牌色
    val Primary: Color          // 主品牌色
    val OnPrimary: Color        // 主品牌色上的文字
    val PrimaryContainer: Color // 主品牌色容器
    val OnPrimaryContainer: Color

    // 辅助色
    val Secondary: Color
    val OnSecondary: Color
    val SecondaryContainer: Color
    val OnSecondaryContainer: Color

    // 背景色
    val Background: Color       // 页面背景
    val OnBackground: Color     // 背景上的文字

    // 表面色
    val Surface: Color          // 卡片/组件表面
    val OnSurface: Color        // 表面上的文字
    val SurfaceVariant: Color   // 变体表面
    val OnSurfaceVariant: Color

    // 文字色
    val TextPrimary: Color      // 主要文字
    val TextSecondary: Color    // 次要文字
    val TextHint: Color         // 提示文字

    // 功能色
    val Divider: Color          // 分割线
    val Error: Color            // 错误
    val OnError: Color
    val Success: Color          // 成功
    val Warning: Color          // 警告

    // 特殊
    val InverseSurface: Color
    val InverseOnSurface: Color
    val Scrim: Color            // 遮罩
}
```

### 2.3 颜色使用规则

- 页面背景使用 `Background`
- 卡片使用 `Surface` 或 `SurfaceVariant`
- 主要文字使用 `OnSurface` / `TextPrimary`
- 次要文字使用 `OnSurfaceVariant` / `TextSecondary`
- 交互元素使用 `Primary`
- 错误状态使用 `Error`
- 成功状态使用 `Success`
- 禁止随意创建未定义的颜色值
- 新增颜色必须先在此定义语义 Token

---

## 3. 圆角系统

### 3.1 圆角 Token

```kotlin
object TuantuanRoundedCorner {
    val Small: Dp  = 8.dp    // 小型组件：Chip、Tag
    val Medium: Dp = 12.dp   // 中型组件：Button、TextField
    val Large: Dp  = 16.dp   // 大型组件：Card、Dialog
    val ExtraLarge: Dp = 24.dp // 特大组件：BottomSheet、Modal
    val Full: Dp   = 50      // 圆形：Avatar、FAB
}
```

### 3.2 圆角使用规则

- 禁止每个组件自己随意决定圆角
- 新组件必须从以上 Token 中选择
- 如果现有 Token 不满足需求，先在此添加新 Token，再使用

---

## 4. 间距系统

### 4.1 间距 Token

```kotlin
object TuantuanSpacing {
    val XS: Dp   = 4.dp     // 极小间距：图标与文字之间
    val SM: Dp   = 8.dp     // 小间距：同组元素之间
    val MD: Dp   = 12.dp    // 中间距：不同组元素之间
    val Base: Dp = 16.dp    // 基础间距：标准内边距
    val LG: Dp   = 20.dp    // 大间距：区块之间
    val XL: Dp   = 24.dp    // 超大间距：页面区块
    val XXL: Dp  = 32.dp    // 极大间距：页面顶部/底部
    val XXXL: Dp = 48.dp    // 特大间距：页面间留白
}
```

### 4.2 间距使用规则

- UI 优先使用设计系统中的 spacing token
- 禁止使用硬编码的间距数值
- 页面水平内边距统一使用 `Base` (16.dp)
- 列表项间距统一使用 `SM` (8.dp) 或 `MD` (12.dp)
- 区块间距统一使用 `LG` (20.dp) 或 `XL` (24.dp)

---

## 5. 排版系统

### 5.1 字体 Token

```kotlin
object TuantuanTypography {
    // 展示 — 首页大标题、年度回顾
    val DisplayLarge: TextStyle
    val DisplayMedium: TextStyle
    val DisplaySmall: TextStyle

    // 标题 — 页面标题
    val HeadlineLarge: TextStyle
    val HeadlineMedium: TextStyle
    val HeadlineSmall: TextStyle

    // 标题 — 区块标题
    val TitleLarge: TextStyle
    val TitleMedium: TextStyle
    val TitleSmall: TextStyle

    // 正文 — 内容文字
    val BodyLarge: TextStyle
    val BodyMedium: TextStyle
    val BodySmall: TextStyle

    // 标签 — 按钮文字、Tab
    val LabelLarge: TextStyle
    val LabelMedium: TextStyle
    val LabelSmall: TextStyle

    // 说明 — 辅助说明
    val Caption: TextStyle
}
```

### 5.2 排版使用规则

- 禁止页面自行随意定义字体大小
- 页面标题使用 `HeadlineMedium` 或 `HeadlineLarge`
- 区块标题使用 `TitleMedium` 或 `TitleLarge`
- 正文内容使用 `BodyLarge` 或 `BodyMedium`
- 辅助说明使用 `BodySmall` 或 `Caption`
- 按钮文字使用 `LabelLarge`
- 新增排版样式必须先在此定义

---

## 6. 组件系统

### 6.1 基础组件

| 组件 | 说明 | 圆角 | 备注 |
|------|------|------|------|
| Button | 主操作按钮 | Medium | 品牌色填充 |
| OutlinedButton | 次操作按钮 | Medium | 品牌色边框 |
| TextButton | 文字按钮 | Medium | 无边框 |
| IconButton | 图标按钮 | Full | 圆形 |
| Card | 内容卡片 | Large | Surface 色 |
| ElevatedCard | 提升卡片 | Large | 带阴影 |
| Chip | 标签/筛选 | Small | 小型圆角 |
| FilterChip | 筛选标签 | Small | 可选中 |
| Dialog | 对话框 | ExtraLarge | 居中弹出 |
| BottomSheet | 底部面板 | ExtraLarge | 从底部滑出 |
| TopBar | 顶部栏 | 无 | 含标题和操作 |
| NavigationBar | 底部导航 | 无 | 5个Tab |
| Divider | 分割线 | 无 | 细线 |

### 6.2 状态组件

| 组件 | 说明 |
|------|------|
| EmptyState | 空数据状态 |
| ErrorState | 错误状态 |
| LoadingState | 加载状态 |
| ProgressIndicator | 进度指示 |

### 6.3 业务组件

| 组件 | 说明 | 备注 |
|------|------|------|
| MediaCard | 媒体展示卡片 | 照片/视频/音频 |
| TimelineCard | 时间轴条目卡片 | 日记条目 |
| GrowthCard | 成长数据卡片 | 身高/体重等 |
| MilestoneCard | 里程碑卡片 | 人生第一次 |
| PhotoGrid | 照片网格 | 多图展示 |
| AgeDisplay | 年龄显示 | 精确到天 |
| BirthdayCountdown | 生日倒计时 | 特殊日期 |
| MoodIndicator | 心情指示 | 表情图标 |
| TagChip | 标签 | 可点击筛选 |

### 6.4 组件设计规则

- 所有组件必须从设计系统定义
- 禁止在页面中临时创建未定义的组件
- 新组件必须先在此注册，说明用途和规格
- 组件内部间距使用 spacing token
- 组件圆角使用 rounded corner token
- 组件颜色使用 semantic color token

---

## 7. 图标系统

### 7.1 图标风格

- 使用 Material Icons 作为基础
- 风格统一：Outlined 风格为主
- 大小统一：24dp 为标准尺寸
- 颜色使用语义化颜色 Token

### 7.2 自定义图标

- 如需自定义图标，必须先在设计系统中定义
- 保持线条粗细一致
- 保持视觉风格与 Material Icons 协调

---

## 8. 阴影与层级

### 8.1 层级定义

```kotlin
object TuantuanElevation {
    val Level0: Dp = 0.dp    // 页面背景
    val Level1: Dp = 1.dp    // 卡片（ resting ）
    val Level2: Dp = 3.dp    // 卡片（ hovered ）
    val Level3: Dp = 6.dp    // BottomSheet、Dialog
    val Level4: Dp = 8.dp    // Modal、Snackbar
    val Level5: Dp = 12.dp   // 最高层级
}
```

### 8.2 阴影使用规则

- 页面背景无阴影（Level0）
- 普通卡片使用 Level1
- 交互中的卡片使用 Level2
- 弹出层使用 Level3+
- 禁止随意使用未定义的阴影值
- 优先使用 Surface/Tone 区分层级，而非阴影

---

## 9. 暗色模式

### 9.1 原则

- 支持暗色模式
- 暗色模式不是简单反色
- 保持语义化颜色 Token 一致
- 暗色模式下照片依然是视觉核心
- 暗色模式下可读性优先

### 9.2 实现

- 使用 Material 3 动态颜色系统
- 所有颜色通过 Token 引用，不硬编码
- 暗色模式下的 Surface 使用深色调
- 文字对比度必须满足 WCAG AA 标准

---

## 10. 设计一致性检查清单

每次 UI 开发完成后，检查：

- [ ] 颜色是否使用语义化 Token？
- [ ] 圆角是否使用 Token？
- [ ] 间距是否使用 Token？
- [ ] 字体是否使用 Token？
- [ ] 组件是否已在设计系统中定义？
- [ ] 暗色模式是否正常？
- [ ] 照片是否仍是视觉核心？
- [ ] 整体风格是否温柔、简洁、不幼稚？

---

*本文件最后更新：Phase 0 — 项目初始化*