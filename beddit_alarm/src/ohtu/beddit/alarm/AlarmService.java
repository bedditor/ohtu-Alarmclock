package ohtu.beddit.alarm;

import android.content.Context;

import java.util.Calendar;

/**
 * Created with IntelliJ IDEA.
 * User: joosakur
 * Date: 22.5.2012
 * Time: 10:16
 * To change this template use File | Settings | File Templates.
 */
public interface AlarmService {
    void addAlarm(Context context, int hours, int minutes, int interval);

    void addWakeUpAttempt(Context context, Calendar time);

    void deleteAlarm(Context context);

    public boolean isAlarmSet(Context context);

    public int getAlarmHours(Context context);

    public int getAlarmMinutes(Context context);

    public int getAlarmInterval(Context context);

    void changeAlarm(Context context, int hours, int minutes, int interval);
}
