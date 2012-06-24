package ohtu.beddit.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/*
 * This class will run on boot. If there was an alarm set before the phone was turned off, we must set it
 * again after booting.
 */

public class BootReceiver extends BroadcastReceiver {
    private AlarmService alarmService;

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

    // Checks if phone has been off when the alarm should have rang. No need to
    // set alarm, if the wake up time has already passed.
    private boolean alarmIsStillValid() {
        return (System.currentTimeMillis() < alarmService.getAlarm().getTimeInMillis());
    }

}
