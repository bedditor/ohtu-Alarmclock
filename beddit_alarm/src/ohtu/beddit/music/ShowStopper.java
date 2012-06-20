package ohtu.beddit.music;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.util.Log;
import ohtu.beddit.io.PreferenceService;

/**
 * Created with IntelliJ IDEA.
 * User: lagvare
 * Date: 19.6.2012
 * Time: 14:20
 * To change this template use File | Settings | File Templates.
 */
public class ShowStopper implements Runnable {
    private int minutes;
    private MusicHandler musicHandler;
    private Vibrator vibrator;

    public ShowStopper(int min, MusicHandler music, Vibrator vibra){
        minutes = min;
        this.musicHandler = music;
        vibrator = vibra;
    }

    @Override
    public void run(){
        try{
            Thread.sleep(1000*60*minutes, 0);
        }catch(InterruptedException i){
            Log.v("ShowStopperCrash", Log.getStackTraceString(i));
        }finally {
            musicHandler.release();
            vibrator.cancel();
        }
    }
}
