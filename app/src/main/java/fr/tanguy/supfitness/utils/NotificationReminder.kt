package fr.tanguy.supfitness.utils

import android.annotation.SuppressLint
import android.app.Notification
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import fr.tanguy.supfitness.R

class NotificationReminder : BroadcastReceiver() {

    @SuppressLint("WrongConstant")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context?, intent: Intent?) {
        val builder: Notification.Builder? = Notification.Builder(context, "weightChannel")
            .setSmallIcon(R.drawable.ic_fitness)
            .setContentTitle("COUCOU")
            .setContentText("Un petit coucou de l'app")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val notificationManagerCompat = NotificationManagerCompat.from(context!!)

        builder?.build()?.let { notificationManagerCompat.notify(200, it) }
    }

}