package ohtu.beddit.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Display;
import android.view.View;
import ohtu.beddit.R;
import ohtu.beddit.alarm.AlarmService;
import ohtu.beddit.alarm.AlarmServiceImpl;
import ohtu.beddit.alarm.WakeUpLock;
import ohtu.beddit.io.PreferenceService;
import ohtu.beddit.music.MusicHandler;
import ohtu.beddit.music.ShowStopper;
import java.util.Calendar;

/**
 * AlarmActivity is the actual presentation of an alarm, it plays alarm sound and vibrates.
 */
public class AlarmActivity extends Activity {

    private final String TAG = "AlarmActivity";
    private MusicHandler music = null;
    private Vibrator vibrator;
    private Thread showStopperThread;

    //The alarm will always snoozed if the activity is closed by any other means than pressing dismiss.
    private boolean wasDismissed;

    private static final float DIALOG_HEIGHT = 0.7f;
    private static final float DIALOG_WIDTH = 0.9f;
    private static final long[] vibratePattern = {0, 200, 500};

    /**
     * Initializes the alarm, plays the music, vibrates the phone, acquires the WakeUpLock.
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WakeUpLock.acquire(this);
        setContentView(R.layout.alarm);
        Display display = getWindowManager().getDefaultDisplay();
        getWindow().setLayout((int) (display.getWidth() * DIALOG_WIDTH),
                (int) (display.getHeight() * DIALOG_HEIGHT));

        Log.v(TAG, "Received alarm at " + Calendar.getInstance().getTime());

        wasDismissed = false;
        removeAlarm();
        makeButtons();
        vibratePhone();
        playMusic();
    }

    /**
     * Finishes the activity, also closes the various things started by onCreate.
     */
    @Override
    public void finish() {
        Log.v(TAG, "finish");
        showStopperThread.interrupt();
        music.release();          // Alarm has rung and we have closed the dialog. Music is released.
        vibrator.cancel();        // Also no need to vibrate anymore.
        WakeUpLock.release();
        super.finish();
    }

    @Override
    public void onPause() {
        Log.v(TAG, "onPause");
        super.onPause();
    }

    /**
     * Puts the alarm on snooze in case this is called, unless the activity was dismissed.
     */
    @Override
    public void onStop() { //We call this when we stop the activity.
        Log.v(TAG, "onStop");
        if (!wasDismissed) {
            snooze();
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.v(TAG, "onDestroy");
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        // Why no onStop here?
        // onStop();
    }

    /**
     * Creates new MusicHandler and starts playing, Also creates ShowStopper.
     */
    private void playMusic() {
        music = new MusicHandler();
        music.setMusic(this);
        music.play(true);
        ShowStopper stopper = new ShowStopper(PreferenceService.getAlarmLength(this), music, vibrator);
        showStopperThread = new Thread(stopper);
        showStopperThread.start();
    }

    /**
     * Vibrates the phone unless user is on call.
     */
    private void vibratePhone() {
        Log.v(TAG, "I want to Vibrate 8==D");

        // Check that the phone wont vibrate if the user is on the phone
        if (((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE)).getCallState() ==
                TelephonyManager.CALL_STATE_IDLE){
            vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(vibratePattern, 0);
            Log.v(TAG, "Vibrator says:" + vibrator.toString());
        }
    }

    /**
     * User pressed the dismiss button. We call finish and set the result intent.
     */
    private void dismiss() {
        wasDismissed = true;
        if (PreferenceService.getShowSleepData(this)) {
            Intent myIntent = new Intent(this, SleepInfoActivity.class);
            myIntent.putExtra("showFeelings", true);
            startActivity(myIntent);
        }
        finish();
    }

    /**
     * Called when we press snooze button or do something else. Sets new alarm few minutes later based on the settings.
     */
    private void snooze() {
        //get snooze length from preferences
        int snoozeLength = PreferenceService.getSnoozeLength(AlarmActivity.this);

        Calendar snoozeTime = Calendar.getInstance();
        snoozeTime.add(Calendar.MINUTE, snoozeLength);

        //set alarm
        Context context = AlarmActivity.this;
        AlarmService alarmService = new AlarmServiceImpl(context);
        alarmService.addAlarm(snoozeTime.get(Calendar.HOUR_OF_DAY), snoozeTime.get(Calendar.MINUTE), 0);

        finish();
    }

    /**
     * Adds onClick listeners to buttons.
     */
    private void makeButtons() {
        findViewById(R.id.alarmActivity_button_dismiss)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlarmActivity.this.dismiss();
                    }
                });
        findViewById(R.id.alarmActivity_button_snooze)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlarmActivity.this.snooze();
                    }
                });

    }

    /**
     * removes Alarm from alarm service.
     */
    private void removeAlarm() {
        new AlarmServiceImpl(this).deleteAlarm();
    }
}
