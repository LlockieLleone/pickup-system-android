package com.tuoguan.teacher.network

data class TodayTaskResponse(
    val taskId: Long,
    val studentId: Long,
    val studentName: String,
    val school: String,
    val expectedPickupTime: String,
    val currentStatus: String
)