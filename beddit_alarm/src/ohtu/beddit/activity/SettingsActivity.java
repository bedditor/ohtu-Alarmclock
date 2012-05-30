package ohtu.beddit.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import ohtu.beddit.R;


public class SettingsActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private ListPreference snoozeTimePref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefs);

        snoozeTimePref = (ListPreference)getPreferenceScreen().findPreference(this.getString(R.string.pref_key_snooze));
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Setup the initial values
        setSnoozeSummary();

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
        if (key.equals(this.getString(R.string.pref_key_snooze))) {
            setSnoozeSummary();
        }
    }

    private void setSnoozeSummary(){
        snoozeTimePref.setSummary(getString(R.string.pref_summary_snooze_length) + " " + snoozeTimePref.getEntry());
    }
}
