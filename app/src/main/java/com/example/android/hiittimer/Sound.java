package com.example.android.hiittimer;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

public class Sound {

    private SoundPool soundPool;

    private int soundIdBell;

    public Sound(Context context) {
            soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC,0);

            soundIdBell = soundPool.load(context, R.raw.button16,1);
    }

    public void playBell(){
        soundPool.play(soundIdBell,1.0f,1.0f,0,0,1.0f);
    }
}
