package ohtu.beddit.alarm;

import java.util.Calendar;

/*
 * Logic class for keeping track of the alarms. Alarm service ensures that new alarms are sent to the Android's
 * alarm manager, saved in a file and a correct notification is displayed. It will also calculate correct times
 * for the first wake up attempts, if the new alarm has an interval.
 *
 * POSSIBLE MODIFICATIONS:
 * Make this class singleton to get rid of the static alarm.
 *
 */
public interface AlarmService {
    /**
     * This method saves a new alarm with an interval to a file and sets it to android AlarmManager
     * @param hours wake up hours
     * @param minutes wake up minutes
     * @param interval wake up interval
     * @return alarm set as an object
     */
    Alarm addAlarm(int hours, int minutes, int interval);

    /**
     * Only sets the alarm to android AlarmManager without writing it to file.
     * @param time when to try wake up next time
     */
    void addWakeUpAttempt(Calendar time);

    /**
     * disables alarm, saves it to file and removes from androids AlarmManager
     */
    void deleteAlarm();

    /**
     * Gets saved alarm
     * @return saved alarm
     */
    public Alarm getAlarm();

    public boolean isAlarmSet();

    public int getAlarmHours();

    public int getAlarmMinutes();

    public int getAlarmInterval();

    /**
     * Checks if alarm is enabled. If true, changes the alarm time by calling {@link #addAlarm(int, int, int)}, else does nothing.
     */
    void changeAlarm(int hours, int minutes, int interval);
}
