package com.example.marija.Models;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Color;
import android.graphics.drawable.Icon;
import android.os.Build;

import com.example.marija.R;

public class NotificationHelper extends ContextWrapper {
    private static final String EDMT_CHANNEL_ID ="edmt.dev.androidnotificationchannel.EDMTDEV";
    private static final String EDMT_CHANNEL_NAME="EDMTDEV Channel";
    private NotificationManager manager;
    public NotificationHelper(Context base){
        super(base);
        createChannels();
    }

    public void createChannels(){
        NotificationChannel edmtChannel;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            edmtChannel= new NotificationChannel(EDMT_CHANNEL_ID,EDMT_CHANNEL_NAME,NotificationManager.IMPORTANCE_DEFAULT);
            getManager().createNotificationChannel(edmtChannel);
        }




    }

    public NotificationManager getManager(){
        if(manager == null){
            manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            return  manager;
        }
        return manager;
    }

    public Notification.Builder getNotification(String title,String title2){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return new Notification.Builder(getApplicationContext(),EDMT_CHANNEL_ID).setContentText(title).setContentTitle(title2).setAutoCancel(true).setSmallIcon(R.drawable.sat);
        }
        return null;
    }
}
