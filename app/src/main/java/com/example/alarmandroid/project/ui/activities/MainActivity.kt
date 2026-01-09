package com.example.alarmandroid.project.ui.activeties

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import com.example.alarmandroid.project.data.system.AlarmScheduler
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import java.util.concurrent.TimeUnit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Switch
import androidx.compose.material3.TimePicker
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList

data class AlarmInfo(val id: Int, val time: String, val isActive: Boolean)

class MainActivity : ComponentActivity() {

    companion object{
        private const val REQ_POST_NOTIF = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED)
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), REQ_POST_NOTIF)
        }

        setContent{
            AlarmSchedulerScreen()
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
    fun AlarmSchedulerScreen(){
        val scheduler = AlarmScheduler()
        val context = LocalContext.current
        val navController = rememberNavController()

        var AlarmList = remember { mutableStateListOf<AlarmInfo>() }

        Scaffold(
            bottomBar = {
                NavigationBar {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = navBackStackEntry?.destination?.route

                    NavigationBarItem(
                        selected = currentRoute == "alarms",
                        onClick = { navController.navigate("alarms")},
                        icon = {Icon(Icons.Default.Alarm, null)},
                        label = {Text("Будильники")}
                        )

                    NavigationBarItem(
                        selected = currentRoute == "scheduler",
                        onClick = {navController.navigate("scheduler")},
                        icon = {Icon(Icons.Default.DateRange, null)},
                        label = {Text("Планувальник")}
                    )

                    NavigationBarItem(
                        selected = currentRoute == "setting",
                        onClick = { navController.navigate("settings")},
                        icon = {Icon(Icons.Default.Settings, null)},
                        label = {Text("Налаштування")}
                    )
                    }
                },
            floatingActionButton = {
                FloatingActionButton(onClick = {
                    navController.navigate("create_new_alarm")
                }) { Icon(Icons.Default.Add, null)}
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = "alarms",
                modifier = Modifier.padding(innerPadding)
            ){
                composable("alarms") {
                    AlarmSchedulerContent(innerPadding, scheduler, context, AlarmList)
                }
                composable("scheduler"){
                    SchedulerScreen(innerPadding)
                }
                composable("settings") {
                    SettingsScreen(innerPadding)
                }
                composable("create_new_alarm") {
                    createNewAlarmScreen(innerPadding, AlarmList)
                }
            }
        }
    }
    @Composable
    fun AlarmSchedulerContent(innerPadding: PaddingValues, scheduler: AlarmScheduler, context: Context, alarmList: SnapshotStateList<AlarmInfo> ){
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            items(alarmList){ alarm ->
                AlarmItem(alarm)
            }
        }
    }

    @Composable
    fun SchedulerScreen(innerPadding: PaddingValues){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Scheduler screen")
        }
    }

    @Composable
    fun SettingsScreen(innerPadding: PaddingValues){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Setting screen")
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun createNewAlarmScreen(innerPadding: PaddingValues, alarmList: SnapshotStateList<AlarmInfo>){
        val context = LocalContext.current
        val scheduler = AlarmScheduler()

        var showTimePicker by remember { mutableStateOf(false) }
        val timePickerState = rememberTimePickerState(is24Hour = true)
        val formattedTime = String.format("%02d:%02d", timePickerState.hour, timePickerState.minute)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Button(onClick = {showTimePicker = true}){
                Text("Оберіть час для будильника")
            }
            if (showTimePicker) {
                AlertDialog(
                    onDismissRequest = {showTimePicker = false},
                    confirmButton = {
                        TextButton(onClick = {
                            scheduler.schedulerAlarmAtTime(context, timePickerState.hour, timePickerState.minute)
                            alarmList.add(
                                AlarmInfo(
                                    id = alarmList.size,
                                    time = formattedTime,
                                    isActive = true)
                            )
                        }) { Text("OK")}
                    },
                    text = { TimePicker(state = timePickerState) }
                )
            }
        }
    }
    @Composable
    fun AlarmItem(alarm: AlarmInfo){
        val context = LocalContext.current
        val scheduler = AlarmScheduler()
        var isChecked by remember { mutableStateOf(alarm.isActive) }

        Card(
            modifier = Modifier.padding(10.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(alarm.time)
                Spacer(Modifier.weight(1f))
                Switch(
                    checked = isChecked,
                    onCheckedChange = {  newValue -> isChecked = newValue }
                )
            }
        }
    }
}
