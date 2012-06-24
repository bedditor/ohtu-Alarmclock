package ohtu.beddit.alarm;

import android.content.Context;

/**
 * Alarm checker is an interface for checking if the customer is in correct sleep stage for waking up.
 * The real implementation uses data from Beddit, but interface allows a random implementation for testing purposes.
 */
public interface AlarmChecker {
    public boolean wakeUpNow(Context context);

    public int getWakeUpAttemptInterval(); //how often (seconds) we should try to wake up

    public int getCheckTime(); //how long (seconds) it may take to do the checking

}
