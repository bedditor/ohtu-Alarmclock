package com.example;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
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

    public static Context usedcontext = null;

    public TextView teksti = null;

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
        RelativeLayout layout = (RelativeLayout)findViewById(R.id.alarmLayout);
        layout.setBackgroundColor(Color.BLACK);
        layout.setBackgroundColor(Color.WHITE);

        //I/O -tests:
        teksti = ((TextView)findViewById(R.id.testText));
        WriteButtonClickListener writeListener = new WriteButtonClickListener();
        ReadButtonClickListener readListener = new ReadButtonClickListener();
        ((Button)findViewById(R.id.writeButton)).setOnClickListener(writeListener);
        ((Button)findViewById(R.id.readButton)).setOnClickListener(readListener);



        MusicHandler alarm = new MusicHandler(usedcontext);
        alarm.setMusic();
        Log.v("Alarm", "Recieved alarm at " + Calendar.getInstance().getTime());
        //Toast.makeText(usedcontext, "Herätys yksinkertainen", Toast.LENGTH_SHORT).show();
        if (alarm.insanityCheck()) {
            alarm.play();
            Utils.sleep(10);
            alarm.stop();
        }else{
            Log.v("Soitto", "Imaginääri musiikki soi ");
        }
        Log.v("Alarm", "Alarm ended at " + Calendar.getInstance().getTime());
    }

    @Override
    public void finish(){
        super.finish();
    }

    @Override
    public void onPause(){
        super.onPause();
        super.finish();
    }

    @Override
    public void onStop(){
        super.onStop();
        super.finish();
    }

    public class SnoozeButtonClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view){
            AlarmScheduler scheduler = new AlarmSchedulerImpl(AlarmActivity.this.getApplicationContext());
            scheduler.addAlarm(AlarmActivity.this, 0, 5, 0);
            AlarmActivity.this.finish();
        }

    }

    public class ActivityDeleteButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            AlarmActivity.this.finish();
        }
    }

    private class WriteButtonClickListener implements View.OnClickListener {
        public void onClick(View view) {
            if(FileHandler.writeToFile("tiedosto","testing", AlarmActivity.this.getApplicationContext())){
                teksti.setText("tallennettu");
            }else{
                teksti.setText("ei tallennettu D:");
            }
        }
    }

    private class ReadButtonClickListener implements View.OnClickListener {
        public void onClick(View view) {
            teksti.setText(FileHandler.getAlarms(AlarmActivity.this.getApplicationContext()));
        }
    }





}
