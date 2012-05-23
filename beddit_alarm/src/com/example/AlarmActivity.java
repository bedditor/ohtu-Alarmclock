package com.example;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TimePicker;

/**
 * Created with IntelliJ IDEA.
 * User: lagvare
 * Date: 22.5.2012
 * Time: 13:24
 * To change this template use File | Settings | File Templates.
 */
public class AlarmActivity extends Activity {


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm);

       /* ((Button)findViewById(R.id.setAlarmButton)).setOnClickListener(setListener);
        ((Button)findViewById(R.id.deleteAlarmButton)).setOnClickListener(deleteListener);*/
        ActivityDeleteButtonClickListener deleteListener = new ActivityDeleteButtonClickListener();
        ((Button)findViewById(R.id.deleteButton)).setOnClickListener(deleteListener);
        LinearLayout layout = (LinearLayout)findViewById(R.id.alarmLayout);
        layout.setBackgroundColor(Color.BLACK);
        layout.setBackgroundColor(Color.WHITE);


    }

    public class ActivityDeleteButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            AlarmActivity.this.finish();
        }
    }
    @Override
    public void finish(){
        super.finish();
    }





}
