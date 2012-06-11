package ohtu.beddit.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.util.Log;
import ohtu.beddit.R;
import ohtu.beddit.io.PreferenceService;
import ohtu.beddit.json.BedditApiController;
import ohtu.beddit.web.BedditWebConnector;


public class SettingsActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener, Preference.OnPreferenceClickListener{

    private PreferenceCategory bedditConnectionPrefs;
    private ListPreference snoozeTimePref;
    private ListPreference userSelectionPreference;
    private Preference forgetButton;
    private Preference advancedButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefs);

    }

    private void initPrefVars() {
        bedditConnectionPrefs = (PreferenceCategory)getPreferenceScreen().findPreference(this.getString(R.string.pref_key_bedditconnect));
        snoozeTimePref = (ListPreference)getPreferenceScreen().findPreference(this.getString(R.string.pref_key_snooze));
        forgetButton = (Preference)getPreferenceScreen().findPreference(this.getString(R.string.pref_key_forget));
        userSelectionPreference = (ListPreference)getPreferenceScreen().findPreference(this.getString(R.string.pref_key_userIndex));
        advancedButton = (Preference)getPreferenceScreen().findPreference(this.getString(R.string.pref_key_advanced));
    }

    @Override
    protected void onResume() {
        super.onResume();

        initPrefVars();

        // Setup the initial values
        updateSnoozeSummary();
        updateLoginDataToSummary();
        updateUserSelectionPreferences();

        // Set up a listener whenever a key changes
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        forgetButton.setOnPreferenceClickListener(this);
        userSelectionPreference.setOnPreferenceClickListener(this);
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
        else if (key.equals(this.getString(R.string.pref_key_userIndex))) {
            updateLoginDataToSummary();
        }
    }

    private void updateSnoozeSummary(){
        snoozeTimePref.setSummary(getString(R.string.pref_summary_snooze_length) + " " + snoozeTimePref.getEntry());
    }

    private void updateLoginDataToSummary(){
        int userIndex = PreferenceService.getUserIndex(this);
        String fullName = PreferenceService.getFullName(this, userIndex) ;
        String username = PreferenceService.getUsername(this, userIndex);
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

    private void updateUserSelectionPreferences(){
        int userCount = 2;
        if(PreferenceService.getUsername(this, 1).equals(""))
            userCount = 1;

        CharSequence[] entries = new CharSequence[userCount];
        CharSequence[] entryValues = new CharSequence[userCount];
        for(int i=0; i<userCount; i++){
            entries[i] = PreferenceService.getFullName(this, i);
            entryValues[i] = Integer.toString(i);
        }

        userSelectionPreference.setEntries(entries);
        userSelectionPreference.setEntryValues(entryValues);
    }

    private void forgetMe(){
        PreferenceService.setUsername(this, "", 0);
        PreferenceService.setUsername(this, "", 1);
        PreferenceService.setFirstname(this, "", 0);
        PreferenceService.setFirstname(this, "", 1);
        PreferenceService.setLastname(this, "", 0);
        PreferenceService.setLastname(this, "", 1);
        PreferenceService.setToken(this, "");
    }


    private String getFullNameFromApi(int userIndex){
        BedditApiController apiController = getApiController();
        return apiController.getFirstName(this, userIndex) + " " + apiController.getLastName(this, userIndex);
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
