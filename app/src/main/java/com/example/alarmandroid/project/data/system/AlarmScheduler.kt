package com.example.alarmandroid.project.data.system

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import com.example.alarmandroid.project.data.models.AlarmType
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.util.Locale

public class AlarmScheduler {
    companion object {
        private const val TAG = "AlarmScheduler"
    }
    fun schedulerAlarm(context: Context, timeInMillisis: Long){

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        if (alarmManager == null)return

        val intent = Intent(context, AlarmReceiver::class.java)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (!alarmManager.canScheduleExactAlarms()){
                try {
                    val settingIntent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                    settingIntent.setData(Uri.parse("package:" + context.packageName))
                    settingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(settingIntent)
                }catch (e: Exception){
                    Log.w(TAG, "unableto open exact alarm settings", e)
                }
                alarmManager.set(AlarmManager.RTC_WAKEUP, timeInMillisis, pendingIntent)
                return
            }
        }
        try {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                timeInMillisis,
                pendingIntent
            )
        }catch (e : SecurityException){
            Log.w(TAG ,"Failed to set exact alarm", e)
            alarmManager.set(AlarmManager.RTC_WAKEUP, timeInMillisis, pendingIntent)
        }
    }
    fun schedulerAlarmAtTime(context: Context, timePickerStateInHours: Int, timePickerStateInMinute: Int, dayOfWeek: Set<DayOfWeek>, type: AlarmType){
        val currentTime = Calendar.getInstance()
        var targetTime = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, timePickerStateInHours)
            set(Calendar.MINUTE, timePickerStateInMinute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        if (dayOfWeek == null || dayOfWeek.isEmpty() || type == AlarmType.ONCE) {
            if (targetTime.before(currentTime)) {
                targetTime.add(java.util.Calendar.DATE, 1)
            }
            schedulerAlarm(context, targetTime.getTimeInMillis())
        } else {
            var clossestTime = Long.Companion.MAX_VALUE

            for (day in dayOfWeek) {
                val tempTarget = targetTime.clone() as java.util.Calendar
                tempTarget.set(java.util.Calendar.DAY_OF_WEEK, day.getValue())

                if (tempTarget.before(currentTime)) {
                    tempTarget.add(java.util.Calendar.WEEK_OF_YEAR, 1)
                }
                val timeInMillis = tempTarget.getTimeInMillis()
                if (timeInMillis < clossestTime) {
                    clossestTime = timeInMillis
                }
            }
            schedulerAlarm(context, clossestTime)
        }
    }

    fun dateTransfer(timePickerStateInHours: Int, timePickerStateInMinute: Int): String {
        var targetTime = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, timePickerStateInHours)
            set(Calendar.MINUTE, timePickerStateInMinute)
        }
            val today = "Today-"

            if (java.util.Calendar.getInstance().after(targetTime)) {
                targetTime.add(java.util.Calendar.DATE, 1)
                var today = "Tomorrow-"
            }
            val sdf = SimpleDateFormat("EEEE, d MMM", Locale.getDefault())

            return today + sdf.format(targetTime.getTime())
    }
}