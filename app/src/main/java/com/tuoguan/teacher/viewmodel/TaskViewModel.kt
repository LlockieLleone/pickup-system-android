package com.tuoguan.teacher.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    fun loadTodayTasks(teacherId: Long) {
        viewModelScope.launch {
            _loading.value = true
            try {
                _tasks.value = RetrofitClient.api.getTodayTasks(teacherId)
                _message.value = "加载成功，共 ${_tasks.value.size} 个任务"
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
        viewModelScope.launch {
            try {
                val response = RetrofitClient.api.confirmStudent(
                    ConfirmStudentRequest(
                        studentId = studentId,
                        operatorId = teacherId,
                        location = location
                    )
                )

                _message.value = response.message

                loadTodayTasks(teacherId)

            } catch (e: Exception) {
                _message.value = "确认失败：${e.message}"
            }
        }
    }
}