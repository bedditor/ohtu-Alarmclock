package ohtu.beddit.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.test.AndroidTestCase;
import ohtu.beddit.alarm.NotificationFactory;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

/**
 * Created with IntelliJ IDEA.
 * User: tahuomo
 * Date: 12.6.2012
 * Time: 12:24
 * To change this template use File | Settings | File Templates.
 */
public class NotificationFactoryTest extends AndroidTestCase {
    NotificationFactory ntf;
    NotificationManager mockManager;

    @Override
    public void setUp() throws Exception {
        mockManager = mock(NotificationManager.class);
        ntf = new NotificationFactory(getContext(), mockManager);
    }


    public void testSetNotification() throws Exception {
        ntf.setNotification(2, 4, 2, 4);
        verify(mockManager).notify(eq(1), any(Notification.class));
    }


    public void testResetNotification() throws Exception {
        ntf.resetNotification();
        verify(mockManager).cancel(1);

    }

    public void testSetDoesNotCallReset() throws Exception {
        ntf.setNotification(2, 4, 2, 4);
        verify(mockManager).notify(eq(1), any(Notification.class));
        verify(mockManager, never()).cancel(1);

    }

    /*  public void testTimeAsStringAMPM() throws Exception {
    android.provider.Settings.System.putInt(getContext().getContentResolver(),
            Settings.System.TIME_12_24,24);
    Assert.assertEquals("15:50", ntf.timeAsString(15,50));

}

public void testTimeAsString() throws Exception {
    android.provider.Settings.System.putInt(getContext().getContentResolver(),
            Settings.System.TIME_12_24,12);

    *//*Resources res = getContext().getResources();
        // Change locale settings in the app.
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.locale = new Locale("en");
        res.updateConfiguration(conf, dm);*//*

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) ;
        intent.setClassName("com.android.settings", "com.android.settings.LanguageSettings");
        getContext().startActivity(intent);

        Thread.sleep(100000);

        Assert.assertEquals("3:50 PM", ntf.timeAsString(15,50));
    }*/


}
