package ohtu.beddit.music;

import android.os.Vibrator;
import android.util.Log;

/**
 * This class will ensure that the alarm will not ring for eternity. It will make a thread that sleeps for
 * a set time. After wake up, it will stop the music and vibration.
 */
public class ShowStopper implements Runnable {
    private final int minutes;
    private final MusicHandler musicHandler;
    private final Vibrator vibrator;

    /**
     *
     * @param min How many minutes the alarm should play and the phone vibrate.
     * @param music The {@link MusicHandler} to stop the music from.
     * @param vibrator The {@link Vibrator} to stop the vibration from.
     */
    public ShowStopper(int min, MusicHandler music, Vibrator vibrator) {
        this.minutes = min;
        this.musicHandler = music;
        this.vibrator = vibrator;
    }

    /**
     * Puts this thread to sleep for the time that is given to this class when created. After that the music is released.
     */
    @Override
    public void run() {
        try {
            Thread.sleep(1000 * 60 * this.minutes, 0);
            this.musicHandler.release();
            this.vibrator.cancel();
        } catch (InterruptedException i) {
            Log.v("ShowStopper", "thread was interrupted");
        }
    }
}
