package ohtu.beddit.music;

import android.os.Vibrator;
import android.util.Log;

/**
 * Created with IntelliJ IDEA.
 * User: lagvare
 * Date: 19.6.2012
 * Time: 14:20
 * To change this template use File | Settings | File Templates.
 */
public class ShowStopper implements Runnable {
    private final int minutes;
    private final MusicHandler musicHandler;
    private final Vibrator vibrator;

    public ShowStopper(int min, MusicHandler music, Vibrator vibrator) {
        this.minutes = min;
        this.musicHandler = music;
        this.vibrator = vibrator;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(1000 * 60 * this.minutes, 0);
        } catch (InterruptedException i) {
            Log.v("ShowStopperCrash", Log.getStackTraceString(i));
        } finally {
            this.musicHandler.release();
            this.vibrator.cancel();
        }
    }
}
