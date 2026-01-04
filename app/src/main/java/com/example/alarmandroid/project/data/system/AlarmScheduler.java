package com.example.alarmandroid.project.data.system;

import android.content.Context;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.net.Uri;
import android.util.Log;

public class AlarmScheduler {
    private static final String TAG = "AlarmScheduler";
    public void schedulerAlarm(Context context, long timeInMillis){

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager == null)return;

        Intent intent = new Intent(context, AlarmReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
                );
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!alarmManager.canScheduleExactAlarms()){
                try{
                Intent settingsIntent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                settingsIntent.setData(Uri.parse("package:" + context.getPackageName()));
                settingsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(settingsIntent);
                }catch (Exception e){
                    Log.w(TAG, "Unable to open exact alarm settings", e);
                }
                alarmManager.set(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);
                return;
            }
            }
        try {
            alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    timeInMillis,
                    pendingIntent
            );
        }catch (SecurityException e){
            Log.w(TAG, "Failed to set exact alarm", e);
            alarmManager.set(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);
        }
        }
}
