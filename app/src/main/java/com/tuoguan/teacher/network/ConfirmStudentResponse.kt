package com.tuoguan.teacher.network

data class ConfirmStudentResponse(
    val success: Boolean,
    val studentId: Long,
    val studentName: String,
    val taskId: Long,
    val eventType: String,
    val currentStatus: String,
    val message: String
)