package com.metronome;


import android.content.Context;
import android.media.AudioManager;
import android.media.ToneGenerator;

public class Metronome {

	private int tempo;
	private int measure;
	private ToneGenerator beep;
	
	public Metronome() {
		tempo = 60;
		measure = 1;
        beep = new ToneGenerator(AudioManager.FLAG_PLAY_SOUND, 100);
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


}
