package ohtu.beddit;

import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import ohtu.beddit.activity.MainActivity;
import ohtu.beddit.alarm.AlarmService;
import ohtu.beddit.views.CustomTimePicker;

public class MyActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private MainActivity mActivity;
    private CustomTimePicker timePicker;

    public MyActivityTest() {
        super("com.example", MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mActivity = this.getActivity();
        timePicker = (CustomTimePicker)mActivity.findViewById(R.id.alarmTimePicker);
        TouchUtils.clickView(this, mActivity.findViewById(R.id.deleteAlarmButton));

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
        AlarmService AlarmService = new AlarmServiceMock();
        mActivity.setAlarmService(AlarmService);
        incrementHours();
        TouchUtils.clickView(this, mActivity.findViewById(R.id.setAlarmButton));
        assertEquals("Did not add alarm", 1, ((AlarmServiceMock) AlarmService).getAdds());
        assertEquals("Wrong hours", timePicker.getHours(), ((AlarmServiceMock)AlarmService).getHours());
        assertEquals("Wrong minutes", timePicker.getMinutes(), ((AlarmServiceMock)AlarmService).getMinutes());
    }

    public void testDeleteAlarm(){
        AlarmService AlarmService = new AlarmServiceMock();
        mActivity.setAlarmService(AlarmService);
        TouchUtils.clickView(this, mActivity.findViewById(R.id.setAlarmButton));
        TouchUtils.clickView(this, mActivity.findViewById(R.id.deleteAlarmButton));
        assertEquals("Did not delete alarm", 1, ((AlarmServiceMock) AlarmService).getDeletes());
    }

    public void testAlarmManagerSet(){
        AlarmManagerMock alarmManager = new AlarmManagerMock();
        mActivity.getAlarmService().setAlarmManager(alarmManager);

        TouchUtils.clickView(this, mActivity.findViewById(R.id.setAlarmButton));
        assertEquals("Did not add alarm to manager", 1, alarmManager.getSets());
    }

    public void testAlarmManagerCancel(){
        AlarmManagerMock alarmManager = new AlarmManagerMock();
        mActivity.getAlarmService().setAlarmManager(alarmManager);

        incrementHours();
        TouchUtils.clickView(this, mActivity.findViewById(R.id.setAlarmButton));
        incrementHours();
        TouchUtils.clickView(this, mActivity.findViewById(R.id.deleteAlarmButton));

        assertEquals("Did not add alarm to manager", 1, alarmManager.getCancels());
        assertTrue("Operations don't match", alarmManager.getOperationSet().equals(alarmManager.getOperationCancel()));
    }


}