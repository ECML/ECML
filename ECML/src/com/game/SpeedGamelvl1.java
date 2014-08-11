package com.game;

import java.util.ArrayList;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ecml.MidiNote;
import com.ecml.MidiTrack;
import com.ecml.R;
import com.ecml.SheetMusic;

/* First implementation of the SpeeGame
 * Wait for the users to play the right note */

public class SpeedGamelvl1 extends SpeedGamelvl {

	private TextView textView;
	private Double currentPulseTime;
	private Double prevPulseTime;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().getDecorView().setBackgroundColor(Color.BLACK);


		tracks = midifile.getTracks();

		// We use by default the instrument n�0 which is the piano
		notes = findNotes(tracks, 0);

		// Launch the game = Play button
		Button play = (Button) findViewById(R.id.play);
		play.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				counter = 0;	// note counter
				currentPulseTime = 0.0; // + notes.get(0).getDuration();
				prevPulseTime = 0.0;

				sheet.ShadeNotes(-10, player.getCurrentPulseTime().intValue(), SheetMusic.DontScroll);
				piano.ShadeNotes(-10, player.getCurrentPulseTime().intValue());

				textView = (TextView) findViewById(R.id.affiche);
				// Humans start counting at 1, not 0
				textView.setText("Play the note n�" + (counter + 1)); 

				// Pitch Detection launching
				pitchPoster = new MicrophonePitchPoster(60);
				pitchPoster.setHandler(new UIUpdateHandler());
				pitchPoster.start();

				reshadeNotes();
			}
		});

		// Change stop button
		Button stop = (Button) findViewById(R.id.stop);
		stop.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				counter = 0;
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

				String test = notes.get(counter).pitch();

				// Check if it is the expected note
				if (test.equals(noteNames[keyDisplay.ordinal()][data.note % 12])) {
					advanceOneNote();
					counter++;
					textView.setText("Play the note n�" + (counter + 1));
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

	public void advanceOneNote() {
		if (midifile == null || sheet == null) {
			return;
		}
		/* Remove any highlighted notes */
		sheet.ShadeNotes(-10, player.getCurrentPulseTime().intValue(), SheetMusic.DontScroll);
		piano.ShadeNotes(-10, player.getCurrentPulseTime().intValue());

		prevPulseTime = currentPulseTime;
		// currentPulseTime += notes.get(counter).getDuration(); WORKS TOO
		currentPulseTime = 0.0 + notes.get(counter).getStartTime() + notes.get(counter).getDuration();
		Log.i("silence", "" + notes.get(counter).getNumber());
		if (currentPulseTime <= midifile.getTotalPulses()) {
			player.setCurrentPulseTime(currentPulseTime);
		}
		player.setCurrentPulseTime(currentPulseTime);
		player.setPrevPulseTime(prevPulseTime);
		reshadeNotes();
	}

	public void reshadeNotes() {
		sheet.ShadeNotes(currentPulseTime.intValue(), prevPulseTime.intValue(), SheetMusic.ImmediateScroll);
		piano.ShadeNotes(currentPulseTime.intValue(), prevPulseTime.intValue());
	}
}
