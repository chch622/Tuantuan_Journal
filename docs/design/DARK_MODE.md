# DARK_MODE.md — 深色模式设计

> **深色模式不是简单反色。**
> **需要重新定义 Background、Surface、Text、Divider、Accent。**

---

## 1. 深色模式原则

- 深色模式是独立主题，不是反色
- 保持语义化颜色 Token 一致
- 深色模式下照片依然是视觉核心
- 深色模式下可读性优先
- 文字对比度必须满足 WCAG AA 标准（4.5:1）

---

## 2. 深色模式色板

### 2.1 Theme 05 — 夜晚（深色模式）

| Token | Dark 值 | 说明 |
|-------|---------|------|
| Primary | #F0B4C8 | 柔和粉（略亮） |
| OnPrimary | #3D0020 | 深色文字 |
| PrimaryContainer | #5D1035 | 深粉容器 |
| OnPrimaryContainer | #FFD9E4 | 浅粉文字 |
| Secondary | #D4BFC6 | 暖灰（略亮） |
| OnSecondary | #3D0020 | 深色文字 |
| SecondaryContainer | #5D4450 | 深灰容器 |
| OnSecondaryContainer | #FFD9E4 | 浅色文字 |
| Tertiary | #C6B59A | 暖棕（略亮） |
| Background | #1A1216 | 深色背景 |
| OnBackground | #ECE0E3 | 暖白文字 |
| Surface | #221A1E | 略浅深色 |
| OnSurface | #ECE0E3 | 暖白文字 |
| SurfaceVariant | #2D2428 | 变体表面 |
| OnSurfaceVariant | #D4C3BC | 暖灰文字 |
| Outline | #84736C | 轮廓线 |
| OutlineVariant | #4D3F39 | 弱轮廓 |
| Error | #FFB4AB | 柔和红 |
| Success | #7DD992 | 柔和绿 |
| Warning | #E8B86D | 柔和橙 |
| Info | #8BB8E8 | 柔和蓝 |

---

## 3. 深色照片体验

照片必须保持真实。

- 不要因为主题切换而给照片套深色滤镜
- 照片周围使用 Surface 色，不是纯黑
- 全屏查看时背景使用纯黑（#000000）以突出照片
- 照片圆角保持一致

---

## 4. 深色模式特殊处理

### 4.1 阴影

深色模式下阴影不可见。使用 Surface 层级区分：

- Level 0: Background (#1A1216)
- Level 1: Surface (#221A1E)
- Level 2: SurfaceVariant (#2D2428)
- Level 3: 更浅的 Surface

### 4.2 分割线

深色模式下分割线使用 OutlineVariant，更弱化。

### 4.3 卡片

深色模式下卡片使用 Surface 色，与背景形成轻微对比。

---

## 5. 深色模式切换

- 跟随系统设置（默认）
- 用户可在"我的 → 主题"中手动选择
- 切换时使用 Crossfade 过渡
- 切换不重启 Activity

---

## 6. 禁止

- 禁止简单反色
- 禁止纯黑背景（#000000），使用深暖色
- 禁止深色模式下降低文字对比度
- 禁止给照片套滤镜

---

*本文件版本：v1.0 | 最后更新：2026-09-27*