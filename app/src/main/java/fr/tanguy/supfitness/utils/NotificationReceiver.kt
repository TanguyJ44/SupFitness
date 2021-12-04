package fr.tanguy.supfitness.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import fr.tanguy.supfitness.ui.weight.WeightHelper

class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (!WeightHelper.currentDateAlreadySaved()) {
            val notificationHelper = context?.let { NotificationHelper(it) }
            notificationHelper?.createNotification()
        }
    }

}