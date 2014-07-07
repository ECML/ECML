package com.metronome;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Looper;

public class Horloge {

	private Timer timer;
	private int currentBeep = 1;
	private ToneGenerator beep;
	private AudioManager audioManager;
	private int volume;
	private int volumeMax;

	
	public Horloge(int tempo, Context context) {
		
		audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		volume = audioManager.getStreamVolume(AudioManager.STREAM_RING);
		volumeMax = audioManager.getStreamMaxVolume(AudioManager.STREAM_RING);
        beep = new ToneGenerator(AudioManager.FLAG_PLAY_SOUND, volume*100/volumeMax);
		timer = new Timer();
        
		TimerTask timerTask = new TimerTask() {
			
			@Override
			public void run() {
				try {

					if (volume != audioManager.getStreamVolume(AudioManager.STREAM_RING)) {
						volume = audioManager.getStreamVolume(AudioManager.STREAM_RING);
						beep.release();
						beep = new ToneGenerator(AudioManager.FLAG_PLAY_SOUND, volume*100/volumeMax);
					}
					
					if(currentBeep % 4 == 0) {
						Horloge.this.beep.startTone(ToneGenerator.TONE_SUP_DIAL, 100);
					}
					else {
						Horloge.this.beep.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 100);
					}
					currentBeep++;
				}
				
				catch (Exception e) {
					System.err.println("ERROR when beeping");
				}
				
			}
		};
		
		timer.schedule(timerTask, new Date(), 60000/tempo);
	}
	
	public void stop() {
		beep.release();
		timer.cancel();
	}
	
	public void purge() {
		timer.purge();
	}
	
}
