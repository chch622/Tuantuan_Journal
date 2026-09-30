package com.tuantuan.journal.ui.theme

import androidx.compose.ui.text.TextStyle

/**
 * 排版 Token — DESIGN_SYSTEM.md 第5节
 * 禁止页面自行随意定义字体大小，必须从此对象取值。
 *
 * 实际值由 Theme.kt 中的 MaterialTheme.typography 提供，
 * 此对象作为语义化访问入口，确保排版一致性。
 */
object TuantuanTypography {
    // 展示 — 首页大标题、年度回顾
    val DisplayLarge: TextStyle get() = Typography.displayLarge
    val DisplayMedium: TextStyle get() = Typography.displayMedium
    val DisplaySmall: TextStyle get() = Typography.displaySmall

    // 标题 — 页面标题
    val HeadlineLarge: TextStyle get() = Typography.headlineLarge
    val HeadlineMedium: TextStyle get() = Typography.headlineMedium
    val HeadlineSmall: TextStyle get() = Typography.headlineSmall

    // 标题 — 区块标题
    val TitleLarge: TextStyle get() = Typography.titleLarge
    val TitleMedium: TextStyle get() = Typography.titleMedium
    val TitleSmall: TextStyle get() = Typography.titleSmall

    // 正文 — 内容文字
    val BodyLarge: TextStyle get() = Typography.bodyLarge
    val BodyMedium: TextStyle get() = Typography.bodyMedium
    val BodySmall: TextStyle get() = Typography.bodySmall

    // 标签 — 按钮文字、Tab
    val LabelLarge: TextStyle get() = Typography.labelLarge
    val LabelMedium: TextStyle get() = Typography.labelMedium
    val LabelSmall: TextStyle get() = Typography.labelSmall

    // 说明 — 辅助说明
    val Caption: TextStyle get() = Typography.labelSmall
}