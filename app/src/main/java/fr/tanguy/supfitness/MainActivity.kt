package fr.tanguy.supfitness

import android.os.Build
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.room.Room
import androidx.room.RoomDatabase
import fr.tanguy.supfitness.database.AppDatabase
import fr.tanguy.supfitness.databinding.ActivityMainBinding
import android.app.NotificationManager

import android.app.NotificationChannel
import android.app.AlarmManager

import android.app.PendingIntent

import android.content.Intent
import fr.tanguy.supfitness.utils.NotificationReminder


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    lateinit var db:RoomDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "supfitness"
        ).allowMainThreadQueries().build()

        //generateWeightNotif()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        AppBarConfiguration(
            setOf(
                R.id.navigation_weight, R.id.navigation_statistics, R.id.navigation_training
            )
        )
        navView.setupWithNavController(navController)
    }

    fun generateWeightNotif() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = "Test"
            val description = "test test"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("weightChannel", name, importance)
            channel.description = description

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)

            setupReminder()
        }
    }

    fun setupReminder() {
        val intent = Intent(this@MainActivity, NotificationReminder::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this@MainActivity, 0, intent, 0)

        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

        val currentTime = System.currentTimeMillis()
        val tenSecondsInMillis = (1000 * 10).toLong()

        alarmManager[AlarmManager.RTC_WAKEUP, currentTime + tenSecondsInMillis] = pendingIntent
    }

}