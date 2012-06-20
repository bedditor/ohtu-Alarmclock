package ohtu.beddit.alarm;

import android.content.Context;
import android.util.Log;

public class AlarmCheckerRandomImpl implements AlarmChecker{
    private double wakeUpChance;

    public AlarmCheckerRandomImpl(double wakeUpChance) {
        this.wakeUpChance = wakeUpChance;
    }

    @Override
    public boolean wakeUpNow(Context context) {
        double random = Math.random();

        Log.v("AlarmChecker:", "Random must be < " + wakeUpChance);
        Log.v("AlarmChecker:", "Random was "+ random);

        return random <= wakeUpChance;
    }

    @Override
    public int getWakeUpAttemptInterval() {
        return 180;
    }

    @Override
    public int getCheckTime() {
        return 10;
    }
}
