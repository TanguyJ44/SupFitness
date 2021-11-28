package fr.tanguy.supfitness.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        // TODO : Vérifier si un poids est déjà save
        val notificationHelper = context?.let { NotificationHelper(it) }
        notificationHelper?.createNotification()
    }

}