package ohtu.beddit.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;

public class AlarmManagerAndroid implements AlarmManagerInterface{
    AlarmManager alarmManager;

    public AlarmManagerAndroid(Context context) {
        alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
    }

    @Override
    public void set(int type, long triggerAtTime, PendingIntent operation) {
        alarmManager.set(type, triggerAtTime, operation);
    }

    @Override
    public void setRepeating(int type, long triggerAtTime, long interval, PendingIntent operation) {
        alarmManager.setRepeating(type, triggerAtTime, interval, operation);
    }

    @Override
    public void setInexactRepeating(int type, long triggerAtTime, long interval, PendingIntent operation) {
        alarmManager.setInexactRepeating(type, triggerAtTime, interval, operation);
    }

    @Override
    public void cancel(PendingIntent operation) {
        alarmManager.cancel(operation);
    }

    @Override
    public void setTime(long millis) {
        alarmManager.setTime(millis);
    }

    @Override
    public void setTimeZone(String timeZone) {
        alarmManager.setTimeZone(timeZone);
    }
}
