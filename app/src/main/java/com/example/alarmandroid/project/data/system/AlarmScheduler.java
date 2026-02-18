package com.example.alarmandroid.project.data.system;

import android.content.Context;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.net.Uri;
import android.util.Log;

import com.example.alarmandroid.project.data.models.AlarmType;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.util.Calendar;
import java.util.Set;

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
        public void schedulerAlarmAtTime(Context context, int timePickerStateInHours, int timePickerStateInMinutes, Set<DayOfWeek> dayOfWeek, AlarmType type){
        Calendar currentTime = Calendar.getInstance();
        Calendar targetTime = Calendar.getInstance();

        targetTime.set(Calendar.HOUR_OF_DAY, timePickerStateInHours);
        targetTime.set(Calendar.MINUTE, timePickerStateInMinutes);
        targetTime.set(Calendar.SECOND, 0);
        targetTime.set(Calendar.MILLISECOND, 0);

        if(dayOfWeek == null || dayOfWeek.isEmpty() || type == AlarmType.ONCE){
            if (targetTime.before(currentTime)){
                targetTime.add(Calendar.DATE, 1);
            }
            schedulerAlarm(context, targetTime.getTimeInMillis());
        } else {
                long clossestTime = Long.MAX_VALUE;

                for(DayOfWeek day : dayOfWeek){
                    Calendar tempTarget = (Calendar) targetTime.clone();
                    tempTarget.set(Calendar.DAY_OF_WEEK, day.getValue());

                    if (tempTarget.before(currentTime)){
                        tempTarget.add(Calendar.WEEK_OF_YEAR, 1);
                }
                    long timeInMillis = tempTarget.getTimeInMillis();
                    if (timeInMillis < clossestTime){clossestTime = timeInMillis;
                    }
                }
                schedulerAlarm(context, clossestTime);
        }
    }

    public String dateTransfer(int timePickerStateInHours, int timePickerStateInMinutes){
        Calendar targetTimeForAlarms = Calendar.getInstance();

        targetTimeForAlarms.set(Calendar.HOUR_OF_DAY, timePickerStateInHours);
        targetTimeForAlarms.set(Calendar.MINUTE, timePickerStateInMinutes);

        String today = "Today-";

        if(Calendar.getInstance().after(targetTimeForAlarms)) {
            targetTimeForAlarms.add(Calendar.DATE, 1);
            today = "Tomorrow-";
        }
        java.text.SimpleDateFormat sdf = new SimpleDateFormat("EEEE, d MMM", java.util.Locale.getDefault());

        return today + sdf.format(targetTimeForAlarms.getTime());
    }
}
