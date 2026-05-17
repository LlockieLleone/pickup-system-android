package com.tuoguan.teacher.ui.task

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tuoguan.teacher.network.ConfirmStudentRequest
import com.tuoguan.teacher.network.RetrofitClient
import com.tuoguan.teacher.network.TodayTaskResponse
import kotlinx.coroutines.launch

@Composable
fun TaskListScreen(
    teacherId: Long,
    teacherName: String
) {
    var tasks by remember { mutableStateOf<List<TodayTaskResponse>>(emptyList()) }
    var message by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(true) }

    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        try {
            tasks = RetrofitClient.api.getTodayTasks(teacherId)
            message = "加载成功，共 ${tasks.size} 个任务"
        } catch (e: Exception) {
            message = "加载失败：${e.message}"
        } finally {
            loading = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text("今日接送任务", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(8.dp))

        Text("当前老师：$teacherName")

        Spacer(modifier = Modifier.height(8.dp))

        Text(message)

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
                                scope.launch {
                                    try {
                                        val response = RetrofitClient.api.confirmStudent(
                                            ConfirmStudentRequest(
                                                studentId = task.studentId,
                                                operatorId = teacherId,
                                                location = task.school
                                            )
                                        )

                                        message = response.message
                                        tasks = RetrofitClient.api.getTodayTasks(teacherId)

                                    } catch (e: Exception) {
                                        message = "确认失败：${e.message}"
                                    }
                                }
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