package ohtu.beddit.alarm;

import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {

    AlarmService alarmService;

    @Override
    public void onReceive(Context context, Intent intent) {
        alarmService = new AlarmServiceImpl(context, (AlarmManager) context.getSystemService(context.ALARM_SERVICE));

        if (alarmService.isAlarmSet(context)){
            addAlarm(context);
        }

    }

    private void addAlarm(Context context){
        int hours = alarmService.getAlarmHours(context);
        int minutes = alarmService.getAlarmMinutes(context);
        int interval = alarmService.getAlarmInterval(context);

        alarmService.addAlarm(context, hours, minutes, interval);
    }

}
