package com.tuantuan.journal.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.tuantuan.journal.R
import com.tuantuan.journal.ui.navigation.BottomNavItem
import com.tuantuan.journal.ui.navigation.TuantuanNavigation
import com.tuantuan.journal.ui.navigation.bottomNavItems

/**
 * 应用主框架。
 *
 * 遵循 PRODUCT_SPEC.md 第4.1节：底部5 Tab导航（首页|记录|＋|成长|我的）。
 * 底部导航栏仅在 Tab 顶层页面显示，详情页等子页面自动隐藏。
 */
@Composable
fun TuantuanApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // 底部导航栏仅在 Tab 顶层路由显示
    val bottomTabRoutes = bottomNavItems.map { it.route }.toSet()
    val showBottomBar = currentDestination?.route in bottomTabRoutes

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    bottomNavItems.forEach { item ->
                        NavigationBarItem(
                            selected = currentDestination?.hierarchy?.any {
                                it.route == item.route
                            } == true,
                            onClick = {
                                navController.navigate(item.route) {
                                    // 切换 Tab 时弹出至起始目的地并保存状态
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = if (currentDestination?.hierarchy?.any {
                                            it.route == item.route
                                        } == true) item.selectedIcon else item.unselectedIcon,
                                    contentDescription = if (item.labelResId != 0) stringResource(item.labelResId) else stringResource(R.string.add)
                                )
                            },
                            label = {
                                if (item.labelResId != 0) {
                                    Text(stringResource(item.labelResId))
                                }
                            },
                            colors = NavigationBarItemDefaults.colors(
                                // Add 按钮使用强调色
                                selectedIconColor = if (item == BottomNavItem.Add) {
                                    Color.Unspecified
                                } else {
                                    NavigationBarItemDefaults.colors().selectedIconColor
                                }
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        TuantuanNavigation(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}