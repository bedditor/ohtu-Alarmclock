package ohtu.beddit.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Button;
import android.view.View.OnClickListener;
import ohtu.beddit.R;
import ohtu.beddit.alarm.AlarmService;
import ohtu.beddit.alarm.AlarmServiceImpl;
import ohtu.beddit.alarm.AlarmTimePicker;
import ohtu.beddit.views.CustomTimePicker;

public class MainActivity extends Activity
{

    private AlarmService alarmService;
    AlarmTimePicker alarmTimePicker;
    Button addAlarm;
    Button deleteAlarm;

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

    }

    private void setUI() {
        //Set clock, buttons and listeners
        alarmTimePicker = (CustomTimePicker)this.findViewById(R.id.alarmTimePicker);

        addAlarm = (Button) findViewById(R.id.setAlarmButton);
        addAlarm.setOnClickListener(new AlarmSetButtonClickListener());
        deleteAlarm = (Button)findViewById(R.id.deleteAlarmButton);
        deleteAlarm.setOnClickListener(new AlarmDeleteButtonClickListener());

        //Set layout
        LinearLayout layout = (LinearLayout)findViewById(R.id.mainLayout);
        layout.setBackgroundColor(Color.WHITE);


    }

    private void setClockHandles() {
        int[] results = alarmService.getAlarm(MainActivity.this);
        CustomTimePicker clock = (CustomTimePicker)alarmTimePicker;
        if(results[0] > 0){
            clock.setHours(results[1]);
            clock.setMinutes(results[2]);
            clock.setInterval(results[3]);
        }
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

        }
    }


    public class AlarmDeleteButtonClickListener implements OnClickListener {
        @Override
        public void onClick(View view) {
            alarmService.deleteAlarm(MainActivity.this);
            MainActivity.this.updateButtons();

        }
    }


    // Set buttons to on/off
    public void updateButtons(){
        if (alarmService.isAlarmSet(this.getApplicationContext())){
            addAlarm.setEnabled(false);
            deleteAlarm.setEnabled(true);
        } else {
            addAlarm.setEnabled(true);
            deleteAlarm.setEnabled(false);
        }
        Log.v("User Interface", "Buttons updated");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        Intent settingsIntent = new Intent(this.getApplicationContext(),
                SettingsActivity.class);

        MenuItem settings = menu.findItem(R.id.settings_menu_button);
        settings.setIntent(settingsIntent);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings_menu_button:
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
