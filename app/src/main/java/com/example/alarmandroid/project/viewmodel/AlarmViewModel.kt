package com.example.alarmandroid.project.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alarmandroid.project.data.local.dao.AlarmDao
import com.example.alarmandroid.project.data.local.entities.AlarmInfo
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AlarmViewModel(private val dao: AlarmDao) : ViewModel() {
    val allAlarms: StateFlow<List<AlarmInfo>> = dao.getAllAlarms()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addAlarm(alarm: AlarmInfo) {
        viewModelScope.launch {
            dao.insertAlarm(alarm)
        }
    }

    fun deleteAlarm(alarm: AlarmInfo) {
        viewModelScope.launch {
            dao.deleteAlarm(alarm)
        }
    }

    fun toggleAlarm(alarm: AlarmInfo, isActive: Boolean) {
        viewModelScope.launch {
            dao.updateAlarm(alarm.copy(isActive = isActive))
        }
    }
}