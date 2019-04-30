package com.example.marija;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.marija.Models.NotificationHelper;

import java.util.Random;

public class AlertReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationHelper notificationHelper = new NotificationHelper(context);

        Notification.Builder builder = notificationHelper.getNotification(intent.getStringExtra("datum"),intent.getStringExtra("vreme"));
        notificationHelper.getManager().notify(new Random().nextInt(), builder.build());


    }
}
