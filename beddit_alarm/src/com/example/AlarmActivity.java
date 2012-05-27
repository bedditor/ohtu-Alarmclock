package com.example;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import java.util.Calendar;

/**
 * Created with IntelliJ IDEA.
 * User: lagvare
 * Date: 22.5.2012
 * Time: 13:24
 * To change this template use File | Settings | File Templates.
 */
public class AlarmActivity extends Activity {

    public MusicHandler music = null;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm);

       /* ((Button)findViewById(R.id.setAlarmButton)).setOnClickListener(setListener);
        ((Button)findViewById(R.id.deleteAlarmButton)).setOnClickListener(deleteListener);*/
        ActivityDeleteButtonClickListener deleteListener = new ActivityDeleteButtonClickListener();
        SnoozeButtonClickListener snoozeListener = new SnoozeButtonClickListener();
        ((Button)findViewById(R.id.deleteButton)).setOnClickListener(deleteListener);
        ((Button)findViewById(R.id.snoozeButton)).setOnClickListener(snoozeListener);
        LinearLayout layout = (LinearLayout)findViewById(R.id.alarmLayout);
        layout.setBackgroundColor(Color.WHITE);

        Context context = AlarmActivity.this.getApplicationContext();
        AlarmScheduler alarmScheduler = new AlarmSchedulerImpl(context);
        alarmScheduler.deleteAlarm(context);

        Log.v("Alarm", "Recieved alarm at " + Calendar.getInstance().getTime());
        music = new MusicHandler();
        music.setMusic(this);
        music.setLooping(true);
        music.play(true);
        Log.v("Alarm", "Alarm ended at " + Calendar.getInstance().getTime());
    }


    @Override
    public void finish(){
        music.release();
        super.finish();
    }

    @Override
    public void onPause(){
        super.onPause();
        //music.pause();
        //super.finish();
    }

    @Override
    public void onStop(){
        super.onStop();
        music.stop();
        super.finish();
    }

    public class SnoozeButtonClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view){
            //get snooze length from preferences
            int snoozeLength = Integer.parseInt(SharedSettings.getSettingString(AlarmActivity.this, R.string.pref_key_snooze));

            Calendar snoozeTime = Calendar.getInstance();
            snoozeTime.add(Calendar.MINUTE, snoozeLength);

            //set alarm
            AlarmScheduler alarmScheduler = new AlarmSchedulerImpl(AlarmActivity.this.getApplicationContext());
            alarmScheduler.addAlarm(AlarmActivity.this, snoozeTime.get(Calendar.HOUR_OF_DAY), snoozeTime.get(Calendar.MINUTE), 0);

            AlarmActivity.this.finish();
        }

    }

    public class ActivityDeleteButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            AlarmActivity.this.finish();
        }
    }

}
