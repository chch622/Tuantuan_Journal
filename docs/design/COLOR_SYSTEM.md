# COLOR_SYSTEM.md — Tuantuan Journal 颜色系统

> **本文件定义 Tuantuan Journal 的完整语义色系统。**
> **所有颜色必须通过语义 Token 引用，禁止硬编码颜色值。**

---

## 1. 主色调方向

| 色彩 | 角色 | 情感 |
|------|------|------|
| 奶油白 | 页面基础背景 | 干净、温暖 |
| 柔和粉 | 主品牌强调 | 温柔、可爱 |
| 暖灰 | 次级文本和边界 | 沉稳、舒适 |
| 柔和辅助色 | 成长/生日/特殊事件/状态 | 活泼、愉悦 |

---

## 2. 颜色比例规则

```
70%  中性背景（奶油白、暖灰）
20%  Surface / Card / Secondary
10%  主强调色（柔和粉）
```

强调色绝对不能铺满屏幕。禁止每个按钮都有鲜艳颜色。

---

## 3. 语义色 Token

### 3.1 品牌色

```kotlin
object TuantuanColors {
    // 品牌色 — 柔和粉
    val Primary: Color            // 主品牌色
    val OnPrimary: Color          // 主品牌色上的文字/图标
    val PrimaryContainer: Color   // 主品牌色容器（浅色调）
    val OnPrimaryContainer: Color // 主品牌色容器上的文字
}
```

### 3.2 辅助色

```kotlin
    // 辅助色 — 暖灰调
    val Secondary: Color
    val OnSecondary: Color
    val SecondaryContainer: Color
    val OnSecondaryContainer: Color
```

### 3.3 第三色

```kotlin
    // 第三色 — 柔和辅助
    val Tertiary: Color
    val OnTertiary: Color
    val TertiaryContainer: Color
    val OnTertiaryContainer: Color
```

### 3.4 背景色

```kotlin
    // 背景色 — 奶油白
    val Background: Color       // 页面背景
    val OnBackground: Color     // 背景上的文字
```

### 3.5 表面色

```kotlin
    // 表面色
    val Surface: Color          // 卡片/组件表面
    val OnSurface: Color        // 表面上的文字
    val SurfaceVariant: Color   // 变体表面
    val OnSurfaceVariant: Color // 变体表面上的文字
```

### 3.6 轮廓

```kotlin
    // 轮廓
    val Outline: Color          // 边框、分割线
    val OutlineVariant: Color   // 弱化边框
```

### 3.7 功能色

```kotlin
    // 功能色
    val Error: Color            // 错误 — 明确但不刺眼
    val OnError: Color          // 错误色上的文字
    val Success: Color          // 成功 — 轻微反馈
    val Warning: Color          // 警告 — 柔和提醒
    val Info: Color             // 信息 — 中性提示
```

### 3.8 反转与遮罩

```kotlin
    // 反转
    val InverseSurface: Color
    val InverseOnSurface: Color
    val Scrim: Color            // 遮罩层
}
```

---

## 4. 主题色板定义

### 4.1 Theme 01 — 樱花（默认主题）

| Token | Light 值 | 说明 |
|-------|----------|------|
| Primary | #E8A0B4 | 柔和粉 |
| OnPrimary | #FFFFFF | 白色文字 |
| PrimaryContainer | #FFD9E4 | 浅粉容器 |
| OnPrimaryContainer | #3D0020 | 深粉文字 |
| Secondary | #7D5F6A | 暖灰棕 |
| OnSecondary | #FFFFFF | 白色文字 |
| SecondaryContainer | #FFD9E4 | 浅粉容器 |
| OnSecondaryContainer | #3D0020 | 深色文字 |
| Tertiary | #8B7A5E | 暖棕 |
| Background | #FFFBF8 | 奶油白 |
| OnBackground | #1F1A1C | 深色文字 |
| Surface | #FFFBF8 | 奶油白表面 |
| OnSurface | #1F1A1C | 深色文字 |
| SurfaceVariant | #F4EDE8 | 暖灰表面 |
| OnSurfaceVariant | #52443C | 暖灰文字 |
| Outline | #84736C | 轮廓线 |
| OutlineVariant | #D4C3BC | 弱轮廓 |
| Error | #BA1A1A | 错误红 |
| Success | #4A8C5C | 成功绿 |
| Warning | #C4862A | 警告橙 |
| Info | #5B7DB1 | 信息蓝 |

---

## 5. 颜色使用规则

### 5.1 页面级

- 页面背景使用 `Background`
- 页面主文字使用 `OnBackground`
- 页面次文字使用 `OnSurfaceVariant`

### 5.2 卡片级

- 卡片背景使用 `Surface`
- 卡片主文字使用 `OnSurface`
- 卡片次文字使用 `OnSurfaceVariant`
- 卡片边框使用 `OutlineVariant`

### 5.3 交互级

- 主要操作使用 `Primary`
- 次要操作使用 `Secondary` 或 `Outlined`
- 交互反馈使用 `PrimaryContainer`

### 5.4 状态级

- 错误使用 `Error`（明确但不刺眼）
- 成功使用 `Success`（轻微反馈即可）
- 警告使用 `Warning`（柔和提醒）
- 信息使用 `Info`（中性提示）

---

## 6. 颜色的情绪原则

| 场景 | 情绪 | 色彩倾向 |
|------|------|----------|
| 普通记录 | 柔和 | 中性色为主，Primary 仅点缀 |
| 成长数据 | 温暖 | 暖色调，Tertiary 辅助 |
| 生日 | 稍微活泼 | 允许 PrimaryContainer 扩大使用 |
| 第一次 | 收藏感 | 温暖 + 轻微强调 |
| 错误 | 明确但不刺眼 | Error 色柔和处理 |
| 成功 | 轻微反馈 | Success 色克制使用 |

不要让错误颜色破坏整体温柔氛围。

---

## 7. 禁止事项

- 禁止在页面中硬编码颜色值
- 禁止每个页面使用不同的颜色体系
- 禁止大面积使用鲜艳强调色
- 禁止使用未在 Token 中定义的颜色
- 新增颜色必须先在此定义语义 Token

---

## 8. 深色模式

详见 → [DARK_MODE.md](DARK_MODE.md)

深色模式不是简单反色。需重新定义：

- Background → 深色调
- Surface → 略浅深色
- Text → 暖白色
- Divider → 微弱暖灰
- Accent → 柔和但可识别

照片必须保持真实，不因主题切换套深色滤镜。

---

## 9. 主题系统

详见 → [THEMES.md](THEMES.md)

主题变化只能影响：

- Color
- Surface
- Icon Accent
- Illustration Accent
- Selected State
- Some Decorative Elements

不得改变：

- 信息结构
- 核心导航
- 数据逻辑
- 交互逻辑

无论用户选择哪个主题，仍然必须一眼看出：这是 Tuantuan Journal。

---

*本文件版本：v1.0 | 最后更新：2026-09-27*