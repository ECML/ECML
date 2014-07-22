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

public class SpeedGamelvl1 extends SpeedGamelvl {
	
	private boolean pause = true;
	private TextView textView;
    private int compteur = 0;
	private int compteurTexte = compteur+1;
    private String test;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		player.getSpeedBar().setProgress(40-30);
		
	
	// Launch the game = Play button
	Button play = (Button) findViewById(R.id.play);
	play.setOnClickListener(new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			
			Double start = player.getprevPulseTime();
			
			Tracks = midifile.getTracks();
			
			// We use by default the intrument n°0 wich is the piano
			Notes = findNotes(Tracks,0);
			
			textView = (TextView) findViewById(R.id.affiche);
			textView.setText("Jouez la note n°"+compteurTexte);
			
      		/*player.Play();
       			
      		while ( start <  Notes.get(0).getStartTime() )
      		{
      			start = player.getprevPulseTime();
      		}
      		    		
    		player.Pause();
    		scrollAnimation.stopMotion();*/
			
	        pitchPoster = new MicrophonePitchPoster(60);
	        pitchPoster.setHandler(new UIUpdateHandler());
	        pitchPoster.start();
								
		}
	});
	}
	
	private final class UIUpdateHandler extends Handler {
        public void handleMessage(Message msg) {
            final MicrophonePitchPoster.PitchData data
                = (MicrophonePitchPoster.PitchData) msg.obj;
            


            if (data != null && data.decibel > -20) {
;
               	//Octave entier
            	//Notes.get(compteur).Octave() == data note
            	// Octave String              	
                //test.equals(noteNames[keyDisplay.ordinal()][data.note])
            	
            	String test = Notes.get(compteur).Pitch();
            	
            	 if ( test.equals(noteNames[keyDisplay.ordinal()][data.note % 12]) )
            	 {
            		 
            		 System.out.println("Dedans : /n");
            		  
            		 /*player.Play();
            		 
            		 Double current = player.getprevPulseTime(); 
            		 
            		 Double fin = null;
					while ( current !=  fin )
            		 {
            			 current = player.getprevPulseTime(); 
            		 }
            		 
            		 player.Pause(); */      		 
            		 
            		 compteur++;
            		 compteurTexte++;

         			 textView.setText("Jouez la note n°"+compteurTexte);
            		 
            	 }
            	
            } else {
                // No valid data to display. Set most elements invisible.
            	  		    
            }
            if (data != null && data.decibel > -60) {

    		    /*Toast.makeText(getApplicationContext(), "Notes are not loud enough",
    				    Toast.LENGTH_LONG).show();*/
            } else {
            	
    		    /*Toast.makeText(getApplicationContext(), "You play the wrong note \nTry to play louder",
    				    Toast.LENGTH_LONG).show();*/
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
