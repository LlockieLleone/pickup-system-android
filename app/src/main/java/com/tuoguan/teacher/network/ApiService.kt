package com.tuoguan.teacher.network

import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @POST("/api/auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): LoginResponse

    @GET("/api/tasks/today")
    suspend fun getTodayTasks(
        @Query("teacherId") teacherId: Long
    ): List<TodayTaskResponse>

    @POST("/api/events/confirm")
    suspend fun confirmStudent(
        @Body request: ConfirmStudentRequest
    ): ConfirmStudentResponse
}