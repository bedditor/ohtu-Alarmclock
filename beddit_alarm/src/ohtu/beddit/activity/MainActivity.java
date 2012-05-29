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
import ohtu.beddit.views.timepicker.CustomTimePicker;

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

        addAlarm = (Button) findViewById(R.id.setAlarmButton);
        addAlarm.setOnClickListener(new AlarmSetButtonClickListener());
        deleteAlarm = (Button)findViewById(R.id.deleteAlarmButton);
        deleteAlarm.setOnClickListener(new AlarmDeleteButtonClickListener());
        LinearLayout layout = (LinearLayout)findViewById(R.id.mainLayout);
        layout.setBackgroundColor(Color.WHITE);

        alarmTimePicker = (CustomTimePicker)this.findViewById(R.id.alarmTimePicker);

        MainActivity.this.setButtons();
        int[] results = alarmService.getAlarm(MainActivity.this);
        CustomTimePicker clock = (CustomTimePicker)alarmTimePicker;
        if(results[0] < 1){
            //muokkaa näppäimiä
        }else{
            clock.setHours(results[1]);
            clock.setMinutes(results[2]);
            clock.setInterval(results[3]);
        }

    }

    @Override
    public void onResume(){
        super.onResume();
        setButtons();
    }

    public void setAlarmService(AlarmService alarmService) {
        this.alarmService = alarmService;

    }

    public AlarmService getAlarmService() {
        return alarmService;
    }

    public class AlarmSetButtonClickListener implements OnClickListener {

        @Override
        public void onClick(View view) {
            alarmService.addAlarm(MainActivity.this, alarmTimePicker.getHours(), alarmTimePicker.getMinutes(), alarmTimePicker.getInterval());
            MainActivity.this.setButtons();

        }
    }

    public class AlarmDeleteButtonClickListener implements OnClickListener {
        @Override
        public void onClick(View view) {
            alarmService.deleteAlarm(MainActivity.this);
            MainActivity.this.setButtons();

        }
    }


    public void setButtons(){
        Log.v("Buttons", "Set");
        if (alarmService.isAlarmSet(this.getApplicationContext())){
            addAlarm.setEnabled(false);
            deleteAlarm.setEnabled(true);
        } else {
            addAlarm.setEnabled(true);
            deleteAlarm.setEnabled(false);
        }

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

}
