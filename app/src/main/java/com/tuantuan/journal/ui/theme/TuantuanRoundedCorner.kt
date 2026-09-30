package com.tuantuan.journal.ui.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * 圆角 Token — DESIGN_SYSTEM.md 第3节
 * 禁止每个组件自行随意决定圆角，必须从此对象取值。
 */
object TuantuanRoundedCorner {
    val Small: Dp      = 8.dp   // 小型组件：Chip、Tag
    val Medium: Dp     = 12.dp  // 中型组件：Button、TextField
    val Large: Dp      = 16.dp  // 大型组件：Card、Dialog
    val ExtraLarge: Dp = 24.dp  // 特大组件：BottomSheet、Modal
    val Full: Dp       = 50.dp  // 圆形：Avatar、FAB
}