package com.example.alarmandroid.project.data.repository.Dto;

import com.google.gson.annotations.SerializedName;

data class AlarmDTO (
    @SerializedName("id") val id: String? = null,
@SerializedName("time") val time: String,
@SerializedName("date") val date: String,
@SerializedName("isActive") val isActive: Boolean = false,
@SerializedName("alarmType") val alarmType: String = "ONCE",
@SerializedName("dayOfWeek") val dayOfWeek: String = ""
)
