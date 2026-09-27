# DESIGN_BIBLE.md — Tuantuan Journal 设计圣经

> **本文件是 Tuantuan Journal UI/UX 与 Animation Design System 的总入口。**
> **所有 UI 开发必须以本文件为最高设计依据之一。**
> **优先级：AGENTS.md → PRODUCT_SPEC.md → DESIGN_BIBLE.md → 专项 Design Docs → 页面设计 → 代码实现**

---

## 1. 产品视觉目标

> **温柔、简洁、可爱但不幼稚。**

Tuantuan Journal 的视觉和交互必须围绕产品精神：

> 这是一个保存团团成长过程和家庭回忆的地方。

它不是普通工具型 App。不是儿童游戏。不是社交平台。不是复杂的数据分析软件。

用户应该感受到：

- 温柔
- 亲切
- 干净
- 柔和
- 安静
- 有生活气息
- 有回忆感
- 有成长感
- 有一点点可爱的情绪
- 但绝对不能幼稚

---

## 2. 设计风格定位

### Soft Emotional Minimalism

中文：**温柔情感极简主义**

关键词：

```
Soft        Warm        Calm
Clean       Friendly    Rounded
Airy        Emotional   Photographic
Natural
```

禁止：

```
Overly childish      Overly cartoonish
Overly colorful      Overly decorative
Overly futuristic    Overly gamified
Overly corporate
```

---

## 3. 视觉核心

> **让用户注意力自然落在团团的内容上。**

照片、故事、声音、成长事件——这些才是真正重要的。

UI 只是承载内容。

---

## 4. 设计层级

```
Content           ← 最高优先级
↓
Primary Action    ← 主要操作
↓
Secondary Action  ← 次要操作
↓
Navigation        ← 导航
↓
Decoration        ← 装饰（永远不能压过内容）
```

---

## 5. 设计核心理念

### 5.1 少即是多

不要为了"功能丰富"把页面塞满。

### 5.2 内容优先

真正重要的是团团的照片、故事、声音、成长事件。UI 只是承载内容。

### 5.3 情绪优先于炫技

设计应该有情感，但不能煽情。

### 5.4 动画服务于理解

动画不能只是为了"看起来高级"。

### 5.5 所有页面必须属于同一个产品

不同页面可以有自己的内容重点，但不允许出现像五个不同 App 拼起来的感觉。

---

## 6. 颜色比例规则

```
70%  中性背景（奶油白、暖灰）
20%  Surface / Card / Secondary
10%  主强调色（柔和粉）
```

强调色绝对不能铺满屏幕。禁止每个按钮都有鲜艳颜色。

详见 → [COLOR_SYSTEM.md](COLOR_SYSTEM.md)

---

## 7. 颜色的情绪原则

| 场景 | 情绪 | 色彩倾向 |
|------|------|----------|
| 普通记录 | 柔和 | 中性色为主 |
| 成长 | 温暖 | 暖色调 |
| 生日 | 稍微活泼 | 允许轻微强调 |
| 错误 | 明确但不刺眼 | Error 色柔和处理 |
| 成功 | 轻微反馈 | Success 色克制使用 |

不要让错误颜色破坏整体温柔氛围。

---

## 8. 设计系统版本

**当前版本：v1.0**

版本记录：

| 版本 | 日期 | 说明 |
|------|------|------|
| v1.0 | 2026-09-27 | 设计圣经初始建立 |

未来重大变化需记录版本号和原因，不得在项目中悄悄改变设计语言。

---

## 9. 专项文档索引

