package com.example.alarmandroid.project.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.alarmandroid.project.data.local.dao.AlarmDao

class AlarmViewModelFactory(private val repository: AlarmDao): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AlarmViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return AlarmViewModel(repository) as T
    }
    throw IllegalArgumentException("Unkown ViewModel class")
    }
}