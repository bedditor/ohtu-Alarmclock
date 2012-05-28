package ohtu.beddit.activity;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import ohtu.beddit.R;


public class SettingsActivity extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.prefs);
    }
}
