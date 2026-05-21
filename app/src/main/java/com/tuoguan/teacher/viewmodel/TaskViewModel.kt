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

    private val _refreshVersion = MutableStateFlow(0)
    val refreshVersion: StateFlow<Int> = _refreshVersion

    fun loadTodayTasks(teacherId: Long) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val response = RetrofitClient.api.getTodayTasks(teacherId)

                if (response.success && response.data != null) {
                    _tasks.value = sortTasks(response.data)
                    _message.value = "加载成功，共 ${response.data.size} 个任务"
                } else {
                    _message.value = response.message
                }
            } catch (e: Exception) {
                _message.value = "加载失败：${e.message}"
            } finally {
                _loading.value = false
                _refreshVersion.value++
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

                if (response.success && response.data != null) {
                    _message.value = response.data.message
                } else {
                    _message.value = response.message
                }

                val refreshedResponse = RetrofitClient.api.getTodayTasks(teacherId)

                if (refreshedResponse.success && refreshedResponse.data != null) {
                    _tasks.value = sortTasks(refreshedResponse.data)
                }

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