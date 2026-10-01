package com.tuantuan.journal.ui.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * 阴影层级 Token — DESIGN_SYSTEM.md 第8节
 * 禁止随意使用未定义的阴影值，必须从此对象取值。
 */
object TuantuanElevation {
    val Level0: Dp = 0.dp    // 页面背景
    val Level1: Dp = 1.dp    // 卡片（resting）
    val Level2: Dp = 3.dp    // 卡片（hovered）
    val Level3: Dp = 6.dp    // BottomSheet、Dialog
    val Level4: Dp = 8.dp    // Modal、Snackbar
    val Level5: Dp = 12.dp   // 最高层级
}