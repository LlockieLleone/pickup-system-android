package com.tuoguan.teacher.data

import android.content.Context

class SessionManager(context: Context) {

    private val prefs = context.getSharedPreferences(
        "teacher_session",
        Context.MODE_PRIVATE
    )

    fun saveSession(
        teacherId: Long,
        teacherName: String
    ) {
        prefs.edit()
            .putLong("teacherId", teacherId)
            .putString("teacherName", teacherName)
            .apply()
    }

    fun getTeacherId(): Long? {
        val id = prefs.getLong("teacherId", -1L)
        return if (id == -1L) null else id
    }

    fun getTeacherName(): String {
        return prefs.getString("teacherName", "") ?: ""
    }

    fun clearSession() {
        prefs.edit().clear().apply()
    }

    fun saveSession(
        teacherId: Long,
        teacherName: String,
        token: String
    ) {
        prefs.edit()
            .putLong("teacherId", teacherId)
            .putString("teacherName", teacherName)
            .putString("token", token)
            .apply()
    }

    fun getToken(): String {
        return prefs.getString("token", "") ?: ""
    }
}