package ohtu.beddit.io;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import ohtu.beddit.R;

import java.io.IOException;

public class PreferenceService {

    private static boolean getSettingBoolean(Context context, int settingId) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(context.getString(settingId), false);
    }

    private static void setSetting(Context context, int settingId, boolean newValue) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putBoolean(context.getString(settingId), newValue);
        prefsEditor.commit();
    }

    private static String getSettingString(Context context, int settingId) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(settingId), "");
    }

    private static void setSetting(Context context, int settingId, String newValue) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString(context.getString(settingId), newValue);
        prefsEditor.commit();
    }

    public static String getToken(Context context){
        return getSettingString(context, R.string.pref_key_userToken);
    }

    public static String getUsername(Context context){
        return getSettingString(context, R.string.pref_key_username);
    }

    public static String getFirstName(Context context){
        return getSettingString(context, R.string.pref_key_first_name);
    }

    public static String getLastName(Context context){
            return getSettingString(context, R.string.pref_key_last_name);
    }

    public static int getAlarmLength(Context context){
        Log.v("integersysteemi", "dis: " + getSettingString(context, R.string.pref_key_alarm_length));
        return Integer.parseInt(getSettingString(context, R.string.pref_key_alarm_length));
    }

    public static String getFullName(Context context){
        return getFirstName(context) + " " + getLastName(context);
    }

    public static void setToken(Context context, String token){
        setSetting(context, R.string.pref_key_userToken, token);
    }

    public static void setUsername(Context context, String username){
        setSetting(context, R.string.pref_key_username, username);
    }

    public static void setFirstName(Context context, String firstName){
        setSetting(context, R.string.pref_key_first_name, firstName);
    }

    public static void setLastName(Context context, String lastName){
        setSetting(context, R.string.pref_key_last_name, lastName);
    }

    public static int getSnoozeLength(Context context){
        try{
            return Integer.parseInt(getSettingString(context, R.string.pref_key_snooze));
        }
        catch (Exception e){
            return 5;
        }
    }

    public static String getColourTheme(Context context){
        return getSettingString(context, R.string.pref_key_colour_theme);
    }

    public static char getWakeUpSleepStage(Context context){
        return getSettingString(context, R.string.pref_key_wake_up_sleep_stage).charAt(0);
    }

    public static boolean getShowSleepData(Context context){
        return getSettingBoolean(context, R.string.pref_key_show_sleep_data_checkbox);
    }

    public static boolean getAwesome(Context context){
        return getSettingBoolean(context, R.string.pref_key_awesome);
    }
}
