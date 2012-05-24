package com.example;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedSettings {
    public final static String PREFS = "beddit_alarm_prefs";

    public static boolean getSettingBoolean(Context context, int settingId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS, 0);
        return prefs.getBoolean(context.getString(settingId), false);
    }

    public static void setSetting(Context context, int settingId, boolean newValue) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS, 0);
        android.content.SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putBoolean(context.getString(settingId), newValue);
        prefsEditor.commit();
    }

    public static String getSettingString(Context context, int settingId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS, 0);
        return prefs.getString(context.getString(settingId), "");
    }

    public static void setSetting(Context context, int settingId, String newValue) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS, 0);
        android.content.SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString(context.getString(settingId), newValue);
        prefsEditor.commit();
    }

    public static int getSettingInt(Context context, int settingId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS, 0);
        return prefs.getInt(context.getString(settingId), 0);
    }

    public static void setSetting(Context context, int settingId, int newValue) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS, 0);
        android.content.SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putInt(context.getString(settingId), newValue);
        prefsEditor.commit();
    }



}
