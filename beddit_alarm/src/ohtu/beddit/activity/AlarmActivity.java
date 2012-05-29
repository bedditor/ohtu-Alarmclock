package ohtu.beddit.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.*;
import ohtu.beddit.R;
import ohtu.beddit.alarm.AlarmService;
import ohtu.beddit.alarm.AlarmServiceImpl;
import ohtu.beddit.alarm.WakeUpLock;
import ohtu.beddit.io.PreferenceService;
import ohtu.beddit.music.MusicHandler;

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

    /** Called when the alarm is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm);
        Log.v(TAG, "Recieved alarm at " + Calendar.getInstance().getTime());

        Log.v(TAG, "I want WakeUpLock");
        WakeUpLock.acquire(this);

        makeNewAlarmServiceAndDeleteAlarm();
        makeButtons();
        vibratePhone();
        playMusic();
    }


    @Override
    public void finish(){
        Log.v(TAG, "Trying to Finish AlarmActivity");
        music.release();          // Alarm has rung and we have closed the dialog. Music is released.
        vibrator.cancel();        // Also no need to vibrate anymore.
        WakeUpLock.release();     // And no need to keep device open.
        super.finish();
    }

    @Override
    public void onPause(){ //We really don't want to go onPause. Rather forcibly keep the activity on top of everything.
                           //TODO: What about when user is on call?
        Log.v(TAG, "Trying to put AlarmActivity on pause");
        super.onPause();
    }

    @Override
    public void onStop(){ //We call this when we stop the activity.
        Log.v(TAG, "Trying to put AlarmActivity on stop");
        super.onStop();
        //WakeUpLock.release();
        //vibrator.cancel();
        super.finish();
    }

    public class SnoozeButtonClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view){
            //get snooze length from preferences
            int snoozeLength = Integer.parseInt(PreferenceService.getSettingString(AlarmActivity.this, R.string.pref_key_snooze));

            Calendar snoozeTime = Calendar.getInstance();
            snoozeTime.add(Calendar.MINUTE, snoozeLength);

            //set alarm
            AlarmService alarmService = new AlarmServiceImpl(AlarmActivity.this.getApplicationContext());
            alarmService.addAlarm(AlarmActivity.this, snoozeTime.get(Calendar.HOUR_OF_DAY), snoozeTime.get(Calendar.MINUTE), 0);

            AlarmActivity.this.finish();
        }
    }

    public class ActivityDeleteButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            AlarmActivity.this.finish();
        }
    }

    private void playMusic() {
        music = new MusicHandler();
        music.setMusic(this);
        music.setLooping(true);
        music.play(true);
    }

    private void vibratePhone() {
        Log.v(TAG, "I want to Vibrate 8==D");
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = { 0, 200, 500 };
        vibrator.vibrate(pattern, 0);
        Log.v(TAG, "Vibrator says:" + vibrator.toString());
    }

    private void makeButtons() {
        ActivityDeleteButtonClickListener deleteListener = new ActivityDeleteButtonClickListener();
        SnoozeButtonClickListener snoozeListener = new SnoozeButtonClickListener();
        ((Button)findViewById(R.id.deleteButton)).setOnClickListener(deleteListener);
        ((Button)findViewById(R.id.snoozeButton)).setOnClickListener(snoozeListener);
        LinearLayout layout = (LinearLayout)findViewById(R.id.alarmLayout);
        layout.setBackgroundColor(Color.WHITE);
    }

    private void makeNewAlarmServiceAndDeleteAlarm() {
        new AlarmServiceImpl(this).deleteAlarm(this);
    }
}
