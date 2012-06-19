package ohtu.beddit.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager;
import ohtu.beddit.R;
import ohtu.beddit.io.PreferenceService;


public class AdvancedSettingsActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener{

    private ListPreference colourThemePref;
    private ListPreference alarmSoundLenghtPref;

    private static final String TAG = "AdvancedSettingsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.advancedprefs);

        initPrefVars();
    }

    private void initPrefVars() {
        colourThemePref = (ListPreference)getPreferenceScreen().findPreference(this.getString(R.string.pref_key_colour_theme));
        alarmSoundLenghtPref = (ListPreference)getPreferenceScreen().findPreference(this.getString(R.string.pref_key_alarm_length));
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Setup the initial values
        setColourThemeSummary();
        setAlarmSoundLenghtSummary();

        // Set up a listener whenever a key changes
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Unregister the listener whenever a key changes
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(this.getString(R.string.pref_key_colour_theme))) {
            setColourThemeSummary();
        }else if(key.equals(this.getString(R.string.pref_key_alarm_length))){
            setAlarmSoundLenghtSummary();
        }
    }

    private void setColourThemeSummary(){
        colourThemePref.setSummary(getString(R.string.pref_summary_colour_theme) + " " + colourThemePref.getEntry());
    }

    private void setAlarmSoundLenghtSummary(){
        alarmSoundLenghtPref.setSummary(getString(R.string.pref_summary_alarm_length) + " "+alarmSoundLenghtPref.getEntry());
    }
}
