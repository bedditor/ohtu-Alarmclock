package com.example;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Button;
import android.widget.TimePicker;
import android.view.View.OnClickListener;

public class MyActivity extends Activity
{

    private AlarmScheduler alarmScheduler;
    AlarmTimePicker alarmTimePicker;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setAlarmScheduler(new AlarmSchedulerImpl(this));



        AlarmSetButtonClickListener setListener = new AlarmSetButtonClickListener();
        AlarmDeleteButtonClickListener deleteListener = new AlarmDeleteButtonClickListener();
        ((Button)findViewById(R.id.setAlarmButton)).setOnClickListener(setListener);
        ((Button)findViewById(R.id.deleteAlarmButton)).setOnClickListener(deleteListener);
        LinearLayout layout = (LinearLayout)findViewById(R.id.mainLayout);
        layout.setBackgroundColor(Color.WHITE);

        alarmTimePicker = (CustomTimePicker)this.findViewById(R.id.alarmTimePicker);

        int[] results = FileHandler.getAlarms(this.getApplicationContext());
        CustomTimePicker clock = (CustomTimePicker)alarmTimePicker;
        if(results[0] == -1){
            //muokkaa näppäimiä
        }else{
            clock.setHours(results[0]);
            clock.setMinutes(results[1]);
            clock.setInterval(results[2]);
        }
    }

    public void setAlarmScheduler(AlarmScheduler alarmScheduler) {
        this.alarmScheduler = alarmScheduler;

    }

    public AlarmScheduler getAlarmScheduler() {
        return alarmScheduler;
    }

    public class AlarmSetButtonClickListener implements OnClickListener {

        @Override
        public void onClick(View view) {
            alarmScheduler.addAlarm(MyActivity.this, alarmTimePicker.getHours(), alarmTimePicker.getMinutes(), alarmTimePicker.getInterval());
            FileHandler.saveAlarm(alarmTimePicker.getHours(), alarmTimePicker.getMinutes(),alarmTimePicker.getInterval(),MyActivity.this.getApplicationContext());
        }
    }

    public class AlarmDeleteButtonClickListener implements OnClickListener {
        @Override
        public void onClick(View view) {
            alarmScheduler.deleteAlarm(MyActivity.this);
        }
    }


}
