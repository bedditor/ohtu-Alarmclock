package ohtu.beddit.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
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
 * Created with IntelliJ IDEA.
 * User: lagvare
 * Date: 22.5.2012
 * Time: 13:24
 * To change this template use File | Settings | File Templates.
 */
public class AlarmActivity extends Activity {

    private final String TAG = "AlarmActivity";
    private MusicHandler music = null;
    private Vibrator vibrator;
    private Thread showStopperThread;

    private static final float DIALOG_HEIGHT = 0.7f;
    private static final float DIALOG_WIDTH = 0.9f;

    /**
     * Called when the alarm is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm);

        Display display = getWindowManager().getDefaultDisplay();

        getWindow().setLayout((int) (display.getWidth() * DIALOG_WIDTH),
                (int) (display.getHeight() * DIALOG_HEIGHT));

        Log.v(TAG, "Received alarm at " + Calendar.getInstance().getTime());

        WakeUpLock.acquire(this);
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
        //TODO: What about when user is on call?
        Log.v(TAG, "onPause");

        finish(); // NEVER leave AlarmActivity ringing in the background, kill on any event that tries to suppress it

        super.onPause();
    }

    @Override
    public void onStop() { //We call this when we stop the activity.
        Log.v(TAG, "onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.v(TAG, "onDestroy");
        super.onDestroy();    //To change body of overridden methods use File | Settings | File Templates.
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
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {0, 200, 500};
        vibrator.vibrate(pattern, 0);
        Log.v(TAG, "Vibrator says:" + vibrator.toString());
    }

    private void dismiss() {
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
