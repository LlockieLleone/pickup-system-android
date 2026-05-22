package com.tuoguan.teacher.ui.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tuoguan.teacher.viewmodel.LoginViewModel

@Composable
fun LoginScreen(
    onLoginSuccess: (Long, String, String) -> Unit,
    loginViewModel: LoginViewModel = viewModel()
) {
    val phone by loginViewModel.phone.collectAsState()
    val password by loginViewModel.password.collectAsState()
    val message by loginViewModel.message.collectAsState()
    val loading by loginViewModel.loading.collectAsState()

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
            onValueChange = { loginViewModel.updatePhone(it) },
            label = { Text("手机号") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { loginViewModel.updatePassword(it) },
            label = { Text("密码") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                loginViewModel.login(onLoginSuccess)
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