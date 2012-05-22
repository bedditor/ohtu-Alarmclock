package com.example;

import android.content.Context;

/**
 * Created with IntelliJ IDEA.
 * User: joosakur
 * Date: 22.5.2012
 * Time: 11:04
 * To change this template use File | Settings | File Templates.
 */
public class AlarmSchedulerMock implements AlarmScheduler {
    int hours = -1;
    int minutes = -1;
    int interval = -1;
    int adds = 0;
    int deletes = 0;

    @Override
    public void addAlarm(Context context, int hours, int minutes, int interval) {
        adds++;
        this.hours = hours;
        this.minutes = minutes;
        this.interval = interval;
    }

    @Override
    public void deleteAlarm(Context context) {
        deletes++;
    }

    @Override
    public void setAlarmManager(AlarmManagerInterface alarmManager) {
        //do nothing
    }

    public int getHours() {
        return hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public int getInterval() {
        return interval;
    }

    public int getAdds() {
        return adds;
    }

    public int getDeletes() {
        return deletes;
    }
}
