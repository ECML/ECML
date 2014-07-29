package com.game;

import java.util.ArrayList;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ecml.ECML;
import com.ecml.MidiNote;
import com.ecml.MidiTrack;
import com.ecml.R;
import com.ecml.R.id;

/* Second implementation of the SpeeGame
 * The Player reads the sheet music at either a very slow, slow or real speed
 * The user must play the notes when they are highlighted on the sheet music */

public class SpeedGamelvln extends SpeedGamelvl {
	
	private TextView textView;
	private boolean end = false;

	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if (ECML.speedLvl == 2) {
			player.getSpeedBar().setProgress(30-30);
		} else if (ECML.speedLvl == 3) {
			player.getSpeedBar().setProgress(70-30);
		} else if (ECML.speedLvl == 4) {
			player.getSpeedBar().setProgress(100-30);
		}
		
		// Extracting the Tracks
		Tracks = midifile.getTracks();
		
		counter = 0;
		// Extracting the notes from the Track file
		// We use by default the intrument n�0 wich is the piano
		Notes = findNotes(Tracks,0);
	
	// Launch the game = Play button
	Button play = (Button) findViewById(R.id.play);
	play.setOnClickListener(new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			point = true;
			
			// If the game has been finished, set the score to 0
			if (end)
			{
				score = 0;
			}
			
      		player.Play();
			
      		// Pitch Detetion launching 
	        pitchPoster = new MicrophonePitchPoster(60);
	        // Adding the handler
	        pitchPoster.setHandler(new UIUpdateHandler());
	        pitchPoster.start();
	        
	        // Displaying score
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
            	
            	// If we reach the end of the midifile, then we stop the player and the pitch detection
                if (counter == Notes.size() ) {
    				end = true;
    				counter = 0;
    				if ( player != null )
    				{
    					player.Stop();
    				}
    				if ( pitchPoster != null )
    				{
    			        pitchPoster.stopSampling();
    				}
    		        pitchPoster = null;
                    
                }
            	
                
            	 Double time = player.getprevPulseTime(); 
            	 while( Notes.get(counter).getStartTime()+Notes.get(counter).getDuration() < time )
            	 {
            		 counter++;
            		 point = true;
            	 }
        	 
            	 String test = Notes.get(counter).Pitch();
            	 
            	 if ( test.equals(noteNames[keyDisplay.ordinal()][data.note % 12]))
            	 {         		 
            		 
            		 int d�but = Notes.get(counter).getStartTime();
            		 int fin = Notes.get(counter).getEndTime();
            		 
            		 if ( player.getprevPulseTime() > d�but && player.getprevPulseTime() < fin)
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