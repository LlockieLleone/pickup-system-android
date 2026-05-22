package com.tuoguan.teacher.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL = "http://10.0.2.2:8080/"

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val originalRequest = chain.request()

            val newRequest = if (TokenStore.token.isNotBlank()) {
                originalRequest.newBuilder()
                    .addHeader("Authorization", "Bearer ${TokenStore.token}")
                    .build()
            } else {
                originalRequest
            }

            chain.proceed(newRequest)
        }
        .build()

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}