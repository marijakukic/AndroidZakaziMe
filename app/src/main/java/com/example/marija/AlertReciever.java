package com.example.marija;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.marija.Models.NotificationHelper;

import java.util.Random;

public class AlertReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationHelper notificationHelper = new NotificationHelper(context);

        Notification.Builder builder = notificationHelper.getNotification("Podsetnik");
        notificationHelper.getManager().notify(new Random().nextInt(), builder.build());
        Log.d("USAO SAM","U RECIEVER");

    }
}
