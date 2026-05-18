package com.tuoguan.teacher.ui.task

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tuoguan.teacher.viewmodel.TaskViewModel

@Composable
fun TaskListScreen(
    teacherId: Long,
    teacherName: String,
    onLogout: () -> Unit,
    taskViewModel: TaskViewModel = viewModel()
) {
    val tasks by taskViewModel.tasks.collectAsState()
    val message by taskViewModel.message.collectAsState()
    val loading by taskViewModel.loading.collectAsState()

    LaunchedEffect(teacherId) {
        taskViewModel.loadTodayTasks(teacherId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "今日接送任务",
                style = MaterialTheme.typography.headlineMedium
            )

            Button(
                onClick = onLogout
            ) {
                Text("退出")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text("当前老师：$teacherName")

        Spacer(modifier = Modifier.height(8.dp))

        if (message.isNotBlank()) {
            Text(message)
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (loading) {
            CircularProgressIndicator()
        } else {
            tasks.forEach { task ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(task.studentName, style = MaterialTheme.typography.titleMedium)
                        Text(task.school)
                        Text("时间：${task.expectedPickupTime}")
                        Text("状态：${task.currentStatus}")

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(
                            onClick = {
                                taskViewModel.confirmStudent(
                                    teacherId = teacherId,
                                    studentId = task.studentId,
                                    location = task.school
                                )
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("确认")
                        }
                    }
                }
            }
        }
    }
}