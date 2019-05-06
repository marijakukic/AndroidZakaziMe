package com.example.marija;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import java.util.concurrent.CopyOnWriteArrayList;

public class CheckIntertetReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        int[] type = {ConnectivityManager.TYPE_MOBILE,ConnectivityManager.TYPE_WIFI};
        if(isNetworkAvalible(context,type)==true){
            return;
        }else{
            Toast.makeText(context,"NO INTERNET!",Toast.LENGTH_SHORT).show();
        }

    }

    public static boolean isNetworkAvalible(Context context, int[] typeNetworks){
        try{
            ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            for(int typeNetwork:typeNetworks){
                NetworkInfo networkInfo = cm.getNetworkInfo(typeNetwork);
                if(networkInfo!=null && networkInfo.getDetailedState().equals(NetworkInfo.State.CONNECTED)){
                    return true;
                }
            }
        }catch(Exception e){
            return false;
        }
        return false;
    }
}
