package com.saad.example.nearbyservices;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import java.util.Calendar;

/**
 * Created by Team Mashwara on 1/1/2020.
 */
public class MyService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO do something useful
        //HFLAG = true;
        //smsHandler.sendEmptyMessageDelayed(DISPLAY_DATA, 1000);

        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        System.out.println(hour);

        if(hour>=1 && hour<12)
        {
            intent.putExtra("pic",1);
        }
        else if((hour>=12 )&& (hour<=16))
        {
            intent.putExtra("pic",2);
        }
        else
        {
            intent.putExtra("pic",3);
        }
        sendBroadcast(intent);

        return Service.START_NOT_STICKY;
    }
}
