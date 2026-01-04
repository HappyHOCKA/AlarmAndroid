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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Settings
import com.example.alarmandroid.project.data.system.AlarmScheduler
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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


data class NavItems(val label: String, val icon: ImageVector)

    val navItems = listOf(
        NavItems("Будтльники", Icons.Default.Alarm),
        NavItems("Планувальник", Icons.Default.DateRange),
        NavItems("Налаштування", Icons.Default.Settings)
    )

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
                    AlarmSchedulerContent(innerPadding, scheduler, context)
                }
                composable("scheduler"){
                    SchedulerScreen(innerPadding)
                }
                composable("settings") {
                    SettingsScreen(innerPadding)
                }
                composable("create_new_alarm") {
                    createNewAlarmScreen(innerPadding)
                }
            }
        }
    }
    @Composable
    fun AlarmSchedulerContent(innerPadding: PaddingValues, scheduler: AlarmScheduler, context: Context){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            var textViewText by remember { mutableStateOf("Alarm is ready") }

            Text(
                text = textViewText,
                modifier = Modifier.padding(bottom = 20.dp)
            )

            Button(onClick = {
                textViewText = "Привіт! Я Kotlin!"
                Toast.makeText(context, "Кнопку натиснуто!", Toast.LENGTH_SHORT).show()

                val currentTime = System.currentTimeMillis()
                val twoMinutesInMillis = TimeUnit.SECONDS.toMillis(10)
                val futureTime = currentTime + twoMinutesInMillis
                scheduler.schedulerAlarm(context, futureTime)
            }){
                Text("Test")
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

    @Composable
    fun createNewAlarmScreen(innerPadding: PaddingValues){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Add Alarm Screen")
        }
    }
}
