package com.example;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

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
        if (insanityCheck())
            if (force) {
                if (player.isPlaying())
                    player.stop();
                player.start();
            } else if (!player.isPlaying())
                player.start();
    }

    //Can be called regardless we have valid music, It just won't do anything.
    public void stop() {
        if (insanityCheck())
            if (player.isPlaying())
                player.stop();
    }

    public void pause() {
        if (insanityCheck())
            if (player.isPlaying())
                player.pause();
    }

    public void release() {
        if (released)
            return;
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
        released = true;
    }
}
