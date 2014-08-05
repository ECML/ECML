package com.game;

import java.util.ArrayList;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ecml.MidiNote;
import com.ecml.MidiTrack;
import com.ecml.R;
import com.ecml.R.id;

/* First implementation of the SpeeGame
 * Wait for the users to play the right note */

public class SpeedGamelvl1 extends SpeedGamelvl {

	private TextView textView;
	private int compteurTexte = counter + 1;
	// TODO Unused variables to delete or to use
	private boolean pause = true;
	private String test;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		counter = 0;	// note counter

		tracks = midifile.getTracks();

		// We use by default the instrument n°0 which is the piano
		notes = findNotes(tracks, 0);

		// Launch the game = Play button
		Button play = (Button) findViewById(R.id.play);
		play.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				Double start = player.getprevPulseTime();

				textView = (TextView) findViewById(R.id.affiche);
				textView.setText("Play the note n°" + compteurTexte);


				// Pitch Detection launching
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
				compteurTexte = counter + 1;
				textView.setText("");

				if (player != null) {
					player.Stop();
				}
				if (pitchPoster != null) {
					pitchPoster.stopSampling();
				}
				pitchPoster = null;
			}
		});

	}

	private final class UIUpdateHandler extends Handler {
		public void handleMessage(Message msg) {
			final MicrophonePitchPoster.PitchData data = (MicrophonePitchPoster.PitchData) msg.obj;

			// If we reach the end of the midifile, then we stop the player and
			// the pitch detection
			if (counter == notes.size()) {
				counter = 0;
				if (player != null) {
					player.Stop();
				}
				if (pitchPoster != null) {
					pitchPoster.stopSampling();
				}
				pitchPoster = null;
				textView.setText("");
			}


			// If the data is non null and loud enough, we test it
			if (data != null && data.decibel > -20) {

				// Octave entier
				// Notes.get(compteur).Octave() == data note
				// Octave String
				// test.equals(noteNames[keyDisplay.ordinal()][data.note])

				String test = notes.get(counter).Pitch();

				// Check if it is the expected note
				if (test.equals(noteNames[keyDisplay.ordinal()][data.note % 12])) {
					counter++;
					compteurTexte++;
					textView.setText("Play the note n°" + compteurTexte);
				}

			} else {
				// No valid data to display.

			}

		}
	}

	private ArrayList<MidiNote> findNotes(ArrayList<MidiTrack> tracks, int instrument) {

		int i = 0;
		search = true;
		while (search) {
			if (instrument == tracks.get(i).getInstrument()) {
				search = false;
			} else {
				i++;
			}
		}
		return tracks.get(i).getNotes();
	}
}
