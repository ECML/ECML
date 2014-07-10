package com.metronome;


import android.app.Activity;

public class MetronomeController {

	private Activity activity;
	private Metronome metronome;
	private Horloge horloge;

	public MetronomeController(Activity activity) {
		this.activity = activity;
		metronome = new Metronome();
	}

	public int getTempo() {
		return metronome.getTempo();
	}

	public void setTempo(int tempo) {
		metronome.setTempo(tempo + 1);
	}

	public int getAccentBeep() {
		return metronome.getAccentBeep();
	}

	public void setAccentBeep(int AccentBeep) {
		metronome.setAccentBeep(AccentBeep);
	}

	public void startMetronome() {
		if (horloge != null) {
			horloge.stop();
		}
		horloge = new Horloge(metronome.getTempo(), metronome.getAccentBeep(), activity.getBaseContext());
	}

	public void stopMetronome() {
		if (horloge != null) {
			horloge.stop();
		}
	}

}
