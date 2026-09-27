# ACCESSIBILITY.md — Tuantuan Journal 无障碍规范

> **本文件定义 Tuantuan Journal 的无障碍标准。**
> **应用面向家庭使用，必须考虑不同年龄和能力的用户。**

---

## 1. 核心原则

### 1.1 感知性

- 信息可被所有用户感知
- 不依赖单一感官（颜色、声音）
- 文本对比度符合 WCAG AA

### 1.2 可操作性

- 所有功能可通过触摸操作
- 触摸目标足够大
- 提供足够操作时间
- 避免闪烁内容

### 1.3 可理解性

- 文本清晰易读
- 操作可预测
- 错误提示可理解
- 导航一致

### 1.4 健壮性

- 兼容辅助技术
- 语义化 UI 结构
- 正确的内容描述

---

## 2. 触摸目标

### 2.1 最小尺寸

- 最小触摸目标：48×48 dp
- 推荐触摸目标：56×56 dp
- 触摸目标间距：≥ 8 dp

### 2.2 列表项

- 列表项最小高度：64 dp
- 可点击区域包含整个列表项
- 图标和文字一起可点击

---

## 3. 文本和排版

### 3.1 字号

| 用途 | 最小字号 | 推荐字号 |
|------|---------|---------|
| 正文 | 14sp | 16sp |
| 标题 | 18sp | 20sp |
| 大标题 | 22sp | 24sp |
| 辅助文字 | 12sp | 12sp |

### 3.2 对比度

| 元素 | 最小对比度 | 标准 |
|------|----------|------|
| 正文文字 | 4.5:1 | WCAG AA |
| 大文字 (≥18sp bold) | 3:1 | WCAG AA |
| 图标 | 3:1 | WCAG AA |
| 装饰元素 | 无要求 | - |

### 3.3 行高

- 正文字体行高：1.5 倍
- 标题字体行高：1.3 倍
- 段落间距：0.5 倍行高

---

## 4. 颜色

### 4.1 不依赖颜色

- 错误状态不仅用红色，还用图标
- 成功状态不仅用绿色，还用图标
- 图表使用形状区分，不仅用颜色
- 链接使用下划线，不仅用颜色

### 4.2 色盲友好

- 避免红绿对比
- 使用蓝橙对比
- 图表使用纹理/形状区分
- 验证颜色在色盲模拟下的可辨识度

---

## 5. 内容描述

### 5.1 Compose 语义

```kotlin
// 所有可点击元素必须有 contentDescription
IconButton(
    onClick = onDelete,
    modifier = Modifier.semantics {
        contentDescription = "删除日记"
    }
) {
    Icon(Icons.Outlined.Delete, contentDescription = null)
}

// 图片必须有内容描述
AsyncImage(
    model = photoUrl,
    contentDescription = "团团在公园玩耍的照片",
    ...
)

// 装饰性图片使用 null
Image(
    painter = painterResource(R.drawable.decoration),
    contentDescription = null  // 纯装饰
)
```

### 5.2 语义树

```kotlin
// 合并语义组
Row(modifier = Modifier.semantics(mergeDescendants = true) {
    contentDescription = "日记：${entry.title}，${entry.date}"
}) {
    Text(entry.title)
    Text(entry.date)
}
```

### 5.3 状态描述

```kotlin
// 切换状态
Switch(
    checked = isEnabled,
    onCheckedChange = onToggle,
    modifier = Modifier.semantics {
        stateDescription = if (isEnabled) "已开启" else "已关闭"
    }
)

// 选中状态
FilterChip(
    selected = isSelected,
    onClick = onClick,
    modifier = Modifier.semantics {
        stateDescription = if (isSelected) "已选中" else "未选中"
    }
) {
    Text(tag.name)
}
```

---

## 6. 导航

### 6.1 TalkBack 导航

- 所有可交互元素可聚焦
- 聚焦顺序符合逻辑
- 聚焦状态清晰可见
- 返回导航一致

### 6.2 键盘导航（如适用）

- Tab 顺序符合逻辑
- 焦点可见
- 快捷键支持

---

## 7. 动画

### 7.1 减少动画

```kotlin
// 检查用户偏好
val reduceMotion by settingsRepository.reduceMotion.collectAsState()

// 根据偏好调整动画
val animationSpec = if (reduceMotion) {
    snap()  // 无动画
} else {
    tween<Float>(durationMillis = 300)
}
```

### 7.2 动画约束

- 不使用闪烁动画（> 3Hz）
- 过渡动画可跳过
- 尊重系统动画设置
- 详见 [ANIMATION_GUIDELINES.md](ANIMATION_GUIDELINES.md)

---

## 8. 测试

### 8.1 自动化测试

- 使用 Accessibility Testing Framework
- 使用 Compose 测试验证语义
- CI 中集成无障碍测试

### 8.2 手动测试

- 开启 TalkBack 测试导航
- 开启放大镜测试布局
- 开启高对比度测试颜色
- 使用色盲模拟器测试

### 8.3 测试清单

- [ ] 所有图片有 contentDescription
- [ ] 触摸目标 ≥ 48dp
- [ ] 文本对比度 ≥ 4.5:1
- [ ] 颜色不是唯一信息来源
- [ ] TalkBack 可导航所有功能
- [ ] 动画尊重 reduceMotion
- [ ] 错误信息可被辅助技术读取
- [ ] 表单有标签和错误提示

---

*本文件最后更新：Phase 0 — 项目初始化*