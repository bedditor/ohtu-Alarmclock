package ohtu.beddit.alarm;

import android.util.Log;

public class AlarmCheckerRandomImpl implements AlarmChecker{
    private double wakeUpChance;

    public AlarmCheckerRandomImpl(double wakeUpChance) {
        this.wakeUpChance = wakeUpChance;
    }

    @Override
    public boolean wakeUpNow(char sleepstage) {
        double random = Math.random();

        Log.v("AlarmChecker:", "Random must be < " + wakeUpChance);
        Log.v("AlarmChecker:", "Random was "+ random);

        if(random <= wakeUpChance){
            return true;
        }
        return false;
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
