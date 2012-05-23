package com.example;

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

    Ringtone music;
    Context context;
    MediaPlayer player;

    public MusicHandler(Context context) {
        this.context = context;
    }


    public void setMusic() {
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        music = RingtoneManager.getRingtone(context, notification);
        player = MediaPlayer.create(context,R.raw.alarm);

    }

    /*
    Returns true if everythings ok.
     */
    public boolean insanityCheck() {
        boolean insane = music == null;
        if (insane)
            Log.w("MusicHandler", "Ringtone is not set for device.");
        return !insane;
    }

    //Can be called regardless we have valid music, It just won't do anything.
    public void play() {
        if (!insanityCheck()) {
            if (!music.isPlaying())
                music.play();
        } else {
            if (!player.isPlaying())
                player.start();
        }

    }

    //Can be called regardless we have valid music, It just won't do anything.
    public void stop() {
        if (!insanityCheck()) {
            if (!music.isPlaying())
                music.stop();
        } else {
            if (!player.isPlaying())
                player.stop();
        }
    }
}
