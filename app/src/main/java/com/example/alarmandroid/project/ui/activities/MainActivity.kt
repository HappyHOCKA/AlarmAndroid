@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.alarmandroid.project.ui.activeties

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.pdf.models.ListItem
import android.icu.util.Calendar
import android.media.Ringtone
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.material3.AlertDialog
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
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
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
import androidx.wear.compose.material3.ButtonGroup
import java.time.DayOfWeek
import com.example.alarmandroid.project.data.local.entities.AlarmInfo
import com.example.alarmandroid.project.data.models.AlarmType

class MainActivity : ComponentActivity() {
    companion object {
        private const val REQ_POST_NOTIF = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
    fun AlarmSchedulerScreen() {
        var isDarkMode by remember { mutableStateOf(true) }

        AlarmAndroidTheme(isDarkMode) {
            val scheduler = AlarmScheduler()
            val context = LocalContext.current
            val navController = rememberNavController()
            val scrollState = rememberScrollState()
            val scope = rememberCoroutineScope()
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            var AlarmList = remember { mutableStateListOf<AlarmInfo>() }


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
                        AlarmSchedulerContent(innerPadding, scheduler, context, AlarmList)
                    }
                    composable("scheduler") {
                        SchedulerScreen(innerPadding)
                    }
                    composable("settings") {
                        SettingsScreen(innerPadding, isDarkMode, onToggle = { isDarkMode = it })
                    }
                    composable("create_new_alarm") {
                        ScrollableTimePicker(innerPadding, AlarmList, navController)
                    }
                }
            }
        }
    }

    @Composable
    fun AlarmSchedulerContent(innerPadding: PaddingValues, scheduler: AlarmScheduler, context: Context, alarmList: SnapshotStateList<AlarmInfo>) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(alarmList) { alarm ->
                AlarmItem(alarm)
            }
        }
    }

    @Composable
    fun SchedulerScreen(innerPadding: PaddingValues) {
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
    fun SettingsScreen(innerPadding: PaddingValues, isDarkMode: Boolean, onToggle: (Boolean) -> Unit
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
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
    fun ScrollableTimePicker(innerPadding: PaddingValues, alarmList: SnapshotStateList<AlarmInfo>, navController: NavController) {
        val context = LocalContext.current
        val scheduler = AlarmScheduler()
        val scrollState = rememberScrollState()
        val hourState = rememberLazyListState(31)
        val minuteState = rememberLazyListState(60)
        val hour = listOf("","00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23","00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23","00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23","00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "")
        val minute = listOf("","00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40","41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56","57", "58", "59","00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40","41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56","57", "58", "59","00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40","41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56","57", "58", "59","00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40","41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56","57", "58", "59", "")
        val hourSnapBehavior = rememberSnapFlingBehavior(hourState)
        val minuteSnapBehavior = rememberSnapFlingBehavior(minuteState)
        val selectedHour = hourState.firstVisibleItemIndex % 24
        val selectedMinute = minuteState.firstVisibleItemIndex % 60
        val dayOfWeek = mutableSetOf<DayOfWeek>()

        var isAlarmSignalActive = true
        var isAlarmVibrationActive = true
        var isAlarmPauseActive = true

Column(
    Modifier.fillMaxSize(),
    horizontalAlignment = Alignment.CenterHorizontally
) {
    Row(
        Modifier
            .height(300.dp)
            .fillMaxWidth()
            .padding(horizontal = 50.dp, vertical = 7.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        LazyColumn(
            Modifier
                .weight(2f)
                .padding(vertical = 5.dp),
            state = hourState,
            flingBehavior = hourSnapBehavior,

            ) {
            items(hour) { item ->
                TimeItem(item)
            }
        }

        Text(
            ":",
            style = MaterialTheme.typography.displayLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )

        LazyColumn(
            Modifier
                .weight(2f)
                .padding(vertical = 5.dp),
            state = minuteState,
            flingBehavior = minuteSnapBehavior
        ) {
            items(minute) { item ->
                TimeItem(item)
            }
        }
    }

    Column(Modifier
        .fillMaxWidth()
        .weight(1f)
        .background(
            color = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(24.dp)
        )
        .verticalScroll(scrollState)
        .padding(vertical = 15.dp)
    ) {
        Row( modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.surface),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text( modifier = Modifier.padding(10.dp) ,text = scheduler.dateTransfer(hourState.firstVisibleItemIndex % 24, minuteState.firstVisibleItemIndex % 60))
            Spacer(Modifier.weight(1f))
            IconButton(
                onClick = {},
                colors = IconButtonDefaults.iconButtonColors(MaterialTheme.colorScheme.surfaceVariant)

            )
            {Icon(Icons.Default.DateRange, null)};
        }
        Row(Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)) {
            ButtonGroup() {
                Button(colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.surface),
                    onClick = {dayOfWeek.add(DayOfWeek.MONDAY) })
                {Text("П", color = Color.White)}
                Button(colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.surface),
                    onClick = {dayOfWeek.add(DayOfWeek.TUESDAY) })
                {Text("В", color = Color.White)}
                Button(colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.surface),
                    onClick = {dayOfWeek.add(DayOfWeek.WEDNESDAY) })
                {Text("С", color = Color.White)}
                Button(colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.surface),
                    onClick = {dayOfWeek.add(DayOfWeek.THURSDAY) })
                {Text("Ч", color = Color.White)}
                Button(colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.surface),
                    onClick = {dayOfWeek.add(DayOfWeek.FRIDAY) })
                {Text("П", color = Color.White)}
                Button(colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.surface),
                    onClick = {dayOfWeek.add(DayOfWeek.SATURDAY) })
                {Text("С", color = Color.White)}
                Button(colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.surface),
                    onClick = {dayOfWeek.add(DayOfWeek.SUNDAY) })
                {Text("Н", color = Color.Red )}
            }
        }
        Row(Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)) {
            var alarmName by remember { mutableStateOf("") }
            TextField(value = alarmName,
                onValueChange = { newText -> alarmName = newText },
                label = { Text("Назва будильника") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(focusedContainerColor = MaterialTheme.colorScheme.surface, unfocusedContainerColor = MaterialTheme.colorScheme.surface)
            )
        }
        Row(Modifier
            .fillMaxWidth()
            .padding(end = 30.dp)) {
            ListItem({Text("Alarm signal")}, supportingContent = {Text(" ")})
            Spacer(Modifier.weight(1f))
            Switch(
                checked = isAlarmSignalActive,
                onCheckedChange = {AlarmSignalOff -> isAlarmSignalActive = AlarmSignalOff}
            )
        }
        Row(Modifier
            .fillMaxWidth()
            .padding(end = 30.dp)) {
            ListItem({Text("Vibration")}, supportingContent = {Text(" ")})
            Spacer(Modifier.weight(1f))
            Switch(
                checked = isAlarmVibrationActive,
                onCheckedChange = {}
            )
        }
        Row(Modifier
            .fillMaxWidth()
            .padding(end = 30.dp)) {
            ListItem({Text("Pause")}, supportingContent = {Text(" ")})
            Spacer(Modifier.weight(1f))
            Switch(
                checked = isAlarmPauseActive,
                onCheckedChange = {}
            )
        }
        Row(Modifier.fillMaxWidth()) {
            Text("Notification background", modifier = Modifier.padding(start = 20.dp))
        }
    }

    Row(
        Modifier.fillMaxWidth(),

    ) {
        Button(modifier = Modifier.weight(1f),
            onClick = { navController.popBackStack() },
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.background)) {
            Text("Скасувати", color = Color.White)
        }

        Button(
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.background),
            onClick = {
                scheduler.schedulerAlarmAtTime(context, selectedHour, selectedMinute, dayOfWeek, AlarmType.ONCE)
                val date = scheduler.dateTransfer(selectedHour, selectedMinute)
                val formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute)

                alarmList.add(AlarmInfo( time = formattedTime, date = date  ))

                navController.popBackStack()
            }
        ) {
            Text("Зберегти", color = Color.White)
        }
    } }
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
}
