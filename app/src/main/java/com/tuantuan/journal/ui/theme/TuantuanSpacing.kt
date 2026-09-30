package com.tuantuan.journal.ui.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * 间距 Token — DESIGN_SYSTEM.md 第4节
 * 禁止使用硬编码间距数值，必须从此对象取值。
 */
object TuantuanSpacing {
    val XS: Dp   = 4.dp     // 极小间距：图标与文字之间
    val SM: Dp   = 8.dp     // 小间距：同组元素之间
    val MD: Dp   = 12.dp    // 中间距：不同组元素之间
    val Base: Dp = 16.dp    // 基础间距：标准内边距
    val LG: Dp   = 20.dp    // 大间距：区块之间
    val XL: Dp   = 24.dp    // 超大间距：页面区块
    val XXL: Dp  = 32.dp    // 极大间距：页面顶部/底部
    val XXXL: Dp = 48.dp    // 特大间距：页面间留白
}