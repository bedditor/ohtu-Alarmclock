package ohtu.beddit.io;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferenceService {
    //public final static String PREFS = "beddit_alarm_prefs";



    public static boolean getSettingBoolean(Context context, int settingId) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(context.getString(settingId), false);
    }

    public static void setSetting(Context context, int settingId, boolean newValue) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putBoolean(context.getString(settingId), newValue);
        prefsEditor.commit();
    }

    public static String getSettingString(Context context, int settingId) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(settingId), null);
    }

    public static void setSetting(Context context, int settingId, String newValue) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString(context.getString(settingId), newValue);
        prefsEditor.commit();
    }



}
