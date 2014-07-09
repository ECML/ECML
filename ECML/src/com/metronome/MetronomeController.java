package com.metronome;


import com.ecml.MetronomeActivity;
import com.ecml.SheetMusicActivity;

public class MetronomeController {
	
	private SheetMusicActivity metronomeSheetMusicActivity;
	private MetronomeActivity metronomeActivity;
	private Metronome metronome;
	private Horloge horloge;

	public MetronomeController(SheetMusicActivity metronomeSheetMusicActivity) {
		this.metronomeSheetMusicActivity = metronomeSheetMusicActivity;
		metronome = new Metronome();
	}
	
	public MetronomeController(MetronomeActivity metronomeActivity) {
		this.metronomeActivity = metronomeActivity;
		metronome = new Metronome();
	}
	
	public int getTempo(){
		return metronome.getTempo();
	}
	
	public void setTempo(int tempo) {
		metronome.setTempo(tempo+1);
	}
	
	public int getAccentBeep(){
		return metronome.getAccentBeep();
	}
	
	public void setAccentBeep(int AccentBeep) {
		metronome.setAccentBeep(AccentBeep);
	}
	
	public void startMetronome() {
		if(horloge != null) {
			horloge.stop();
		}
		horloge = new Horloge(metronome.getTempo(), metronome.getAccentBeep(), metronomeActivity.getBaseContext());
	}
	
	public void stopMetronome() {
		if(horloge != null) {
			horloge.stop();
		}
	}
	
}
