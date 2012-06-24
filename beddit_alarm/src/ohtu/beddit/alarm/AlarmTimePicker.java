package ohtu.beddit.alarm;

/**
 * Defines an interface for a time picker, if future developers ever want to change it.
 */
public interface AlarmTimePicker {
    public int getHours();

    public int getMinutes();

    public int getInterval();

    public void setHours(int h);

    public void setMinutes(int m);

    public void setInterval(int i);

    public void setEnabled(boolean enabled);

    public boolean isEnabled();

    public void setBackgroundColor(int c);

    public void setForegroundColor(int c);

    public void setSpecialColor(int c);

    public void set24HourMode(boolean b);

    public void addAlarmTimeChangedListener(AlarmTimeChangedListener l);
}
