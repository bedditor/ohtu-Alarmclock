package ohtu.beddit.alarm;

import android.content.Context;

/**
 * Alarm checker is an interface for checking if the customer is in correct sleep stage for waking up.
 * The real implementation uses data from Beddit, but interface allows a random implementation for testing purposes.
 */
public interface AlarmChecker {

    /**
     * This method checks if the customer is in right sleep stage to be woken up.
     *
     * @param context AlarmChecker needs application context for possible PreferenceService or API calls
     * @return true if user is in right sleep stage and should be woken up, false otherwise
     */
    public boolean wakeUpNow(Context context);

    /**
     *  How often the sleep date will be checked.
     */
    public int getWakeUpAttemptInterval();

    /**
     *  How long does it take to check data from the internet
     */
    public int getCheckTime();

}
