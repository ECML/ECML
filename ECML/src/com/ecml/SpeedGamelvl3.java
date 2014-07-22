package com.ecml;

import java.util.ArrayList;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.game.MicrophonePitchPoster;

public class SpeedGamelvl3 extends SpeedGamelvl {
	
	private int score = 0;
    private int i = 0;
	private TextView textView;
	boolean point = true;

	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		player.getSpeedBar().setProgress(60-30);
		
		Tracks = midifile.getTracks();
		
		Notes = findNotes(Tracks,0);
	
	// Launch the game = Play button
	Button play = (Button) findViewById(R.id.play);
	play.setOnClickListener(new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			
      		player.Play();
			
	        pitchPoster = new MicrophonePitchPoster(60);
	        pitchPoster.setHandler(new UIUpdateHandler());
	        pitchPoster.start();
	        
			textView = (TextView) findViewById(R.id.affiche);
			textView.setText("Score :"+score+"/"+Notes.size());
			
			
		}
	});
	}
	
	private final class UIUpdateHandler extends Handler {
        public void handleMessage(Message msg) {
            final MicrophonePitchPoster.PitchData data
                = (MicrophonePitchPoster.PitchData) msg.obj;
            
            if (data != null && data.decibel > -20) {
            	
                if (i == Notes.size() ) {
                    player.Stop();
                    pitchPoster.stopSampling();
                    pitchPoster = null;
                    
                }
            	
            	 Double time = player.getprevPulseTime(); 
            	 while( Notes.get(i).getStartTime()+Notes.get(i).getDuration() < time )
            	 {
            		 i++;
            		 point = true;
            	 }
        	 
            	 String test = Notes.get(i).Pitch();
            	 
            	 if ( test.equals(noteNames[keyDisplay.ordinal()][data.note % 12]))
            	 {
            		 System.out.println("Dedans : ");            		 
            		 
            		 int début = Notes.get(i).getStartTime();
            		 int fin = Notes.get(i).getEndTime();
            		 
            		 if ( player.getprevPulseTime() > début && player.getprevPulseTime() < fin)
            		 {
            			 if ( point)
            			 {
            			 score++;
            			 point = false;
            			 }
            			 textView.setText("Score :"+score+"/"+Notes.size());
            		 }

            	 }
            	
            } else {
                // No valid data to display.
            	
            }
        }
	}
	
	private ArrayList<MidiNote> findNotes(ArrayList<MidiTrack> tracks, int instrument){
		
		int i = 0;
		search = true;
		while(search){
			if( instrument == Tracks.get(i).getInstrument()){
				search = false;
			}
			else{
				i++;
			}
		}
		return Tracks.get(i).getNotes();
	}
	
	
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction() & MotionEvent.ACTION_MASK;
        boolean result = scrollAnimation.onTouchEvent(event);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                // If we touch while music is playing, stop the midi player 
                
                    this.PauseEcoute();
                    scrollAnimation.stopMotion();
                
                return result;

            case MotionEvent.ACTION_MOVE:
                return result;

            case MotionEvent.ACTION_UP:
                return result;

            default:
                return false;
        }
        
    }

}
