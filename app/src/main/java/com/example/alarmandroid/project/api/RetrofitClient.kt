package com.example.alarmandroid.project.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import kotlin.getValue
import kotlin.jvm.java

object RetrofitClient {
     private const val BASE_URL = "http://10.0.2.2:8080/"

     val apiService: AlarmApiService by lazy {
          Retrofit.Builder()
               .baseUrl(BASE_URL)
               .addConverterFactory(GsonConverterFactory.create())
               .build()
               .create(AlarmApiService::class.java)
     }
}

