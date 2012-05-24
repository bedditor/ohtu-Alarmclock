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
    boolean alarmSet = false;

    public AlarmSchedulerMock(){
        this.hours = -1;
        this.minutes = -1;
        this.interval = -1;
        this.adds = 0;
        this.deletes = 0;
        this.alarmSet = false;
    }

    @Override
    public void addAlarm(Context context, int hours, int minutes, int interval) {
        adds++;
        this.hours = hours;
        this.minutes = minutes;
        this.interval = interval;
        this.alarmSet = true;
    }

    @Override
    public void deleteAlarm(Context context) {
        this.alarmSet = false;
        deletes++;
    }

    @Override
    public void setAlarmManager(AlarmManagerInterface alarmManager) {
        //do nothing
    }

    @Override
    public int[] getAlarm(Context context) {
        return new int[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isAlarmSet(Context context) {
        return alarmSet;  //To change body of implemented methods use File | Settings | File Templates.
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
