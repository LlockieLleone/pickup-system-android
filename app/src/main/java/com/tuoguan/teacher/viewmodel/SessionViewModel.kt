package com.tuoguan.teacher.viewmodel

import androidx.lifecycle.ViewModel
import com.tuoguan.teacher.data.SessionManager
import com.tuoguan.teacher.network.TokenStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SessionViewModel : ViewModel() {

    private val _teacherId = MutableStateFlow<Long?>(null)
    private val _token = MutableStateFlow("")
    val token: StateFlow<String> = _token
    val teacherId: StateFlow<Long?> = _teacherId

    private val _teacherName = MutableStateFlow("")
    val teacherName: StateFlow<String> = _teacherName

    fun loadSession(sessionManager: SessionManager) {
        _teacherId.value = sessionManager.getTeacherId()
        _teacherName.value = sessionManager.getTeacherName()
        _token.value = sessionManager.getToken()
        TokenStore.token = sessionManager.getToken()
    }

    fun login(
        teacherId: Long,
        teacherName: String,
        token: String,
        sessionManager: SessionManager
    ) {
        sessionManager.saveSession(teacherId, teacherName, token)
        _teacherId.value = teacherId
        _teacherName.value = teacherName
        _token.value = token
        TokenStore.token = token
    }

    fun logout(sessionManager: SessionManager) {
        sessionManager.clearSession()
        _teacherId.value = null
        _teacherName.value = ""
        _token.value = ""
        TokenStore.token = ""
    }
}