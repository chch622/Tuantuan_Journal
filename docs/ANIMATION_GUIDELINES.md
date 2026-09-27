# ANIMATION_GUIDELINES.md — Tuantuan Journal 动画规范

> **本文件定义 Tuantuan Journal 的动画原则和规范。**
> **所有动画实现必须遵循本规范，禁止无目的动画。**

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

---

## 2. 动画时长标准

```kotlin
object TuantuanDuration {
    val Instant: Int = 50     // 即时反馈：按钮按压
    val Quick: Int = 150      // 快速过渡：开关切换
    val Normal: Int = 300     // 标准过渡：页面切换
    val Slow: Int = 500       // 慢速过渡：复杂展开
    val Emphasis: Int = 700   // 强调动画：里程碑庆祝
}
```

### 使用规则

- 大多数交互反馈：Quick (150ms)
- 页面切换和内容过渡：Normal (300ms)
- 复杂的展开/收起：Slow (500ms)
- 禁止超过 700ms 的常规动画
- 庆祝类动画不超过 2 秒

---

## 3. 缓动曲线标准

```kotlin
object TuantuanEasing {
    // 标准缓动
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

### 使用规则

- 进入动画：Decelerate
- 退出动画：Accelerate
- 常规过渡：Standard
- BottomSheet、拖拽：Spring
- 禁止随意自定义缓动曲线

---

## 4. 页面切换动画

### 4.1 前进导航

```
新页面：从右侧淡入 + 轻微位移
当前页面：轻微向左位移 + 淡出
```

- 时长：Normal (300ms)
- 缓动：Decelerate（进入）/ Accelerate（退出）
- 位移距离：30dp 以内

### 4.2 返回导航

```
当前页面：向右位移 + 淡出
前一页面：淡入 + 轻微位移恢复
```

- 时长：Normal (300ms)
- 缓动：与前进相反

### 4.3 底部 Tab 切换

```
新内容：淡入
旧内容：淡出
```

- 时长：Quick (150ms)
- 无位移，仅淡入淡出
- 保持 Tab 栏不动

---

## 5. 卡片出现动画

### 5.1 列表项出现

```
位移（从下方 20dp）+ 淡入
```

- 时长：Normal (300ms)
- 缓动：Decelerate
- 交错延迟：每项 50ms，最多 8 项交错
- 超过 8 项直接显示，不再动画

### 5.2 卡片删除

```
高度收缩 + 淡出
```

- 时长：Normal (300ms)
- 缓动：Accelerate
- 删除后相邻项平滑移动填充空位

---

## 6. 图片查看动画

### 6.1 图片打开

```
Hero Transition：从缩略图位置放大到全屏
```

- 时长：Slow (500ms)
- 缓动：Standard
- 共享元素过渡

### 6.2 图片关闭

```
从全屏缩小回缩略图位置
```

- 时长：Normal (300ms)
- 缓动：Standard

### 6.3 图片缩放

```
手势跟随缩放
```

- 使用 TransformableState
- 无固定时长，跟随手指
- 松手后弹性回弹或保持

---

## 7. Bottom Sheet 动画

```
从底部滑入 + Spring 缓动
```

- 时长：Normal (300ms) + Spring
- 缓动：Spring (dampingRatio = MediumBouncy)
- 遮罩淡入：Quick (150ms)
- 下拉关闭跟随手势

---

## 8. 删除动画

### 8.1 原则

- 不要使用夸张动画
- 不要让删除看起来"有趣"
- 表达"正在移除"即可

### 8.2 实现

```
淡出 + 轻微缩小
```

- 时长：Normal (300ms)
- 缓动：Accelerate
- 配合确认对话框，不直接删除

---

## 9. 成长里程碑动画

### 9.1 允许的庆祝效果

- 轻微的星星闪烁
- 花瓣飘落
- 简单的庆祝图标

### 9.2 约束

- 持续时间不超过 2 秒
- 粒子数量不超过 20 个
- 不使用 Blur 效果
- 不使用大量透明层
- 动画结束后完全释放资源
- 尊重系统"减少动画"设置

---

## 10. 状态切换动画

### 10.1 加载状态

```
淡入 Skeleton 或 ProgressIndicator
```

- 时长：Quick (150ms)

### 10.2 加载完成

```
Skeleton 淡出 + 内容淡入
```

- 时长：Normal (300ms)

### 10.3 错误状态

```
内容淡出 + ErrorState 淡入
```

- 时长：Normal (300ms)

### 10.4 空状态

```
EmptyState 淡入
```

- 时长：Normal (300ms)

---

## 11. 微交互

### 11.1 按钮按压

```
轻微缩小 (0.95x) + 恢复
```

- 时长：Instant (50ms) + Quick (150ms)

### 11.2 开关切换

```
滑块滑动 + 颜色过渡
```

- 时长：Quick (150ms)

### 11.3 收藏/标记

```
图标缩放弹跳 + 颜色变化
```

- 时长：Quick (150ms)
- Spring 缓动

---

## 12. 性能约束

### 禁止在动画过程中：

- 执行重型数据库操作
- 同步大文件 IO
- 大量图片实时解码
- 无意义的 Compose recomposition
- 阻塞 UI 主线程

### 必须遵守：

- 动画前预加载所需资源
- 动画期间避免新的数据请求
- 使用 `remember` 和 `derivedStateOf` 减少重组
- 图片使用缩略图，不在动画中加载原图
- 尊重系统"减少动画"无障碍设置

```kotlin
// 检查系统动画设置
val density = LocalDensity.current
val areAnimationsEnabled = !LocalAccessibilityManager.current.areAnimationsDisabled()
```

---

## 13. 动画检查清单

每次添加动画后，检查：

- [ ] 动画是否有明确目的？
- [ ] 时长是否在标准范围内？
- [ ] 缓动是否使用标准曲线？
- [ ] 是否尊重"减少动画"设置？
- [ ] 动画期间是否有重型操作？
- [ ] 动画是否流畅（60fps）？
- [ ] 动画是否影响数据安全（如动画中删除）？
- [ ] 整体感觉是否温柔、轻盈？

---

*本文件最后更新：Phase 0 — 项目初始化*