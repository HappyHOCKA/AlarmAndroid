package com.example.alarmandroid.project.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alarmandroid.project.api.RetrofitClient
import com.example.alarmandroid.project.data.local.entities.AlarmInfo
import com.example.alarmandroid.project.data.models.AlarmType
import com.example.alarmandroid.project.data.repository.Dto.AlarmDTO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AlarmViewModel : ViewModel() {
    private val _alarms = MutableStateFlow<List<AlarmInfo>>(emptyList())
    val allAlarms: StateFlow<List<AlarmInfo>> = _alarms

    init {
        fetchAlarms()
    }

    fun fetchAlarms(){
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.getAlarms()
                _alarms.value = response
            }catch (e: Exception){
        }
    }
    }
    fun

    fun AlarmDTO.toEntity(): AlarmInfo {
        return AlarmInfo(
            time = this.time,
            isActive = this.isActive,
            type = if (this.alarmType == "ONCE") AlarmType.ONCE else AlarmType.RECURRING,
            id = this.id,
            date = this.date,
            )
    }
}