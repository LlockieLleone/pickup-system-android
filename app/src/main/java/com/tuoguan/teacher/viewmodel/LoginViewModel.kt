package com.tuoguan.teacher.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tuoguan.teacher.network.LoginRequest
import com.tuoguan.teacher.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val _phone = MutableStateFlow("13700137000")
    val phone: StateFlow<String> = _phone

    private val _password = MutableStateFlow("123456")
    val password: StateFlow<String> = _password

    private val _message = MutableStateFlow("")
    val message: StateFlow<String> = _message

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    fun updatePhone(value: String) {
        _phone.value = value
    }

    fun updatePassword(value: String) {
        _password.value = value
    }

    fun login(
        onSuccess: (Long, String) -> Unit
    ) {
        viewModelScope.launch {
            _loading.value = true
            _message.value = ""

            try {
                val response = RetrofitClient.api.login(
                    LoginRequest(
                        phone = _phone.value,
                        password = _password.value
                    )
                )

                if (response.success && response.data != null) {
                    onSuccess(
                        response.data.teacherId,
                        response.data.teacherName
                    )
                } else {
                    _message.value = response.message
                }

            } catch (e: Exception) {
                _message.value = "登录失败：${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }
}