# DESIGN_REVIEW_CHECKLIST.md — 设计审查清单

> **以后每新增页面必须检查此清单。**
> **所有检查项必须通过才能合并。**

---

## Visual Check

- [ ] 颜色是否使用语义色 Token？（TuantuanColors）
- [ ] Typography 是否使用 Token？（TuantuanTypography）
- [ ] Spacing 是否使用 Token？（TuantuanSpacing）
- [ ] 圆角是否使用 Token？（TuantuanRoundedCorner）
- [ ] 组件是否已在 COMPONENTS.md 中定义？
- [ ] 图标是否使用 Material Icons Outlined？
- [ ] 整体风格是否温柔、简洁、不幼稚？

---

## UX Check

- [ ] 用户是否知道下一步？
- [ ] 是否有清晰反馈？
- [ ] 是否容易误触？
- [ ] 是否容易返回？
- [ ] 是否有空状态？
- [ ] 是否有错误状态？
- [ ] 是否有加载状态？
- [ ] 成功反馈是否克制？
- [ ] 危险操作是否需要确认？
- [ ] 文案是否温柔？（避免"No Data"等）

---

## Motion Check

- [ ] 动画是否有意义？
- [ ] 动画时长是否使用 Token？（TuantuanDuration）
- [ ] 缓动是否使用 Token？（TuantuanEasing）
- [ ] 是否可能掉帧？
- [ ] 是否尊重"减少动画"设置？
- [ ] 动画是否温柔、轻盈？

---

## Accessibility Check

- [ ] 触控区域 ≥ 48dp？
- [ ] 文字对比度 ≥ 4.5:1？
- [ ] 不依赖颜色表达信息？
- [ ] 功能图标有 contentDescription？
- [ ] 支持字体放大？
- [ ] TalkBack 导航顺序合理？

---

## Performance Check

- [ ] LazyColumn 有 key 和 contentType？
- [ ] 图片使用缩略图？
- [ ] 动画期间无重型操作？
- [ ] 无无限循环动效？
- [ ] 无超重 Blur？

---

## Consistency Check

- [ ] 与 DESIGN_BIBLE.md 一致？
- [ ] 与已有页面风格一致？
- [ ] 没有创建重复组件？
- [ ] 没有硬编码值？
- [ ] 深色模式正常？

---

## Product Check

- [ ] 是否符合产品定位？（温柔、简洁、可爱但不幼稚）
- [ ] 是否符合 Soft Emotional Minimalism？
- [ ] 照片是否仍是视觉核心？
- [ ] 没有无意义装饰？
- [ ] 没有模仿社交 App？
- [ ] 没有模仿游戏 UI？

---

*本文件版本：v1.0 | 最后更新：2026-09-27*