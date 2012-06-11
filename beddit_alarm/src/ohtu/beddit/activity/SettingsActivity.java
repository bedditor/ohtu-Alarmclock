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
import ohtu.beddit.json.BedditApiController;
import ohtu.beddit.web.BedditWebConnector;


public class SettingsActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener, Preference.OnPreferenceClickListener{

    private ListPreference snoozeTimePref;
    private ListPreference sleepStagePref;
    private Preference forgetButton;
    private Preference advancedButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefs);

    }

    private void initPrefVars() {
        snoozeTimePref = (ListPreference)getPreferenceScreen().findPreference(this.getString(R.string.pref_key_snooze));
        sleepStagePref = (ListPreference)getPreferenceScreen().findPreference(this.getString(R.string.pref_key_wake_up_sleep_stage));
        forgetButton = (Preference)getPreferenceScreen().findPreference(this.getString(R.string.pref_key_forget));
        advancedButton = (Preference)getPreferenceScreen().findPreference(this.getString(R.string.pref_key_advanced));
    }

    @Override
    protected void onResume() {
        super.onResume();

        initPrefVars();

        // Setup the initial values
        updateSnoozeSummary();
        updateLoginDataToSummary();

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
            updateSnoozeSummary();
        }
        else if (key.equals(this.getString(R.string.pref_key_wake_up_sleep_stage))) {
            updateSleepStageSummary();
        }
    }

    private void updateSnoozeSummary(){
        snoozeTimePref.setSummary(getString(R.string.pref_summary_snooze_length) + " " + snoozeTimePref.getEntry());
    }

    private void updateSleepStageSummary(){
        sleepStagePref.setSummary(sleepStagePref.getEntry());
    }



    private void updateLoginDataToSummary(){
        String fullName = PreferenceService.getFullName(this) ;
        String username = PreferenceService.getUsername(this);
        String token = PreferenceService.getToken(this);

        if(token.equals("") || username.equals("")){
            forgetButton.setSummary(getString(R.string.pref_not_logged_in));
        }
        else{ //joko token tai username löytyy
            forgetButton.setSummary(getString(R.string.pref_logged_in_as) + " " + fullName);
        }
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if(preference.getKey().equals(this.getString(R.string.pref_key_forget))){
            forgetMe();
            updateLoginDataToSummary();
            startAuthActivity();
        }
        else if(preference.getKey().equals(this.getString(R.string.pref_key_advanced))){
            startAdvancedSettingsActivity();
        }

        return true;
    }

    private void startAuthActivity() {
        Intent myIntent = new Intent(this, AuthActivity.class);
        this.startActivityForResult(myIntent, 3);
    }

    private void startAdvancedSettingsActivity() {
        Intent myIntent = new Intent(this, AdvancedSettingsActivity.class);
        this.startActivity(myIntent);
    }

    private void forgetMe(){
        PreferenceService.setUsername(this, "");
        PreferenceService.setFirstName(this, "");
        PreferenceService.setLastName(this, "");
        PreferenceService.setToken(this, "");
    }


    private BedditApiController getApiController(){
        return new BedditApiController(new BedditWebConnector());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (3) : {
                if (resultCode == Activity.RESULT_OK) {
                    Log.v("SettingsActivity", "OAuth failure");
                    Intent resultIntent = new Intent((String) null);
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                }
                break;
            }
        }
    }


}
