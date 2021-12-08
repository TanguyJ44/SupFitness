package fr.tanguy.supfitness.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import java.util.*


class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val currentDate = "${Date().date}/${Date().month}/${Date().year}"
        val sharedPref: SharedPreferences = context!!.getSharedPreferences(
            "myPrefs",
            Context.MODE_PRIVATE
        )

        if (sharedPref.getBoolean("notif-alarm", false)) {
            if (!sharedPref.getString("current-weight-saved", "").equals(currentDate)) {
                val notificationHelper = context?.let { NotificationHelper(it) }
                notificationHelper?.createNotification()
            }
        }
    }

}