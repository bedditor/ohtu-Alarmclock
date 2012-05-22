package com.example;

import android.os.Bundle;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TimePicker;

/**
 * This is a simple framework for a test of an Application.  See
 * {@link android.test.ApplicationTestCase ApplicationTestCase} for more information on
 * how to write and extend Application tests.
 * <p/>
 * To run this test, you can type:
 * adb shell am instrument -w \
 * -e class com.example.MyActivityTest \
 * com.example.tests/android.test.InstrumentationTestRunner
 */
public class MyActivityTest extends ActivityInstrumentationTestCase2<MyActivity> {

    private MyActivity mActivity;
    private TimePicker mTimePicker;

    public MyActivityTest() {
        super("com.example", MyActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mActivity = this.getActivity();

        mTimePicker = (TimePicker)mActivity.findViewById(R.id.alarmTimePicker);
    }

    public void testTimePickerHoursAdd(){
        int hoursBefore = mTimePicker.getCurrentHour();
        incrementHours();
        int hoursAfter = mTimePicker.getCurrentHour();
        assertEquals(hoursBefore+1, hoursAfter);
    }

    private void incrementHours(){
        TouchUtils.clickView(this, ((ViewGroup)((ViewGroup)mTimePicker.getChildAt(0)).getChildAt(0)).getChildAt(0));
    }
}
