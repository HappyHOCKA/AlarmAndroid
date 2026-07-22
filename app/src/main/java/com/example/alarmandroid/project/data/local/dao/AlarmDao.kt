package com.example.alarmandroid.project.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.alarmandroid.project.data.local.convertors.typeConvertors
import com.example.alarmandroid.project.data.local.entities.AlarmInfo
import com.example.alarmandroid.project.data.models.AlarmType
import kotlinx.coroutines.flow.Flow


@Dao
interface AlarmDao {
    @Query("SELECT * FROM alarms")
    fun getAllAlarms(): Flow<List<AlarmInfo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlarm(alarm: AlarmInfo): Unit

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlarms(alarms: List<AlarmInfo>): Unit

    @Update
    suspend fun updateAlarm(alarms: AlarmInfo): Unit

    @Update
    suspend fun updateAlarms(alarms: List<AlarmInfo>): Unit

    @Delete
    suspend fun deleteAlarm(alarm: AlarmInfo): Unit

    @Delete
    suspend fun deleteAlarms(alarms: List<AlarmInfo>): Unit
}