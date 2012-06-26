package ohtu.beddit.alarm;

import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.content.Context;
import android.os.PowerManager;
import android.util.Log;

/**
 * Static class which will ensure that we will be on top and out of lock to display current information.
 * Mainly used to display the Alarm dialog with the dismiss and snooze buttons.
 */
public class WakeUpLock {
    private static PowerManager.WakeLock sWakeLock;
    private static final String TAG = "WakeUpLock";
    private static KeyguardLock keyguardLock;

    /**
     * Here we acquire the wakeUpLock. Unlock the keyguard for our object, Wake up the device and light up the screen.
     * Designed as it was in android 1.5 alarm clock.
     * @param context
     */
    public static void acquire(Context context) {
        Log.v(TAG, "Acquiring wake lock");
        if (sWakeLock != null) {
            sWakeLock.release();
        }
        KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        keyguardLock = keyguardManager.newKeyguardLock("TAG");
        keyguardLock.disableKeyguard();

        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);

        sWakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK |
                                   PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, TAG);
        sWakeLock.acquire();
    }

    /**
     * Release this after you have done the work needed. Enables device lock and releases WakeLock. So if screen was
     * locked and black before acquiring then after releasing we will return to that state.
     */
    public static void release() {
        Log.v(TAG, "Releasing wake lock");
        if (sWakeLock != null) {
            sWakeLock.release();
            sWakeLock = null;
        }
        keyguardLock.reenableKeyguard();
    }
}
