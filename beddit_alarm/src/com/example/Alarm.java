package com.example;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;


public class Alarm extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent)
    {
        AlarmHandler alarm = new AlarmHandler(context);
        alarm.setMusic();
        Log.v("Alarm", "Recieved alarm at " + Calendar.getInstance().getTime());
        Toast.makeText(context, "Herätys yksinkertainen", Toast.LENGTH_SHORT).show();
        if (alarm.insanityCheck()) {
            alarm.play();
            Utils.sleep(10);
            alarm.stop();
        }
        Log.v("Alarm", "Alarm ended at " + Calendar.getInstance().getTime());
    }

}