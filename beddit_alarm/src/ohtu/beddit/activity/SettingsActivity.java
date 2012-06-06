package ohtu.beddit.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.util.Log;
import ohtu.beddit.R;
import ohtu.beddit.io.PreferenceService;


public class SettingsActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener, Preference.OnPreferenceClickListener{

    private ListPreference snoozeTimePref;
    private Preference forgetButton;
    private Preference advancedButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefs);

        initPrefVars();
    }

    private void initPrefVars() {
        snoozeTimePref = (ListPreference)getPreferenceScreen().findPreference(this.getString(R.string.pref_key_snooze));
        forgetButton = (Preference)getPreferenceScreen().findPreference(this.getString(R.string.pref_key_forget));
        advancedButton = (Preference)getPreferenceScreen().findPreference(this.getString(R.string.pref_key_advanced));

    }

    @Override
    protected void onResume() {
        super.onResume();

        // Setup the initial values
        setSnoozeSummary();
        updateForgetMeButton();

        // Set up a listener whenever a key changes
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        forgetButton.setOnPreferenceClickListener(this);
        advancedButton.setOnPreferenceClickListener(this);
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

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if(preference.getKey().equals(this.getString(R.string.pref_key_forget))){
            forgetMe();
            updateForgetMeButton();
            startAuthActivity();

        }
        else if(preference.getKey().equals(this.getString(R.string.pref_key_advanced))){
            Intent myIntent = new Intent(this, AdvancedSettingsActivity.class);
            this.startActivity(myIntent);
        }

        return true;
    }

    private void forgetMe(){
        PreferenceService.setSetting(this, R.string.pref_key_username, "");
        PreferenceService.setSetting(this, R.string.pref_key_userToken, "");
    }

    private void updateForgetMeButton(){
        String username = PreferenceService.getSettingString(this, R.string.pref_key_username);
        String token = PreferenceService.getSettingString(this, R.string.pref_key_userToken);
        if(token.equals("") || username.equals("")){
            forgetButton.setSummary(getString(R.string.pref_not_logged_in));
            forgetButton.setEnabled(false);
        }
        else{ //joko token tai username löytyy
            forgetButton.setSummary(getString(R.string.pref_logged_in_as) + " " + username);
            forgetButton.setEnabled(true);
        }
    }

    private void startAuthActivity() {
        Intent myIntent = new Intent(this, AuthActivity.class);
        this.startActivityForResult(myIntent, 3);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (3) : {
                if (resultCode == Activity.RESULT_OK) {
                    Log.v("SettingsActivity", "We got message to finish main.");
                    Intent resultIntent = new Intent((String) null);
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                }
                break;
            }
        }
    }


}
