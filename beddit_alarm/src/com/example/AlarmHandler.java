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
public class AlarmHandler {

    Ringtone music;
    Context context;

    public AlarmHandler(Context context) {
        this.context = context;
    }


    public void setMusic() {
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        music = RingtoneManager.getRingtone(context, notification);

    }

    /*
    Returns true if everythings ok.
     */
    public boolean insanityCheck() {
        boolean insane = music == null;
        if (insane)
            Log.w("AlarmHandler", "Ringtone is not set for device.");
        return !insane;
    }

    //Can be called regardless we have valid music, It just won't do anything.
    public void play() {
        if (!insanityCheck() && !music.isPlaying())
            music.play();
    }

    //Can be called regardless we have valid music, It just won't do anything.
    public void stop() {
        if (!insanityCheck() && !music.isPlaying())
            music.stop();
    }
}
