package com.example;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;


public class Alarm extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent)
    {
        Log.v("jee", "lol");
        Toast.makeText(context, "Herätys yksinkertainen", Toast.LENGTH_SHORT).show();
    }
}