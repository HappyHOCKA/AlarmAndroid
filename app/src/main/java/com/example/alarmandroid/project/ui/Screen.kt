package com.example.alarmandroid.project.ui

sealed class Screen(val route: String) {
    object AlarmScreen: Screen("alarm_screen")
    object AddAlarm: Screen("add_alarm_screen")
    object SettingScreen: Screen("setting_screen")
    object SchedulerScreen: Screen("scheduler_screen")
}