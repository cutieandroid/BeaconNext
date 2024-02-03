package com.example.beaconnext;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.example.beaconnext.env.Cred;
import com.example.beaconnext.service.ForegroundScan;
import com.kontakt.sdk.android.common.KontaktSDK;

public class App extends Application {

    public static final String API_KEY = Cred.Kontaktapi;

    @Override
    public void onCreate() {
        super.onCreate();
        KontaktSDK.initialize(API_KEY);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "running_channel", "running_channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }
        // Intent i= new Intent(App.this, ForegroundScan.class);
         //startService(i);

    }
}
