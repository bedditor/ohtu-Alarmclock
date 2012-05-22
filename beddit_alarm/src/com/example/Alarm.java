package com.example;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Message;
import android.util.Log;
import android.view.View;
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
        Intent a = new Intent(context, AlarmActivity.class);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_USER_ACTION);
        context.startActivity(a);

    }

}