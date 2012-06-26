package ohtu.beddit.alarm;

import android.content.Context;
import android.util.Log;

/**
 * This class is used for manual testing purposes. It will use simple random function to determine if
 * if the customer is in the correct sleep stage to wake him up.
 */
public class AlarmCheckerRandomImpl implements AlarmChecker {

    /**
     * Probability for correct sleep stage. I.e. 0,3 = 30% probability for waking up during interval.
     */
    private final double wakeUpChance;

    /**
     *  How often the sleep date will be checked
     */
    private final static int wakeAttemptInterval = 180;

    /**
     * How long does it take to check the sleep stage
     */
    private final static int checkTime = 10;

    /**
     * Constructor for making AlarmChecker for testing purposes.
     * @param wakeUpChance Probability for wake up as a double. 0,3 = 30% probability
     */
    public AlarmCheckerRandomImpl(double wakeUpChance) {
        this.wakeUpChance = wakeUpChance;
    }


    /**
     *  wakeUpNow() checks for correct sleep stage in real implementation. Random implementation
     *  replaces internet connection with a simple random function defining if we are in the correct
     *  sleep stage or not.
     *
     * @param context Not used in this method.
     * @return true if should wake up, false if not
     */
    @Override
    public boolean wakeUpNow(Context context) {
        double random = Math.random();

        Log.v("AlarmChecker:", "Random must be < " + wakeUpChance);
        Log.v("AlarmChecker:", "Random was " + random);

        return random <= wakeUpChance;
    }

    @Override
    public int getWakeUpAttemptInterval() {
        return wakeAttemptInterval;
    }

}
