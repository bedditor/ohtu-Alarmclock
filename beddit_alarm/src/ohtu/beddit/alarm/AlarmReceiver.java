package ohtu.beddit.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import ohtu.beddit.activity.AlarmActivity;

import java.util.Calendar;


public class AlarmReceiver extends BroadcastReceiver {
    private final String TAG = "Alarm Receiver";
    private int wakeUpAttemptInterval; //how often (seconds) we try to wake up
    private int checkTime; //how long (seconds) it may take to do the checking
    private AlarmService alarmService;


    @Override
    public void onReceive(Context context, Intent intent)
    {
        alarmService = new AlarmServiceImpl(context);
        AlarmChecker alarmChecker = new AlarmCheckerRandomImpl(0.3);
        //AlarmChecker alarmChecker = new AlarmCheckerRealImpl();
        checkTime = alarmChecker.getCheckTime();
        wakeUpAttemptInterval = alarmChecker.getWakeUpAttemptInterval();
        char sleepstage = 'L'; //the sleep stage you want to wake up from

        Log.v(TAG, "Received alarm");
        Log.v(TAG, "second until last wake up "+ getSecondsUntilLastWakeUpTime(context));
        if(getSecondsUntilLastWakeUpTime(context) < 3){ //last time reached, wake up
            Log.v(TAG, "Wake up time reached, starting alarm");
            startAlarm(context);
        }
        else if(getSecondsUntilLastWakeUpTime(context) <= checkTime){ //no time to do any more checking, schedule final wake up
            Log.v(TAG, "No time to check anymore, schedule wake up");
            alarmService.addWakeUpAttempt(getLastWakeUpTime(context));
        }
        else if(alarmChecker.wakeUpNow(context)){ //check if we should wake up now
            Log.v(TAG, "Alarm checker gave permission to wake up, starting alarm");
            startAlarm(context);
        }
        else{ // schedule next try
            Log.v(TAG, "Scheduling next try");
            scheduleNextTry(context);
        }
    }

    private void startAlarm(Context context) {
        Intent newIntent = new Intent(context, AlarmActivity.class);
        newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_USER_ACTION);
        context.startActivity(newIntent);
    }

    private int getSecondsUntilLastWakeUpTime(Context context){
        Calendar currentTime = Calendar.getInstance();
        Calendar wakeUpTime = getLastWakeUpTime(context);
        long timeDifference = wakeUpTime.getTimeInMillis() - currentTime.getTimeInMillis();
        return (int)(timeDifference/1000);
    }

    private Calendar getLastWakeUpTime(Context context){
        Calendar wakeUpTime = Calendar.getInstance();
        wakeUpTime.set(Calendar.HOUR_OF_DAY, alarmService.getAlarmHours());
        wakeUpTime.set(Calendar.MINUTE, alarmService.getAlarmMinutes());
        wakeUpTime.set(Calendar.SECOND, 0);
        wakeUpTime.set(Calendar.MILLISECOND, 0);

        if (wakeUpTime.getTimeInMillis() + 10000 < System.currentTimeMillis()){
            wakeUpTime.add(Calendar.DAY_OF_YEAR, 1);
        }
        return wakeUpTime;
    }

    private void scheduleNextTry(Context context){
        Calendar timeForNextTry = Calendar.getInstance();
        timeForNextTry.add(Calendar.SECOND, wakeUpAttemptInterval);

        Calendar lastWakeUpTime = getLastWakeUpTime(context);

        if(timeForNextTry.before(lastWakeUpTime)){ //still time for another interval
            Log.v(TAG, "next try scheduled to "+timeForNextTry.get(Calendar.HOUR_OF_DAY)+":"+timeForNextTry.get(Calendar.MINUTE)+":"+timeForNextTry.get(Calendar.SECOND));
            alarmService.addWakeUpAttempt(timeForNextTry);
        }
        else{ //no more time, schedule final wake up
            Log.v(TAG, "next try scheduled to last wake up time "+lastWakeUpTime.get(Calendar.HOUR_OF_DAY)+":"+lastWakeUpTime.get(Calendar.MINUTE)+":"+lastWakeUpTime.get(Calendar.SECOND));
            alarmService.addWakeUpAttempt(lastWakeUpTime);
        }
    }



}