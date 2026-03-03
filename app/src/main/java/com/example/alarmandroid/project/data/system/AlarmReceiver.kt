package com.example.alarmandroid.project.data.system

import android.R
import android.util.Log
import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import androidx.core.app.NotificationCompat;

public class AlarmReceiver : BroadcastReceiver() {
    companion object {
        public const val TAG = "AlarmReceiver"
        public const val CHANNEL_ID = "ALARM_CHANNEL"
    }
    override fun onReceive(context: Context, intent: Intent){
       val soundPlayerIntent = Intent(context, AlarmReceiver::class.java)

        Log.d(TAG,"Work")
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
        if (notificationManager == null) {
            Log.w("TAG", "Notification Manager is null")
            return
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Alarm Notifications",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
            context.startForegroundService(soundPlayerIntent)
        } else {
            context.startService(soundPlayerIntent)
        }
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_lock_idle_alarm)
            .setContentTitle("Alarm")
            .setContentText("Your alarm is ringing!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        val notification = builder.build()
        notificationManager.notify(1, notification)
    }
}