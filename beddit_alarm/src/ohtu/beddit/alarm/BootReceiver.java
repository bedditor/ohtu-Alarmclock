package ohtu.beddit.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


/**
 * BootReceiver will run on boot. BootReceiver checks if there was an alarm set before the phone was turned off.
 * If there was, it must be set again to the Android alarm manager.
 */

public class BootReceiver extends BroadcastReceiver {
    private AlarmService alarmService;


    /**
     * onReceive() is called when the phone is booted. It makes a new instance of {@link
     * ohtu.beddit.alarm.AlarmService} for checking if alarm existed prior booting and sets a new alarm,
     * if there was one.
     *
     * @param context Context tells the application context this broadcast receiver is linked to
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        alarmService = new AlarmServiceImpl(context);
        if (alarmService.isAlarmSet() && alarmIsStillValid()) {
            addAlarm();
        } else {
            alarmService.deleteAlarm();
        }
    }

    private void addAlarm() {
        int hours = alarmService.getAlarmHours();
        int minutes = alarmService.getAlarmMinutes();
        int interval = alarmService.getAlarmInterval();

        alarmService.addAlarm(hours, minutes, interval);
    }

    /**
     * Calculates if the alarm was before current time.
     *
     * @return true if alarm is still valid, false if outdated
     */
    private boolean alarmIsStillValid() {
        return (System.currentTimeMillis() < alarmService.getAlarm().getTimeInMillis());
    }

}
