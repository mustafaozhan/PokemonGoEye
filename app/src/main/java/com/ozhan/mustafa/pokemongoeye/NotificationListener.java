package com.ozhan.mustafa.pokemongoeye;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

/**
 * Created by Mustafa Özhan on 7/28/16.
 */
public class NotificationListener extends NotificationListenerService {
    Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context=getApplicationContext();

    }

    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);

        String NT_pack=sbn.getPackageName();
        String NT_ticker=sbn.getNotification().tickerText.toString();
        Bundle NT_extras=sbn.getNotification().extras;
        String NT_title=NT_extras.getString("android.title");
        String NT_text=NT_extras.getCharSequence("android.text").toString();
        Log.d("Yeni Bildirim: ", "Package: "+NT_pack+", Title: "+NT_title+", Text: "+NT_text);

    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
    }
}
