package com.ozhan.mustafa.pokemongoeye

import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log

/**
 * Created by Mustafa Özhan on 7/28/16.
 */
class NotificationListener : NotificationListenerService() {
    internal lateinit var context: Context

    override fun onCreate() {
        super.onCreate()
        context = applicationContext

    }

    override fun onBind(intent: Intent): IBinder? {
        return super.onBind(intent)
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    override fun onNotificationPosted(sbn: StatusBarNotification) {
        super.onNotificationPosted(sbn)

        val NT_pack = sbn.packageName
        val NT_ticker = sbn.notification.tickerText.toString()
        val NT_extras = sbn.notification.extras
        val NT_title = NT_extras.getString("android.title")
        val NT_text = NT_extras.getCharSequence("android.text")!!.toString()
        Log.d("Yeni Bildirim: ", "Package: $NT_pack, Title: $NT_title, Text: $NT_text")

    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        super.onNotificationRemoved(sbn)
    }
}
