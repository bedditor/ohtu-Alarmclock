package com.example;

import android.view.ViewGroup;
import android.widget.TimePicker;
import android.widget.Toast;

public class AlarmTimePickerSimpleImpl implements AlarmTimePicker {
    private TimePicker timePicker;
    private boolean enabled;

    public AlarmTimePickerSimpleImpl(TimePicker timePicker) {
        this.timePicker = timePicker;
        enabled = true;

        try {
            if (((ViewGroup) timePicker.getChildAt(0)).getChildCount() > 2)
                ((ViewGroup) timePicker.getChildAt(0)).getChildAt(2).setBackgroundResource(R.drawable.simplebutton);   // AM/PM button
            for (int j=0; j < timePicker.getChildCount(); j++)  {
                for (int i=0; i<2; i++) {
                    ((ViewGroup) ((ViewGroup) timePicker.getChildAt(0)).getChildAt(i)).getChildAt(0).setBackgroundResource(R.drawable.uparrow);  // incr time
                    ((ViewGroup) ((ViewGroup) timePicker.getChildAt(0)).getChildAt(i)).getChildAt(1).setBackgroundResource(R.drawable.simplebutton);  // time button
                    ((ViewGroup) ((ViewGroup) timePicker.getChildAt(0)).getChildAt(i)).getChildAt(2).setBackgroundResource(R.drawable.downarrow);  // decr time
                }
            }
        } catch (Throwable t) {
            //Toast.makeText(this.getApplicationContext(), "error", 1000);
        }
    }

    @Override
    public int getHours() {
        return timePicker.getCurrentHour();
    }

    @Override
    public int getMinutes() {
        return timePicker.getCurrentMinute();
    }

    @Override
    public int getInterval() {
        return 10;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }


}
