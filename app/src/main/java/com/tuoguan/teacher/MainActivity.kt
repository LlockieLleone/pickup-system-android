package com.tuoguan.teacher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.tuoguan.teacher.state.AppState
import com.tuoguan.teacher.ui.login.LoginScreen
import com.tuoguan.teacher.ui.task.TaskListScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            var appState by remember { mutableStateOf(AppState()) }

            if (!appState.loggedIn) {
                LoginScreen(
                    onLoginSuccess = { id, name ->
                        appState = AppState(
                            loggedIn = true,
                            teacherId = id,
                            teacherName = name
                        )
                    }
                )
            } else {
                TaskListScreen(
                    teacherId = appState.teacherId!!,
                    teacherName = appState.teacherName
                )
            }
        }
    }
}