package com.example;

import android.app.AlarmManager;
import android.app.PendingIntent;

public class AlarmManagerMock implements AlarmManagerInterface {

    int sets = 0;
    int cancels = 0;
    long triggerTime = -1;
    PendingIntent operationSet;
    PendingIntent operationCancel;


    @Override
    public void set(int type, long triggerAtTime, PendingIntent operation) {
        sets++;
        triggerTime = triggerAtTime;
        operationSet = operation;
    }

    @Override
    public void setRepeating(int type, long triggerAtTime, long interval, PendingIntent operation) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setInexactRepeating(int type, long triggerAtTime, long interval, PendingIntent operation) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void cancel(PendingIntent operation) {
        cancels++;
        operationCancel = operation;
    }

    @Override
    public void setTime(long millis) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setTimeZone(String timeZone) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public int getSets() {
        return sets;
    }

    public int getCancels() {
        return cancels;
    }

    public long getTriggerTime() {
        return triggerTime;
    }

    public PendingIntent getOperationSet() {
        return operationSet;
    }

    public PendingIntent getOperationCancel() {
        return operationCancel;
    }
}
