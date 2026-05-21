package com.tuoguan.teacher.network

import com.tuoguan.teacher.model.EventType
import com.tuoguan.teacher.model.TaskStatus

data class ConfirmStudentResponse(
    val success: Boolean,
    val studentId: Long,
    val studentName: String,
    val taskId: Long,
    val eventType: EventType,
    val currentStatus: TaskStatus,
    val message: String
)