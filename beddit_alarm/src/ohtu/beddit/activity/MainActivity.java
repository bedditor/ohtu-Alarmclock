package ohtu.beddit.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
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

import ohtu.beddit.api.ApiController;
import ohtu.beddit.api.jsonclassimpl.ApiControllerClassImpl;
import ohtu.beddit.utils.DialogUtils;
import ohtu.beddit.views.timepicker.CustomTimePicker;
import ohtu.beddit.io.PreferenceService;
import ohtu.beddit.web.BedditException;
import ohtu.beddit.web.UnauthorizedException;

public class MainActivity extends Activity implements AlarmTimeChangedListener
{
    private static final String TAG = "MainActivity";
    private AlarmService alarmService;
    private Toast myToast;
    private AlarmTimePicker alarmTimePicker;
    private Button addAlarmButton;
    private Button deleteAlarmButton;

    private static final int DARK_THEME_BACKGROUND = Color.BLACK;
    private static final int DARK_THEME_FOREGROUND = Color.WHITE;
    private static final int LIGHT_THEME_BACKGROUND = Color.WHITE;
    private static final int LIGHT_THEME_FOREGROUND = Color.BLACK;
    private static final int BEDDIT_ORANGE = Color.argb(255,255,89,0);

    public static final int FROM_AUTHENTICATION = 2;
    public static final int FROM_SETTINGS = 3;
    public static final int FROM_SLEEP_INFO = 4;
    public static final int FROM_HELP = 5;

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
        initializeUI();
        checkToken();

        /*testDialog();
        testDialog();
        testDialog();
        testDialog();
        testDialog();*/
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        Log.v(TAG, "onWindowFocusChanged to " + hasFocus);
        super.onWindowFocusChanged(hasFocus);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void onResume(){
        super.onResume();
        toggleButtonStates();
        updateColours();
        update24HourMode();

        if (alarmService.isAlarmSet())
            setClockHands();

        Log.v(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        Log.v(TAG, "onPause");

        super.onPause();    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    protected void onRestart() {
        Log.v(TAG, "onRestart");
        super.onRestart();    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    protected void onDestroy() {
        Log.v(TAG, "onDestroy");
        super.onDestroy();    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    protected void onStop() {
        Log.v(TAG, "onStop");
        super.onStop();    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    protected void onStart() {
        Log.v(TAG, "onStart");
        super.onStart();    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void finish() {
        Log.v(TAG, "Finishing");
        super.finish();
    }

    private void checkToken() {
        String token = PreferenceService.getToken(this);
        if (token == null || token.equals(""))  {
            Log.v(TAG,"Token was empty");
            startAuthActivity();
        } else {
            Log.v(TAG, "Starting token validation");
            new TokenChecker().execute();
        }
    }

    private class TokenChecker extends AsyncTask<Void,Void,Boolean> {
        @Override
        protected Boolean doInBackground(Void... voids) {
            return MainActivity.this.isTokenValid();
        }

        @Override
        protected void onPostExecute(Boolean isValid) {
            Log.v(TAG, "Token was "+(isValid?"valid":"invalid"));
            if (!isValid) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage(getString(R.string.mainActivity_tokenExpiredDialog_text))
                        .setCancelable(false)
                        .setPositiveButton(getString(R.string.mainActivity_tokenExpiredDialog_yes), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                MainActivity.this.startAuthActivity();
                            }
                        })
                        .setNegativeButton(getString(R.string.mainActivity_tokenExpiredDialog_no), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                MainActivity.this.finish();
                            }
                        });
                AlertDialog tokenExpiredDialog = builder.create();
                tokenExpiredDialog.show();
            }
        }
    }

    private boolean isTokenValid() {
        Log.v(TAG, "Validating token");
        try {
            ApiController apiController = new ApiControllerClassImpl();
            apiController.updateUserData(this);
            String username = apiController.getUsername(this, 0);
            PreferenceService.setUsername(this, username);
        }
        catch (UnauthorizedException e){
            Log.v(TAG, "Unauthorized!");
            PreferenceService.setToken(this, "");
            if(alarmService.isAlarmSet()){
                alarmService.deleteAlarm();
                showMeTheToast(getString(R.string.toast_alarmremoved));
            }
            return false;
        }
        catch (BedditException e) {
            Log.v(TAG, "Problem with connection");
        }
        return true;
    }

    private void startAuthActivity() {
        Log.v(TAG,"Starting authActivity");
        Intent myIntent = new Intent(this, AuthActivity.class);
        startActivityForResult(myIntent, FROM_AUTHENTICATION);
    }

    private void initializeUI() {
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
            layout.setBackgroundColor(DARK_THEME_BACKGROUND);
            alarmTimePicker.setBackgroundColor(DARK_THEME_BACKGROUND);
            alarmTimePicker.setForegroundColor(DARK_THEME_FOREGROUND);
            alarmTimePicker.setSpecialColor(BEDDIT_ORANGE);
            ((TextView)findViewById(R.id.interval_title)).setTextColor(DARK_THEME_FOREGROUND);
        }
        else if(theme.equals("light")){
            layout.setBackgroundColor(LIGHT_THEME_BACKGROUND);
            alarmTimePicker.setBackgroundColor(LIGHT_THEME_BACKGROUND);
            alarmTimePicker.setForegroundColor(LIGHT_THEME_FOREGROUND);
            alarmTimePicker.setSpecialColor(BEDDIT_ORANGE);
            ((TextView)findViewById(R.id.interval_title)).setTextColor(LIGHT_THEME_FOREGROUND);
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
            MainActivity.this.toggleButtonStates();
            // Tell the user about what we did.
            showMeTheToast(getString(R.string.toast_alarmset));
        }
    }

    public class AlarmDeleteButtonClickListener implements OnClickListener {
        @Override
        public void onClick(View view) {
            alarmService.deleteAlarm();
            MainActivity.this.toggleButtonStates();
            showMeTheToast(getString(R.string.toast_alarmremoved));
        }
    }

    public void toggleButtonStates(){
        Log.v(TAG, "Buttons toggled");
        addAlarmButton.setEnabled(!alarmService.isAlarmSet());
        deleteAlarmButton.setEnabled(alarmService.isAlarmSet());
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
                startActivityForResult(item.getIntent(), FROM_SETTINGS);
                break;
            case R.id.help_menu_button:
                startActivityForResult(item.getIntent(), FROM_HELP);
                break;
            case R.id.sleep_info_button:
                startActivityForResult(item.getIntent(), FROM_SLEEP_INFO);
                break;
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (FROM_AUTHENTICATION) :
            case (FROM_SETTINGS) :
            case (FROM_HELP) :
            case (FROM_SLEEP_INFO) :
            default :
                handleActivityResult(resultCode);
                break;
        }
    }

    private void handleActivityResult(int resultCode) {
        switch(resultCode) {
            case (AuthActivity.RESULT_FAILED) :
                DialogUtils.createActivityClosingDialog(this, getString(R.string.login_or_authorisation_failed), getString(R.string.button_text_close));
                break;
            case (AuthActivity.RESULT_CANCELLED) :
                DialogUtils.createActivityClosingDialog(this, getString(R.string.must_connect_to_beddit_account), getString(R.string.button_text_close));
                break;
        }
    }

    //älä poista vielä
    public void testDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Spam api requests:");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes I will", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //BedditWebConnector blob = new BedditWebConnector();
                //try{
                AlarmChecker check = new AlarmCheckerRealImpl();
                check.wakeUpNow(MainActivity.this);
            }
        });
        AlertDialog alert = builder.create();
        alert.show();

    }

}
