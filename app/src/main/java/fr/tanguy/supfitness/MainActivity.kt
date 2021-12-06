package fr.tanguy.supfitness

import android.annotation.SuppressLint
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

import android.app.AlarmManager

import android.app.PendingIntent
import android.content.Context

import android.content.Intent
import android.content.SharedPreferences
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.google.android.material.appbar.MaterialToolbar
import fr.tanguy.supfitness.utils.NotificationReceiver
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    lateinit var db: RoomDatabase

    lateinit var sharedPref: SharedPreferences
    lateinit var editor: SharedPreferences.Editor

    @SuppressLint("CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPref = getPreferences(Context.MODE_PRIVATE)
        editor = sharedPref.edit()

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "supfitness"
        ).allowMainThreadQueries().build()

        if (sharedPref.getBoolean("notif-alarm", false)) {
            weightAlarmNotif()
        }

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

        val toolbarMenu: MaterialToolbar = findViewById(R.id.materialToolbar)
        toolbarMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.settings -> popupSettings()
            }
            true
        }

    }

    @SuppressLint("InflateParams", "UseSwitchCompatOrMaterialCode")
    private fun popupSettings() {
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater?
        val popupView: View = inflater!!.inflate(R.layout.fragment_settings, null)

        val switchAlarmNotif: Switch = popupView.findViewById(R.id.switchAlarmNotif)

        switchAlarmNotif.isChecked = sharedPref.getBoolean("notif-alarm", false)

        val width = LinearLayout.LayoutParams.MATCH_PARENT
        val height = LinearLayout.LayoutParams.MATCH_PARENT
        val focusable = true

        val popupWindow = PopupWindow(popupView, width, height, focusable)

        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0)

        val settingsReturnButton: Button = popupView.findViewById(R.id.settingsReturnButton)
        settingsReturnButton.setOnClickListener {
            popupWindow.dismiss()
        }

        switchAlarmNotif.setOnClickListener {
            if (switchAlarmNotif.isChecked) {
                weightAlarmNotif()
                editor.putBoolean("notif-alarm", true)
                editor.commit()
            } else {
                editor.putBoolean("notif-alarm", false)
                editor.commit()
            }
        }
    }


    @SuppressLint("UnspecifiedImmutableFlag")
    private fun weightAlarmNotif() {
        val calendar: Calendar = Calendar.getInstance()

        calendar.set(Calendar.HOUR_OF_DAY, 14)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)

        if (calendar.time < Date()) calendar.add(Calendar.DAY_OF_MONTH, 1)

        val intent = Intent(applicationContext, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )

    }

}