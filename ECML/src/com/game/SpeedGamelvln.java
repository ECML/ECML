package com.game;

import java.util.ArrayList;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecml.MidiNote;
import com.ecml.MidiTrack;
import com.ecml.R;

/* Second implementation of the SpeedGame
 * The Player reads the sheet music at either a very slow, slow or real speed
 * The user must play the notes when they are highlighted on the sheet music */

public class SpeedGamelvln extends SpeedGamelvl {

	private TextView textView;
	private boolean end = false;
	public static final int level = 1;
	private boolean firstTry = true;
	TextView percentage;
	TextView appreciation;
	ImageView star;
	TextView scoreGame;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		percentage = (TextView) findViewById(R.id.percentage);
		appreciation = (TextView) findViewById(R.id.appreciation);
		star = (ImageView) findViewById(R.id.star);
		scoreGame = (TextView) findViewById(R.id.score);

		int lvl = this.getIntent().getIntExtra("level", 2);
		if (lvl == 2) {
			player.getSpeedBar().setProgress(30 - 30);
		} else if (lvl == 3) {
			player.getSpeedBar().setProgress(70 - 30);
		} else if (lvl == 4) {
			player.getSpeedBar().setProgress(100 - 30);
		}

		// Extracting the tracks
		tracks = midifile.getTracks();

		counter = 0;
		// Extracting the notes from the Track file
		// We use by default the intrument n�0 wich is the piano
		notes = findNotes(tracks, 0);

		// Launch the game = Play button
		Button play = (Button) findViewById(R.id.play);
		play.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				point = true;

				// If the game has been finished, set the score to 0
				if (end) {
					score = 0;
				}

				player.Play();

				// Pitch Detection launching
				pitchPoster = new MicrophonePitchPoster(60);
				// Adding the handler
				pitchPoster.setHandler(new UIUpdateHandler());
				pitchPoster.start();

				// Displaying score
				textView = (TextView) findViewById(R.id.affiche);
				textView.setText("Score :" + score + "/" + notes.size());
			}
		});
	}

	private final class UIUpdateHandler extends Handler {
		public void handleMessage(Message msg) {
			final MicrophonePitchPoster.PitchData data = (MicrophonePitchPoster.PitchData) msg.obj;

			if (data != null && data.decibel > -20) {

				// If we reach the end of the midifile, then we stop the player
				// and the pitch detection
				if (counter == notes.size()) {
					end = true;
					Log.i("result", "" + "bouh");
					counter = 0;
					SpeedGamelvl.speedGameView.setVisibility(View.GONE);
					SpeedGamelvl.result.setVisibility(View.VISIBLE);
					double percentageScore = score * 100 / notes.size();
					percentage.setText(String.valueOf(percentageScore) + "%");
					if (percentageScore >= 90) {
						percentage.setTextColor(Color.GREEN);
						appreciation.setText("Congratulations!");
					} else {
						percentage.setTextColor(Color.RED);
						appreciation.setText("Try again!");
						star.setVisibility(View.GONE);
					}
					

					if (player != null) {
						player.Stop();
					}
					if (pitchPoster != null) {
						pitchPoster.stopSampling();
					}
					pitchPoster = null;

				}

				Double time = player.getPrevPulseTime();
				while (notes.get(counter).getStartTime() + notes.get(counter).getDuration() < time) {
					counter++;
					Log.i("counter", "" + counter);
					point = true;
				}

				String test = notes.get(counter).Pitch();

				if (test.equals(noteNames[keyDisplay.ordinal()][data.note % 12])) {
					
					int debut = notes.get(counter).getStartTime();
					int fin = notes.get(counter).getEndTime();

					if (player.getPrevPulseTime() > debut && player.getPrevPulseTime() < fin) {

						if (point) {
							point = false;
							if (firstTry == true) {
								score++;
							}
							firstTry = true;
						}
						textView.setText("Score :" + score + "/" + notes.size());
					} else {
						firstTry = false;
					}

				}
			} else {
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

	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction() & MotionEvent.ACTION_MASK;
		boolean result = sheet.getScrollAnimation().onTouchEvent(event);
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			// If we touch while music is playing, stop the midi player
			this.PauseEcoute();
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
