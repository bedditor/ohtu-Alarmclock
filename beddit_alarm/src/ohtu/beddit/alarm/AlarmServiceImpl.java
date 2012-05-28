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

    private final String TAG = "Alarm Service";
    AlarmManagerInterface alarmManager;

    public AlarmServiceImpl(Context context) {
        this.alarmManager = new AlarmManagerAndroid(context);
    }

    @Override
    public void setAlarmManager(AlarmManagerInterface alarmManager){
        this.alarmManager = alarmManager;
    }

    //this method saves a new alarm with an interval
    @Override
    public void addAlarm(Context context, int hours, int minutes, int interval){
        FileHandler.saveAlarm(hours, minutes, interval, true, context);

        Log.v(TAG, "alarm set to "+hours+":"+minutes);

        // Calculate first wake up try
        Calendar calendar = calculateAlarm(hours, minutes, interval);
        String time = calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE);


        addAlarmTry(context, calendar);

        // Tell the user about what we did.
        Toast.makeText(context, "Hälytys asetettu", Toast.LENGTH_LONG).show();
    }

    //this method sets alarm manager to try wake up on given time
    public void addAlarmTry(Context context, Calendar time){
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        String timeString = time.get(Calendar.HOUR_OF_DAY) + ":" + time.get(Calendar.MINUTE) + ":" + time.get(Calendar.SECOND);
        Log.v(TAG, "next wake up try set to "+timeString);

        alarmManager.set(AlarmManager.RTC_WAKEUP, time.getTimeInMillis(), sender);
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

    //this method calculates time for the first try to wake up
    private Calendar calculateAlarm(int hour, int minute, int interval) {
        Calendar alarmTime = Calendar.getInstance();
        alarmTime.set(Calendar.HOUR_OF_DAY, hour);
        alarmTime.set(Calendar.MINUTE, minute);
        alarmTime.set(Calendar.SECOND, 0);
        alarmTime.set(Calendar.MILLISECOND, 0);
        if(alarmTime.before(Calendar.getInstance())){
            alarmTime.add(Calendar.DAY_OF_YEAR, 1);
        }

        alarmTime.add(Calendar.MINUTE, -interval);

        Calendar currentTime = Calendar.getInstance();
        if(alarmTime.after(currentTime)){
            return alarmTime;
        }
        else return currentTime;
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
