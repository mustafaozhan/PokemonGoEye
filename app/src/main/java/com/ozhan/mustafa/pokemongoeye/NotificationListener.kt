package com.ozhan.mustafa.pokemongoeye

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log

@SuppressLint("Registered", "OverrideAbstract")
/**
 * Created by Mustafa Özhan on 7/28/16.
 */
class NotificationListener : NotificationListenerService() {
    private lateinit var context: Context

    override fun onCreate() {
        super.onCreate()
        context = applicationContext

    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    override fun onNotificationPosted(sbn: StatusBarNotification) {
        super.onNotificationPosted(sbn)

        val ntPack = sbn.packageName
        val ntTicker = sbn.notification.tickerText.toString()
        val ntExtras = sbn.notification.extras
        val ntTitle = ntExtras.getString("android.title")
        val ntText = ntExtras.getCharSequence("android.text")!!.toString()
        Log.d("Yeni Bildirim: ", "Package: $ntPack, Title: $ntTitle, Text: $ntText")

    }

}
