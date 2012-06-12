package ohtu.beddit.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.format.DateFormat;
import android.view.*;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;
import ohtu.beddit.R;
import ohtu.beddit.alarm.*;

import ohtu.beddit.json.BedditApiController;
import ohtu.beddit.views.timepicker.CustomTimePicker;
import ohtu.beddit.io.PreferenceService;
import ohtu.beddit.web.BedditWebConnector;
import ohtu.beddit.web.MalformedBedditJsonException;

public class MainActivity extends Activity implements AlarmTimeChangedListener
{
    private static final String TAG = "MainActivity";
    private AlarmService alarmService;
    private Toast myToast;
    private AlarmTimePicker alarmTimePicker;
    private Button addAlarmButton;
    private Button deleteAlarmButton;


    /** Called when the alarm is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Log.v(TAG, "OnCreate");
        alarmService = new AlarmServiceImpl(this);

        //initialize default values for settings if called for the first time
        PreferenceManager.setDefaultValues(this, R.xml.prefs, true);
        PreferenceManager.setDefaultValues(this, R.xml.advancedprefs, true);
        setUI();

        // Update buttons and clock handles
        updateButtons();
        setClockHands();
        if(!isTokenValid()){
            startAuthActivity();
        }
        // leave britney alone! testDialog();
    }

    @Override
    public void onResume(){
        super.onResume();
        updateButtons();
        updateColours();
        update24HourMode();
        setClockHands();
        Log.v(TAG, "OnResume");
    }

    @Override
    public void finish() {
        Log.v(TAG, "Finishing");
        super.finish();
    }

    private boolean isTokenValid() {
        Log.v(TAG,"Validating token");
        String token = PreferenceService.getToken(this);
        if (token == null || token.equals("")) {
            Log.v(TAG,"Token was empty");
            return false;
        }

        BedditApiController apiController = new BedditApiController(new BedditWebConnector());
        String username;
        try {
            apiController.updateUserInfo(this);
            username = apiController.getUsername(0);
        } catch (MalformedBedditJsonException e) {
            Log.v(TAG,"Got malformed json while trying to get username and validate token");
            return false;
        }

        if (username == null || username.equals("")) {
            Log.v(TAG,"Username was empty while validating token");
            return false;
        }
        Log.v(TAG,"Token was valid");
        return true;
    }

    private void startAuthActivity() {
        Log.v(TAG,"Starting authActivity");
        Intent myIntent = new Intent(this, AuthActivity.class);
        startActivityForResult(myIntent,2);
    }





    private void setUI() {
        //Set clock, buttons and listeners
        alarmTimePicker = (CustomTimePicker)this.findViewById(R.id.alarmTimePicker);
        alarmTimePicker.addAlarmTimeChangedListener(this);

        addAlarmButton = (Button) findViewById(R.id.setAlarmButton);
        addAlarmButton.setOnClickListener(new AlarmSetButtonClickListener());
        deleteAlarmButton = (Button)findViewById(R.id.deleteAlarmButton);
        deleteAlarmButton.setOnClickListener(new AlarmDeleteButtonClickListener());

        myToast = Toast.makeText(getBaseContext(), "", Toast.LENGTH_SHORT);

        updateColours();
    }

    private void updateColours(){
        String theme = PreferenceService.getColourTheme(this);
        LinearLayout layout = (LinearLayout)findViewById(R.id.mainLayout);
        if(theme.equals("dark")){
            layout.setBackgroundColor(Color.BLACK);
            alarmTimePicker.setBackgroundColor(Color.BLACK);
            alarmTimePicker.setForegroundColor(Color.WHITE);
            alarmTimePicker.setSpecialColor(Color.argb(255,255,89,0));
            ((TextView)findViewById(R.id.interval_title)).setTextColor(Color.WHITE);
        }
        else if(theme.equals("light")){
            layout.setBackgroundColor(Color.WHITE);
            alarmTimePicker.setBackgroundColor(Color.WHITE);
            alarmTimePicker.setForegroundColor(Color.BLACK);
            alarmTimePicker.setSpecialColor(Color.argb(255,255,89,0));
            ((TextView)findViewById(R.id.interval_title)).setTextColor(Color.BLACK);
        }
    }

    private void update24HourMode(){
        alarmTimePicker.set24HourMode(DateFormat.is24HourFormat(this));
    }

    private void setClockHands() {
        alarmTimePicker.setHours(alarmService.getAlarmHours());
        alarmTimePicker.setMinutes(alarmService.getAlarmMinutes());
        alarmTimePicker.setInterval(alarmService.getAlarmInterval());
    }

    @Override
    public void onAlarmTimeChanged(int hours, int minutes, int interval) {
        alarmService.changeAlarm(hours, minutes, interval);

        if (alarmService.isAlarmSet())
            showMeTheToast(getString(R.string.toast_alarmupdated));

    }

    private void showMeTheToast(final String message) {

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                myToast.setText(message);
                myToast.setGravity(Gravity.TOP | Gravity.RIGHT, 0, 0);
                myToast.show();
            }
        });
    }

    public class AlarmSetButtonClickListener implements OnClickListener {

        @Override
        public void onClick(View view) {
            alarmService.addAlarm(alarmTimePicker.getHours(), alarmTimePicker.getMinutes(), alarmTimePicker.getInterval());
            MainActivity.this.updateButtons();
            // Tell the user about what we did.
            showMeTheToast(getString(R.string.toast_alarmset));
        }
    }


    public class AlarmDeleteButtonClickListener implements OnClickListener {
        @Override
        public void onClick(View view) {
            alarmService.deleteAlarm();
            MainActivity.this.updateButtons();
            showMeTheToast(getString(R.string.toast_alarmremoved));

        }
    }

    // Set buttons to on/off
    public void updateButtons(){
        if (alarmService.isAlarmSet()){
            addAlarmButton.setEnabled(false);
            deleteAlarmButton.setEnabled(true);
        } else {
            addAlarmButton.setEnabled(true);
            deleteAlarmButton.setEnabled(false);
        }
        Log.v(TAG, "Buttons updated");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        MenuItem settings = menu.findItem(R.id.settings_menu_button);
        settings.setIntent(new Intent(this.getApplicationContext(), SettingsActivity.class));

        MenuItem help = menu.findItem(R.id.help_menu_button);
        help.setIntent(new Intent(this.getApplicationContext(), HelpActivity.class));

        MenuItem sleepInfo = menu.findItem(R.id.sleep_info_button);
        sleepInfo.setIntent(new Intent(this.getApplicationContext(), SleepInfoActivity.class));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings_menu_button:
                startActivityForResult(item.getIntent(), 3);
                break;
            case R.id.help_menu_button:
                startActivity(item.getIntent());
                break;
            case R.id.sleep_info_button:
                startActivity(item.getIntent());
                break;
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (2) : {
                if (resultCode == Activity.RESULT_OK) {
                    Log.v(TAG, "AuthActivity sent fail");
                    createDialog();
                }
                break;
            }
            case (3) : {
                if (resultCode == Activity.RESULT_OK) {
                    Log.v(TAG, "SettingsActivity sent fail");
                    createDialog();
                }
                break;
            }
        }
    }

    public void createDialog(){
        Log.v(TAG, "Dialog created");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("CRASH!");
        builder.setCancelable(false);
        builder.setPositiveButton("D:", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Log.v(TAG, "Dialog ok clicked");
                MainActivity.this.finish();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void testDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Spam api requests:");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes I will", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                BedditWebConnector blob = new BedditWebConnector();
                /*try{
                    Log.v("derp", "tila on: "+blob.getQueueStateJson(MainActivity.this, AlarmCheckerRealImpl.getQueryDateString()));
                    Log.v("derp", "post: " + blob.requestDataAnalysis(MainActivity.this, AlarmCheckerRealImpl.getQueryDateString()));
                }catch(MalformedBedditJsonException m){
                    Log.v("derp", "D: virhe");
                }*/
                AlarmChecker check = new AlarmCheckerRealImpl();
                check.wakeUpNow(MainActivity.this, 'M');
            }
        });
        AlertDialog alert = builder.create();
        alert.show();

    }

}
