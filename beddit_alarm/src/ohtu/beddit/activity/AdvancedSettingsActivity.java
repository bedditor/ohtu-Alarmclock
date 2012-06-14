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

    private static final String TAG = "AdvancedSettingsActivity";

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

    @Override
    public void onAttachedToWindow() {
        Log.v(TAG, "SETTING KEYGUARD ON");
        Log.v(TAG, "onAttachedToWindow");
        this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD);
        super.onAttachedToWindow();    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_HOME) {
            Log.v(TAG, "HOME PRESSED");
            finish();
        }

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Log.v(TAG, "BACK PRESSED");
        }

        if (keyCode == KeyEvent.KEYCODE_CALL) {
            Log.v(TAG, "CALL PRESSED");
            handleCallButton();
            finish();
        }

        return super.onKeyDown(keyCode, event);
    }

    private void handleCallButton() {
        Log.v(TAG, "HANDLING CALL BUTTON");
        Intent exitIntent = new Intent(this, ExitActivity.class);
        exitIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(exitIntent);
    }
}
