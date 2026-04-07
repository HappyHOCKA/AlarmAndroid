package com.example.alarmandroid.project.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.alarmandroid.project.ui.activeties.AlarmItem
import com.example.alarmandroid.project.viewmodel.AlarmViewModel
    @Composable
    fun AlarmScreen(
        viewModel: AlarmViewModel,
        modifier: Modifier = Modifier) {
        val alarms by viewModel.allAlarms.collectAsState()

        LazyColumn(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(alarms) { alarm ->
                AlarmItem(alarm)
            }
        }
    }