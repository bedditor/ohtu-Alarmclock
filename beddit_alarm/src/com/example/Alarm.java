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
        Intent newintent = new Intent(context, AlarmActivity.class);
        newintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_USER_ACTION);
        context.startActivity(newintent);
    }

}