package com.example;

import android.content.Context;

/**
 * Created with IntelliJ IDEA.
 * User: joosakur
 * Date: 22.5.2012
 * Time: 10:16
 * To change this template use File | Settings | File Templates.
 */
public interface AlarmScheduler {
    void addAlarm(Context context, int hours, int minutes, int interval);

    void deleteAlarm(Context context);
}