| 文档 | 说明 |
|------|------|
| [DESIGN_TOKENS.md](DESIGN_TOKENS.md) | 完整 Design Token 系统 |
| [COLOR_SYSTEM.md](COLOR_SYSTEM.md) | 语义色系统与颜色规则 |
| [TYPOGRAPHY.md](TYPOGRAPHY.md) | 字体规范与排版规则 |
| [SPACING.md](SPACING.md) | 间距系统与布局原则 |
| [SHAPES.md](SHAPES.md) | 圆角、形状系统 |
| [ICONOGRAPHY.md](ICONOGRAPHY.md) | 图标系统 |
| [COMPONENTS.md](COMPONENTS.md) | 组件库（Atoms/Molecules/Organisms） |
| [NAVIGATION.md](NAVIGATION.md) | 导航设计 |
| [HOME_PAGE.md](HOME_PAGE.md) | 首页设计 |
| [TIMELINE_PAGE.md](TIMELINE_PAGE.md) | 时间轴页面设计 |
| [CALENDAR_PAGE.md](CALENDAR_PAGE.md) | 日历页面设计 |
| [GALLERY_PAGE.md](GALLERY_PAGE.md) | 照片墙设计 |
| [ENTRY_EDITOR.md](ENTRY_EDITOR.md) | 记录编辑器设计 |
| [GROWTH_PAGE.md](GROWTH_PAGE.md) | 成长页面设计 |
| [MILESTONE_PAGE.md](MILESTONE_PAGE.md) | 里程碑页面设计 |
| [SETTINGS_PAGE.md](SETTINGS_PAGE.md) | 设置页面设计 |
| [MEDIA_UX.md](MEDIA_UX.md) | 媒体交互设计 |
| [GESTURES.md](GESTURES.md) | 手势设计 |
| [MICRO_INTERACTIONS.md](MICRO_INTERACTIONS.md) | 微交互系统 |
| [ANIMATION_BIBLE.md](ANIMATION_BIBLE.md) | 动画圣经 |
| [MOTION_TOKENS.md](MOTION_TOKENS.md) | 动效 Token |
| [EMPTY_STATES.md](EMPTY_STATES.md) | 空状态设计 |
| [ERROR_STATES.md](ERROR_STATES.md) | 错误状态设计 |
| [LOADING_STATES.md](LOADING_STATES.md) | 加载状态设计 |
| [DARK_MODE.md](DARK_MODE.md) | 深色模式设计 |
| [THEMES.md](THEMES.md) | 主题系统 |
| [RESPONSIVE.md](RESPONSIVE.md) | 响应式设计 |
| [ACCESSIBILITY.md](ACCESSIBILITY.md) | 无障碍设计 |
| [PERFORMANCE_UI.md](PERFORMANCE_UI.md) | UI 性能规范 |
| [DESIGN_DECISIONS.md](DESIGN_DECISIONS.md) | 设计决策日志 |
| [DESIGN_REVIEW_CHECKLIST.md](DESIGN_REVIEW_CHECKLIST.md) | 设计审查清单 |

---

## 10. 与已有规范的关系

本设计圣经与以下已有文档协调：

| 文档 | 关系 |
|------|------|
| [DESIGN_SYSTEM.md](../DESIGN_SYSTEM.md) | 原设计系统，本圣经是其完整扩展 |
| [ANIMATION_GUIDELINES.md](../ANIMATION_GUIDELINES.md) | 原动画规范，ANIMATION_BIBLE.md 是其完整扩展 |
| [ARCHITECTURE.md](../ARCHITECTURE.md) | 技术架构，设计与技术实现协调 |
| [PERFORMANCE.md](../PERFORMANCE.md) | 性能规范，UI 性能必须遵循 |
| [ACCESSIBILITY.md](../ACCESSIBILITY.md) | 无障碍规范，设计必须满足 |

如发现冲突：不得静默解决。记录冲突，选择符合产品核心原则的方案，并记录到 DESIGN_DECISIONS.md。

---

## 11. 产品感情原则

整个 App 的情绪应该像：

> 一个温柔的家庭相册 + 一本持续更新的成长日记 + 一个安静的成长档案。

而不是：

> 数据后台。

---

## 12. 设计语言

### 推荐用词

```
记录  留下  保存  回忆  成长  第一次  今天  慢慢长大
```

### 避免用词（用户界面层）

```
上传  提交  数据条目  对象  媒体资源  记录项  任务  成就
```

除非是技术层。

---

## 13. 关于"可爱"的限制

可爱来自：

- 圆角
- 留白
- 柔和色彩
- 小图标
- 适度插画
- 微动画
- 温柔文字

而不是：

- 大面积卡通
- 彩虹色
- 大量表情 Emoji
- 卡通贴纸铺满界面
- 游戏化积分

---

## 14. 关于"高级感"的限制

高级感来自：

- 一致性
- 留白
- 层级
- 排版
- 图片质量
- 动画节奏
- 微妙的颜色

不是来自：

- 黑金色
- 大量玻璃拟态
- 大量发光
- 大量渐变
- 大面积 Blur

---

## 15. 禁止的 UI 行为

- 每个页面一个颜色体系
- 每个页面一种圆角
- 每个页面一种按钮
- 大量阴影
- 大量渐变
- 无意义动画
- 过度弹窗
- 大量标签堆叠
- 功能堆叠
- 模仿社交 App
- 模仿游戏 UI
- 为了"高级"而复杂

---

## 16. 页面设计优先级

设计任何页面，优先级必须是：

```
1. 内容
2. 用户任务
3. 信息层级
4. 交互
5. 动画
6. 装饰
```

而不是：

```
1. 炫酷
2. 动画
3. 装饰
4. 功能
```

---

## 17. "禁止跑偏"的设计规则

如果未来需求与产品核心原则冲突：

1. 是否符合产品情绪？
2. 是否提高用户价值？
3. 是否损害流畅性？
4. 是否破坏视觉统一？

如果存在明显冲突：必须指出，提出更符合现有设计语言的替代方案。

---

## 18. 最终设计目标

未来一个完全不了解项目的人，打开 App 后应该能够自然感觉：

> "这是一个专门记录一个孩子成长的温柔 App。"

用户不应该感觉：

> "这是一个 AI 自动生成出来的模板 App。"

---

*本文件版本：v1.0 | 最后更新：2026-09-27*