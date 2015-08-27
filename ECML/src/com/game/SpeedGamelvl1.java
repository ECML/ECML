package com.game;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ScoreFollowerLibrary.ScoreFollower;
import com.ScoreFollowerLibrary.nl.metaphoric.scorefollower.lib.AnalyzeListener;
import com.ScoreFollowerLibrary.nl.metaphoric.scorefollower.lib.feature.FrameVector;
import com.ecml.R;
import com.ecml.SheetMusic;

/* First implementation of the SpeeGame
 * Wait for the users to play the right note */

public class SpeedGamelvl1 extends SpeedGamelvl implements AnalyzeListener {

    public static final String TAG = "SpeedGamelvl1";

	private Double currentPulseTime;	/* The current pulse time */
	private Double prevPulseTime;		/* The previous pulse time */
	private boolean firstTry;			/* The test whether the user played the right note on the first try */

    private ScoreFollower scoreFollower; /* The interface between library and ECML */


	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		percentage = (TextView) findViewById(R.id.percentage);
		appreciation = (TextView) findViewById(R.id.appreciation);
		star = (ImageView) findViewById(R.id.star);
		scoreDisplay = (TextView) findViewById(R.id.score);

		tracks = midifile.getTracks();
        scoreFollower = new ScoreFollower(this, tracks.get(0));

		// We use by default the instrument n0 which is the piano
		findNotes();

        playNoteDisplay = (TextView) findViewById(R.id.playNoteDisplay);

		// Launch the game = Play button
		Button play = (Button) findViewById(R.id.play);
		play.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                counter = 0; // note counter
                score = 0; // The number of points has to be set back to 0 otherwise the user could cheat
                firstTry = true;
                currentPulseTime = 0.0; // + notes.get(0).getDuration();
                prevPulseTime = 0.0;

                sheet.ShadeNotes(-10, player.getCurrentPulseTime().intValue(), SheetMusic.DontScroll);
                piano.ShadeNotes(-10, player.getCurrentPulseTime().intValue());


                // Humans start counting at 1, not 0
                playNoteDisplay.setText("Play the note n" + (counter + 1));

                scoreDisplay.setText(score + "/" + notes.size());

                // Start the position matcher
                scoreFollower.restartMatcher();

                Log.e("SpeedGameLvl1", "Demarrage du microphone reader");

                scoreFollower.startRecord();

            }
        });

		// Change stop button
		Button stop = (Button) findViewById(R.id.stop);
		stop.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				counter = 0;
				playNoteDisplay.setText("");

				if (player != null) {
					player.Stop();
				}
				/*if (pitchPoster != null) {
					pitchPoster.stopSampling();
				}*/
				if (micReader != null) {
					micReader.stopRecorder();
				}
			}
		});

	}

    /** Called when micReader is started. */
    @Override
    public void onNewAnalysisData(final FrameVector v) {

        // If we reach the end of the midifile, then we stop the player and
        // the score following
        if (counter == notes.size()) {
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
            if (micReader != null) {
                micReader.stopRecorder();
            }
            micReader = null;
        }

        // Matching
        int position = scoreFollower.getPosition(v);  // best estimated position of the note played
        if (scoreFollower.isPlaying()) {
            Log.e(TAG, "New position: " + position + " & counter: " + counter);
            Log.e(TAG, "Score: " + score);
            if (position == counter) {
                if (firstTry) {
                    score++;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            scoreDisplay.setText(score + "/" + notes.size());
                        }
                    });
                }
                advanceOneNote();
                counter++;
                firstTry = true;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        playNoteDisplay.setText("Play the note n" + (counter + 1));
                    }
                });
            } else {
                firstTry = false;
                Log.e(TAG, "test");
                scoreFollower.restartMatcher(counter - 1);
            }
        } else {
            Log.e(TAG, "Don't hear anything for now");
        }
    }

	/** Shade the next note to play each time a note is played */
	public void advanceOneNote() {
		if (midifile == null || sheet == null) {
			return;
		}
		// Remove any highlighted notes
		sheet.ShadeNotes(-10, player.getCurrentPulseTime().intValue(), SheetMusic.DontScroll);
		piano.ShadeNotes(-10, player.getCurrentPulseTime().intValue());

		prevPulseTime = currentPulseTime;
		// currentPulseTime += notes.get(counter).getDuration(); WORKS TOO
		currentPulseTime = 0.0 + notes.get(counter).getStartTime() + notes.get(counter).getDuration();
		if (currentPulseTime <= midifile.getTotalPulses()) {
			player.setCurrentPulseTime(currentPulseTime);
		}
		player.setCurrentPulseTime(currentPulseTime);
		player.setPrevPulseTime(prevPulseTime);
		reshadeNotes();
	}

	/** Reshade the current notes to play */
	public void reshadeNotes() {
		sheet.ShadeNotes(currentPulseTime.intValue(), prevPulseTime.intValue(), SheetMusic.ImmediateScroll);
		piano.ShadeNotes(currentPulseTime.intValue(), prevPulseTime.intValue());
	}

}

