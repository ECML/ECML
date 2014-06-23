package com.metronome;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Looper;

public class Horloge {

	private Timer timer;
	private int measure;
	private ToneGenerator beep;
	private ToneGenerator firstBeep;

	
	public Horloge(int tempo, int measure, ToneGenerator beep, ToneGenerator firstBeep) {
		
		this.beep = beep;
		this.firstBeep = firstBeep;
		this.measure = measure;
		timer = new Timer();
        
		TimerTask timerTask = new TimerTask() {
			
			@Override
			public void run() {
				
				try {
					Horloge.this.beep.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 100);
				}
				
				catch (Exception e) {
					System.err.println("ERROR when beeping");
				}
				
			}
		};
		
		timer.schedule(timerTask, new Date(), 60000/tempo);
	}
	
	public void stop() {
		timer.cancel();
	}
	
	public void purge() {
		timer.purge();
	}
	
}
