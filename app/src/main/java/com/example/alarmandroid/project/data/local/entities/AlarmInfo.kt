package com.example.alarmandroid.project.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.alarmandroid.project.data.models.AlarmType

@Entity(tableName = "alarms")
data class AlarmInfo(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val time: String,
    val label: String = "",
    val repeatOnDayOfWeek: Set<Int> = emptySet(),//1 = Sunday, 7 = Saturday
    val type: AlarmType = AlarmType.ONCE,

    var date: String,
    var isActive: Boolean = true,
    var isAlarmPauseActive: Boolean = true,

    var isAlarmSignalActive: Boolean = true,
    //val ringtoneUri: String,
    //  var volume: Int = 70,

    var isAlarmVibrationActive: Boolean = true,
//    var vibrationPattern: String,

)