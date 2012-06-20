package ohtu.beddit.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {

    AlarmService alarmService;

    @Override
    public void onReceive(Context context, Intent intent) {
        alarmService = new AlarmServiceImpl(context);

        if (alarmService.isAlarmSet()){
            addAlarm(context);
        }
    }

    private void addAlarm(Context context){
        int hours = alarmService.getAlarmHours();
        int minutes = alarmService.getAlarmMinutes();
        int interval = alarmService.getAlarmInterval();

        alarmService.addAlarm(hours, minutes, interval);
    }

}
