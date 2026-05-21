package com.tuoguan.teacher.ui.task

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tuoguan.teacher.model.TaskStatus
import com.tuoguan.teacher.network.TodayTaskResponse
import com.tuoguan.teacher.viewmodel.TaskViewModel
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox

@OptIn(ExperimentalMaterial3Api::class)
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
    val confirmingStudentId by taskViewModel.confirmingStudentId.collectAsState()

    var selectedTask by remember { mutableStateOf<TodayTaskResponse?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }
    val refreshVersion by taskViewModel.refreshVersion.collectAsState()

    LaunchedEffect(teacherId) {
        taskViewModel.loadTodayTasks(teacherId)
    }

    LaunchedEffect(refreshVersion) {
        if (message.isNotBlank()) {
            snackbarHostState.showSnackbar(message)
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
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

                Button(onClick = onLogout) {
                    Text("退出")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text("当前老师：$teacherName")

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "任务数：${tasks.size}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            PullToRefreshBox(
                isRefreshing = loading,
                onRefresh = {
                    taskViewModel.loadTodayTasks(teacherId)
                },
                modifier = Modifier.fillMaxSize()
            ) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(tasks) { task ->
                        TaskCard(
                            task = task,
                            isConfirming = confirmingStudentId == task.studentId,
                            onConfirmClick = {
                                selectedTask = task
                            }
                        )
                    }
                }
            }
        }
    }

    selectedTask?.let { selected ->
        AlertDialog(
            onDismissRequest = {
                selectedTask = null
            },
            title = {
                Text("确认操作")
            },
            text = {
                Text("确定要为 ${selected.studentName} 执行「${getActionText(selected.currentStatus)}」吗？")
            },
            confirmButton = {
                Button(
                    enabled = confirmingStudentId == null,
                    onClick = {
                        taskViewModel.confirmStudent(
                            teacherId = teacherId,
                            studentId = selected.studentId,
                            location = selected.school
                        )
                        selectedTask = null
                    }
                ) {
                    Text("确定")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        selectedTask = null
                    }
                ) {
                    Text("取消")
                }
            }
        )
    }
}

@Composable
private fun TaskCard(
    task: TodayTaskResponse,
    isConfirming: Boolean,
    onConfirmClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = task.studentName,
                style = MaterialTheme.typography.titleMedium
            )

            Text(text = task.school)

            Text(text = "时间：${task.expectedPickupTime}")

            Text(text = "状态：${getStatusText(task.currentStatus)}")

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = onConfirmClick,
                enabled = canConfirm(task.currentStatus) && !isConfirming,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    if (isConfirming) {
                        "处理中..."
                    } else {
                        getActionText(task.currentStatus)
                    }
                )
            }
        }
    }
}

private fun getActionText(status: TaskStatus): String {
    return when (status) {
        TaskStatus.PENDING_PICKUP -> "确认接到"
        TaskStatus.PICKED_UP -> "确认到达"
        TaskStatus.ARRIVED -> "确认接走"
        TaskStatus.RELEASED -> "已完成"
        TaskStatus.LEAVE -> "请假"
        TaskStatus.PARENT_PICKUP -> "家长自接"
        TaskStatus.EXCEPTION -> "异常"
    }
}

private fun getStatusText(status: TaskStatus): String {
    return when (status) {
        TaskStatus.PENDING_PICKUP -> "待接"
        TaskStatus.PICKED_UP -> "已接到"
        TaskStatus.ARRIVED -> "已到托管"
        TaskStatus.RELEASED -> "已接走"
        TaskStatus.LEAVE -> "请假"
        TaskStatus.PARENT_PICKUP -> "家长自接"
        TaskStatus.EXCEPTION -> "异常"
    }
}

private fun canConfirm(status: TaskStatus): Boolean {
    return when (status) {
        TaskStatus.PENDING_PICKUP,
        TaskStatus.PICKED_UP,
        TaskStatus.ARRIVED -> true

        TaskStatus.RELEASED,
        TaskStatus.LEAVE,
        TaskStatus.PARENT_PICKUP,
        TaskStatus.EXCEPTION -> false
    }
}