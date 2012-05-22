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
        assertEquals((hoursBefore)%24+1, hoursAfter);
    }

    private void incrementHours(){
        TouchUtils.clickView(this, ((ViewGroup)((ViewGroup)mTimePicker.getChildAt(0)).getChildAt(0)).getChildAt(0));
    }

    public void testSetAlarm(){
        AlarmScheduler alarmScheduler = new AlarmSchedulerMock();
        mActivity.setAlarmScheduler(alarmScheduler);
        incrementHours();
        TouchUtils.clickView(this, mActivity.findViewById(R.id.setAlarmButton));
        assertEquals("Did not add alarm", 1, ((AlarmSchedulerMock) alarmScheduler).getAdds());
        assertEquals("Wrong hours", (int)mTimePicker.getCurrentHour(), ((AlarmSchedulerMock)alarmScheduler).getHours());
        assertEquals("Wrong minutes", (int)mTimePicker.getCurrentMinute(), ((AlarmSchedulerMock)alarmScheduler).getMinutes());
        assertEquals("Wrong interval (hard coded to 0)", 0, ((AlarmSchedulerMock)alarmScheduler).getInterval());
    }

    public void testDeleteAlarm(){
        AlarmScheduler alarmScheduler = new AlarmSchedulerMock();
        mActivity.setAlarmScheduler(alarmScheduler);
        TouchUtils.clickView(this, mActivity.findViewById(R.id.deleteAlarmButton));
        assertEquals("Did not delete alarm", 1, ((AlarmSchedulerMock) alarmScheduler).getDeletes());
    }

    public void testAlarmManagerSet(){
        AlarmManagerMock alarmManager = new AlarmManagerMock();
        mActivity.getAlarmScheduler().setAlarmManager(alarmManager);

        TouchUtils.clickView(this, mActivity.findViewById(R.id.setAlarmButton));
        assertEquals("Did not add alarm to manager", 1, alarmManager.getSets());
    }

    public void testAlarmManagerCancel(){
        AlarmManagerMock alarmManager = new AlarmManagerMock();
        mActivity.getAlarmScheduler().setAlarmManager(alarmManager);

        incrementHours();
        TouchUtils.clickView(this, mActivity.findViewById(R.id.setAlarmButton));
        incrementHours();
        TouchUtils.clickView(this, mActivity.findViewById(R.id.deleteAlarmButton));

        assertEquals("Did not add alarm to manager", 1, alarmManager.getCancels());
        assertTrue("Operations don't match", alarmManager.getOperationSet().equals(alarmManager.getOperationCancel()));
    }


}