package com.tuantuan.journal.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Timeline
import androidx.compose.ui.graphics.vector.ImageVector
import com.tuantuan.journal.R

/**
 * 底部导航项定义。
 *
 * 遵循 PRODUCT_SPEC.md 第4.1节：
 * 首页 | 记录 | ＋ | 成长 | 我的
 */
sealed class BottomNavItem(
    val route: String,
    @StringRes val labelResId: Int,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    data object Home : BottomNavItem(
        route = "bottom_home",
        labelResId = R.string.nav_home,
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home
    )

    data object Record : BottomNavItem(
        route = "bottom_record",
        labelResId = R.string.nav_record,
        selectedIcon = Icons.Filled.Book,
        unselectedIcon = Icons.Outlined.Book
    )

    data object Add : BottomNavItem(
        route = "bottom_add",
        labelResId = 0, // Add 按钮无标签
        selectedIcon = Icons.Filled.Add,
        unselectedIcon = Icons.Filled.Add
    )

    data object Growth : BottomNavItem(
        route = "bottom_growth",
        labelResId = R.string.nav_growth,
        selectedIcon = Icons.Filled.Timeline,
        unselectedIcon = Icons.Outlined.Timeline
    )

    data object Profile : BottomNavItem(
        route = "bottom_profile",
        labelResId = R.string.nav_profile,
        selectedIcon = Icons.Filled.Person,
        unselectedIcon = Icons.Outlined.Person
    )
}

val bottomNavItems = listOf(
    BottomNavItem.Home,
    BottomNavItem.Record,
    BottomNavItem.Add,
    BottomNavItem.Growth,
    BottomNavItem.Profile
)