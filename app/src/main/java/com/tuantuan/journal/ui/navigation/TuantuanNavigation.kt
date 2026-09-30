package com.tuantuan.journal.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.tuantuan.journal.ui.screen.child.ChildAddScreen
import com.tuantuan.journal.ui.screen.child.ChildDetailScreen
import com.tuantuan.journal.ui.screen.child.ChildEditScreen
import com.tuantuan.journal.ui.screen.child.ChildListScreen
import com.tuantuan.journal.ui.screen.diary.DiaryAddScreen
import com.tuantuan.journal.ui.screen.diary.DiaryDetailScreen
import com.tuantuan.journal.ui.screen.diary.DiaryEditScreen
import com.tuantuan.journal.ui.screen.diary.DiaryListScreen
import com.tuantuan.journal.ui.screen.growth.GrowthScreen
import com.tuantuan.journal.ui.screen.home.HomeScreen
import com.tuantuan.journal.ui.screen.profile.ProfileScreen
import com.tuantuan.journal.ui.screen.record.RecordScreen
import com.tuantuan.journal.ui.screen.search.SearchScreen
import com.tuantuan.journal.ui.screen.tag.TagManageScreen

/**
 * 应用导航图。
 *
 * 底部 Tab 路由（顶层目的地，切换时保存/恢复状态）：
 * - bottom_home → HomeScreen
 * - bottom_record → RecordScreen（Phase 2 占位）
 * - bottom_add → AddScreen（快速添加入口）
 * - bottom_growth → GrowthScreen（Phase 2 占位）
 * - bottom_profile → ProfileScreen（Phase 2 占位）
 *
 * 子页面路由（从 Tab 页面导航进入，显示返回按钮）：
 * - children/{childId} → 儿童详情/编辑/添加
 * - diaries/{entryId} → 日记详情/编辑/添加
 * - search → 搜索
 * - tags → 标签管理
 */
@Composable
fun TuantuanNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = BottomNavItem.Home.route,
        modifier = modifier
    ) {
        // ===== 底部 Tab 路由 =====

        composable(BottomNavItem.Home.route) {
            HomeScreen(
                onChildClick = { childId ->
                    navController.navigate(Screen.ChildDetail.createRoute(childId))
                },
                onAddChildClick = {
                    navController.navigate(Screen.ChildAdd.route)
                },
                onTagManageClick = {
                    navController.navigate(Screen.TagManage.route)
                }
            )
        }

        composable(BottomNavItem.Record.route) {
            RecordScreen()
        }

        composable(BottomNavItem.Add.route) {
            // 快速添加入口：显示儿童列表供选择，选择后导航至添加日记
            ChildListScreen(
                onChildClick = { childId ->
                    navController.navigate(Screen.DiaryAdd.createRoute(childId))
                },
                onAddChildClick = {
                    navController.navigate(Screen.ChildAdd.route)
                },
                onBackClick = { navController.popBackStack() },
                title = "选择儿童添加日记"
            )
        }

        composable(BottomNavItem.Growth.route) {
            GrowthScreen()
        }

        composable(BottomNavItem.Profile.route) {
            ProfileScreen()
        }

        // ===== 子页面路由 =====

        composable(Screen.ChildList.route) {
            ChildListScreen(
                onChildClick = { childId ->
                    navController.navigate(Screen.ChildDetail.createRoute(childId))
                },
                onAddChildClick = {
                    navController.navigate(Screen.ChildAdd.route)
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.ChildDetail.route,
            arguments = listOf(navArgument("childId") { type = NavType.StringType })
        ) {
            val childId = it.arguments?.getString("childId") ?: return@composable
            ChildDetailScreen(
                childId = childId,
                onEditClick = { navController.navigate(Screen.ChildEdit.createRoute(childId)) },
                onDiaryClick = { entryId ->
                    navController.navigate(Screen.DiaryDetail.createRoute(entryId))
                },
                onAddDiaryClick = {
                    navController.navigate(Screen.DiaryAdd.createRoute(childId))
                },
                onViewDiariesClick = {
                    navController.navigate(Screen.DiaryList.createRoute(childId))
                },
                onDeleteClick = { navController.popBackStack() },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.ChildEdit.route,
            arguments = listOf(navArgument("childId") { type = NavType.StringType })
        ) {
            val childId = it.arguments?.getString("childId") ?: return@composable
            ChildEditScreen(
                childId = childId,
                onSaved = { navController.popBackStack() },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.ChildAdd.route) {
            ChildAddScreen(
                onSaved = { navController.popBackStack() },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.DiaryList.route,
            arguments = listOf(navArgument("childId") { type = NavType.StringType })
        ) {
            val childId = it.arguments?.getString("childId") ?: return@composable
            DiaryListScreen(
                childId = childId,
                onDiaryClick = { entryId ->
                    navController.navigate(Screen.DiaryDetail.createRoute(entryId))
                },
                onAddDiaryClick = {
                    navController.navigate(Screen.DiaryAdd.createRoute(childId))
                },
                onSearchClick = {
                    navController.navigate(Screen.Search.createRoute(childId))
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.DiaryDetail.route,
            arguments = listOf(navArgument("entryId") { type = NavType.StringType })
        ) {
            val entryId = it.arguments?.getString("entryId") ?: return@composable
            DiaryDetailScreen(
                entryId = entryId,
                onEditClick = { navController.navigate(Screen.DiaryEdit.createRoute(entryId)) },
                onDeleteClick = { navController.popBackStack() },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.DiaryEdit.route,
            arguments = listOf(navArgument("entryId") { type = NavType.StringType })
        ) {
            val entryId = it.arguments?.getString("entryId") ?: return@composable
            DiaryEditScreen(
                entryId = entryId,
                onSaved = { navController.popBackStack() },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.DiaryAdd.route,
            arguments = listOf(navArgument("childId") { type = NavType.StringType })
        ) {
            val childId = it.arguments?.getString("childId") ?: return@composable
            DiaryAddScreen(
                childId = childId,
                onSaved = { navController.popBackStack() },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.Search.route,
            arguments = listOf(navArgument("childId") { type = NavType.StringType })
        ) {
            val childId = it.arguments?.getString("childId") ?: return@composable
            SearchScreen(
                childId = childId,
                onEntryClick = { entryId ->
                    navController.navigate(Screen.DiaryDetail.createRoute(entryId))
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.TagManage.route) {
            TagManageScreen(onBackClick = { navController.popBackStack() })
        }
    }
}