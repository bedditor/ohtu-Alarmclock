package ohtu.beddit.alarm;

public class AlarmCheckerRandomImpl implements AlarmChecker{
    private double wakeUpChance;

    public AlarmCheckerRandomImpl(double wakeUpChance) {
        this.wakeUpChance = wakeUpChance;
    }

    @Override
    public boolean wakeUpNow() {
        if(Math.random() <= wakeUpChance)
            return true;

        return false;
    }

    @Override
    public int getWakeUpAttemptInterval() {
        return 30;
    }

    @Override
    public int getCheckTime() {
        return 10;
    }
}
