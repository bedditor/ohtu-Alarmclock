package com.example;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.util.Calendar;

public class AlarmSchedulerImpl implements AlarmScheduler {

    AlarmManagerInterface alarmManager;

    public AlarmSchedulerImpl(Context context) {
        this.alarmManager = new AlarmManagerAndroid(context);
    }

    public void setAlarmManager(AlarmManagerInterface alarmManager){
        this.alarmManager = alarmManager;
    }

    @Override
    public void addAlarm(Context context, int hours, int minutes, int interval){
        Intent intent = new Intent(context, Alarm.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);

        // Calculate alarm to go off
        Calendar calendar = calculateAlarm(hours, minutes, 0);

        // Schedule the alarm!
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);

        // Tell the user about what we did.
        Toast.makeText(context, "Hälytys asetettu", Toast.LENGTH_LONG).show();

    }

    @Override
    public void deleteAlarm(Context context){
        Intent intent = new Intent(context, Alarm.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);

        // Schedule the alarm!
        AlarmManager am = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        am.cancel(sender);

        Toast.makeText(context, "Hälytys poistettu", Toast.LENGTH_LONG).show();

    }


    private Calendar calculateAlarm(int hour, int minute, int interval) {
        Calendar c = Calendar.getInstance();

        c.setTimeInMillis(System.currentTimeMillis());
        int nowHour = c.get(Calendar.HOUR_OF_DAY);
        int nowMinute = c.get(Calendar.MINUTE);

        // if alarm is behind current time, advance one day
        if (hour < nowHour  || hour == nowHour && minute <= nowMinute) {
            c.add(Calendar.DAY_OF_YEAR, 1);
        }
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        return c;


    }


}
