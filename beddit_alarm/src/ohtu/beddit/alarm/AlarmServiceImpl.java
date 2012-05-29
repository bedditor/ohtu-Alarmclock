package ohtu.beddit.alarm;


import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.util.Log;

import ohtu.beddit.R;
import ohtu.beddit.activity.MainActivity;
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

        // Calculate first wake up try
        Calendar calendar = calculateFirstWakeUpAttempt(hours, minutes, interval);
        setNotification(1,interval,calendar.getTimeInMillis(),context);
        addWakeUpAttempt(context, calendar);
    }

    //this method sets alarm manager to try wake up on given time
    public void addWakeUpAttempt(Context context, Calendar time){
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        String timeString = time.get(Calendar.HOUR_OF_DAY) + ":" + time.get(Calendar.MINUTE) + ":" + time.get(Calendar.SECOND);
        Log.v(TAG, "next wake up try set to "+timeString);
        alarmManager.set(AlarmManager.RTC_WAKEUP, time.getTimeInMillis(), sender);
    }

    //this method calculates time for the first try to wake up
    private Calendar calculateFirstWakeUpAttempt(int hour, int minute, int interval) {
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

    @Override
    public void deleteAlarm(Context context){
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);

        // Cancel the alarm!
        alarmManager.cancel(sender);
        FileHandler.disableAlarm(context);

        resetNotification(1,context);

    }

    public boolean isAlarmSet(Context context){
        int [] alarms = getAlarm(context);
        if (alarms[0] < 1){
            return false;
        }
        return true;
    }

    private int[] getAlarm(Context context){
        return FileHandler.getAlarm(context);
    }

    public int setNotification(int ID, long interval, long time ,Context context){
        Log.v("Notification","added");
        NotificationManager notificationman= (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        int icon = R.drawable.kello48;
        Notification notification = new Notification(icon,"",System.currentTimeMillis());
        Intent intention = new Intent(context, MainActivity.class);
        PendingIntent pendIntent = PendingIntent.getActivity(context, 0,intention,0);

        interval = interval * 60 * 1000;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        String intervalBegin = timeAsString(hour, minute);

        calendar.setTimeInMillis(time+interval);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);

        String intervalEnd = timeAsString(hour,minute);

        String timeprint = intervalBegin + " - " + intervalEnd;
        if(intervalBegin.equals(intervalEnd))
            timeprint = intervalBegin;
        notification.setLatestEventInfo(context, "Hälytys asetettu: ",timeprint,pendIntent);
        notificationman.notify(ID, notification);
        return ID;
    }

    //staattinen funktio jonka voi tunkea muualle
    public static String timeAsString(int hour, int minute){
        String hourString = "";
        String minuteString = "";
        if (hour < 10)
            hourString += "0";
        if (minute < 10)
            minuteString += "0";
        hourString += hour;
        minuteString += minute;
        return hourString+":"+minuteString;
    }

    public void resetNotification(int ID, Context context){
        NotificationManager notificationman= (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationman.cancel(ID);
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
