package com.game;

import java.util.ArrayList;

import com.ecml.MidiNote;
import com.ecml.MidiTrack;
import com.ecml.R;
import com.ecml.R.id;
import com.ecml.SheetMusic;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ReadingGameBeginner extends ReadingGame {

	View choice;
	View result;

	private TextView textView;
	private int compteurTexte = counter + 1;
	private ColorDrawable orangeColor;
	private int numberPoints = 0;
	private int firstTry = 0;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		counter = 0; // note counter
		Tracks = midifile.getTracks();

		// We use by default the instrument n°0 which is the piano
		Notes = findNotes(Tracks, 0);
		
		//Launch the game
		textView = (TextView) findViewById(R.id.affiche);
		textView.setText("Choose which one is the note number " + compteurTexte + "         " + "Score : " + numberPoints + "/" + Notes.size());
		Log.i("color", "" + SheetMusic.NoteColor(2));
		//SheetMusic.NoteColors[0] = Color.GREEN;
		
		

		// Change stop button
		Button stop = (Button) findViewById(R.id.stop);
		stop.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				counter = 0;
				compteurTexte = counter + 1;
				textView.setText("");

				if (player != null) {
					player.Stop();
				}
			}
		});

		// If we reach the end of the midifile, then we stop the player
		if (counter == Notes.size()) {
			counter = 0;
			if (player != null) {
				player.Stop();
			}
			textView.setText("");
		}

		// La button
		final Button la = (Button) findViewById(R.id.la);
		la.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				testNote("A", la);
			}
		});

		// La sharp button
		final Button lad = (Button) findViewById(R.id.lad);
		lad.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				testNote("A#", lad);
			}
		});
		
		orangeColor = (ColorDrawable) la.getBackground();

		// Si button
		final Button si = (Button) findViewById(R.id.si);
		si.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				testNote("B", si);
			}
		});

		// Do button
		final Button donote = (Button) findViewById(R.id.donote);
		donote.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				testNote("C", donote);
			}
		});

		// Do sharp button
		final Button dod = (Button) findViewById(R.id.dod);
		dod.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				testNote("C#", dod);
			}
		});

		// Ré button
		final Button re = (Button) findViewById(R.id.re);
		re.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				testNote("D", re);
			}
		});

		// Ré sharp button
		final Button red = (Button) findViewById(R.id.red);
		red.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				testNote("D#", red);
			}
		});

		// Mi button
		final Button mi = (Button) findViewById(R.id.mi);
		mi.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				testNote("E", mi);
			}
		});

		// Fa button
		final Button fa = (Button) findViewById(R.id.fa);
		fa.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				testNote("F", fa);
			}
		});

		// Fa sharp button
		final Button fad = (Button) findViewById(R.id.fad);
		fad.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				testNote("F#", fad);
			}
		});

		// Sol button
		final Button sol = (Button) findViewById(R.id.sol);
		sol.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				testNote("G", sol);
			}
		});

		// Sol sharp button
		final Button sold = (Button) findViewById(R.id.sold);
		sold.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				testNote("G#", sold);				
			}
		});

	}
	
	private void testNote (String letter, Button btn){
		firstTry = firstTry + 1;
		String test = Notes.get(counter).PitchString();
		// Check if it is the expected note
		if (letter == test) {
			if (firstTry == 1) {numberPoints = numberPoints + 1;}
			GreenToOrange(btn);
			firstTry = 0;
			Log.i("score", "" + numberPoints);
			counter++;
			compteurTexte++;
			textView.setText("Choose which one is the note number " + compteurTexte + "         " + "Score : " + numberPoints + "/" + Notes.size());
		} else {
			redToOrange(btn);
		}
	}

	private ArrayList<MidiNote> findNotes(ArrayList<MidiTrack> tracks, int instrument) {

		int i = 0;
		search = true;
		while (search) {
			if (instrument == Tracks.get(i).getInstrument()) {
				search = false;
			} else {
				i++;
			}
		}
		return Tracks.get(i).getNotes();
	}

	private void redToOrange(Button btn) {
		// Let's change background's color from red to inital orange color.
		ColorDrawable[] color = { new ColorDrawable(Color.RED), orangeColor };
		TransitionDrawable trans = new TransitionDrawable(color);
		// This will work also on old devices. The latest API says you have to
		// use setBackground instead.
		btn.setBackgroundDrawable(trans);
		trans.startTransition(300);
	}

	private void GreenToOrange(Button btn) {
		// Let's change background's color from green to initial orange color.
		ColorDrawable[] color = { new ColorDrawable(Color.GREEN), orangeColor };
		TransitionDrawable trans = new TransitionDrawable(color);
		// This will work also on old devices. The latest API says you have to
		// use setBackground instead.
		btn.setBackgroundDrawable(trans);
		trans.startTransition(300);
	}

}
