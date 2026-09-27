# NAVIGATION.md — Tuantuan Journal 导航设计

> **本文件定义 Tuantuan Journal 的导航结构与交互。**

---

## 1. 底部导航

### 1.1 固定结构

```
首页  |  记录  |  ＋  |  成长  |  我的
```

### 1.2 Tab 定义

| Tab | 图标 | 说明 |
|-----|------|------|
| 首页 | home_outlined | 团团主页、今日、最近回忆 |
| 记录 | timeline_outlined | 时间轴、日历、照片墙 |
| ＋ | add | 新建记录（中心按钮） |
| 成长 | child_care_outlined | 成长数据、里程碑 |
| 我的 | person_outlined | 设置、主题、备份 |

### 1.3 中心按钮

"＋"是整个 App 的主要操作入口。

- 使用 Full 圆形
- Primary 色填充
- 不能做成夸张的悬浮按钮
- 视觉上与导航栏融合，不突兀突出
- 点击后打开新建记录页面

### 1.4 选中/未选中

- 未选中：弱化（OnSurfaceVariant）
- 选中：明确但温柔（Primary）
- 切换时：轻微 icon / indicator 动画
- 不能使用过于鲜艳的选中色

---

## 2. 页面导航层级

```
Level 0  底部导航 Tab 页面
Level 1  Tab 内的子页面（如照片详情）
Level 2  从子页面进入的详情（如全屏照片）
Level 3  模态页面（Dialog、BottomSheet）
```

---

## 3. 导航交互

### 3.1 前进导航

- 新页面：从右侧淡入 + 轻微位移
- 当前页面：轻微向左位移 + 淡出
- 时长：Normal (300ms)
- 缓动：Decelerate（进入）/ Accelerate（退出）

### 3.2 返回导航

- 当前页面：向右位移 + 淡出
- 前一页面：淡入 + 轻微位移恢复
- 时长：Normal (300ms)

### 3.3 Tab 切换

- 新内容：淡入
- 旧内容：淡出
- 时长：Quick (150ms)
- 无位移，仅淡入淡出
- 保持 Tab 栏不动

---

## 4. 记录中心导航

记录中心顶部使用：

```
时间轴  |  日历  |  照片墙
```

不要再增加大量顶部 Tab。

切换方式：横向滑动或点击 Tab。

---

## 5. 返回逻辑

### 5.1 系统返回

- Level 2 → Level 1：关闭详情
- Level 1 → Level 0：回到 Tab 页
- Level 0：退出 App（不强制确认）

### 5.2 上滑返回

- 全屏照片查看：支持下滑关闭
- BottomSheet：支持下滑关闭

---

## 6. 深层链接

暂不需要。App 完全离线，无外部链接需求。

---

## 7. 导航状态保存

- Tab 切换时保持各 Tab 的滚动位置
- 返回详情页时保持之前的滚动位置
- 使用 Navigation Compose 的 `saveState` 和 `restoreState`

---

*本文件版本：v1.0 | 最后更新：2026-09-27*