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
 * This class will take care of playing the alarm tone and releasing media player after use. The class also has method
 * for setting correct audio levels for certain situations.
 */
public class MusicHandler {

    private final String TAG = "MusicHandler";
    private final MediaPlayer player;
    private boolean released;

    private static final float VOLUME_OFF = 0.0f;
    private static final float RINGING_VOLUME = 0.125f;

    /**
     * Initializes the MusicHandler.
     */
    public MusicHandler() {
        player = new MediaPlayer();
        released = true;
    }

    /**
     * Needs the Context of the Activity to create mediaPlayer for the specific Activity.
     */
    public void setMusic(Context context) {
        AssetFileDescriptor alarmTone;
        alarmTone = context.getResources().openRawResourceFd(R.raw.alarm);

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

    /**
     * Returns true if the player isn't released.
    */
    boolean insanityCheck() {
        return !released;
    }

    /**
     * Will try to play the music if the music isn't playing. Log's information if isn't success.
     * @param force True will force play the music if we have initialized the player.
     */
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

    /**
     * Releases the player to the best of it's capabilities. Look in LogCat for more information on success.
     */
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

    /**
     * Sets the Volume level in sensible range. Handles special case of someone is calling or if on call.
     * @param context
     */
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
