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
     * Called when the alarm is first created.
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


    @Override
    public void finish() {
        Log.v(TAG, "finish");
        showStopperThread.interrupt();
        music.release();          // Alarm has rung and we have closed the dialog. Music is released.
        vibrator.cancel();        // Also no need to vibrate anymore.
        WakeUpLock.release();     // And no need to keep device open.
        super.finish();
    }

    @Override
    public void onPause() { //We really don't want to go onPause. Rather forcibly keep the activity on top of everything.
        Log.v(TAG, "onPause");
        super.onPause();
    }

    @Override
    public void onStop() { //We call this when we stop the activity.
        Log.v(TAG, "onStop");
        if (!wasDismissed)
            snooze();
        super.onStop();
        //finish();
    }

    @Override
    protected void onDestroy() {
        Log.v(TAG, "onDestroy");
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        // do not respond to back button in AlarmActivity
    }

    private void playMusic() {
        music = new MusicHandler();
        music.setMusic(this);
        music.play(true);
        ShowStopper stopper = new ShowStopper(PreferenceService.getAlarmLength(this), music, vibrator);
        showStopperThread = new Thread(stopper);
        showStopperThread.start();
    }

    private void vibratePhone() {
        Log.v(TAG, "I want to Vibrate 8==D");

        // Check that the phone wont vibrate if the user is on the phone
        if (((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE)).getCallState() == TelephonyManager.CALL_STATE_IDLE){
            vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(vibratePattern, 0);
            Log.v(TAG, "Vibrator says:" + vibrator.toString());
        }
    }

    private void dismiss() {
        wasDismissed = true;
        if (PreferenceService.getShowSleepData(AlarmActivity.this)) {
            Intent myIntent = new Intent(AlarmActivity.this, SleepInfoActivity.class);
            AlarmActivity.this.startActivity(myIntent);
        }
        AlarmActivity.this.finish();
    }

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

    private void removeAlarm() {
        new AlarmServiceImpl(this).deleteAlarm();
    }
}
