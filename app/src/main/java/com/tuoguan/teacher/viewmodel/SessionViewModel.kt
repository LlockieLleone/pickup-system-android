package com.tuoguan.teacher.viewmodel

import androidx.lifecycle.ViewModel
import com.tuoguan.teacher.data.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SessionViewModel : ViewModel() {

    private val _teacherId = MutableStateFlow<Long?>(null)
    val teacherId: StateFlow<Long?> = _teacherId

    private val _teacherName = MutableStateFlow("")
    val teacherName: StateFlow<String> = _teacherName

    fun loadSession(sessionManager: SessionManager) {
        _teacherId.value = sessionManager.getTeacherId()
        _teacherName.value = sessionManager.getTeacherName()
    }

    fun login(
        teacherId: Long,
        teacherName: String,
        sessionManager: SessionManager
    ) {
        sessionManager.saveSession(teacherId, teacherName)
        _teacherId.value = teacherId
        _teacherName.value = teacherName
    }

    fun logout(sessionManager: SessionManager) {
        sessionManager.clearSession()
        _teacherId.value = null
        _teacherName.value = ""
    }
}