package ohtu.beddit.activity;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.os.SystemClock;
import android.provider.Settings;
import android.test.AndroidTestCase;
import android.util.Log;
import junit.framework.Assert;
import ohtu.beddit.alarm.AlarmServiceImpl;
import ohtu.beddit.alarm.NotificationFactory;
import ohtu.beddit.io.FileHandler;

import java.util.Calendar;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created with IntelliJ IDEA.
 * User: tahuomo
 * Date: 13.6.2012
 * Time: 12:14
 * To change this template use File | Settings | File Templates.
 */
public class AlarmServiceTest extends AndroidTestCase {
    FileHandler mockFileHandler;
    AlarmManager mockAlarmManager;
    NotificationFactory mockNotifications;
    AlarmServiceImpl alarmservice;

    @Override
    public void setUp() throws Exception {
        mockNotifications = mock(NotificationFactory.class);
        mockFileHandler= mock(FileHandler.class);
        mockAlarmManager = mock(AlarmManager.class);
        when(mockFileHandler.getAlarm()).thenReturn(new int[4]);
        alarmservice = new AlarmServiceImpl(getContext(), mockAlarmManager, mockFileHandler, mockNotifications);
    }


    public void testAlarmNotSetWhenCreated() throws Exception{
        Assert.assertFalse(alarmservice.isAlarmSet());
    }

    public void testAlarmSetAfterAddAlarm() throws Exception{
        alarmservice.addAlarm(4,5,15);
        Assert.assertTrue(alarmservice.isAlarmSet());
    }

    public void testAddAlarm() throws Exception {
        Calendar c = Calendar.getInstance();
        alarmservice.addAlarm(c.get(Calendar.HOUR), c.get(Calendar.MINUTE), 15);
        verify(mockFileHandler).saveAlarm(c.get(Calendar.HOUR), c.get(Calendar.MINUTE),15, true);
        verify(mockNotifications).setNotification(anyInt(), anyInt(), anyInt(), anyInt());
        verify(mockAlarmManager).set(anyInt(), anyLong(),any(PendingIntent.class));
    }





 /*   //this method saves a new alarm with an interval
    @Override
    public void addAlarm(int hours, int minutes, int interval){
        fileHandler.saveAlarm(hours, minutes, interval, true);

        // Calculate first wake up try
        Calendar calendar = calculateFirstWakeUpAttempt(hours, minutes, interval);

        notfFactory.setNotification(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), hours, minutes);
        addWakeUpAttempt(calendar);
        alarmIsSet = true;
    }

    @Override
    public void changeAlarm(int hours, int minutes, int interval){
        if (alarmIsSet){
            addAlarm(hours, minutes, interval);
        }
    }

    //this method sets alarm manager to try wake up on given time
    public void addWakeUpAttempt(Calendar time){
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        String timeString = time.get(Calendar.HOUR_OF_DAY) + ":" + time.get(Calendar.MINUTE) + ":" + time.get(Calendar.SECOND);
        Log.v(TAG, "next wake up try set to "+timeString);
        alarmManager.set(AlarmManager.RTC_WAKEUP, time.getTimeInMillis(), sender);
    }

    //this method calculates time for the first try to wake up
    private Calendar calculateFirstWakeUpAttempt(int hour, int minute, int interval) {
        Calendar alarmTime = Calendar.getInstance();
        alarmTime.set(Calendar.HOUR_OF_DAY, hour);
        alarmTime.set(Calendar.MINUTE, minute);
        alarmTime.set(Calendar.SECOND, 0);
        alarmTime.set(Calendar.MILLISECOND, 0);
        if(alarmTime.before(Calendar.getInstance())){
            alarmTime.add(Calendar.DAY_OF_YEAR, 1);
        }

        alarmTime.add(Calendar.MINUTE, -interval);


        Calendar currentTime = Calendar.getInstance();
        if(alarmTime.after(currentTime)){
            return alarmTime;
        }
        else return currentTime;
    }

    @Override
    public void deleteAlarm(){
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);

        // Cancel the alarm!
        alarmManager.cancel(sender);
        fileHandler.disableAlarm();

        notfFactory.resetNotification();
        alarmIsSet = false;

    }

    public boolean isAlarmSet(){
        return alarmIsSet;
    }

    private boolean checkAlarmFromFile(){
        int [] alarms = getAlarm();
        if (alarms[0] < 1){
            return false;
        }
        return true;
    }

    private int[] getAlarm(){
        return fileHandler.getAlarm();
    }

    @Override
    public int getAlarmHours() {
        return getAlarm()[1];
    }

    @Override
    public int getAlarmMinutes() {
        return getAlarm()[2];
    }

    @Override
    public int getAlarmInterval() {
        return getAlarm()[3];
    }*/






}
