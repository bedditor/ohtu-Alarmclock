package com.example;

import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;


public class MyActivityTest extends ActivityInstrumentationTestCase2<MyActivity> {

    private MyActivity mActivity;
    private CustomTimePicker timePicker;

    public MyActivityTest() {
        super("com.example", MyActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mActivity = this.getActivity();
        timePicker = (CustomTimePicker)mActivity.findViewById(R.id.alarmTimePicker);
    }

    public void testTimePickerHoursAdd(){
        timePicker.setHours(21);
        timePicker.setMinutes(30);
        int hoursBefore = timePicker.getHours();
        incrementHours();
        int hoursAfter = timePicker.getHours();
        assertEquals((hoursBefore)%24+1, hoursAfter);
    }

    private void incrementHours(){
        timePicker.setHours(timePicker.getHours()+1);
    }

    public void testSetAlarm(){
        AlarmScheduler alarmScheduler = new AlarmSchedulerMock();
        mActivity.setAlarmScheduler(alarmScheduler);
        incrementHours();
        TouchUtils.clickView(this, mActivity.findViewById(R.id.setAlarmButton));
        assertEquals("Did not add alarm", 1, ((AlarmSchedulerMock) alarmScheduler).getAdds());
        assertEquals("Wrong hours", timePicker.getHours(), ((AlarmSchedulerMock)alarmScheduler).getHours());
        assertEquals("Wrong minutes", timePicker.getMinutes(), ((AlarmSchedulerMock)alarmScheduler).getMinutes());
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