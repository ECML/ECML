package com.ecml;

import java.util.ArrayList;
import java.util.zip.CRC32;


import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.game.*;

/* First implementation of the SpeeGame
 * Wait for the users to play the right note */

public class SpeedGamelvl1 extends SpeedGamelvl {
	
	private boolean pause = true;
	private TextView textView;
	private int compteurTexte = counter+1;
    private String test;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		counter = 0;	// note counter
		
		Tracks = midifile.getTracks();
		
		// We use by default the intrument n°0 wich is the piano
		Notes = findNotes(Tracks,0);
	
	// Launch the game = Play button
	Button play = (Button) findViewById(R.id.play);
	play.setOnClickListener(new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			
			Double start = player.getprevPulseTime();
						
			textView = (TextView) findViewById(R.id.affiche);
			textView.setText("Play the note n°"+compteurTexte);
						
			
			// Pitch Detetion launching 
	        pitchPoster = new MicrophonePitchPoster(60);
	        pitchPoster.setHandler(new UIUpdateHandler());
	        pitchPoster.start();
								
		}
	});
	
	// Change stop button
	Button stop = (Button) findViewById(R.id.stop);
	stop.setOnClickListener(new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			counter = 0;
			compteurTexte = counter+1;
			textView.setText("");
			
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
	});
	
	}
	
	private final class UIUpdateHandler extends Handler {
        public void handleMessage(Message msg) {
            final MicrophonePitchPoster.PitchData data
                = (MicrophonePitchPoster.PitchData) msg.obj;
            
        	// If we reach the end of the midifile, then we stop the player and the pitch detection
            if (counter == Notes.size() ) {
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
				textView.setText("");
            }
            

            // If the data is non null and loud enough, we test it
            if (data != null && data.decibel > -20) {

               	//Octave entier
            	//Notes.get(compteur).Octave() == data note
            	// Octave String              	
                //test.equals(noteNames[keyDisplay.ordinal()][data.note])
            	
            	String test = Notes.get(counter).Pitch();
            	
            	// Check if it is the expected note
            	 if ( test.equals(noteNames[keyDisplay.ordinal()][data.note % 12]) )
            	 {            		 
            		 counter++;
            		 compteurTexte++;
         			 textView.setText("Play the note n°"+compteurTexte);          		 
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
}
