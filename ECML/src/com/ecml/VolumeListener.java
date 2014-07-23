package com.ecml;

import android.content.Context;
import android.database.ContentObserver;
import android.media.AudioManager;
import android.os.Handler;

public class VolumeListener extends ContentObserver {
    private Context context;
    private MidiPlayer player;

    public VolumeListener(Context context, Handler handler, MidiPlayer player) {
        super(handler);
        this.context = context;
        this.player = player;
    }

    @Override
    public boolean deliverSelfNotifications() {
        return super.deliverSelfNotifications();
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);

        AudioManager audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int volume = audio.getStreamVolume(AudioManager.STREAM_MUSIC);

        if (volume != 0) {
        	player.volume = volume;
        }
        
        if (volume == 0 && !player.mute) {
		    player.mute();
        }
        if (volume != 0 && player.mute) {
        	player.unmute();
        }
    }
}
