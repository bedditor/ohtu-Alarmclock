package ohtu.beddit.alarm;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import ohtu.beddit.io.FileHandler;

import java.util.Calendar;

public class AlarmServiceImpl implements AlarmService {

    AlarmManagerInterface alarmManager;

    public AlarmServiceImpl(Context context) {
        this.alarmManager = new AlarmManagerAndroid(context);
    }

    @Override
    public void setAlarmManager(AlarmManagerInterface alarmManager){
        this.alarmManager = alarmManager;
    }

    @Override
    public void addAlarm(Context context, int hours, int minutes, int interval){
        FileHandler.saveAlarm(hours, minutes, interval, true, context);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);

        // Calculate alarm to go off
        Calendar calendar = calculateAlarm(hours, minutes, 0);
        String time = calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE);
        Log.v("Herätys ", time);

        // Schedule the alarm! Muuta kommentoinnit toisinpäin testatessa!!!
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
        //alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 3000, sender);


        // Tell the user about what we did.
        Toast.makeText(context, "Hälytys asetettu", Toast.LENGTH_LONG).show();

    }

    @Override
    public void deleteAlarm(Context context){
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);

        // Cancel the alarm!
        alarmManager.cancel(sender);
        FileHandler.disableAlarm(context);

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

    public int[] getAlarm(Context context){
         return FileHandler.getAlarm(context);
    }

    public boolean isAlarmSet(Context context){
        int [] alarms = getAlarm(context);
        Log.v("Alarmi setattu:", "" + alarms[0]);
        if (alarms[0] < 1){
            return false;
        }
        return true;
    }

    @Override
    public int getAlarmHours(Context context) {
        return getAlarm(context)[1];
    }

    @Override
    public int getAlarmMinutes(Context context) {
        return getAlarm(context)[2];
    }

    @Override
    public int getAlarmInterval(Context context) {
        return getAlarm(context)[3];
    }


}
