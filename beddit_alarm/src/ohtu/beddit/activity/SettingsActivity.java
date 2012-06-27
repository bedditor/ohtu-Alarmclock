package ohtu.beddit.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.util.Log;
import ohtu.beddit.R;
import ohtu.beddit.alarm.AlarmService;
import ohtu.beddit.alarm.AlarmServiceImpl;
import ohtu.beddit.io.PreferenceService;

/**
 * This class displays and handles the menu screen for application settings.
 */
public class SettingsActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener, Preference.OnPreferenceClickListener {
    private ListPreference snoozeTimePref;
    private ListPreference sleepStagePref;
    private Preference forgetButton;
    private Preference advancedButton;

    private final String TAG = "SettingsActivity";

    private static final int FROM_AUTHENTICATION = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefs);
        Log.v(TAG, "onCreate");
    }

    /**
     * Initializes the activitys variables and calls updateSleepStageSummary().
     */
    private void initPrefVars() {
        snoozeTimePref = (ListPreference) getPreferenceScreen().findPreference(this.getString(R.string.pref_key_snooze));
        sleepStagePref = (ListPreference) getPreferenceScreen().findPreference(this.getString(R.string.pref_key_wake_up_sleep_stage));
        forgetButton = getPreferenceScreen().findPreference(this.getString(R.string.pref_key_forget));
        advancedButton = getPreferenceScreen().findPreference(this.getString(R.string.pref_key_advanced));
        updateSleepStageSummary();
    }

    /**
     * Updates data when returning to this activity and continues.
     */
    @Override
    protected void onResume() {
        super.onResume();
        Log.v(TAG, "onResume");
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
        Log.v(TAG, "onPause");
        // Unregister the listener whenever a key changes
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    /**
     * When preferences are changed this method will update the summary for the preference in question.
     * @param sharedPreferences
     * @param key
     */
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(this.getString(R.string.pref_key_snooze))) {
            updateSnoozeSummary();
        } else if (key.equals(this.getString(R.string.pref_key_wake_up_sleep_stage))) {
            updateSleepStageSummary();
        }
    }

    private void updateSnoozeSummary() {
        snoozeTimePref.setSummary(getString(R.string.pref_summary_snooze_length) + " " + snoozeTimePref.getEntry());
    }

    private void updateSleepStageSummary() {
        sleepStagePref.setSummary(sleepStagePref.getEntry());
    }

    private void updateLoginDataToSummary() {
        String fullName = PreferenceService.getFullName(this);
        String username = PreferenceService.getUsername(this);
        String token = PreferenceService.getToken(this);

        if (token.equals("") || username.equals("")) {
            forgetButton.setSummary(getString(R.string.pref_not_logged_in));
        } else {
            forgetButton.setSummary(getString(R.string.pref_logged_in_as) + " " + fullName);
        }
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (preference.getKey().equals(this.getString(R.string.pref_key_forget))) {
            AlarmService alarmService = new AlarmServiceImpl(this);
            if (alarmService.isAlarmSet()) {
                showDisconnectDialog();
            } else disconnect();
        } else if (preference.getKey().equals(this.getString(R.string.pref_key_advanced))) {
            startAdvancedSettingsActivity();
        }

        return true;
    }

    private void startAuthActivity() {
        Log.v(TAG, "starting auth activity");
        Intent myIntent = new Intent(this, AuthActivity.class);
        this.startActivityForResult(myIntent, FROM_AUTHENTICATION);
    }

    private void startAdvancedSettingsActivity() {
        Intent myIntent = new Intent(this, AdvancedSettingsActivity.class);
        startActivity(myIntent);
    }

    private void forgetMe() {
        PreferenceService.setUsername(this, "");
        PreferenceService.setFirstName(this, "");
        PreferenceService.setLastName(this, "");
        PreferenceService.setToken(this, "");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case (FROM_AUTHENTICATION):
                handleAuthActivityResult(resultCode);
                break;
        }
    }

    private void handleAuthActivityResult(int resultCode) {
        switch (resultCode) {
            case (AuthActivity.RESULT_CANCELLED):
            case (AuthActivity.RESULT_FAILED):
                setResult(resultCode);
                finish();
                break;
        }
    }

    private void showDisconnectDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.disconnect_dialog_message));
        builder.setCancelable(true);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                AlarmService alarmService = new AlarmServiceImpl(SettingsActivity.this);
                alarmService.deleteAlarm();
                disconnect();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Do nothing
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void disconnect() {
        forgetMe();
        updateLoginDataToSummary();
        startAuthActivity();
    }
}
