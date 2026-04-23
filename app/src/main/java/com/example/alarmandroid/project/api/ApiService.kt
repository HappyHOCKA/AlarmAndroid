package com.example.alarmandroid.project.api

import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.POST
import retrofit2.http.DELETE
import retrofit2.http.Body
import retrofit2.http.Path
import com.example.alarmandroid.project.data.repository.Dto.AlarmDTO
import retrofit2.Response

interface ApiService {
    @GET("api/alarms")
    suspend fun getAllAlarms(): Response<List<AlarmDTO>>

    @GET("api/alarm/{id}")
    suspend fun getAlarmById(@Path("id") id: Int): AlarmDTO

    @POST("api/alarm")
    suspend fun createAlarm(@Body alarm: AlarmDTO): AlarmDTO

    @PUT("api/alarm/{id}")
    suspend fun updateAlarm(@Path("id") id: Int, @Body alarm: AlarmDTO): AlarmDTO

    @DELETE("api/alarms/{id}")
    suspend fun deleteAlarms(@Path("id") id: Int): Void
}