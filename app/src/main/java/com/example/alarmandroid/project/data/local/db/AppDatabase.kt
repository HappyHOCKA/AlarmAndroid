package com.example.alarmandroid.project.data.local.db

import android.content.Context
import android.provider.Telephony
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.alarmandroid.project.data.local.convertors.typeConvertors
import com.example.alarmandroid.project.data.local.dao.AlarmDao
import com.example.alarmandroid.project.data.local.entities.AlarmInfo

@Database(entities = [AlarmInfo::class], version = 1, exportSchema = false)
@TypeConverters(typeConvertors::class)
abstract class AppDatabase : RoomDatabase(){
    abstract fun alarmDao() : AlarmDao

    companion object{
        @Volatile
        private var INSTANCE : AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "alarm_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}