package com.tuoguan.teacher.ui.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tuoguan.teacher.data.SessionManager
import com.tuoguan.teacher.ui.login.LoginScreen
import com.tuoguan.teacher.ui.task.TaskListScreen
import com.tuoguan.teacher.viewmodel.SessionViewModel

@Composable
fun AppNavigation() {

    val navController = rememberNavController()
    val context = LocalContext.current
    val sessionManager = SessionManager(context)

    val sessionViewModel: SessionViewModel = viewModel()

    val teacherId by sessionViewModel.teacherId.collectAsState()
    val teacherName by sessionViewModel.teacherName.collectAsState()

    var sessionLoaded by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        sessionViewModel.loadSession(sessionManager)
        sessionLoaded = true
    }

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            if (!sessionLoaded) {
                Text("加载中...")
            } else if (teacherId != null) {
                LaunchedEffect(teacherId) {
                    navController.navigate(Screen.TaskList.route) {
                        popUpTo(Screen.Login.route) {
                            inclusive = true
                        }
                    }
                }
            } else {
                LoginScreen(
                    onLoginSuccess = { id, name, token ->
                        sessionViewModel.login(id, name, token, sessionManager)

                        navController.navigate(Screen.TaskList.route) {
                            popUpTo(Screen.Login.route) {
                                inclusive = true
                            }
                        }
                    }
                )
            }
        }

        composable(Screen.TaskList.route) {
            if (teacherId != null) {
                TaskListScreen(
                    teacherId = teacherId!!,
                    teacherName = teacherName,
                    onLogout = {
                        sessionViewModel.logout(sessionManager)

                        navController.navigate(Screen.Login.route) {
                            popUpTo(0) {
                                inclusive = true
                            }
                        }
                    }
                )
            } else {
                LaunchedEffect(Unit) {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) {
                            inclusive = true
                        }
                    }
                }
            }
        }
    }
}