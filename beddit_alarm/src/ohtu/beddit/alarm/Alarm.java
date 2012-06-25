package ohtu.beddit.alarm;

import ohtu.beddit.utils.TimeUtils;
import java.util.Calendar;


/**
 *  Alarm  represents a single alarm. A single alarm has the wake up time and a length of the interval.
 */
public class Alarm {

    /**
     * Interval for wake up
     */
    private int interval;

    /**
     * Wake up time as a Calendar object
     */
    private Calendar wakeUpTime = Calendar.getInstance();

    /**
     * Boolean telling is alarm enabled or not.  We want info about the last alarm in memory if an alarm is not set.
     */
    private boolean enabled;

    public Alarm() {
    }

    /**
     * @param hours  Wake up hour in 24h format
     * @param minutes Wake up minutes
     * @param interval Interval length
     * @param enabled Is alarm enabled or info about the last alarm
     */
    public Alarm(int hours, int minutes, int interval, boolean enabled) {
        this.interval = interval;
        this.enabled = enabled;
        this.wakeUpTime = TimeUtils.timeToCalendar(hours, minutes);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public long getTimeInMillis() {
        return wakeUpTime.getTimeInMillis();
    }

    public void setTimeInMillis(long timeInMillis) {
        wakeUpTime.setTimeInMillis(timeInMillis);
    }

    public Calendar getTimeInCalendar() {
        return wakeUpTime;
    }

    public int getHours() {
        return wakeUpTime.get(Calendar.HOUR_OF_DAY);
    }

    public int getMinutes() {
        return wakeUpTime.get(Calendar.MINUTE);
    }

}
