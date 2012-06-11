package ohtu.beddit.io;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import ohtu.beddit.R;

public class PreferenceService {

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
        return prefs.getString(context.getString(settingId), "");
    }

    public static void setSetting(Context context, int settingId, String newValue) {
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

    public static String getFirstname(Context context){
        return getSettingString(context, R.string.pref_key_first_name);
    }

    public static String getLastname(Context context){
            return getSettingString(context, R.string.pref_key_last_name);
    }

    public static String getFullName(Context context){
        return getFirstname(context) + " " + getLastname(context);
    }

    public static void setToken(Context context, String token){
        setSetting(context, R.string.pref_key_userToken, token);
    }

    public static void setUsername(Context context, String username){
        setSetting(context, R.string.pref_key_username, username);
    }

    public static void setFirstname(Context context, String firstname){
        setSetting(context, R.string.pref_key_first_name, firstname);
    }

    public static void setLastname(Context context, String lastname){
        setSetting(context, R.string.pref_key_last_name, lastname);
    }

    public static int getSnoozeLength(Context context){
        try{
            return Integer.parseInt(getSettingString(context, R.string.pref_key_snooze));
        }
        catch (Exception e){
            return 5;
        }
    }

    public static void setSnoozeLength(Context context, String snoozeLength){
        setSetting(context, R.string.pref_key_snooze, snoozeLength);
    }

    public static void setSnoozeLength(Context context, int snoozeLength){
        setSnoozeLength(context, Integer.toString(snoozeLength));
    }

    public static String getColourTheme(Context context){
        return getSettingString(context, R.string.pref_key_colour_theme);
    }

    public static void setColourTheme(Context context, String theme){
        setSetting(context, R.string.pref_key_colour_theme, theme);
    }
}
