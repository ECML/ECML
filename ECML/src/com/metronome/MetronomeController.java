package com.metronome;


import com.ecml.SheetMusicActivity;
import com.metronome.Horloge;
import com.metronome.Metronome;

public class MetronomeController {
	
	private SheetMusicActivity metronomeActivity;
	private Metronome metronome;
	private Horloge horloge;

	public MetronomeController(SheetMusicActivity metronomeActivity){
		this.metronomeActivity = metronomeActivity;
		metronome = new Metronome();
	}
	
	public int getTempo(){
		return metronome.getTempo();
	}
	
	public void setTempo(int tempo){
		metronome.setTempo(tempo);
	}
	
	public void startMetronome(){
		if(horloge != null){
			horloge.stop();
		}
		horloge = new Horloge(metronome.getTempo(), metronome.getMeasure(), metronome.getBeep(), metronome.getFirstBeep());
	}
	
	public void stopMetronome(){
		if(horloge != null){
			horloge.stop();
		}
		
	}
	
	

	
}
