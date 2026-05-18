package com.tuoguan.teacher.ui.navigation

sealed class Screen(val route: String) {

    data object Login : Screen("login")

    data object TaskList : Screen("task_list")
}