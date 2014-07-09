package com.metronome;

public class Metronome {

	private int tempo;
	private int AccentBeep;

	public Metronome() {
		tempo = 60;
		AccentBeep = 0;
	}

	public int getTempo() {
		return tempo;
	}

	public void setTempo(int tempo) {
		this.tempo = tempo;
	}

	public int getAccentBeep() {
		return AccentBeep;
	}

	public void setAccentBeep(int AccentBeep) {
		this.AccentBeep = AccentBeep;
	}

}
