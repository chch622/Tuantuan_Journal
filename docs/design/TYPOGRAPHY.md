# TYPOGRAPHY.md — Tuantuan Journal 字体规范

> **本文件定义 Tuantuan Journal 的完整 Typography 系统。**
> **禁止页面自行随意定义字体大小。新增排版样式必须先在此定义。**

---

## 1. 字体设计原则

- 默认优先使用 Android / 系统可靠字体
- 禁止为了"可爱"加入大量艺术字体
- 中文必须保证：清晰、稳定、长文本舒适、数字清晰
- 字体层级必须清晰，帮助用户理解信息结构

---

## 2. Typography Scale

```kotlin
object TuantuanTypography {
    // 展示 — 首页大标题、年度回顾
    val DisplayLarge: TextStyle   // 57sp / Normal
    val DisplayMedium: TextStyle  // 45sp / Normal
    val DisplaySmall: TextStyle   // 36sp / Normal

    // 标题 — 页面标题
    val HeadlineLarge: TextStyle  // 32sp / Normal
    val HeadlineMedium: TextStyle // 28sp / Normal
    val HeadlineSmall: TextStyle  // 24sp / Normal

    // 标题 — 区块标题
    val TitleLarge: TextStyle     // 22sp / Medium
    val TitleMedium: TextStyle    // 16sp / Medium
    val TitleSmall: TextStyle     // 14sp / Medium

    // 正文 — 内容文字
    val BodyLarge: TextStyle      // 16sp / Normal
    val BodyMedium: TextStyle     // 14sp / Normal
    val BodySmall: TextStyle      // 12sp / Normal

    // 标签 — 按钮文字、Tab
    val LabelLarge: TextStyle     // 14sp / Medium
    val LabelMedium: TextStyle    // 12sp / Medium
    val LabelSmall: TextStyle     // 11sp / Medium

    // 说明 — 辅助说明
    val Caption: TextStyle        // 12sp / Normal
}
```

---

## 3. 字体使用规则

| 用途 | Token | 说明 |
|------|-------|------|
| 首页大标题 | DisplayLarge/Medium | 极少使用，仅首页和年度回顾 |
| 页面标题 | HeadlineMedium/Large | 每个页面顶部 |
| 区块标题 | TitleMedium/Large | 内容区块标题 |
| 正文内容 | BodyLarge/Medium | 日记正文、描述文字 |
| 辅助说明 | BodySmall/Caption | 日期、标签、提示 |
| 按钮文字 | LabelLarge | 所有按钮 |
| Tab 文字 | LabelMedium | 底部导航、Tab 标签 |

---

## 4. 数字显示原则

以下数字需要较好的视觉层级：

- 年龄（X岁X个月X天）
- 日期
- 身高、体重
- 天数
- 倒计时

**数字视觉重量应高于说明文字。**

```kotlin
// 年龄显示示例
// "1" → DisplayMedium, Primary
// "岁" → BodyMedium, OnSurfaceVariant
// "8" → DisplayMedium, Primary
// "个月" → BodyMedium, OnSurfaceVariant
// "12" → DisplayMedium, Primary
// "天" → BodyMedium, OnSurfaceVariant
```

---

## 5. 行高规则

| 类别 | 行高倍数 | 说明 |
|------|----------|------|
| Display | 1.2 | 标题紧凑 |
| Headline | 1.3 | 标题舒适 |
| Title | 1.4 | 区块标题 |
| Body | 1.5 | 正文阅读舒适 |
| Label | 1.3 | 标签紧凑 |
| Caption | 1.4 | 说明文字 |

---

## 6. 字重使用

| 字重 | 用途 | 说明 |
|------|------|------|
| Normal (400) | 正文、说明 | 默认字重 |
| Medium (500) | 标题、标签 | 强调层级 |
| Bold (700) | 极少使用 | 仅数字强调、极重要标题 |

禁止大面积使用 Bold。标题层级通过字号和颜色区分，而非仅靠字重。

---

## 7. 中文字体考虑

- 默认使用系统中文字体（Noto Sans CJK / 系统默认）
- 中文正文 14sp 以上保证清晰
- 中文标题避免过小字号（≥ 16sp）
- 长中文段落使用 BodyLarge (16sp) 保证舒适
- 数字和中文混排时注意基线对齐

---

## 8. 字体放大适配

系统字体变大时：

- 不能出现文本截断
- 不能出现按钮文字消失
- 不能出现卡片高度固定导致溢出
- 必须使用可扩展布局
- 文字容器使用 `wrapContentHeight`
- 最大字号限制在 DisplayLarge 的 1.5 倍以内

---

## 9. 禁止事项

- 禁止页面自行随意定义字体大小
- 禁止使用未在 Token 中定义的字号
- 禁止为了"可爱"使用艺术字体
- 禁止大面积使用 Bold 字重
- 新增排版样式必须先在此定义

---

*本文件版本：v1.0 | 最后更新：2026-09-27*