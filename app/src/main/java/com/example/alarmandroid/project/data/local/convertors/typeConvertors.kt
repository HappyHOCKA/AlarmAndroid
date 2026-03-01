package com.example.alarmandroid.project.data.local.convertors

import androidx.room.TypeConverter
import com.example.alarmandroid.project.data.models.AlarmType

class typeConvertors {
    @TypeConverter
    fun AlarmTypeToString(alarmType: AlarmType): String {
        return alarmType.name
    }

    @TypeConverter
    fun StringToAlarmType(type: String): AlarmType {
        return try {
            AlarmType.valueOf(type)
        } catch (e: IllegalArgumentException) {
            AlarmType.ONCE
        }
    }

    @TypeConverter
    fun SetIntToString(set: Set<Int>): String{
        return set.joinToString(",")
    }

    @TypeConverter
    fun StringToSetInt(string: String): Set<Int>{
        if (string.isEmpty()) return emptySet()
        return string
            .split(",")
            .mapNotNull { it.toIntOrNull() }
            .toSet()
    }
}
