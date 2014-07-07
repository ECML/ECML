package com.ecml;

import java.io.File;
import java.io.IOException;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class AudioRecordingActivity extends Activity {

	private long fileName;
	private String pathAudio;
	private String ext;

	private static final String AUDIO_RECORDER_FILE_EXT_3GP = ".3gp";
	private static final String AUDIO_RECORDER_FILE_EXT_MP4 = ".mp4";
	private static final String AUDIO_RECORDER_FOLDER = "AudioRecords";
	private MediaRecorder recorder = null;
	private int currentFormat = 0;
	private int output_formats[] = { MediaRecorder.OutputFormat.MPEG_4,
			MediaRecorder.OutputFormat.THREE_GPP };
	private String file_exts[] = { AUDIO_RECORDER_FILE_EXT_MP4,
			AUDIO_RECORDER_FILE_EXT_3GP };
	MediaPlayer mp = new MediaPlayer();
	private boolean isAudioRecording;
	private boolean existAudioRecord;

	private static String ECMLPath = "ECML/";

	final Context context = this;

	private boolean audioPaused;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(android.R.style.Theme_Holo_Light);
		setContentView(R.layout.audiorecording);


		ActionBar ab = getActionBar();
		ColorDrawable colorDrawable = new ColorDrawable(getResources().getColor(R.color.orange));
		ab.setBackgroundDrawable(colorDrawable);


		// Start audio recording button
		TextView startAudioRecording = (TextView) findViewById(R.id.startAudioRecording);
		startAudioRecording.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!isAudioRecording) {
					Toast.makeText(context, "Start audio recording",
							Toast.LENGTH_SHORT).show();
					startAudioRecording();
				} else {
					Toast.makeText(context, "Stop Recording first",
							Toast.LENGTH_SHORT).show();
				}

			}
		});

		// Stop audio recording button
		TextView stopAudioRecording = (TextView) findViewById(R.id.stopAudioRecording);
		stopAudioRecording.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isAudioRecording) {
					Toast.makeText(context, "Stop audio recording",
							Toast.LENGTH_SHORT).show();
					stopAudioRecording();
				} else {
					Toast.makeText(context, "Not Recording", Toast.LENGTH_SHORT)
							.show();
				}

			}
		});

		// Play last record
		TextView replayAudioRecording = (TextView) findViewById(R.id.replayAudioRecording);
		replayAudioRecording.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!isAudioRecording && existAudioRecord) {
					Toast.makeText(context, "Playing last record",
							Toast.LENGTH_SHORT).show();
					String filename = fileName + ext;
					playAudio();
				} else {
					Toast.makeText(context, "Not Recent Audio Record",
							Toast.LENGTH_SHORT).show();
				}

			}
		});


		// Pause replay
		TextView pauseReplay = (TextView) findViewById(R.id.pauseReplay);
		pauseReplay.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				pauseAudio();

			}
		});

	}

	private String getFilenameAudio() {
		String filepath = Environment.getExternalStorageDirectory().getPath();
		File file = new File(filepath, ECMLPath + AUDIO_RECORDER_FOLDER);
		if (!file.exists()) {
			file.mkdirs();
		}
		fileName = System.currentTimeMillis();
		pathAudio = file.getAbsolutePath();
		ext = file_exts[currentFormat];
		return (pathAudio + "/" + fileName + ext);
	}

	private void startAudioRecording() {
		recorder = new MediaRecorder();
		recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		recorder.setOutputFormat(output_formats[currentFormat]);
		recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		recorder.setOutputFile(getFilenameAudio());
		recorder.setOnErrorListener(errorListener);
		recorder.setOnInfoListener(infoListener);
		try {
			recorder.prepare();
			recorder.start();
			isAudioRecording = true;
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void stopAudioRecording() {
		isAudioRecording = false;
		if (null != recorder) {
			recorder.stop();
			recorder.reset();
			recorder.release();
			recorder = null;
			existAudioRecord = true;
		}
	}

	private void playAudio() {
		if (!mp.isPlaying()) {
			if (audioPaused == false) {
				mp.reset();
				try {
					mp.setDataSource(pathAudio + "/" + fileName + ext);
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					mp.prepare();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			audioPaused = false;
			Toast.makeText(AudioRecordingActivity.this,
					"Play Last Audio Record", Toast.LENGTH_SHORT).show();
			mp.start();
		}
	}

	private void pauseAudio() {
		if (mp.isPlaying()) {
			mp.pause();
			audioPaused = true;
			Toast.makeText(AudioRecordingActivity.this,
					"Last Audio Record Paused", Toast.LENGTH_SHORT).show();
		}
	}

	private MediaRecorder.OnErrorListener errorListener = new MediaRecorder.OnErrorListener() {
		@Override
		public void onError(MediaRecorder mr, int what, int extra) {
			Toast.makeText(AudioRecordingActivity.this,
					"Error: " + what + ", " + extra, Toast.LENGTH_SHORT).show();
		}
	};

	private MediaRecorder.OnInfoListener infoListener = new MediaRecorder.OnInfoListener() {
		@Override
		public void onInfo(MediaRecorder mr, int what, int extra) {
			Toast.makeText(AudioRecordingActivity.this,
					"Warning: " + what + ", " + extra, Toast.LENGTH_SHORT)
					.show();
		}
	};

}
