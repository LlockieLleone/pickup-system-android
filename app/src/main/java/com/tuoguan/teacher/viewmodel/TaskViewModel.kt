package com.tuoguan.teacher.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tuoguan.teacher.model.TaskStatus
import com.tuoguan.teacher.network.ConfirmStudentRequest
import com.tuoguan.teacher.network.RetrofitClient
import com.tuoguan.teacher.network.TodayTaskResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TaskViewModel : ViewModel() {

    private val _tasks = MutableStateFlow<List<TodayTaskResponse>>(emptyList())
    val tasks: StateFlow<List<TodayTaskResponse>> = _tasks

    private val _message = MutableStateFlow("")
    val message: StateFlow<String> = _message

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _confirmingStudentId = MutableStateFlow<Long?>(null)
    val confirmingStudentId: StateFlow<Long?> = _confirmingStudentId

    fun loadTodayTasks(teacherId: Long) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val result = RetrofitClient.api.getTodayTasks(teacherId)
                _tasks.value = sortTasks(result)
                _message.value = "加载成功，共 ${result.size} 个任务"
            } catch (e: Exception) {
                _message.value = "加载失败：${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }

    fun confirmStudent(
        teacherId: Long,
        studentId: Long,
        location: String
    ) {
        if (_confirmingStudentId.value != null) {
            return
        }

        viewModelScope.launch {
            _confirmingStudentId.value = studentId

            try {
                val response = RetrofitClient.api.confirmStudent(
                    ConfirmStudentRequest(
                        studentId = studentId,
                        operatorId = teacherId,
                        location = location
                    )
                )

                _message.value = response.message

                val refreshedTasks = RetrofitClient.api.getTodayTasks(teacherId)
                _tasks.value = sortTasks(refreshedTasks)

            } catch (e: Exception) {
                _message.value = "确认失败：${e.message}"
            } finally {
                _confirmingStudentId.value = null
            }
        }
    }

    private fun sortTasks(tasks: List<TodayTaskResponse>): List<TodayTaskResponse> {
        return tasks.sortedWith(
            compareBy<TodayTaskResponse> { getStatusOrder(it.currentStatus) }
                .thenBy { it.expectedPickupTime }
                .thenBy { it.studentName }
        )
    }

    private fun getStatusOrder(status: TaskStatus): Int {
        return when (status) {
            TaskStatus.PENDING_PICKUP -> 1
            TaskStatus.PICKED_UP -> 2
            TaskStatus.ARRIVED -> 3
            TaskStatus.EXCEPTION -> 4
            TaskStatus.LEAVE -> 5
            TaskStatus.PARENT_PICKUP -> 6
            TaskStatus.RELEASED -> 7
        }
    }
}