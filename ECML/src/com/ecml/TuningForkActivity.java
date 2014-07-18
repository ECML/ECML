package com.ecml;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.SeekBar;

import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class TuningForkActivity extends Activity {

	// sets up the objects that are on the screen
	private TextView mFreq;
	private ToggleButton mToggle;
	private int mSineFreq;
	private TextView previousOctave;
	private TextView nextOctave;
	private TextView previousNote;
	private TextView nextNote;
	private SeekBar mPitchBar;
	private TextView mOctave;
	private TextView mNote;
	private TextView mRefNote;
	private boolean running = false;

	// variables for tone generation
	private final int sampleRate = 44000;
	private final int targetSamples = 5500;
	private int numSamples = 5500;   // calculated with respect to frequency later
	private int numCycles = 500;    // calculated with respect to frequency later

	// the array is made bigger than needed so they can be adjusted
	private double sample[] = new double[targetSamples * 2];
	private byte generatedSnd[] = new byte[2 * 2 * targetSamples];

	private String[] notes = { "G\u266F/ A\u266D", "A", "A\u266F/ B\u266D", "B", "C", "C\u266F/ D\u266D", "D", "D\u266F/ E\u266D", "E", "F",
			"F\u266F/ G\u266D", "G" };

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tuning_fork);

		/* Action Bar */
		ActionBar ab = getActionBar();
		ColorDrawable colorDrawable = new ColorDrawable(getResources().getColor(R.color.orange));
		ab.setBackgroundDrawable(colorDrawable);
		/* End of Action bar */

		// hook up the buttons etc to their instances
		mToggle = (ToggleButton) findViewById(R.id.toggleButton);
		previousOctave = (TextView) findViewById(R.id.previousOctave);
		nextOctave = (TextView) findViewById(R.id.nextOctave);
		previousNote = (TextView) findViewById(R.id.previousNote);
		nextNote = (TextView) findViewById(R.id.nextNote);
		mPitchBar = (SeekBar) findViewById(R.id.PitchBar);
		mOctave = (TextView) findViewById(R.id.numberOctave);
		mNote = (TextView) findViewById(R.id.letterNote);
		mFreq = (TextView) findViewById(R.id.numberFrequence);
		mRefNote = (TextView) findViewById(R.id.textViewAdjRefPitch);

		// Hook up button presses to the appropriate event handlers.
		mToggle.setOnClickListener(mToggleListener);
		mPitchBar.setOnSeekBarChangeListener(mSineFreqBarListener);

		previousOctave.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mSineFreq > 12) {
					mSineFreq -= 12;
					updateView();
				}
			}

		});

		nextOctave.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mSineFreq < 124 - 12) {
					mSineFreq += 12;
					updateView();
				}
			}

		});

		previousNote.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mSineFreq > 1) {
					mSineFreq -= 1;
					updateView();
				}
			}

		});

		nextNote.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mSineFreq < 124) {
					mSineFreq += 1;
					updateView();
				}
			}

		});
		
		mRefNote.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mPitchBar.setProgress(100);
			}
		});

		// initialize the buttons to desired values
		mToggle.setChecked(false);
		mSineFreq = 61;
		mPitchBar.setMax(200);
		mPitchBar.setProgress(100);
		updateView();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// kill the child thread
		running = false;
	}


	/**
	 * A call-back for when the user presses the toggle button
	 */
	OnClickListener mToggleListener = new OnClickListener() {
		public void onClick(View v) {

			// kill any existing threads in case the button is being spammed
			running = false;

			// check if light is off, if so, turn it on
			if (mToggle.isChecked()) { // turn on the sound
				genTone(convertProgress_Hz(mSineFreq));
				new BeepTask().execute();
			}

		}
	};
	
	/**
	 * A call-back for when the user moves the sine seek bars
	 */
	OnSeekBarChangeListener mSineFreqBarListener = new OnSeekBarChangeListener() {

		public void onStopTrackingTouch(SeekBar seekBar) {
			genTone(convertProgress_Hz(mSineFreq));
		}

		public void onStartTrackingTouch(SeekBar seekBar) {


		}

		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
			if (mSineFreq < 1)
				mSineFreq = 1;
			updateView();
		}
	};

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}


	// this runs the process in a background thread so the UI isn't locked up
	private class BeepTask extends AsyncTask<Void, Void, Void> {
		// private class BlinkFlashTask extends AsyncTask<CharSequence, Void,
		// Void> {

		protected void onProgressUpdate(Void... voids) {

		}

		@SuppressWarnings("unused")
		protected void onPostExecute(Void... voids) {
			// turn the light off when done

		}

		@SuppressWarnings("unused")
		protected void onCancelled(Void... voids) {
			// turn the light off when done

		}

		@Override
		protected Void doInBackground(Void... voids) {
			// protected Void doInBackground(CharSequence... patternBuffer) {
			running = true;
			playSound();
			// loop with breaker variable from the parent
			return null;
		}
	}

	// Based on but modified and improved from
	// http://stackoverflow.com/questions/2413426/playing-an-arbitrary-tone-with-android
	// functions for tone generation
	void genTone(double freqOfTone) {

		// clean out the arrays
		for (int i = 0; i < targetSamples * 2; ++i) {
			sample[i] = 0;
		}
		for (int i = 0; i < targetSamples * 2 * 2; ++i) {
			generatedSnd[i] = (byte) 0x0000;
		}

		// calculate adjustments to make the sample start and stop evenly
		numCycles = (int) (0.5 + freqOfTone * targetSamples / sampleRate);
		numSamples = (int) (0.5 + numCycles * sampleRate / freqOfTone);

		// fill out the array
		for (int i = 0; i < numSamples; ++i) {
			sample[i] = Math.sin(2 * Math.PI * i / (sampleRate / freqOfTone));
		}

		// convert to 16 bit pcm sound array
		// assumes the sample buffer is normalized.
		int idx = 0;
		for (double dVal : sample) {
			// scale loudness by frequency
			double amplitude = (double) (32767 * 5 / (Math.log(freqOfTone)));
			if (amplitude > 32767)
				amplitude = 32767;
			// scale signal to amplitude
			short val = (short) (dVal * amplitude);
			// in 16 bit wav PCM, first byte is the low order byte
			generatedSnd[idx++] = (byte) (val & 0x00ff);
			generatedSnd[idx++] = (byte) ((val & 0xff00) >>> 8);

		}

		// Log.d("DEBUG", Double.toString((32767/Math.log(freqOfTone))) );
	}


	void playSound() {

		final AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, sampleRate, AudioFormat.CHANNEL_CONFIGURATION_MONO,
				AudioFormat.ENCODING_PCM_16BIT, numSamples * 2, AudioTrack.MODE_STREAM);
		audioTrack.write(generatedSnd, 0, numSamples * 2);
		audioTrack.play();
		while (running == true) {
			audioTrack.write(generatedSnd, 0, numSamples * 2);
		}
		audioTrack.stop();
		running = false;
	}


	// functions to convert progress bar into time and frequency
	private double convertProgress_Hz(int progress) {
		double Hz = 440;
		// http://www.phy.mtu.edu/~suits/NoteFreqCalcs.html
		// Java was bad at powers math of non integers, so made a loop to do the
		// powers

		// A440 base pitch is adjusted down 5 octaves by multiplying by
		// 2^(-60/12) = 0.03125
		Hz = (427.5 + 0.125 * (float) mPitchBar.getProgress()) * 0.03125;
		// Raise the base pitch to the 2^n/12 power
		for (int m = 1; m < (progress); m++) {
			Hz = Hz * 1.0594630943593;  // 2^(1/12)
		}
		return Hz;
	}

	private void updateView() {
		mFreq.setText(Double.toString(convertProgress_Hz(mSineFreq)));
		mOctave.setText(Integer.toString((mSineFreq - 4) / 12));
		mNote.setText(notes[mSineFreq - 12 * ((mSineFreq) / 12)]);
		genTone(convertProgress_Hz(mSineFreq));
		if (mSineFreq < 37) {
			Toast.makeText(getApplicationContext(), "You can't hear < 100Hz on a tablet speaker", Toast.LENGTH_LONG).show();
		}
	}

}