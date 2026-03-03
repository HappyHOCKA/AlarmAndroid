package com.example.alarmandroid.project.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alarmandroid.project.data.local.dao.AlarmDao
import com.example.alarmandroid.project.data.local.entities.AlarmInfo
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AlarmViewModel(private val alarmDao: AlarmDao): ViewModel() {
    val allAlarms: StateFlow<List<AlarmInfo>> = alarmDao.getAllAlarms().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun addAlarm(alarm: AlarmInfo){
        viewModelScope.launch {
            alarmDao.insertAlarm(alarm)
        }
    }

    fun addAlarms(alarms: List<AlarmInfo>){
        viewModelScope.launch {
            alarmDao.insertAlarms(alarms)
        }
    }

    fun deleteAlarm(alarm: AlarmInfo){
        viewModelScope.launch {
            alarmDao.deletaAlarm(alarm)
        }
    }

    fun deleteAlarms(alarms: List<AlarmInfo>){
        viewModelScope.launch {
            alarmDao.deletaAlarms(alarms)
        }
    }

    fun updateAlarm(alarm: AlarmInfo){
        viewModelScope.launch {
            alarmDao.updateAlarm(alarm)
        }
    }

    fun updateAlarms(alarms: List<AlarmInfo>){
        viewModelScope.launch {
            alarmDao.updateAlarms(alarms)
        }
    }
}