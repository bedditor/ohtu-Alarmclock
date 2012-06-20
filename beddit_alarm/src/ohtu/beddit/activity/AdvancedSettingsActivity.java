package ohtu.beddit.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import ohtu.beddit.R;


public class AdvancedSettingsActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private ListPreference colourThemePref;
    private ListPreference alarmSoundLengthPref;

    private static final String TAG = "AdvancedSettingsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.advancedprefs);

        initPrefVars();
    }

    private void initPrefVars() {
        colourThemePref = (ListPreference) getPreferenceScreen().findPreference(this.getString(R.string.pref_key_colour_theme));
        alarmSoundLengthPref = (ListPreference) getPreferenceScreen().findPreference(this.getString(R.string.pref_key_alarm_length));
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Setup the initial values
        setColourThemeSummary();
        setAlarmSoundLengthSummary();

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
        } else if (key.equals(this.getString(R.string.pref_key_alarm_length))) {
            setAlarmSoundLengthSummary();
        }
    }

    private void setColourThemeSummary() {
        colourThemePref.setSummary(getString(R.string.pref_summary_colour_theme) + " " + colourThemePref.getEntry());
    }

    private void setAlarmSoundLengthSummary() {
        alarmSoundLengthPref.setSummary(getString(R.string.pref_summary_alarm_length) + " " + alarmSoundLengthPref.getEntry());
    }
}
