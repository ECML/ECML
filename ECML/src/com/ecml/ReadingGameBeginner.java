package com.ecml;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ReadingGameBeginner extends ReadingGame {

	View choice;
	View result;

	private TextView textView;
	private int compteurTexte = counter + 1;

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

}
