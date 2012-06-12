package ohtu.beddit.music;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import ohtu.beddit.R;

/**
 * Created with IntelliJ IDEA.
 * User: juho
 * Date: 22.5.2012
 * Time: 10:44
 * To change this template use File | Settings | File Templates.
 */
public class MusicHandler {

    private final String TAG = "MusicHandler";
    private MediaPlayer player;
    private boolean released;


    public MusicHandler() {
        player = null;
        released = true;
    }

    /*
    Needs the Context of the Activity to create mediaplayer for the spesific Activity.
     */
    public void setMusic(Context context) {
        //Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        player = MediaPlayer.create(context, R.raw.alarm);
        released = false;
        player.setVolume(1f, 1f);

        Log.v(TAG, "Initialized MusicPlayer and set music infernally high");
    }

    public void setLooping(boolean loop) {
        player.setLooping(loop);
    }
    /*
   Returns true if everythings ok.
    */
    //TODO: Make this more sane (do actual check if the music we are playing can be found (memorycard) and basic null check)
    public boolean insanityCheck() {
        if (released)
            return false;
        return true;
    }

    //Can be called regardless we have valid music, It just won't do anything.
    public void play(boolean force) {
        boolean success = false;
        if (insanityCheck())
            if (force) {
                if (player.isPlaying()) {
                    player.stop();
                }
                player.start();
                success = true;
            } else if (!player.isPlaying()) {
                player.start();
                success = true;
            }
        if (success)
            Log.v(TAG, "Started playing alarm music!");
        else
            Log.v(TAG, "Did not start playing music. Maybe you haven't initialized player.");
    }

    //Can be called regardless we have valid music, It just won't do anything.
    public void stop() {
        if (insanityCheck())
            if (player.isPlaying()) {
                player.stop();
                Log.v(TAG, "Stopped playing music.");
                return;
            }
        Log.v(TAG, "Something failed when tried to do stop music from playing. Maybe player was released already?");
    }

    public void pause() {
        if (insanityCheck())
            if (player.isPlaying()) {
                player.pause();
                Log.v(TAG, "Paused the music.");
            }
    }

    public void release() {
        if (released) {
            Log.v(TAG, "Tried to release player when it was already released");
            return;
        }try {
            if (player != null) {
                if (player.isPlaying()) {
                    player.stop();
                }
                player.release();
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
        Log.v(TAG, "Released the music.");
        released = true;
    }
}
