package com.example.alarmandroid.project.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.alarmandroid.project.data.local.entities.AlarmInfo
import kotlinx.coroutines.flow.Flow

@Dao
interface AlarmDao {
@Query("SELECT * FROM alarms")
fun getAllAlarms(): Flow<List<AlarmInfo>>

@Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlarm(alarm: AlarmInfo)

    @Update
    suspend fun updateAlarm(alarm: AlarmInfo)

    @Update
    suspend fun deletaAlarm(alarm: AlarmInfo)
}