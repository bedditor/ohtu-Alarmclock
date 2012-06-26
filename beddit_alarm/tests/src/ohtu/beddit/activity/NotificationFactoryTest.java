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

}
