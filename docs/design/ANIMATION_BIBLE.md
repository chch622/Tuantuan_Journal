# ANIMATION_BIBLE.md — Tuantuan Journal 动画圣经

> **本文件是 Tuantuan Journal 动画系统的完整规范。**
> **动画必须服务于理解，不能只是为了"看起来高级"。**
> **本文件是 ANIMATION_GUIDELINES.md 的完整扩展。**

---

## 1. 动画核心原则

### 1.1 动画必须帮助用户理解

- 页面之间的层级关系
- 内容之间的关联
- 状态的变化
- 操作的结果

### 1.2 动画风格

**关键词：** 流畅、自然、轻盈、有目的

**不是：** 炫技、花哨、夸张、无意义

### 1.3 动画服务于理解

动画不能只是为了"看起来高级"。每个动画必须回答：

- 这个动画帮助用户理解什么？
- 没有这个动画会怎样？
- 这个动画会不会让人等太久？

---

## 2. 动画时长 Token

详见 → [MOTION_TOKENS.md](MOTION_TOKENS.md)

```kotlin
object TuantuanDuration {
    val Instant: Int  = 50     // 即时反馈：按钮按压
    val Quick: Int    = 150    // 快速过渡：开关切换
    val Normal: Int   = 300    // 标准过渡：页面切换
    val Slow: Int     = 500    // 慢速过渡：复杂展开
    val Emphasis: Int = 700    // 强调动画：里程碑庆祝
}
```

禁止每个动画随便写一个时长。必须从 Token 中选择。

---

## 3. 缓动曲线 Token

详见 → [MOTION_TOKENS.md](MOTION_TOKENS.md)

```kotlin
object TuantuanEasing {
    val Standard   = CubicBezierEasing(0.2f, 0f, 0f, 1f)
    val Decelerate = CubicBezierEasing(0f, 0f, 0f, 1f)    // 进入屏幕
    val Accelerate = CubicBezierEasing(0.3f, 0f, 1f, 1f)  // 离开屏幕
    val Linear     = CubicBezierEasing(1f, 1f, 1f, 1f)    // 进度条
    val Spring     = spring<Float>(DampingRatioMediumBouncy, StiffnessMedium)
}
```

原则：进入和退出不应该完全一样。

---

## 4. 页面切换动画

### 4.1 前进导航

```
新页面：从右侧淡入 + 轻微位移（30dp 以内）
当前页面：轻微向左位移 + 淡出
```

- 时长：Normal (300ms)
- 缓动：Decelerate（进入）/ Accelerate（退出）

### 4.2 返回导航

```
当前页面：向右位移 + 淡出
前一页面：淡入 + 轻微位移恢复
```

- 时长：Normal (300ms)

### 4.3 底部 Tab 切换

```
新内容：淡入
旧内容：淡出
```

- 时长：Quick (150ms)
- 无位移，仅淡入淡出
- 保持 Tab 栏不动

### 4.4 原则

页面切换应该体现：用户从一个"空间"进入另一个空间。

而不是：突然切换整个画面。

---

## 5. 共享元素过渡

重点用于：

- 首页照片 → 照片详情
- 时间轴照片 → 全屏

尽可能建立视觉连续性。

```kotlin
SharedTransitionLayout {
    // 照片缩略图 → 全屏
    // 时长：Slow (500ms)
    // 缓动：Standard
}
```

---

## 6. 列表动画

### 6.1 列表项出现

```
位移（从下方 20dp）+ 淡入
```

- 时长：Normal (300ms)
- 缓动：Decelerate
- 交错延迟：每项 50ms，最多 8 项交错
- 超过 8 项直接显示，不再动画

### 6.2 列表项删除

```
淡出 + 高度收缩
```

- 时长：Normal (300ms)
- 缓动：Accelerate
- 删除后相邻项平滑移动填充空位

---

## 7. 照片动画

### 7.1 照片加载

```
占位色 → 缩略图（Crossfade） → 清晰图（Crossfade）
```

避免突然闪白。

### 7.2 照片打开

```
Hero Transition：从缩略图位置放大到全屏
```

- 时长：Slow (500ms)
- 缓动：Standard

### 7.3 照片关闭

```
从全屏缩小回缩略图位置
```

- 时长：Normal (300ms)
- 缓动：Standard

---

## 8. Bottom Sheet 动画

```
从底部滑入 + Spring 缓动
```

- 时长：Normal (300ms) + Spring
- 遮罩淡入：Quick (150ms)
- 下拉关闭跟随手势

---

## 9. Dialog 动画

```
淡入 + 轻微缩放（0.9x → 1.0x）
```

- 时长：Quick (150ms)
- 缓动：Decelerate
- 遮罩淡入：Quick (150ms)

---

## 10. 生日动画

允许 1-2 秒短暂庆祝。

- 花瓣、星星、柔和闪光
- 粒子数量 ≤ 20
- 不使用 Blur
- 动画结束后完全停止
- 尊重系统"减少动画"设置

---

## 11. 成长里程碑动画

可以：星星、花瓣、柔和闪光。

不能：游戏胜利式大型特效。

- 时长：Emphasis (700ms) - 2000ms
- 粒子数量 ≤ 20
- 不使用 Blur

---

## 12. Spring 动画

适用于：

- Bottom Sheet
- 卡片交互
- 小型位置变化
- 柔和反馈

不要用于所有动画。

---

## 13. 滚动动画

不要因为滚动距离导致 UI 过度缩放。

优先：轻微透明、偏移、尺寸变化。

禁止：大幅缩放、视差过度。

---

## 14. 减少动画模式

详见 → [ACCESSIBILITY.md](ACCESSIBILITY.md)

如果系统启用了 Reduce Motion：

- 减少位移
- 减少缩放
- 减少大型转场
- 减少粒子
- 保留必要状态反馈
- 使用 Instant (50ms) 或直接切换

---

## 15. 动画性能约束

详见 → [PERFORMANCE_UI.md](PERFORMANCE_UI.md)

禁止在动画过程中：

- 执行重型数据库操作
- 同步大文件 IO
- 大量图片实时解码
- 无意义的 Compose recomposition
- 阻塞 UI 主线程

---

## 16. 动画检查清单

- [ ] 动画是否有明确目的？
- [ ] 时长是否在标准范围内？
- [ ] 缓动是否使用标准曲线？
- [ ] 是否尊重"减少动画"设置？
- [ ] 动画期间是否有重型操作？
- [ ] 动画是否流畅（60fps）？
- [ ] 动画是否影响数据安全？
- [ ] 整体感觉是否温柔、轻盈？

---

*本文件版本：v1.0 | 最后更新：2026-09-27*