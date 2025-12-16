package com.example.alarmandroid.project.ui.activeties

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import java.util.concurrent.TimeUnit
import com.example.alarmandroid.R
import com.example.alarmandroid.project.data.system.AlarmScheduler
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent{

        val testButton: Button = findViewById(R.id.button)
        val myTextView: TextView = findViewById(R.id.textView)

        testButton.setOnClickListener {
            myTextView.text = "Привіт! Я Kotlin!"

            Toast.makeText(this, "Кнопку натиснуто!", Toast.LENGTH_SHORT).show()

            val currentTime = System.currentTimeMillis()
            val twoMinutesInMillis = TimeUnit.MINUTES.toMillis(2)
            val futureTime = currentTime + twoMinutesInMillis

            val scheduler = AlarmScheduler()

            scheduler.schedulerAlarm(this, futureTime)
        }
        }
    }
}