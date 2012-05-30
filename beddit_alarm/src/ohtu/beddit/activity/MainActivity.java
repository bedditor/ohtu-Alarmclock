package ohtu.beddit.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.Toast;
import ohtu.beddit.R;
import ohtu.beddit.alarm.AlarmService;
import ohtu.beddit.alarm.AlarmServiceImpl;
import ohtu.beddit.alarm.AlarmTimePicker;
import ohtu.beddit.io.FileHandler;
import ohtu.beddit.io.PreferenceService;
import ohtu.beddit.views.CustomTimePicker;

public class MainActivity extends Activity
{

    private AlarmService alarmService;
    private AlarmTimePicker alarmTimePicker;
    private Button addAlarmButton;
    private Button deleteAlarmButton;


    /** Called when the alarm is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setAlarmService(new AlarmServiceImpl(this));

        //initialize default values for settings if called for the first time
        PreferenceManager.setDefaultValues(this, R.xml.prefs, false);
        setUI();

        // Update buttons and clock handles
        updateButtons();
        setClockHandles();

        boolean debugWeb = true;
        String token = PreferenceService.getSettingString(this, R.string.pref_key_userToken);
        if ( (token == null || token.equals("")) && debugWeb) {
            Intent myIntent = new Intent(this, AuthActivity.class);
            this.startActivity(myIntent);
        }

    }

    private void setUI() {
        //Set clock, buttons and listeners
        alarmTimePicker = (CustomTimePicker)this.findViewById(R.id.alarmTimePicker);

        addAlarmButton = (Button) findViewById(R.id.setAlarmButton);
        addAlarmButton.setOnClickListener(new AlarmSetButtonClickListener());
        deleteAlarmButton = (Button)findViewById(R.id.deleteAlarmButton);
        deleteAlarmButton.setOnClickListener(new AlarmDeleteButtonClickListener());

        //Set layout
        LinearLayout layout = (LinearLayout)findViewById(R.id.mainLayout);
        layout.setBackgroundColor(Color.WHITE);

    }

    private void setClockHandles() {
        Context context = MainActivity.this;
        CustomTimePicker clock = (CustomTimePicker)alarmTimePicker;
        clock.setHours(alarmService.getAlarmHours(context));
        clock.setMinutes(alarmService.getAlarmMinutes(context));
        clock.setInterval(alarmService.getAlarmInterval(context));
    }

    @Override
    public void onResume(){
        super.onResume();
        updateButtons();
    }



    public class AlarmSetButtonClickListener implements OnClickListener {

        @Override
        public void onClick(View view) {
            alarmService.addAlarm(MainActivity.this, alarmTimePicker.getHours(), alarmTimePicker.getMinutes(), alarmTimePicker.getInterval());
            MainActivity.this.updateButtons();
            // Tell the user about what we did.
            Toast.makeText(MainActivity.this, getString(R.string.toast_alarmset), Toast.LENGTH_LONG).show();

        }
    }


    public class AlarmDeleteButtonClickListener implements OnClickListener {
        @Override
        public void onClick(View view) {
            alarmService.deleteAlarm(MainActivity.this);
            MainActivity.this.updateButtons();
            Toast.makeText(MainActivity.this, getString(R.string.toast_alarmremoved), Toast.LENGTH_LONG).show();
        }
    }


    // Set buttons to on/off
    public void updateButtons(){
        if (alarmService.isAlarmSet(this.getApplicationContext())){
            addAlarmButton.setEnabled(false);
            deleteAlarmButton.setEnabled(true);
        } else {
            addAlarmButton.setEnabled(true);
            deleteAlarmButton.setEnabled(false);
        }
        Log.v("User Interface", "Buttons updated");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        MenuItem settings = menu.findItem(R.id.settings_menu_button);
        settings.setIntent(new Intent(this.getApplicationContext(), SettingsActivity.class));

        MenuItem help = menu.findItem(R.id.help_menu_button);
        help.setIntent(new Intent(this.getApplicationContext(), HelpActivity.class));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings_menu_button:
                startActivity(item.getIntent());
                break;
            case R.id.help_menu_button:
                startActivity(item.getIntent());
                break;
        }
        return true;
    }

    // These methods are for tests

    public void setAlarmService(AlarmService alarmService) {
        this.alarmService = alarmService;

    }

    public AlarmService getAlarmService() {
        return alarmService;
    }

}
