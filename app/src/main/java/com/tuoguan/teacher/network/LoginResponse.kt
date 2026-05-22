package com.tuoguan.teacher.network

data class LoginResponse(
    val success: Boolean,
    val teacherId: Long,
    val teacherName: String,
    val role: String,
    val token: String,
    val message: String
)