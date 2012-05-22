package com.example;

import android.app.AlarmManager;
import android.app.PendingIntent;

public class AlarmManagerMock implements AlarmManagerInterface {

    @Override
    public void set(int type, long triggerAtTime, PendingIntent operation) {
        //To change body of implemented methods use File | Settings | File Templates.
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
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setTime(long millis) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setTimeZone(String timeZone) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
