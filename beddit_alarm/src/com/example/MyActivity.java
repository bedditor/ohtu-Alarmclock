package com.example;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.view.ViewGroup;
import android.widget.TimePicker;
import android.widget.Toast;
import android.view.View.OnClickListener;

public class MyActivity extends Activity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        AlarmSetButtonClickListener l = new AlarmSetButtonClickListener();
        ((Button)findViewById(R.id.setAlarmButton)).setOnClickListener(l);

        TimePicker mTimePicker = (TimePicker)this.findViewById(R.id.alarmTimePicker);
        try {
            if (((ViewGroup) mTimePicker.getChildAt(0)).getChildCount() > 2)
                ((ViewGroup) mTimePicker.getChildAt(0)).getChildAt(2).setBackgroundResource(R.drawable.simplebutton);   // AM/PM button
            for (int j=0; j < mTimePicker.getChildCount(); j++)  {
                for (int i=0; i<2; i++) {
                    ((ViewGroup) ((ViewGroup) mTimePicker.getChildAt(0)).getChildAt(i)).getChildAt(0).setBackgroundResource(R.drawable.simplebutton);  // incr time
                    ((ViewGroup) ((ViewGroup) mTimePicker.getChildAt(0)).getChildAt(i)).getChildAt(1).setBackgroundResource(R.drawable.simplebutton);  // time button
                    ((ViewGroup) ((ViewGroup) mTimePicker.getChildAt(0)).getChildAt(i)).getChildAt(2).setBackgroundResource(R.drawable.simplebutton);  // decr time
                }
            }
        } catch (Throwable t) {
            Toast.makeText(this.getApplicationContext(), "error", 1000);
        }
    }

    public class AlarmSetButtonClickListener implements OnClickListener {

        @Override
        public void onClick(View view) {
            Toast.makeText(getApplicationContext(), "!!!!!", Toast.LENGTH_SHORT).show();
        }
    }
}
