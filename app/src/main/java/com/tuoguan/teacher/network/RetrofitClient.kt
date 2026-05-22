package com.tuoguan.teacher.network

import com.tuoguan.teacher.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private val BASE_URL = BuildConfig.BASE_URL

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
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}