package com.metronome;


import com.ecml.SheetMusicActivity;
import com.ecml.MetronomeActivity;
import com.metronome.Horloge;
import com.metronome.Metronome;

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
	
	public void startMetronome() {
		if(horloge != null) {
			horloge.stop();
		}
		horloge = new Horloge(metronome.getTempo(), metronome.getMeasure(), metronome.getBeep());
	}
	
	public void stopMetronome() {
		if(horloge != null) {
			horloge.stop();
		}
	}
	
}
