package ohtu.beddit.alarm;

import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.content.Context;
import android.os.PowerManager;
import android.util.Log;

/**
 * Created with IntelliJ IDEA.
 * User: juho
 * Date: 28.5.2012
 * Time: 13:53
 * To change this template use File | Settings | File Templates.
 */
public class WakeUpLock {
    private static PowerManager.WakeLock sWakeLock;
    private static final String TAG = "WakeUpLock";
    private static KeyguardLock keyguardLock;

    public static void acquire(Context context) {
        Log.v(TAG, "Acquiring wake lock");
        if (sWakeLock != null) {
            sWakeLock.release();
        }
        KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        keyguardLock = keyguardManager.newKeyguardLock("TAG");
        keyguardLock.disableKeyguard();

        PowerManager pm =
                (PowerManager) context.getSystemService(Context.POWER_SERVICE);

        sWakeLock = pm.newWakeLock(
                PowerManager.SCREEN_BRIGHT_WAKE_LOCK |
                        PowerManager.FULL_WAKE_LOCK |
                        PowerManager.ACQUIRE_CAUSES_WAKEUP |
                        PowerManager.ON_AFTER_RELEASE, TAG);
        sWakeLock.acquire();
    }

    public static void release() {
        Log.v(TAG, "Releasing wake lock");
        if (sWakeLock != null) {
            sWakeLock.release();
            sWakeLock = null;
        }
        keyguardLock.reenableKeyguard();
    }
}
