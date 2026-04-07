package com.example.alarmandroid.project.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material3.ButtonGroup
import com.example.alarmandroid.project.data.local.entities.AlarmInfo
import com.example.alarmandroid.project.data.models.AlarmType
import com.example.alarmandroid.project.data.system.AlarmScheduler
import com.example.alarmandroid.project.ui.activeties.TimeItem
import com.example.alarmandroid.project.viewmodel.AlarmViewModel
import java.time.DayOfWeek

@Composable
fun ScrollableTimePicker(
    modifier: Modifier,
    viewModel: AlarmViewModel,
    goBack: () -> Boolean
) {
    val context = LocalContext.current
    val scheduler = AlarmScheduler()
    val scrollState = rememberScrollState()
    val hourState = rememberLazyListState(31)
    val minuteState = rememberLazyListState(60)
    val hour = listOf(
        "",
        "00",
        "01",
        "02",
        "03",
        "04",
        "05",
        "06",
        "07",
        "08",
        "09",
        "10",
        "11",
        "12",
        "13",
        "14",
        "15",
        "16",
        "17",
        "18",
        "19",
        "20",
        "21",
        "22",
        "23",
        "00",
        "01",
        "02",
        "03",
        "04",
        "05",
        "06",
        "07",
        "08",
        "09",
        "10",
        "11",
        "12",
        "13",
        "14",
        "15",
        "16",
        "17",
        "18",
        "19",
        "20",
        "21",
        "22",
        "23",
        "00",
        "01",
        "02",
        "03",
        "04",
        "05",
        "06",
        "07",
        "08",
        "09",
        "10",
        "11",
        "12",
        "13",
        "14",
        "15",
        "16",
        "17",
        "18",
        "19",
        "20",
        "21",
        "22",
        "23",
        "00",
        "01",
        "02",
        "03",
        "04",
        "05",
        "06",
        "07",
        "08",
        "09",
        "10",
        "11",
        "12",
        "13",
        "14",
        "15",
        "16",
        "17",
        "18",
        "19",
        "20",
        "21",
        "22",
        "23",
        ""
    )
    val minute = listOf(
        "",
        "00",
        "01",
        "02",
        "03",
        "04",
        "05",
        "06",
        "07",
        "08",
        "09",
        "10",
        "11",
        "12",
        "13",
        "14",
        "15",
        "16",
        "17",
        "18",
        "19",
        "20",
        "21",
        "22",
        "23",
        "24",
        "25",
        "26",
        "27",
        "28",
        "29",
        "30",
        "31",
        "32",
        "33",
        "34",
        "35",
        "36",
        "37",
        "38",
        "39",
        "40",
        "41",
        "42",
        "43",
        "44",
        "45",
        "46",
        "47",
        "48",
        "49",
        "50",
        "51",
        "52",
        "53",
        "54",
        "55",
        "56",
        "57",
        "58",
        "59",
        "00",
        "01",
        "02",
        "03",
        "04",
        "05",
        "06",
        "07",
        "08",
        "09",
        "10",
        "11",
        "12",
        "13",
        "14",
        "15",
        "16",
        "17",
        "18",
        "19",
        "20",
        "21",
        "22",
        "23",
        "24",
        "25",
        "26",
        "27",
        "28",
        "29",
        "30",
        "31",
        "32",
        "33",
        "34",
        "35",
        "36",
        "37",
        "38",
        "39",
        "40",
        "41",
        "42",
        "43",
        "44",
        "45",
        "46",
        "47",
        "48",
        "49",
        "50",
        "51",
        "52",
        "53",
        "54",
        "55",
        "56",
        "57",
        "58",
        "59",
        "00",
        "01",
        "02",
        "03",
        "04",
        "05",
        "06",
        "07",
        "08",
        "09",
        "10",
        "11",
        "12",
        "13",
        "14",
        "15",
        "16",
        "17",
        "18",
        "19",
        "20",
        "21",
        "22",
        "23",
        "24",
        "25",
        "26",
        "27",
        "28",
        "29",
        "30",
        "31",
        "32",
        "33",
        "34",
        "35",
        "36",
        "37",
        "38",
        "39",
        "40",
        "41",
        "42",
        "43",
        "44",
        "45",
        "46",
        "47",
        "48",
        "49",
        "50",
        "51",
        "52",
        "53",
        "54",
        "55",
        "56",
        "57",
        "58",
        "59",
        "00",
        "01",
        "02",
        "03",
        "04",
        "05",
        "06",
        "07",
        "08",
        "09",
        "10",
        "11",
        "12",
        "13",
        "14",
        "15",
        "16",
        "17",
        "18",
        "19",
        "20",
        "21",
        "22",
        "23",
        "24",
        "25",
        "26",
        "27",
        "28",
        "29",
        "30",
        "31",
        "32",
        "33",
        "34",
        "35",
        "36",
        "37",
        "38",
        "39",
        "40",
        "41",
        "42",
        "43",
        "44",
        "45",
        "46",
        "47",
        "48",
        "49",
        "50",
        "51",
        "52",
        "53",
        "54",
        "55",
        "56",
        "57",
        "58",
        "59",
        ""
    )
    val hourSnapBehavior = rememberSnapFlingBehavior(hourState)
    val minuteSnapBehavior = rememberSnapFlingBehavior(minuteState)
    val selectedHour = hourState.firstVisibleItemIndex % 24
    val selectedMinute = minuteState.firstVisibleItemIndex % 60
    val dayOfWeek = mutableSetOf<DayOfWeek>()

    var isAlarmSignalActive = true
    var isAlarmVibrationActive = true
    var isAlarmPauseActive = true

    Column(
        modifier.fillMaxSize(),
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

        Column(
            Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(24.dp)
                )
                .verticalScroll(scrollState)
                .padding(vertical = 15.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.surface),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    modifier = Modifier.padding(10.dp),
                    text = scheduler.dateTransfer(
                        hourState.firstVisibleItemIndex % 24,
                        minuteState.firstVisibleItemIndex % 60
                    )
                )
                Spacer(Modifier.weight(1f))
                IconButton(
                    onClick = {},
                    colors = IconButtonDefaults.iconButtonColors(MaterialTheme.colorScheme.surfaceVariant)

                )
                { Icon(Icons.Default.DateRange, null) };
            }
            Row(
                Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                ButtonGroup() {
                    Button(
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.surface),
                        onClick = { dayOfWeek.add(DayOfWeek.MONDAY) })
                    { Text("П", color = Color.White) }
                    Button(
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.surface),
                        onClick = { dayOfWeek.add(DayOfWeek.TUESDAY) })
                    { Text("В", color = Color.White) }
                    Button(
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.surface),
                        onClick = { dayOfWeek.add(DayOfWeek.WEDNESDAY) })
                    { Text("С", color = Color.White) }
                    Button(
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.surface),
                        onClick = { dayOfWeek.add(DayOfWeek.THURSDAY) })
                    { Text("Ч", color = Color.White) }
                    Button(
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.surface),
                        onClick = { dayOfWeek.add(DayOfWeek.FRIDAY) })
                    { Text("П", color = Color.White) }
                    Button(
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.surface),
                        onClick = { dayOfWeek.add(DayOfWeek.SATURDAY) })
                    { Text("С", color = Color.White) }
                    Button(
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.surface),
                        onClick = { dayOfWeek.add(DayOfWeek.SUNDAY) })
                    { Text("Н", color = Color.Red) }
                }
            }
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
            ) {
                var alarmName by remember { mutableStateOf("") }
                TextField(
                    value = alarmName,
                    onValueChange = { newText -> alarmName = newText },
                    label = { Text("Назва будильника") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface
                    )
                )
            }
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(end = 30.dp)
            ) {
                ListItem({ Text("Alarm signal") }, supportingContent = { Text(" ") })
                Spacer(Modifier.weight(1f))
                Switch(
                    checked = isAlarmSignalActive,
                    onCheckedChange = { AlarmSignalOff -> isAlarmSignalActive = AlarmSignalOff }
                )
            }
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(end = 30.dp)
            ) {
                ListItem({ Text("Vibration") }, supportingContent = { Text(" ") })
                Spacer(Modifier.weight(1f))
                Switch(
                    checked = isAlarmVibrationActive,
                    onCheckedChange = {}
                )
            }
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(end = 30.dp)
            ) {
                ListItem({ Text("Pause") }, supportingContent = { Text(" ") })
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
            Modifier.fillMaxWidth()
        ) {
            Button(
                modifier = Modifier.weight(1f),
                onClick = { goBack() },
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.background)
            ) {
                Text("Скасувати", color = Color.White)
            }

            Button(
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.background),
                onClick = {
                    scheduler.schedulerAlarmAtTime(
                        context,
                        selectedHour,
                        selectedMinute,
                        dayOfWeek,
                        AlarmType.ONCE
                    )
                    val date = scheduler.dateTransfer(selectedHour, selectedMinute)
                    val formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute)

                    val newAlarm = AlarmInfo(
                        time = formattedTime,
                        date = date,
                    )

                    viewModel.addAlarm(newAlarm)

                    goBack()
                }
            ) {
                Text("Зберегти", color = Color.White)
            }
        }
    }
}
