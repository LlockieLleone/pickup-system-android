package com.tuoguan.teacher.network

data class ConfirmStudentRequest(
    val method: String = "MANUAL",
    val studentId: Long,
    val operatorId: Long,
    val location: String
)