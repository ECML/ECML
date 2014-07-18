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

import com.game.*;

public class SpeedGamelvl1 extends SpeedGameLvl {
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
	// Launch the game = Play button
	Button play = (Button) findViewById(R.id.play);
	play.setOnClickListener(new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			
			/*Notes = findNotes(Tracks,0);
			
	        pitchPoster = new MicrophonePitchPoster(60);
	        pitchPoster.setHandler(new UIUpdateHandler());
	        pitchPoster.start();*/
								
		}
	});
	}
	
	private final class UIUpdateHandler extends Handler {
        public void handleMessage(Message msg) {
            final MicrophonePitchPoster.PitchData data
                = (MicrophonePitchPoster.PitchData) msg.obj;
            
            int i = 0;

            if (data != null && data.decibel > -20) {
               /* frequencyDisplay.setText(String.format(data.frequency < 200 ? "%.1fHz" : "%.0fHz",
                                                       data.frequency));
                final String printNote = noteNames[keyDisplay.ordinal()][data.note % 12];
                noteDisplay.setText(printNote.substring(0, 1));
                final String accidental = printNote.length() > 1 ? printNote.substring(1) : "";
                flatDisplay.setVisibility("b".equals(accidental) ? View.VISIBLE : View.INVISIBLE);
                sharpDisplay.setVisibility("#".equals(accidental) ? View.VISIBLE : View.INVISIBLE);
                nextNote.setText(noteNames[keyDisplay.ordinal()][(data.note + 1) % 12]);
                prevNote.setText(noteNames[keyDisplay.ordinal()][(data.note + 11) % 12]);
                final int c = Math.abs(data.cent) > centThreshold
                        ? Color.rgb(255, 50, 50)
                        : Color.rgb(50, 255, 50);
                noteDisplay.setTextColor(c);
                flatDisplay.setTextColor(c);
                sharpDisplay.setTextColor(c);
                offsetCentView.setValue((int) data.cent);
                staffView.pushNote(data.note);*/
            	System.out.println(Notes.get(i).toString()+"\n");
            	System.out.println(noteNames[keyDisplay.ordinal()][data.note % 12]+"\n");
            	
            	 if ( Notes.get(i).toString() == noteNames[keyDisplay.ordinal()][data.note % 12])
            	 {
            		 
            	 }
            	
            } else {
                // No valid data to display. Set most elements invisible.
            	
                /*frequencyDisplay.setText("");
                final int ghostColor = Color.rgb(40,  40,  40);
                noteDisplay.setTextColor(ghostColor);
                flatDisplay.setTextColor(ghostColor);
                sharpDisplay.setTextColor(ghostColor);
                prevNote.setText("");
                nextNote.setText("");
                offsetCentView.setValue(100);  // out of range, not displayed.
                staffView.pushNote(-1);*/
            }
            if (data != null && data.decibel > -60) {
               /* decibelView.setVisibility(View.VISIBLE);
                decibelView.setText(String.format("%.0fdB", data.decibel));*/
            } else {
                //decibelView.setVisibility(View.INVISIBLE);
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
