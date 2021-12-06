package fr.tanguy.supfitness.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

import android.content.SharedPreferences
import androidx.preference.PreferenceManager


class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val sharedPref: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

        if (sharedPref.getBoolean("notif-alarm", false)) {
            if (!sharedPref.getBoolean("current-weight-saved", false)) {
                val notificationHelper = context?.let { NotificationHelper(it) }
                notificationHelper?.createNotification()
            }
        }
    }

}