package com.metronome;


import android.content.Context;
import android.media.AudioManager;
import android.media.ToneGenerator;

public class Metronome {

	private int tempo;
	
	
	public Metronome() {
		tempo = 60;
	}

	public int getTempo() {
		return tempo;
	}

	public void setTempo(int tempo) {
		this.tempo = tempo;
	}


}
