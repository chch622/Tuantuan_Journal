# THEMES.md — 主题系统

> **主题变化只能影响颜色和装饰，不能改变信息结构和交互逻辑。**
> **无论用户选择哪个主题，仍然必须一眼看出：这是 Tuantuan Journal。**

---

## 1. 主题系统原则

- 主题只改变视觉感受，不改变功能
- 所有主题共享同一套组件、布局、动画
- 主题切换不需要重启 App
- 默认跟随系统设置

---

## 2. 主题列表

### Theme 01 — 樱花（默认）

- 关键词：Soft Pink / Warm Cream
- Primary: #E8A0B4（柔和粉）
- Background: #FFFBF8（奶油白）
- 情感：温柔、可爱

### Theme 02 — 奶油

- 关键词：Warm Beige / Ivory
- Primary: #C9A96E（暖金）
- Background: #FFF8EE（暖米色）
- 情感：温暖、中性

### Theme 03 — 薄荷

- 关键词：Soft Green / Warm White
- Primary: #7EBF8E（柔和绿）
- Background: #F8FBF5（暖白）
- 情感：清新、自然

### Theme 04 — 天空

- 关键词：Soft Blue / White
- Primary: #7EB3D4（柔和蓝）
- Background: #F8FAFB（白色）
- 情感：宁静、开阔

### Theme 05 — 夜晚

- 关键词：Deep Blue / Soft Lavender / Warm White Text
- Primary: #F0B4C8（亮粉）
- Background: #1A1216（深色）
- 情感：安静、沉浸

---

## 3. 主题影响范围

### 3.1 可以改变

- Color（所有语义色 Token）
- Surface 色调
- Icon Accent 色
- Illustration Accent 色
- Selected State 色
- 装饰性元素色

### 3.2 不能改变

- 信息结构
- 核心导航
- 数据逻辑
- 交互逻辑
- 组件布局
- 动画时长
- 间距 Token
- 圆角 Token
- 字体 Token

---

## 4. 主题实现

### 4.1 技术方案

使用 Material 3 动态颜色系统：

```kotlin
// 每个主题定义一组 Color Scheme
// Light 和 Dark 分别定义
// 通过 TuantuanTheme 切换

@Composable
fun TuantuanTheme(
    theme: TuantuanTheme = TuantuanTheme.Sakura,
    darkMode: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkMode -> theme.darkColorScheme
        else -> theme.lightColorScheme
    }
    
    MaterialTheme(
        colorScheme = colorScheme,
        typography = TuantuanTypography,
        content = content
    )
}
```

### 4.2 主题枚举

```kotlin
enum class TuantuanTheme {
    SAKURA,    // 樱花
    CREAM,     // 奶油
    MINT,      // 薄荷
    SKY,       // 天空
    NIGHT      // 夜晚
}
```

---

## 5. 主题选择 UI

在"我的 → 主题"中：

- 显示 5 个主题色块预览
- 选中标记 Primary 色
- 切换后即时预览
- 支持"跟随系统"选项

---

## 6. 主题一致性

无论用户选择哪个主题，仍然必须一眼看出：这是 Tuantuan Journal。

- 圆角系统不变
- 间距系统不变
- 字体系统不变
- 组件结构不变
- 动画系统不变
- 只有颜色在变

---

## 7. 禁止

- 禁止主题改变布局
- 禁止主题改变交互
- 禁止主题改变组件结构
- 禁止不同主题使用不同的圆角/间距
- 禁止创建过于极端的主题（纯黑、纯白、高饱和度）

---

*本文件版本：v1.0 | 最后更新：2026-09-27*