package com.tuantuan.journal.ui.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object ChildList : Screen("children")
    data object ChildDetail : Screen("children/{childId}") {
        fun createRoute(childId: String) = "children/$childId"
    }
    data object ChildEdit : Screen("children/{childId}/edit") {
        fun createRoute(childId: String) = "children/$childId/edit"
    }
    data object ChildAdd : Screen("children/add")
    data object DiaryList : Screen("children/{childId}/diaries") {
        fun createRoute(childId: String) = "children/$childId/diaries"
    }
    data object DiaryDetail : Screen("diaries/{entryId}") {
        fun createRoute(entryId: String) = "diaries/$entryId"
    }
    data object DiaryEdit : Screen("diaries/{entryId}/edit") {
        fun createRoute(entryId: String) = "diaries/$entryId/edit"
    }
    data object DiaryAdd : Screen("children/{childId}/diaries/add") {
        fun createRoute(childId: String) = "children/$childId/diaries/add"
    }
    data object Search : Screen("children/{childId}/search") { fun createRoute(childId: String) = "children/$childId/search" }
    data object TagManage : Screen("tags")
    data object Growth : Screen("children/{childId}/growth") {
        fun createRoute(childId: String) = "children/$childId/growth"
    }
    data object Milestone : Screen("children/{childId}/milestones") {
        fun createRoute(childId: String) = "children/$childId/milestones"
    }
}