package com.tuoguan.teacher.ui.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.tuoguan.teacher.network.LoginRequest
import com.tuoguan.teacher.network.RetrofitClient
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    onLoginSuccess: (Long, String) -> Unit
) {
    var phone by remember { mutableStateOf("13700137000") }
    var password by remember { mutableStateOf("123456") }
    var message by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("托管接送系统", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("手机号") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("密码") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                loading = true
                message = ""

                scope.launch {
                    try {
                        val response = RetrofitClient.api.login(
                            LoginRequest(phone, password)
                        )

                        if (response.success) {
                            onLoginSuccess(response.teacherId, response.teacherName)
                        } else {
                            message = response.message
                        }

                    } catch (e: Exception) {
                        message = "登录失败：${e.message}"
                    } finally {
                        loading = false
                    }
                }
            },
            enabled = !loading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (loading) "登录中..." else "登录")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (message.isNotBlank()) {
            Text(message)
        }
    }
}