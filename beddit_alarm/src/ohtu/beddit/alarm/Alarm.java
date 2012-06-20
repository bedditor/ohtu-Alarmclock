package ohtu.beddit.alarm;

import ohtu.beddit.utils.TimeUtils;

import java.util.Calendar;

/**
 * Created with IntelliJ IDEA.
 * User: joosakur
 * Date: 18.6.2012
 * Time: 11:01
 * To change this template use File | Settings | File Templates.
 */
public class Alarm {

    private int interval;
    private boolean enabled;
    private Calendar calendar = Calendar.getInstance();

    public Alarm() {}

    public Alarm(int hours, int minutes, int interval, boolean enabled) {
        this.interval = interval;
        this.enabled = enabled;
        this.calendar = TimeUtils.timeToCalendar(hours, minutes);
    }

    public Alarm(long timeInMillis, int interval, boolean enabled) {
        calendar.setTimeInMillis(timeInMillis);
        this.interval = interval;
        this.enabled = enabled;
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
        return calendar.getTimeInMillis();
    }

    public void setTimeInMillis(long timeInMillis) {
        calendar.setTimeInMillis(timeInMillis);
    }

    public Calendar getTimeInCalendar() {
        return calendar;
    }

    public int getHours(){
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public int getMinutes(){
        return calendar.get(Calendar.MINUTE);
    }




}
