# ICONOGRAPHY.md — Tuantuan Journal 图标系统

> **本文件定义 Tuantuan Journal 的图标规范。**
> **图标必须简洁、圆润、线条协调、风格统一。**

---

## 1. 图标风格

- 使用 **Material Icons Outlined** 作为基础图标集
- 风格统一：Outlined 风格为主
- 线条粗细一致
- 视觉风格与 Material Icons 协调

### 禁止

- 一套页面使用线性图标，另一套使用填充图标，再一套使用 3D 图标
- 混用不同风格的图标集
- 使用过于复杂的自定义图标

---

## 2. 图标尺寸 Token

```kotlin
object TuantuanIconSize {
    val Small: Dp  = 16.dp   // 辅助图标：标签内、卡片角标
    val Medium: Dp = 24.dp   // 标准图标：导航、按钮、列表
    val Large: Dp  = 32.dp   // 强调图标：空状态、页面标题
    val XLarge: Dp = 48.dp   // 空状态插图、大图标
}
```

---

## 3. 图标颜色规则

| 场景 | 颜色 Token | 说明 |
|------|-----------|------|
| 导航（未选中） | OnSurfaceVariant | 弱化 |
| 导航（选中） | Primary | 明确但温柔 |
| 操作按钮 | Primary | 主操作 |
| 辅助操作 | OnSurfaceVariant | 次要操作 |
| 卡片内图标 | OnSurfaceVariant | 辅助信息 |
| 状态图标 | 对应功能色 | Error/Success/Warning |
| 空状态图标 | OutlineVariant | 极弱化 |

禁止使用过于鲜艳的选中色。

---

## 4. 底部导航图标

```
首页     → home_outlined
记录     → timeline_outlined
＋       → add (中心按钮，特殊处理)
成长     → child_care_outlined
我的     → person_outlined
```

### 选中/未选中规则

- 未选中：弱化（OnSurfaceVariant）
- 选中：明确但温柔（Primary）
- 切换时：轻微 icon / indicator 动画

---

## 5. 页面常用图标映射

| 功能 | 图标 | 风格 |
|------|------|------|
| 搜索 | search | Outlined |
| 筛选 | filter_list | Outlined |
| 返回 | arrow_back | Outlined |
| 关闭 | close | Outlined |
| 更多 | more_vert | Outlined |
| 编辑 | edit | Outlined |
| 删除 | delete_outline | Outlined |
| 分享 | share | Outlined |
| 照片 | photo | Outlined |
| 视频 | videocam | Outlined |
| 音频 | mic | Outlined |
| 日期 | calendar_today | Outlined |
| 标签 | label | Outlined |
| 心情 | mood | Outlined |
| 地点 | place | Outlined |
| 设置 | settings | Outlined |
| 主题 | palette | Outlined |
| 备份 | backup | Outlined |
| 提醒 | notifications | Outlined |
| 关于 | info | Outlined |

---

## 6. 自定义图标

如需自定义图标：

1. 必须先在设计系统中定义
2. 保持线条粗细与 Material Icons 一致（2dp stroke）
3. 保持视觉风格协调
4. 使用 SVG 格式
5. 提供 24dp 标准尺寸
6. 记录到 DESIGN_DECISIONS.md

---

## 7. 触控区域

图标视觉尺寸可以为 Small (16dp) 或 Medium (24dp)，但触控区域必须 ≥ 48dp。

```kotlin
// ✅ 正确：图标小但触控区域大
IconButton(onClick = { /* ... */ }) {
    Icon(
        imageVector = Icons.Outlined.Search,
        contentDescription = "搜索",
        modifier = Modifier.size(TuantuanIconSize.Medium)
    )
}
// IconButton 默认触控区域 48dp

// ❌ 错误：触控区域太小
Icon(
    imageVector = Icons.Outlined.Search,
    contentDescription = "搜索",
    modifier = Modifier
        .size(TuantuanIconSize.Medium)
        .clickable { /* ... */ }  // 触控区域只有 24dp
)
```

---

## 8. 无障碍

所有图标必须提供 `contentDescription`：

- 装饰性图标：`contentDescription = null`
- 功能性图标：提供简洁描述
- 状态图标：描述状态而非图标本身

```kotlin
// ✅ 正确
Icon(Icons.Outlined.Delete, contentDescription = "删除")

// ❌ 错误
Icon(Icons.Outlined.Delete, contentDescription = "垃圾桶图标")
```

---

*本文件版本：v1.0 | 最后更新：2026-09-27*