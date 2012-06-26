package ohtu.beddit.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import ohtu.beddit.activity.AlarmActivity;
import java.util.Calendar;


/**This class is used for receiving alarm broadcasts during the wake up interval and deciding what to do with them.
 * @see <a href="http://lmgtfy.com/?q=Broadcastreceiver+javadoc">BroadcastReceiver</a>
 */

public class AlarmReceiver extends BroadcastReceiver{
    private final String TAG = "Alarm Receiver";
    private int wakeUpAttemptInterval; //how often (seconds) we try to wake up
    private AlarmService alarmService;

    /**
     * This method receives Intents during the wake up interval, and either schedules a new broadcast or starts up the alarm.
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        alarmService = new AlarmServiceImpl(context);

        // Switch the commented lines around if testing wake up without real sleep data from Beddit servers
        //AlarmChecker alarmChecker = new AlarmCheckerRandomImpl(0.3);
        AlarmChecker alarmChecker = new AlarmCheckerRealImpl();


        int checkTime = alarmChecker.getCheckTime();
        wakeUpAttemptInterval = alarmChecker.getWakeUpAttemptInterval();

        if (getSecondsUntilLastWakeUpTime() < 3) { //last time reached, wake up
            startAlarm(context);
        } else if (getSecondsUntilLastWakeUpTime() <= checkTime) { //no time to do any more checking, set final wake up
            alarmService.addWakeUpAttempt(getLastWakeUpTime());
        } else if (alarmChecker.wakeUpNow(context)) { //check if we should wake up now
            startAlarm(context);
        } else {
            scheduleNextTry();
        }
    }

    private void startAlarm(Context context) {
        Intent newIntent = new Intent(context, AlarmActivity.class);
        newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_USER_ACTION);
        context.startActivity(newIntent);
    }

    private int getSecondsUntilLastWakeUpTime() {
        Calendar currentTime = Calendar.getInstance();
        Calendar wakeUpTime = getLastWakeUpTime();
        long timeDifference = wakeUpTime.getTimeInMillis() - currentTime.getTimeInMillis();
        return (int) (timeDifference / 1000);
    }

    private Calendar getLastWakeUpTime() {
        return alarmService.getAlarm().getTimeInCalendar();
    }

    private void scheduleNextTry() {
        Calendar timeForNextTry = Calendar.getInstance();
        timeForNextTry.add(Calendar.SECOND, wakeUpAttemptInterval);

        Calendar lastWakeUpTime = getLastWakeUpTime();

        if (timeForNextTry.before(lastWakeUpTime)) { //still time for another check
            Log.v(TAG, "next try scheduled to " + timeForNextTry.get(Calendar.HOUR_OF_DAY) + ":" +
                    timeForNextTry.get(Calendar.MINUTE) + ":" + timeForNextTry.get(Calendar.SECOND));
            alarmService.addWakeUpAttempt(timeForNextTry);
        } else { //no more time, schedule final wake up
            Log.v(TAG, "next try scheduled to last wake up time " + lastWakeUpTime.get(Calendar.HOUR_OF_DAY) + ":" +
                    lastWakeUpTime.get(Calendar.MINUTE) + ":" + lastWakeUpTime.get(Calendar.SECOND));
            alarmService.addWakeUpAttempt(lastWakeUpTime);
        }
    }


}