package ohtu.beddit.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import ohtu.beddit.R;
import ohtu.beddit.io.PreferenceService;


public class SettingsActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener, Preference.OnPreferenceClickListener{

    private ListPreference snoozeTimePref;
    private Preference forgetButton;
    private Preference reloginButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefs);

        initPrefVars();
    }

    private void initPrefVars() {
        snoozeTimePref = (ListPreference)getPreferenceScreen().findPreference(this.getString(R.string.pref_key_snooze));
        forgetButton = (Preference)getPreferenceScreen().findPreference(this.getString(R.string.pref_key_forget));
        reloginButton = (Preference)getPreferenceScreen().findPreference(this.getString(R.string.pref_key_relogin));

    }

    @Override
    protected void onResume() {
        super.onResume();

        // Setup the initial values
        setSnoozeSummary();
        handleForgetPref();

        // Set up a listener whenever a key changes
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        forgetButton.setOnPreferenceClickListener(this);
        reloginButton.setOnPreferenceClickListener(this);

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

    private void handleForgetPref(){
        String username = PreferenceService.getSettingString(this, R.string.pref_key_username);
        String token = PreferenceService.getSettingString(this, R.string.pref_key_userToken);
        if((token == null || token.equals("")) && (username == null || username.equals(""))){
            forgetButton.setSummary(getString(R.string.pref_not_logged_in));
            forgetButton.setEnabled(false);
        }
        else{ //joko token tai username löytyy
            forgetButton.setSummary(getString(R.string.pref_logged_in_as) + " " + username);
            forgetButton.setEnabled(true);
        }

    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if(preference.getKey().equals(this.getString(R.string.pref_key_forget))){
            PreferenceService.setSetting(this, R.string.pref_key_username, "");
            PreferenceService.setSetting(this, R.string.pref_key_userToken, "");
            handleForgetPref();
        }
        else if(preference.getKey().equals(this.getString(R.string.pref_key_relogin))){
            Intent myIntent = new Intent(this, AuthActivity.class);
            this.startActivity(myIntent);
        }
        return true;
    }

}
