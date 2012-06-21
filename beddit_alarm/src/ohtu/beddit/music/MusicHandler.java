package ohtu.beddit.music;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.telephony.TelephonyManager;
import android.util.Log;
import ohtu.beddit.R;
import ohtu.beddit.io.PreferenceService;

/**
 * Created with IntelliJ IDEA.
 * User: juho
 * Date: 22.5.2012
 * Time: 10:44
 * To change this template use File | Settings | File Templates.
 */
public class MusicHandler {

    private final String TAG = "MusicHandler";
    private final MediaPlayer player;
    private boolean released;

    private static final float VOLUME_OFF = 0.0f;
    private static final float RINGING_VOLUME = 0.125f;


    public MusicHandler() {
        player = new MediaPlayer();
        released = true;
    }

    /*
    Needs the Context of the Activity to create mediaPlayer for the specific Activity.
     */
    public void setMusic(Context context) {
        AssetFileDescriptor alarmTone;
        if (PreferenceService.getAwesome(context)) {
            alarmTone = context.getResources().openRawResourceFd(R.raw.awesome);
        } else {
            alarmTone = context.getResources().openRawResourceFd(R.raw.alarm);
        }

        try {
            player.reset();
            player.setAudioStreamType(AudioManager.STREAM_ALARM);
            player.setLooping(true);
            player.setDataSource(alarmTone.getFileDescriptor(), alarmTone.getStartOffset(), alarmTone.getLength());
            setReasonableVolume(context);
            player.prepare();
            released = false;
            Log.v(TAG, "Initialized MusicPlayer and set music infernally high");
        } catch (Exception e) {
            Log.v(TAG, "something crashed");
        }
    }

    /*
   Returns true if everything is ok.
    */
    //TODO: Make this more sane (do actual check if the music we are playing can be found (memory card) and basic null check)
    boolean insanityCheck() {
        return !released;
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
        if (success) {
            Log.v(TAG, "Started playing alarm music!");
        } else {
            Log.v(TAG, "Did not start playing music. Maybe you haven't initialized player.");
        }

    }

    public void release() {
        if (released) {
            Log.v(TAG, "Tried to release player when it was already released");
            return;
        }
        try {
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

    //Check if customer is on the phone or the phone is ringing
    private void setReasonableVolume(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        int callState = tm.getCallState();
        if (callState == TelephonyManager.CALL_STATE_OFFHOOK) {
            Log.v(TAG, "Customer on the phone, let's change volume");
            player.setVolume(VOLUME_OFF, VOLUME_OFF);
        } else if (callState == TelephonyManager.CALL_STATE_RINGING) {
            Log.v(TAG, "Someone is calling! Let's decrease volume.");
            player.setVolume(RINGING_VOLUME, RINGING_VOLUME);
        } else {
            player.setVolume(1f, 1f);
        }
    }

}
