package com.example.alarm

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.alarm.databinding.ActivityMainBinding
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    lateinit var alarmManager: AlarmManager
    lateinit var calender: Calendar
    lateinit var picker: MaterialTimePicker
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.setTime.setOnClickListener() {
            showTimePicker()
        }
        binding.setAlarm.setOnClickListener() {
            setAlarm()
        }


        binding.cancelBtn.setOnClickListener() {
            cancelAlarm()
        }

    }


    private fun showTimePicker() {
        picker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setHour(12)
            .setMinute(0)
            .setTitleText("Selecet Alarm time")
            .build()

        picker.show(supportFragmentManager, "foxandroid")

        picker.addOnPositiveButtonClickListener() {
            if (picker.hour > 12) {
                binding.timer.text = String.format("%02d:%02d PM", picker.hour - 12, picker.minute)
            } else {
                binding.timer.text = String.format("%02d:%02d AM", picker.hour, picker.minute)
            }

            calender = Calendar.getInstance()
            calender[Calendar.HOUR_OF_DAY] = picker.hour
            calender[Calendar.MINUTE] = picker.minute
            calender[Calendar.SECOND] = 0
            calender[Calendar.MILLISECOND] = 0


        }
    }

    private fun setAlarm() {
        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calender.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
        Toast.makeText(this, "secueesfully set", Toast.LENGTH_SHORT).show()


    }

    private fun cancelAlarm() {
        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
        Toast.makeText(this, "secueesfully cancel", Toast.LENGTH_SHORT).show()

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.BASE) {
            val name: CharSequence = "foxandroidReminderChannel"
            val description = "Channel For Alarm Manager"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel=NotificationChannel("foxandroid",name, importance)
            channel.description=description
            val notificationManager= getSystemService(
                NotificationManager::class.java
            )

            notificationManager.createNotificationChannel(channel)

        }
    }

}

