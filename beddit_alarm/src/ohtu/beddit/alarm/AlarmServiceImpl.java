package ohtu.beddit.alarm;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import ohtu.beddit.io.FileHandler;
import ohtu.beddit.utils.TimeUtils;

import java.util.Calendar;

public class AlarmServiceImpl implements AlarmService {

    private final String TAG = "Alarm Service";
    private AlarmManager alarmManager;
    private FileHandler fileHandler;
    private Context context;
    private NotificationFactory notificationFactory;
    private static Alarm alarm;

    public AlarmServiceImpl(Context context) {
        this.context = context.getApplicationContext();
        this.alarmManager = (AlarmManager) this.context.getSystemService(Context.ALARM_SERVICE);
        this.fileHandler = new FileHandler(this.context);
        this.notificationFactory = new NotificationFactory(this.context);
        alarm = getAlarmFromFile();
    }

    public AlarmServiceImpl(Context context, AlarmManager alarmManager, FileHandler filehandler, NotificationFactory notificationFactory) {
        this.context = context.getApplicationContext();
        this.alarmManager = alarmManager;
        this.fileHandler = filehandler;
        this.notificationFactory = notificationFactory;
        alarm = getAlarmFromFile();
    }

    //this method saves a new alarm with an interval
    @Override
    public Alarm addAlarm(int hours, int minutes, int interval) {
        alarm = fileHandler.saveAlarm(hours, minutes, interval, true);
        if (alarm.isEnabled()) { //write succeeded
            Calendar calendar = calculateFirstWakeUpAttempt(hours, minutes, interval);
            notificationFactory.setNotification(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), hours, minutes);
            addWakeUpAttempt(calendar);
            Log.v(TAG, "Adding alarm failed");
        }
        return alarm;
    }

    @Override
    public void changeAlarm(int hours, int minutes, int interval) {
        if (alarm.isEnabled()) {
            alarm = addAlarm(hours, minutes, interval);
        }
    }

    //this method sets alarm manager to try wake up on given time
    public void addWakeUpAttempt(Calendar time) {
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        String timeString = time.get(Calendar.HOUR_OF_DAY) + ":" + time.get(Calendar.MINUTE) + ":" + time.get(Calendar.SECOND);
        Log.v(TAG, "next wake up try set to " + timeString);
        alarmManager.set(AlarmManager.RTC_WAKEUP, time.getTimeInMillis(), sender);
    }

    //this method calculates time for the first try to wake up
    private Calendar calculateFirstWakeUpAttempt(int hour, int minute, int interval) {
        Calendar alarmTime = TimeUtils.timeToCalendar(hour, minute);
        alarmTime.add(Calendar.MINUTE, -interval);

        Calendar currentTime = Calendar.getInstance();
        if (alarmTime.after(currentTime)) {
            return alarmTime;
        } else return currentTime;
    }

    @Override
    public void deleteAlarm() {
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);

        // Cancel the alarm!
        alarmManager.cancel(sender);
        fileHandler.disableAlarm();

        notificationFactory.resetNotification();
        alarm.setEnabled(false);
    }

    public boolean isAlarmSet() {
        return alarm.isEnabled();
    }

    @Override
    public int getAlarmHours() {
        return alarm.getHours();
    }

    @Override
    public int getAlarmMinutes() {
        return alarm.getMinutes();
    }

    @Override
    public int getAlarmInterval() {
        return alarm.getInterval();
    }

    @Override
    public Alarm getAlarm() {
        return alarm;
    }

    private Alarm getAlarmFromFile() {
        return fileHandler.getAlarm();
    }


}
