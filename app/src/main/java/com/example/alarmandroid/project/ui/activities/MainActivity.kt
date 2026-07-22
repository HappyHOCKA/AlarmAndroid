@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.alarmandroid.project.ui.activeties

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.alarmandroid.project.data.system.AlarmScheduler
import com.example.alarmandroid.project.ui.theme.AlarmAndroidTheme
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.graphics.Color
import com.example.alarmandroid.project.data.local.db.AppDatabase
import java.time.DayOfWeek
import com.example.alarmandroid.project.data.local.entities.AlarmInfo
import com.example.alarmandroid.project.data.models.AlarmType
import com.example.alarmandroid.project.ui.screens.AlarmScreen
import com.example.alarmandroid.project.ui.screens.ScrollableTimePicker
import com.example.alarmandroid.project.viewmodel.AlarmViewModel
import com.example.alarmandroid.project.viewmodel.AlarmViewModelFactory

class MainActivity : ComponentActivity() {
    companion object {
        private const val REQ_POST_NOTIF = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = AppDatabase.getDatabase(applicationContext)
        val alarmDao = db.alarmDao()

        val viewModel: AlarmViewModel by viewModels { AlarmViewModelFactory(alarmDao) }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            )
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    REQ_POST_NOTIF
                )
        }

        setContent {
           var isDarkMode by remember { mutableStateOf(true) }
            AlarmAndroidTheme(isDarkMode) {
                val context = LocalContext.current
                AlarmSchedulerApp( viewModel, isDarkMode) { isDarkMode = it}
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQ_POST_NOTIF) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Notification permission granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Notification permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @Composable
    fun AlarmSchedulerApp(
        viewModel: AlarmViewModel,
        isDarkMode: Boolean,
        onToggle: (Boolean) -> Unit
    ) {
        val navController = rememberNavController()
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            Scaffold(
                floatingActionButton = {
                    if(currentRoute != "create_new_alarm")
                        FloatingActionButton(onClick = {
                            navController.navigate("create_new_alarm")
                        }) { Icon(Icons.Default.Add, null) }
                },
                bottomBar = {
                    NavigationBar {
                        NavigationBarItem(
                            selected = currentRoute == "alarms",
                            onClick = { navController.navigate("alarms") },
                            icon = { Icon(Icons.Default.Alarm, null) },
                            label = { Text("Будильники") }
                        )

                        NavigationBarItem(
                            selected = currentRoute == "scheduler",
                            onClick = { navController.navigate("scheduler") },
                            icon = { Icon(Icons.Default.DateRange, null) },
                            label = { Text("Планувальник") }
                        )

                        NavigationBarItem(
                            selected = currentRoute == "setting",
                            onClick = { navController.navigate("settings") },
                            icon = { Icon(Icons.Default.Settings, null) },
                            label = { Text("Налаштування") }
                        )
                    }
                }
            ) { innerPadding ->
                NavHost(
                    navController = navController,
                    startDestination = "alarms",
                    modifier = Modifier.padding(innerPadding)
                ) {
                    composable("alarms") {
                        AlarmScreen(viewModel, Modifier.padding(innerPadding) )
                    }
                    composable("scheduler") {
                        SchedulerScreen(Modifier.padding(innerPadding))
                    }
                    composable("settings") {
                        SettingsScreen(Modifier.padding(innerPadding), isDarkMode, onToggle = onToggle)
                    }
                    composable("create_new_alarm") {
                        ScrollableTimePicker( Modifier.padding(innerPadding),viewModel, goBack = { navController.popBackStack() })
                    }
                }
            }
        }
    }

    @Composable
    fun SchedulerScreen(modifier: Modifier) {
        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Scheduler screen")
        }
    }

    @Composable
    fun SettingsScreen(modifier: Modifier, isDarkMode: Boolean, onToggle: (Boolean) -> Unit
    ) {
        Column(modifier = modifier.padding(10.dp)) {
            Row(modifier = Modifier.padding(16.dp)) {
                Text(
                    "Dark Theme",
                )
                Spacer(Modifier.weight(1f))
                Switch(
                    checked = isDarkMode,
                    onCheckedChange = { onToggle(it) }
                )
            }
        }
    }

    @Composable
    fun AlarmItem(alarm: AlarmInfo) {
        val context = LocalContext.current
        val scheduler = AlarmScheduler()
        var isChecked by remember { mutableStateOf(alarm.isActive) }

        Card(
            modifier = Modifier
                .padding(10.dp),
            RoundedCornerShape(24.dp),
            CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    alarm.time,
                    style = MaterialTheme.typography.displaySmall
                )
                Spacer(Modifier.weight(1f))
                Text(
                    alarm.date,
                    modifier = Modifier.padding(30.dp),
                )
                Switch(
                    checked = isChecked,
                    onCheckedChange = { newValue -> isChecked = newValue }
                )
            }
        }
    }
    @Composable
    fun TimeItem(time: String){
        Card(Modifier
            .fillMaxWidth()
            .height(95.dp)
            .padding(vertical = 10.dp),
            colors =CardDefaults.cardColors(containerColor = Color.Transparent),
            elevation = CardDefaults.cardElevation(0.dp)
        ) {
            Box(Modifier
                .fillMaxWidth()
                .height(95.dp),
                Alignment.Center
            ){
                Text(time, style = MaterialTheme.typography.displayMedium)
            }
        }
    }
