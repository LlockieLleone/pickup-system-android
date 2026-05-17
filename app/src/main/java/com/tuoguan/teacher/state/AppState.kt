package com.tuoguan.teacher.state

data class AppState(
    val loggedIn: Boolean = false,
    val teacherId: Long? = null,
    val teacherName: String = ""
)