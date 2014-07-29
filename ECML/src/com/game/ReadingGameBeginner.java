package com.game;

import java.util.ArrayList;

import com.ecml.MidiNote;
import com.ecml.MidiTrack;
import com.ecml.R;
import com.ecml.R.id;

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

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		counter = 0; // note counter
		Tracks = midifile.getTracks();

		// We use by default the instrument n°0 which is the piano
		Notes = findNotes(Tracks, 0);

		// Launch the game = Play button
		Button play = (Button) findViewById(R.id.play);
		play.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				textView = (TextView) findViewById(R.id.affiche);
				textView.setText("Choose which one is the note number " + compteurTexte);
				Log.i("note", "" + Notes.get(counter).Pitch());

			}
		});

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
				String test = Notes.get(counter).PitchString();
				// Check if it is the expected note
				if ("A" == test) {
					GreenToOrange(la);
					counter++;
					compteurTexte++;
					textView.setText("Choose which one is the note number " + compteurTexte);
				} else {
					redToOrange(la);
				}
			}
		});

		// La sharp button
		final Button lad = (Button) findViewById(R.id.lad);
		lad.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String test = Notes.get(counter).PitchString();
				// Check if it is the expected note
				if ("A#" == test) {
					GreenToOrange(lad);
					counter++;
					compteurTexte++;
					textView.setText("Choose which one is the note number " + compteurTexte);
				} else {
					redToOrange(lad);
				}
			}
		});

		// Si button
		final Button si = (Button) findViewById(R.id.si);
		si.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String test = Notes.get(counter).PitchString();
				// Check if it is the expected note
				if ("B" == test) {
					GreenToOrange(si);
					counter++;
					compteurTexte++;
					textView.setText("Choose which one is the note number " + compteurTexte);
				} else {
					redToOrange(si);
				}
			}
		});

		// Do button
		final Button donote = (Button) findViewById(R.id.donote);
		donote.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String test = Notes.get(counter).PitchString();
				// Check if it is the expected note
				if ("C" == test) {
					GreenToOrange(donote);
					counter++;
					compteurTexte++;
					textView.setText("Choose which one is the note number " + compteurTexte);
				} else {
					redToOrange(donote);
				}
			}
		});

		// Do sharp button
		final Button dod = (Button) findViewById(R.id.dod);
		dod.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String test = Notes.get(counter).PitchString();
				// Check if it is the expected note
				if ("C#" == test) {
					GreenToOrange(dod);
					counter++;
					compteurTexte++;
					textView.setText("Choose which one is the note number " + compteurTexte);
				} else {
					redToOrange(dod);
				}
			}
		});

		// Ré button
		final Button re = (Button) findViewById(R.id.re);
		re.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String test = Notes.get(counter).PitchString();
				// Check if it is the expected note
				if ("D" == test) {
					GreenToOrange(re);
					counter++;
					compteurTexte++;
					textView.setText("Choose which one is the note number " + compteurTexte);
				} else {
					redToOrange(re);
				}
			}
		});

		// Ré sharp button
		final Button red = (Button) findViewById(R.id.red);
		red.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String test = Notes.get(counter).PitchString();
				// Check if it is the expected note
				if ("D#" == test) {
					GreenToOrange(red);
					counter++;
					compteurTexte++;
					textView.setText("Choose which one is the note number " + compteurTexte);
				} else {
					redToOrange(red);
				}
			}
		});

		// Mi button
		final Button mi = (Button) findViewById(R.id.mi);
		mi.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String test = Notes.get(counter).PitchString();
				// Check if it is the expected note
				if ("E" == test) {
					GreenToOrange(mi);
					counter++;
					compteurTexte++;
					textView.setText("Choose which one is the note number " + compteurTexte);

				} else {
					redToOrange(mi);
				}
			}
		});

		// Fa button
		final Button fa = (Button) findViewById(R.id.fa);
		fa.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String test = Notes.get(counter).PitchString();
				// Check if it is the expected note
				if ("F" == test) {
					GreenToOrange(fa);
					counter++;
					compteurTexte++;
					textView.setText("Choose which one is the note number " + compteurTexte);
				} else {
					redToOrange(fa);
				}
			}
		});

		// Fa sharp button
		final Button fad = (Button) findViewById(R.id.fad);
		fad.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String test = Notes.get(counter).PitchString();
				// Check if it is the expected note
				if ("F#" == test) {
					GreenToOrange(fad);
					counter++;
					compteurTexte++;
					textView.setText("Choose which one is the note number " + compteurTexte);
				} else {
					redToOrange(fad);
				}
			}
		});

		// Sol button
		final Button sol = (Button) findViewById(R.id.sol);
		sol.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String test = Notes.get(counter).PitchString();
				// Check if it is the expected note
				if ("G" == test) {
					GreenToOrange(sol);
					counter++;
					compteurTexte++;
					textView.setText("Choose which one is the note number " + compteurTexte);
				} else {
					redToOrange(sol);
				}
			}
		});

		// Sol sharp button
		final Button sold = (Button) findViewById(R.id.sold);
		sold.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String test = Notes.get(counter).PitchString();
				// Check if it is the expected note
				if ("G#" == test) {
					GreenToOrange(sold);
					counter++;
					compteurTexte++;
					textView.setText("Choose which one is the note number " + compteurTexte);
				} else {
					redToOrange(sold);
				}
			}
		});

		orangeColor = (ColorDrawable) la.getBackground();

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
