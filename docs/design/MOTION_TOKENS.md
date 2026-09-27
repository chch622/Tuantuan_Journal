# MOTION_TOKENS.md — 动效 Token

> **本文件定义 Tuantuan Journal 的动画时长和缓动 Token。**
> **所有动画必须使用这些 Token，禁止随意写时长。**

---

## 1. 时长 Token

```kotlin
object TuantuanDuration {
    val Instant: Int  = 50     // 即时反馈：按钮按压、开关切换
    val Quick: Int    = 150    // 快速过渡：Tab切换、颜色变化
    val Normal: Int   = 300    // 标准过渡：页面切换、卡片出现
    val Slow: Int     = 500    // 慢速过渡：Hero Transition、复杂展开
    val Emphasis: Int = 700    // 强调动画：里程碑庆祝
}
```

### 1.1 使用规则

| 交互类型 | Token | 时长 |
|----------|-------|------|
| 按钮按压 | Instant + Quick | 50ms + 150ms |
| 开关切换 | Quick | 150ms |
| Tab 切换 | Quick | 150ms |
| 颜色变化 | Quick | 150ms |
| 页面切换 | Normal | 300ms |
| 卡片出现 | Normal | 300ms |
| 列表项交错 | Normal + 50ms stagger | 300ms + 50ms×n |
| BottomSheet | Normal | 300ms |
| Dialog | Quick | 150ms |
| Hero Transition | Slow | 500ms |
| 里程碑庆祝 | Emphasis | 700ms |
| 生日庆祝 | Emphasis - 2000ms | 700ms - 2000ms |

### 1.2 禁止

- 禁止超过 700ms 的常规动画
- 庆祝类动画不超过 2 秒
- 禁止使用 300ms、500ms 以外的随意值（如 427ms、730ms）

---

## 2. 缓动 Token

```kotlin
object TuantuanEasing {
    // 标准缓动 — 常规过渡
    val Standard = CubicBezierEasing(0.2f, 0f, 0f, 1f)

    // 减速缓动 — 进入屏幕
    val Decelerate = CubicBezierEasing(0f, 0f, 0f, 1f)

    // 加速缓动 — 离开屏幕
    val Accelerate = CubicBezierEasing(0.3f, 0f, 1f, 1f)

    // 线性 — 进度条、旋转
    val Linear = CubicBezierEasing(1f, 1f, 1f, 1f)

    // Spring — 自然弹性
    val Spring = spring<Float>(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessMedium
    )
}
```

### 2.1 使用规则

| 场景 | 缓动 | 说明 |
|------|------|------|
| 进入动画 | Decelerate | 元素进入屏幕 |
| 退出动画 | Accelerate | 元素离开屏幕 |
| 常规过渡 | Standard | 大多数动画 |
| 进度条 | Linear | 匀速 |
| BottomSheet | Spring | 自然弹性 |
| 卡片交互 | Spring | 柔和反馈 |
| 按钮按压 | Standard | 快速过渡 |

### 2.2 原则

> 进入和退出不应该完全一样。

- 进入：Decelerate（减速，感觉从远处来）
- 退出：Accelerate（加速，感觉往远处去）

### 2.3 禁止

- 禁止随意自定义缓动曲线
- 禁止所有动画使用相同缓动

---

## 3. 交错动画

### 3.1 列表项交错

```
每项延迟：50ms
最大交错项数：8
超过 8 项：直接显示，不再动画
```

### 3.2 禁止

- 不要一次给所有项目添加巨大 stagger 动画
- 大量内容：静态显示 + 轻量首屏动画

---

## 4. 动画组合

### 4.1 常用组合

| 动画 | 组合 | 时长 | 缓动 |
|------|------|------|------|
| 页面进入 | FadeIn + SlideIn | Normal | Decelerate |
| 页面退出 | FadeOut + SlideOut | Normal | Accelerate |
| 卡片出现 | FadeIn + SlideUp(20dp) | Normal | Decelerate |
| 卡片删除 | FadeOut + ScaleDown(0.9x) | Normal | Accelerate |
| Dialog | FadeIn + ScaleUp(0.9→1.0) | Quick | Decelerate |
| BottomSheet | SlideUp + Spring | Normal | Spring |
| 按钮按压 | ScaleDown(0.95x) + ScaleUp(1.0x) | Instant+Quick | Standard |
| 照片全屏 | SharedElement + FadeIn | Slow | Standard |

### 4.2 禁止组合

- 不要同时使用超过 3 个动画属性
- 不要在同一个元素上同时使用位移+缩放+旋转

---

*本文件版本：v1.0 | 最后更新：2026-09-27*