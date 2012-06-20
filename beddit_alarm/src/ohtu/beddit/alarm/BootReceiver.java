package ohtu.beddit.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {

    AlarmService alarmService;
    private final String TAG = "BOOT";

    @Override
    public void onReceive(Context context, Intent intent) {
        alarmService = new AlarmServiceImpl(context);
        if (alarmService.isAlarmSet() && alarmIsStillValid()){
            addAlarm(context);
        } else {
            alarmService.deleteAlarm();
        }
    }

    private void addAlarm(Context context){
        int hours = alarmService.getAlarmHours();
        int minutes = alarmService.getAlarmMinutes();
        int interval = alarmService.getAlarmInterval();

        alarmService.addAlarm(hours, minutes, interval);
    }

    // Checks if phone has been off when the alarm should have rang. No need to
    // set alarm, if the wake up time has already passed.
    private boolean alarmIsStillValid(){
        return (System.currentTimeMillis() < alarmService.getAlarm().getTimeInMillis());
    }

}
