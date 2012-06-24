package ohtu.beddit.alarm;

import java.util.Calendar;

/**
 * Defines an interface for Alarm Service class
 */
public interface AlarmService {
    Alarm addAlarm(int hours, int minutes, int interval);

    void addWakeUpAttempt(Calendar time);

    void deleteAlarm();

    public Alarm getAlarm();

    public boolean isAlarmSet();

    public int getAlarmHours();

    public int getAlarmMinutes();

    public int getAlarmInterval();

    void changeAlarm(int hours, int minutes, int interval);
}
