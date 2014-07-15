package com.ecml;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class TuningForkActivity extends Activity {

	// sets up the objects that are on the screen
	private TextView mFreq;
	private ToggleButton mToggle;
	private SeekBar mSineFreqBar;
	private SeekBar mPitchBar;
	private TextView mTextPitch;
	private TextView mOctave;
	private TextView mNote;
	private TextView mN;
	private boolean running = false;

	// sets up menu items for the pop up menu
	static final private int BACK_ID = Menu.FIRST;
	static final private int INFO_ID = Menu.FIRST + 1;

	// variables for tone generation
	private final int sampleRate = 44000;
	private final int targetSamples = 5500;
	private int numSamples = 5500;   // calculated with respect to frequency later
	private int numCycles = 500;    // calculated with respect to frequency later

	// variable for accelorometer
	private SensorManager mSensorManager;
	private float mAccel; 		 // acceleration apart from gravity
	private float mAccelCurrent; // current acceleration including gravity
	private float mAccelLast; 	 // last acceleration including gravity


	// the array is made bigger than needed so they can be adjusted
	private double sample[] = new double[targetSamples * 2];
	private byte generatedSnd[] = new byte[2 * 2 * targetSamples];

	private String[] notes = { "G\u266F/ A\u266D", "A", "A\u266F/ B\u266D", "B", "C", "C\u266F/ D\u266D", "D", "D\u266F/ E\u266D", "E", "F", "F\u266F/ G\u266D", "G" };

	private long beeptime = 0;

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
		mToggle = (ToggleButton) findViewById(R.id.toggleButton1);
		mSineFreqBar = (SeekBar) findViewById(R.id.SineFreqBar);
		mPitchBar = (SeekBar) findViewById(R.id.PitchBar);
		mTextPitch = (TextView) findViewById(R.id.textViewFreq);
		mOctave = (TextView) findViewById(R.id.textOctave);
		mNote = (TextView) findViewById(R.id.textNote);
		mN = (TextView) findViewById(R.id.textViewN);
		mFreq = (TextView) findViewById(R.id.textViewHz);

		// Hook up button presses to the appropriate event handlers.
		((ToggleButton) findViewById(R.id.toggleButton1)).setOnClickListener(mToggleListener);
		((SeekBar) findViewById(R.id.SineFreqBar)).setOnSeekBarChangeListener(mSineFreqBarListener);
		((SeekBar) findViewById(R.id.PitchBar)).setOnSeekBarChangeListener(mSineFreqBarListener);

		// initialize the buttons to desired values
		mToggle.setChecked(false);
		mSineFreqBar.setMax(124);
		mSineFreqBar.setProgress(61);
		mPitchBar.setMax(200);
		mPitchBar.setProgress(100);

		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
		mAccel = 0.00f;
		mAccelCurrent = SensorManager.GRAVITY_EARTH;
		mAccelLast = SensorManager.GRAVITY_EARTH;


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
				genTone(convertProgress_Hz(mSineFreqBar.getProgress()));
				new BeepTask().execute();
			}

		}
	};


	/**
	 * A call-back for when the user moves the sine seek bars
	 */
	OnSeekBarChangeListener mSineFreqBarListener = new OnSeekBarChangeListener() {

		public void onStopTrackingTouch(SeekBar seekBar) {
			genTone(convertProgress_Hz(mSineFreqBar.getProgress()));
			if (mSineFreqBar.getProgress() < 37) {
				// makes a little message pop up
				Toast wmsg = Toast.makeText(getApplicationContext(), "you can't hear < 100Hz on a tablet speaker", Toast.LENGTH_LONG);
				wmsg.setGravity(Gravity.TOP, wmsg.getXOffset() / 2, wmsg.getYOffset() / 2);
				wmsg.setDuration(1);
				wmsg.show();
			}

		}

		public void onStartTrackingTouch(SeekBar seekBar) {


		}

		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
			if (mSineFreqBar.getProgress() < 1)
				mSineFreqBar.setProgress(1);

			mFreq.setText(Double.toString(convertProgress_Hz(mSineFreqBar.getProgress())));
			mTextPitch.setText(Double.toString(427.5 + 0.125 * (float) mPitchBar.getProgress()));
			// genTone(convertProgress_Hz(mSineFreqBar.getProgress()));
			mOctave.setText(Integer.toString((mSineFreqBar.getProgress() - 4) / 12));
			mNote.setText(notes[mSineFreqBar.getProgress() - 12 * ((mSineFreqBar.getProgress()) / 12)]);
			mN.setText(Integer.toString(mSineFreqBar.getProgress() - 52 - 9));


		}
	};


	private final SensorEventListener mSensorListener = new SensorEventListener() {

		public void onSensorChanged(SensorEvent se) {
			float x = se.values[0];
			float y = se.values[1];
			float z = se.values[2];
			mAccelLast = mAccelCurrent;
			mAccelCurrent = (float) Math.sqrt((double) (x * x + y * y + z * z));
			float delta = mAccelCurrent - mAccelLast;
			mAccel = mAccel * 0.9f + delta; // perform low-cut filter

			if (mAccel > 3) {

				// dont respond to shakes again for 1 sec
				if (SystemClock.elapsedRealtime() > beeptime + 1000) {
					if (running == false) {
						mToggle.setChecked(true);
						genTone(convertProgress_Hz(mSineFreqBar.getProgress()));
						new BeepTask().execute();
					} else {
						running = false;
						mToggle.setChecked(false);
					}
					beeptime = SystemClock.elapsedRealtime();
					// makes a little message pop up
					Toast smsg = Toast.makeText(getApplicationContext(), "shake", Toast.LENGTH_LONG);
					smsg.setGravity(Gravity.CENTER, smsg.getXOffset() / 2, smsg.getYOffset() / 2);
					smsg.setDuration(1);
					smsg.show();
				}
			}
		}

		public void onAccuracyChanged(Sensor sensor, int accuracy) {
		}
	};

	@Override
	protected void onResume() {
		super.onResume();
		mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	protected void onStop() {
		mSensorManager.unregisterListener(mSensorListener);
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

	/**
	 * Called when your activity's options menu needs to be created.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		// Items for Menu buttons
		menu.add(0, BACK_ID, 0, "back").setShortcut('0', 'b');
		menu.add(0, INFO_ID, 0, "info").setShortcut('1', 'i');

		return true;
	}

	/**
	 * Called right before your activity's option menu is displayed.
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);

		return true;
	}

	/**
	 * Called when a menu item is selected.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case BACK_ID:
			finish();
			return true;
		case INFO_ID:
			// makes a little message pop up
			Toast imsg = Toast
					.makeText(
							this,
							"Siliconfish Hobby Applications\nwww.workingsi.com\nThis app is totally freeware, no ads\nThis is a hobby project so be nice\nHey - it was free wasn't it?",
							Toast.LENGTH_LONG);
			imsg.setGravity(Gravity.CENTER, imsg.getXOffset() / 2, imsg.getYOffset() / 2);
			imsg.show();

			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	/**
	 * A call-back for when the user presses the back button.
	 */
	OnClickListener mBackListener = new OnClickListener() {
		public void onClick(View v) {
			finish();
		}
	};


}