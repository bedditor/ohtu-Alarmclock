package ohtu.beddit.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import ohtu.beddit.R;
import ohtu.beddit.io.PreferenceService;


public class AdvancedSettingsActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener{

    private ListPreference colourThemePref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.advancedprefs);

        initPrefVars();
    }

    private void initPrefVars() {
        colourThemePref = (ListPreference)getPreferenceScreen().findPreference(this.getString(R.string.pref_key_colour_theme));
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Setup the initial values
        setColourThemeSummary();

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
        }
    }

    private void setColourThemeSummary(){
        colourThemePref.setSummary(getString(R.string.pref_summary_colour_theme) + " " + colourThemePref.getEntry());
    }

}
