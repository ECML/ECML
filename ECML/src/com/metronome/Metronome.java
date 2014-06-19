package com.metronome;

import android.media.AudioManager;
import android.media.ToneGenerator;

public class Metronome {

	private int tempo;
	private int measure;
	private ToneGenerator beep;
	private ToneGenerator firstBeep;
	
	public Metronome(){
		tempo = 60;
		measure = 1;
        beep = new ToneGenerator(AudioManager.FLAG_PLAY_SOUND, 100);
        firstBeep = new ToneGenerator(AudioManager.FLAG_PLAY_SOUND, 100);
	}

	public int getTempo() {
		return tempo;
	}

	public void setTempo(int tempo) {
		this.tempo = tempo;
	}

	public int getMeasure() {
		return measure;
	}

	public ToneGenerator getBeep() {
		return beep;
	}

	public void setBeep(ToneGenerator beep) {
		this.beep = beep;
	}

	public ToneGenerator getFirstBeep() {
		return firstBeep;
	}

	public void setFirstBeep(ToneGenerator firstBeep) {
		this.firstBeep = firstBeep;
	}
	
	
	
}
