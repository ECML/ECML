/*
 * Copyright (c) 2011-2012 Madhav Vaidyanathan
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License version 2.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 */

package com.ecml;

/***************************************************************************************************************/
/***************************************************************************************************************/
/***************************************************************************************************************/
/******************************************** IMPORTS FOR ADD-UPS **********************************************/
/***************************************************************************************************************/
/***************************************************************************************************************/
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.CRC32;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.CamcorderProfile;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.metronome.MetronomeController;

/***************************************************************************************************************/
/***************************************************************************************************************/
/**************************************** END OF IMPORTS FOR ADD-UPS *******************************************/
/***************************************************************************************************************/
/***************************************************************************************************************/
/***************************************************************************************************************/

/**
 * @class SheetMusicActivity
 * 
 *        The SheetMusicActivity is the main activity.<br>
 *        The main components are:<br>
 *        - MidiPlayer : The buttons and speed bar at the top.<br>
 *        - Piano : For highlighting the piano notes during playback.<br>
 *        - SheetMusic : For highlighting the sheet music notes during playback.
 * 
 */
public class SheetMusicActivity extends Activity implements SurfaceHolder.Callback, KeyListener {

	/*** MidiSheet variables ***/

	public static final String MidiTitleID = "MidiTitleID";
	public static final int settingsRequestCode = 1;

	private MidiPlayer player; /* The play/stop/rewind toolbar */
	private Piano piano; /* The piano at the top */
	private SheetMusic sheet; /* The sheet music */
	private LinearLayout layout; /* THe layout */
	private MidiFile midifile; /* The midi file to play */
	private MidiOptions options; /* The options for sheet music and sound */
	private long midiCRC; /* CRC of the midi bytes */

	/*** End of MidiSheet variables ***/

	/*************************************************************************************************************/
	/*************************************************************************************************************/
	/*************************************************************************************************************/
	/**************************************** VARIABLES FOR ADD-UPS **********************************************/
	/*************************************************************************************************************/
	/*************************************************************************************************************/

	/*** Audio Recording Variables ***/

	private long fileName;
	private String pathAudio;
	private String ext;

	private static final String AUDIO_RECORDER_FILE_EXT_3GP = ".3gp";
	private static final String AUDIO_RECORDER_FILE_EXT_MP4 = ".mp4";
	private static final String AUDIO_RECORDER_FOLDER = "AudioRecords";
	private MediaRecorder recorder = null;
	private int currentFormat = 0;
	private int output_formats[] = { MediaRecorder.OutputFormat.MPEG_4, MediaRecorder.OutputFormat.THREE_GPP };
	private String file_exts[] = { AUDIO_RECORDER_FILE_EXT_MP4, AUDIO_RECORDER_FILE_EXT_3GP };
	MediaPlayer mp = new MediaPlayer();
	private boolean isAudioRecording;
	private boolean existAudioRecord;

	/*** End of Audio Recording Variables ***/

	/*** Video Recording Variables ***/

	SurfaceView surfaceView;
	SurfaceHolder surfaceHolder;
	public MediaRecorder mrec;
	private Camera mCamera;
	private static final String VIDEO_RECORDER_FOLDER = "VideoRecords";
	private String pathVideo;
	private boolean isVideoRecording;
	private boolean existVideoRecord;
	boolean front = true;
	View topLayout;

	/*** End of Video Recording Variables ***/

	/*** File Variables ***/

	private static String sdcardPath = "sdcard/";
	private static String ECMLPath = "ECML/";
	private static final String MUSIC_SHEET_FOLDER = "MusicSheets";

	/*** End of File Variables ***/
	final Context context = this;

	/*** Metronome Variables ***/

	MetronomeController metronomeController;
	View metronomeView;
	View v2;

	/*** End of Metronome Variables ***/

	/**********************************************************************************************************/
	/**********************************************************************************************************/
	/************************************ END OF VARIABLES FOR ADD-UPS ****************************************/
	/**********************************************************************************************************/
	/**********************************************************************************************************/
	/**********************************************************************************************************/

	/**
	 * Create this SheetMusicActivity.<br>
	 * The Intent should have two parameters:<br>
	 * - data: The uri of the midi file to open.<br>
	 * - MidiTitleID: The title of thesong (String)<br>
	 */
	@Override
	public void onCreate(Bundle state) {
		super.onCreate(state);
		setTheme(android.R.style.Theme_Holo_Light);

		ClefSymbol.LoadImages(this);
		TimeSigSymbol.LoadImages(this);
		MidiPlayer.LoadImages(this);

		// Parse the MidiFile from the raw bytes
		Uri uri = this.getIntent().getData();
		String title = this.getIntent().getStringExtra(MidiTitleID);
		if (title == null) {
			title = uri.getLastPathSegment();
		}
		FileUri file = new FileUri(uri, title);
		this.setTitle("ECML: " + title);
		byte[] data;
		try {
			data = file.getData(this);
			midifile = new MidiFile(data, title);
		} catch (MidiFileException e) {
			this.finish();
			return;
		}

		// Initialize the settings (MidiOptions).
		// If previous settings have been saved, used those

		options = new MidiOptions(midifile);

		CRC32 crc = new CRC32();
		crc.update(data);
		midiCRC = crc.getValue();
		SharedPreferences settings = getPreferences(0);
		options.scrollVert = settings.getBoolean("scrollVert", true);
		options.shade1Color = settings.getInt("shade1Color", options.shade1Color);
		options.shade2Color = settings.getInt("shade2Color", options.shade2Color);
		options.showPiano = settings.getBoolean("showPiano", true);
		String json = settings.getString("" + midiCRC, null);
		MidiOptions savedOptions = MidiOptions.fromJson(json);
		if (savedOptions != null) {
			options.merge(savedOptions);
		}
		createView();
		createSheetMusic(options);

		metronomeController = new MetronomeController(this);
		updateTempoView();
		setSliderListener();

		ActionBar ab = getActionBar();
		ColorDrawable colorDrawable = new ColorDrawable(getResources().getColor(R.color.orange));
		ab.setBackgroundDrawable(colorDrawable);

		/**********************************************************************************************************/
		/**********************************************************************************************************/
		/**********************************************************************************************************/
		/********************************************* BUTTONS ****************************************************/
		/**********************************************************************************************************/
		/**********************************************************************************************************/

		// Create the library folder if it doesn't exist
		File file_library = new File(sdcardPath + ECMLPath);
		if (!file_library.exists()) {
			if (!file_library.mkdirs()) {
				Log.e("TravellerLog :: ", "Problem creating the Library");
			}
		}

		// Create the folder containing the music sheets ( in the library)
		File musicSheets = new File(sdcardPath + ECMLPath.concat(MUSIC_SHEET_FOLDER));
		if (!musicSheets.exists()) {
			if (!musicSheets.mkdirs()) {
				Log.e("TravellerLog :: ", "Problem creating the Music sheets folder");
			}
		}

		// Create the folder containing the records ( in the library)
		File records = new File(sdcardPath + ECMLPath.concat(AUDIO_RECORDER_FOLDER));
		if (!records.exists()) {
			if (!records.mkdirs()) {
				Log.e("TravellerLog :: ", "Problem creating the Audio records folder");
			}
		}

		// create the folder containing the video records
		File videorecords = new File(sdcardPath + ECMLPath.concat(VIDEO_RECORDER_FOLDER));
		if (!videorecords.exists()) {
			if (!videorecords.mkdirs()) {
				Log.e("TravellerLog :: ", "Problem creating the Video records folder");
			}
		}

		isAudioRecording = false;
		isVideoRecording = false;

		surfaceView = (SurfaceView) findViewById(R.id.surface_camera);
		surfaceHolder = surfaceView.getHolder();
		surfaceHolder.addCallback(this);
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

		/*** End of side activities ***/

		/**********************************************************************************************************/
		/**********************************************************************************************************/
		/********************************************* END OF BUTTONS *********************************************/
		/**********************************************************************************************************/
		/**********************************************************************************************************/
		/**********************************************************************************************************/

	} // END ONCREATE

	/* Create the MidiPlayer and Piano views */
	void createView() {
		layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		player = new MidiPlayer(this);
		piano = new Piano(this);

		topLayout = getLayoutInflater().inflate(R.layout.main_top, layout, false);
		metronomeView = getLayoutInflater().inflate(R.layout.metronome, layout, false);

		topLayout.setVisibility(View.GONE);
		metronomeView.setVisibility(View.GONE);

		layout.addView(topLayout);
		layout.addView(metronomeView);

		player.pianoButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				options.showPiano = !options.showPiano;
				player.SetPiano(piano, options);
				SharedPreferences.Editor editor = getPreferences(0).edit();
				editor.putBoolean("showPiano", options.showPiano);
				String json = options.toJson();
				if (json != null) {
					editor.putString("" + midiCRC, json);
				}
				editor.commit();
			}
		});

		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.x;
		int height = size.y;

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
		params.gravity = Gravity.CENTER_HORIZONTAL;

		layout.addView(piano, params);
		layout.addView(player);
		setContentView(layout);
		getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.orange));
		player.SetPiano(piano, options);
		layout.requestLayout();
	}

	/** Create the SheetMusic view with the given options */
	private void createSheetMusic(MidiOptions options) {
		if (sheet != null) {
			layout.removeView(sheet);
		}
		if (!options.showPiano) {
			piano.setVisibility(View.GONE);
		} else {
			piano.setVisibility(View.VISIBLE);
		}
		sheet = new SheetMusic(this);
		sheet.init(midifile, options);
		sheet.setPlayer(player);
		layout.addView(sheet);
		piano.SetMidiFile(midifile, options, player);
		piano.SetShadeColors(options.shade1Color, options.shade2Color);
		player.SetMidiFile(midifile, options, sheet);
		layout.requestLayout();
		sheet.callOnDraw();
	}

	/** Always display this activity in landscape mode. */
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	/** When the menu button is pressed, initialize the menus. */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		/***********************************************************************************************************/
		/***********************************************************************************************************/
		/***********************************************************************************************************/
		/********************************************* ACTION BAR **************************************************/
		/***********************************************************************************************************/
		/***********************************************************************************************************/

		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.actionbar, menu);

		/******************************* VIDEO RECORDING ACTION VIEW ************************************/
		/** Get the action view of the menu item whose id is video */
		View v = (View) menu.findItem(R.id.video).getActionView();

		/** Get the edit text from the action view */
		ImageView stopVideoRecording = (ImageView) v.findViewById(R.id.recordstopbtn);
		ImageView startVideoRecording = (ImageView) v.findViewById(R.id.recordbtn);
		ImageView replayVideoRecording = (ImageView) v.findViewById(R.id.recordplaybtn);
		ImageView switchCamera = (ImageView) v.findViewById(R.id.switchcamerabtn);

		startVideoRecording.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				surfaceView.setVisibility(View.VISIBLE);
				if (!isVideoRecording && !isAudioRecording) {
					if (front == true) {
						mCamera = openFrontFacingCamera();
					} else {
						mCamera = Camera.open();
					}
					try {
						startVideoRecording();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					Toast.makeText(context, "Stop Recording first", Toast.LENGTH_SHORT).show();
				}

			}
		});

		stopVideoRecording.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if (isVideoRecording) {
					stopVideoRecording();
					surfaceView.setVisibility(View.GONE);
				} else {
					Toast.makeText(context, "Not Recording", Toast.LENGTH_SHORT).show();
				}
				topLayout.setVisibility(View.GONE);
			}
		});

		replayVideoRecording.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!isVideoRecording && !isAudioRecording && existVideoRecord) {
					replayVideoRecording();
				} else {
					Toast.makeText(context, "No Recent Video Record", Toast.LENGTH_SHORT).show();
				}

			}
		});

		switchCamera.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				front = !front;
				surfaceView.setVisibility(View.VISIBLE);

			}
		});
		/******************************* END OF VIDEO RECORDING ACTION VIEW **********************************/

		/******************************* AUDIO RECORDING ACTION VIEW **********************************/
		/** Get the action view of the menu item whose id is video */
		View v3 = (View) menu.findItem(R.id.audio).getActionView();

		ImageView stopAudioRecording = (ImageView) v3.findViewById(R.id.recordaudiostopbtn);
		ImageView startAudioRecording = (ImageView) v3.findViewById(R.id.recordaudiobtn);
		ImageView replayAudioRecording = (ImageView) v3.findViewById(R.id.recordaudioplaybtn);

		startAudioRecording.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!isVideoRecording && !isAudioRecording) {
					startAudioRecording();
				} else {
					Toast.makeText(context, "Stop Recording first", Toast.LENGTH_SHORT).show();
				}

			}
		});

		stopAudioRecording.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isAudioRecording) {
					stopAudioRecording();
				} else {
					Toast.makeText(context, "Not Recording", Toast.LENGTH_SHORT).show();
				}

			}
		});

		replayAudioRecording.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!isVideoRecording && !isAudioRecording && existAudioRecord) {
					String filename = fileName + ext;
					playAudio(pathAudio, filename, mp);
				} else {
					Toast.makeText(context, "Not Recent Audio Record", Toast.LENGTH_SHORT).show();
				}

			}
		});
		/******************************* END OF AUDIO RECORDING ACTION VIEW **********************************/

		/******************************* METRONOME ACTION VIEW **********************************/
		/** Get the action view of the menu item whose id is video */
		View v2 = (View) menu.findItem(R.id.metronome).getActionView();

		TextView tempoView = ((TextView) v2.findViewById(R.id.tempo));
		tempoView.setText("Tempo : " + metronomeController.getTempo() + "");

		SeekBar slider = (SeekBar) v2.findViewById(R.id.slider);
		slider.setMax(200);
		slider.setProgress(metronomeController.getTempo());
		slider.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				metronomeController.startMetronome();
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				metronomeController.stopMetronome();
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

				metronomeController.setTempo(progress);
				updateTempoView();
			}
		});

		TextView startMetronome = (TextView) v2.findViewById(R.id.startmetronome);
		TextView stopMetronome = (TextView) v2.findViewById(R.id.stopmetronome);

		startMetronome.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				metronomeController.startMetronome();
			}
		});

		stopMetronome.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				metronomeController.stopMetronome();
			}
		});
		/*******************************
		 * END OF METRONOME ACTION VIEW **********************************
		 * 
		 * /
		 ***********************************************************************************************************/
		/***********************************************************************************************************/
		/***************************************** END OF ACTION BAR ***********************************************/
		/***********************************************************************************************************/
		/***********************************************************************************************************/
		/***********************************************************************************************************/

		if (player != null) {
			player.Pause();
		}

		return true;
	}

	/**
	 * Callback when a menu item is selected.<br>
	 * - Choose Song : Choose a new song<br>
	 * - Song Settings : Adjust the sheet music and sound options<br>
	 * - Save As Images: Save the sheet music as PNG images<br>
	 * - Help : Display the HTML help screen<br>
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {		
		case R.id.song_settings:
			changeSettings();
			return true;
		case R.id.save_images:
			showSaveImagesDialog();
			return true;
		case R.id.help:
			showHelp();
			return true;
		case R.id.youtube:
			showYoutube();
			return true;
		case R.id.video:
			if (surfaceView.getVisibility() != View.VISIBLE) {
				surfaceView.setVisibility(View.VISIBLE);
			}
			topLayout.setVisibility(View.VISIBLE);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}


	/**
	 * To change the sheet music options, start the SettingsActivity. Pass the
	 * current MidiOptions as a parameter to the Intent. Also pass the 'default'
	 * MidiOptions as a parameter to the Intent. When the SettingsActivity has
	 * finished, the onActivityResult() method will be called.
	 */
	private void changeSettings() {
		MidiOptions defaultOptions = new MidiOptions(midifile);
		Intent intent = new Intent(this, SettingsActivity.class);
		intent.putExtra(SettingsActivity.settingsID, options);
		intent.putExtra(SettingsActivity.defaultSettingsID, defaultOptions);
		startActivityForResult(intent, settingsRequestCode);
	}

	/* Show the "Save As Images" dialog */
	private void showSaveImagesDialog() {
		LayoutInflater inflator = LayoutInflater.from(this);
		final View dialogView = inflator.inflate(R.layout.save_images_dialog, null);
		final EditText filenameView = (EditText) dialogView.findViewById(R.id.save_images_filename);
		filenameView.setText(midifile.getFileName().replace("_", " "));
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.save_images_str);
		builder.setView(dialogView);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface builder, int whichButton) {
				saveAsImages(filenameView.getText().toString());
			}
		});
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface builder, int whichButton) {
			}
		});
		AlertDialog dialog = builder.create();
		dialog.show();
	}

	/* Save the current sheet music as PNG images. */
	private void saveAsImages(String name) {
		String filename = name;
		boolean scrollVert = options.scrollVert;
		if (!options.scrollVert) {
			options.scrollVert = true;
			createSheetMusic(options);
		}
		try {
			int numpages = sheet.GetTotalPages();
			for (int page = 1; page <= numpages; page++) {
				Bitmap image = Bitmap.createBitmap(SheetMusic.PageWidth + 40, SheetMusic.PageHeight + 40, Bitmap.Config.ARGB_8888);
				Canvas imageCanvas = new Canvas(image);
				sheet.DrawPage(imageCanvas, page);
				File path = Environment.getExternalStoragePublicDirectory(ECMLPath + MUSIC_SHEET_FOLDER);
				File file;
				if (numpages > 1) {
					file = new File(path, "" + filename + " -  page " + page + ".png");
				} else {
					file = new File(path, "" + filename + ".png");
				}
				path.mkdirs();
				OutputStream stream = new FileOutputStream(file);
				image.compress(Bitmap.CompressFormat.PNG, 0, stream);
				image = null;
				stream.close();

				// Inform the media scanner about the file
				MediaScannerConnection.scanFile(this, new String[] { file.toString() }, null, null);
			}
		} catch (IOException e) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Error saving image to file " + ECMLPath + MUSIC_SHEET_FOLDER + filename + ".png");
			builder.setCancelable(false);
			builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
				}
			});
			AlertDialog alert = builder.create();
			alert.show();
		} catch (NullPointerException e) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Ran out of memory while saving image to file " + ECMLPath + MUSIC_SHEET_FOLDER + filename + ".png");
			builder.setCancelable(false);
			builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
				}
			});
			AlertDialog alert = builder.create();
			alert.show();
		}
		options.scrollVert = scrollVert;
		createSheetMusic(options);
	}

	/** Show the HTML help screen. */
	private void showHelp() {
		Intent intent = new Intent(this, HelpActivity.class);
		startActivity(intent);
	}

	/** Change blanks and ":" to "+" in a String */
	private String spaceToPlus(String title) {
		String newTitle = "";
		boolean last = false; // tell whether last char is a '+' or not
		for (int i = 0; i < title.length(); i++) {
			if ((title.charAt(i) == ' ' || title.charAt(i) == ':') && !last) {
				newTitle = newTitle + "+";
				last = true;
			} else if (title.charAt(i) == ' ' || title.charAt(i) == ':') {

			} else {
				newTitle = newTitle + title.charAt(i);
				last = false;
			}
		}
		return newTitle;
	}

	/** Get the current instrument for track 0 */
	public String instrumentYoutube() {
		String instrument = "";
		if (MidiOptions.instruments[0] == 0 || MidiOptions.instruments[0] == 1 || MidiOptions.instruments[0] == 2 || MidiOptions.instruments[0] == 3
				|| MidiOptions.instruments[0] == 4 || MidiOptions.instruments[0] == 5 || MidiOptions.instruments[0] == 6) {
			instrument = "piano";
		} else if (MidiOptions.instruments[0] == 25 || MidiOptions.instruments[0] == 26 || MidiOptions.instruments[0] == 27
				|| MidiOptions.instruments[0] == 28 || MidiOptions.instruments[0] == 29 || MidiOptions.instruments[0] == 30
				|| MidiOptions.instruments[0] == 31 || MidiOptions.instruments[0] == 32) {
			instrument = "guitar";
		}

		else if (MidiOptions.instruments[0] == 33 || MidiOptions.instruments[0] == 34 || MidiOptions.instruments[0] == 35
				|| MidiOptions.instruments[0] == 36) {
			instrument = "bass";
		} else {
			instrument = MidiFile.Instruments[MidiOptions.instruments[0]];
		}
		return instrument;
	}

	/** Launch Youtube on Navigator and search for the current song */
	private void showYoutube() {
		String songTitle = this.getIntent().getStringExtra(MidiTitleID);
		Intent myWebLink = new Intent(android.content.Intent.ACTION_VIEW);
		String instrument = instrumentYoutube();
		myWebLink.setData(Uri.parse("http://www.youtube.com/results?search_query=" + spaceToPlus(songTitle + " " + instrument)));
		startActivity(myWebLink);
	}


	/**
	 * This is the callback when the SettingsActivity is finished. Get the
	 * modified MidiOptions (passed as a parameter in the Intent). Save the
	 * MidiOptions. The key is the CRC checksum of the midi data, and the value
	 * is a JSON dump of the MidiOptions. Finally, re-create the SheetMusic View
	 * with the new options.
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode != settingsRequestCode) {
			return;
		}
		options = (MidiOptions) intent.getSerializableExtra(SettingsActivity.settingsID);

		// Check whether the default instruments have changed.
		for (int i = 0; i < options.instruments.length; i++) {
			if (options.instruments[i] != midifile.getTracks().get(i).getInstrument()) {
				options.useDefaultInstruments = false;
			}
		}
		// Save the options.
		SharedPreferences.Editor editor = getPreferences(0).edit();
		editor.putBoolean("scrollVert", options.scrollVert);
		editor.putInt("shade1Color", options.shade1Color);
		editor.putInt("shade2Color", options.shade2Color);
		editor.putBoolean("showPiano", options.showPiano);
		String json = options.toJson();
		if (json != null) {
			editor.putString("" + midiCRC, json);
		}
		editor.commit();

		// Recreate the sheet music with the new options
		createSheetMusic(options);
	}

	/** When this activity resumes, redraw all the views */
	@Override
	protected void onResume() {
		super.onResume();
		layout.requestLayout();
		player.invalidate();
		piano.invalidate();
		if (sheet != null) {
			sheet.invalidate();
		}
		layout.requestLayout();

	}

	/** When this activity pauses, stop the music */
	@Override
	protected void onPause() {
		if (player != null) {
			player.Pause();
		}
		super.onPause();
		mp.stop();
		metronomeController.stopMetronome();

	}

	/**********************************************************************************************************/
	/**********************************************************************************************************/
	/**********************************************************************************************************/
	/************************************** FUNCTIONS FOR ADD-UPS *********************************************/
	/**********************************************************************************************************/
	/**********************************************************************************************************/

	/*** Audio Recording functions ***/

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

	private void playAudio(String path, String fileName, MediaPlayer mp) {
		try {
			mp.setDataSource(path + "/" + fileName);
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
		if (!mp.isPlaying()) {
			Toast.makeText(SheetMusicActivity.this, "Play Last Audio Record", Toast.LENGTH_SHORT).show();
			mp.start();
		}
	}

	private MediaRecorder.OnErrorListener errorListener = new MediaRecorder.OnErrorListener() {
		@Override
		public void onError(MediaRecorder mr, int what, int extra) {
			Toast.makeText(SheetMusicActivity.this, "Error: " + what + ", " + extra, Toast.LENGTH_SHORT).show();
		}
	};

	private MediaRecorder.OnInfoListener infoListener = new MediaRecorder.OnInfoListener() {
		@Override
		public void onInfo(MediaRecorder mr, int what, int extra) {
			Toast.makeText(SheetMusicActivity.this, "Warning: " + what + ", " + extra, Toast.LENGTH_SHORT).show();
		}
	};

	/*** End of Audio Recording Functions ***/

	/*** Video Recording Functions ***/

	protected void startVideoRecording() throws IOException {
		mrec = new MediaRecorder(); // Works well
		mCamera.stopPreview();
		mCamera.unlock();
		mrec.setCamera(mCamera);

		mrec.setPreviewDisplay(surfaceHolder.getSurface());
		mrec.setVideoSource(MediaRecorder.VideoSource.CAMERA);
		mrec.setAudioSource(MediaRecorder.AudioSource.MIC);

		if (front == true) {
			mrec.setProfile(CamcorderProfile.get(Camera.CameraInfo.CAMERA_FACING_FRONT, CamcorderProfile.QUALITY_HIGH));
		} else {
			mrec.setProfile(CamcorderProfile.get(Camera.CameraInfo.CAMERA_FACING_BACK, CamcorderProfile.QUALITY_HIGH));
		}

		mrec.setOutputFile(getFilenameVideo());
		mrec.setVideoFrameRate(10);

		mrec.prepare();
		isVideoRecording = true;
		mrec.start();
	}

	protected void stopVideoRecording() {
		isVideoRecording = false;
		existVideoRecord = true;
		mrec.stop();
		releaseMediaRecorder();
		releaseCamera();
	}

	private void releaseMediaRecorder() {
		if (mrec != null) {
			mrec.reset(); // clear recorder configuration
			mrec.release(); // release the recorder object
			mrec = null;
			mCamera.lock(); // lock camera for later use
		}
	}

	private void releaseCamera() {
		if (mCamera != null) {
			mCamera.release(); // release the camera for other applications
			if (front == true) {
				mCamera = openFrontFacingCamera();
				mCamera.release();
			} else {
				mCamera = Camera.open();
				mCamera.release();
			}

		}
	}

	private void replayVideoRecording() {
		String filename = fileName + ext;
		String lastvideo = pathVideo + "/" + filename;
		Intent intentToPlayVideo = new Intent(Intent.ACTION_VIEW);
		intentToPlayVideo.setDataAndType(Uri.parse(lastvideo), "video/*");
		startActivity(intentToPlayVideo);
		this.finish();
	}

	private String getFilenameVideo() {
		String filepath = Environment.getExternalStorageDirectory().getPath();
		File file = new File(filepath, ECMLPath + VIDEO_RECORDER_FOLDER);
		if (!file.exists()) {
			file.mkdirs();
		}
		fileName = System.currentTimeMillis();
		pathVideo = file.getAbsolutePath();
		ext = file_exts[currentFormat];
		return (pathVideo + "/" + fileName + ext);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
	}

	private Camera openFrontFacingCamera() {
		int cameraCount = 0;
		Camera cam = null;
		Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
		cameraCount = Camera.getNumberOfCameras();
		for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
			Camera.getCameraInfo(camIdx, cameraInfo);
			if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
				try {
					cam = Camera.open(camIdx);
				} catch (RuntimeException e) {

				}
			}
		}

		return cam;

	}

	/*** End of Video Recording Functions ***/

	/*** Mute Button ***/

	@Override
	public int getInputType() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		int action = event.getAction();
		switch (keyCode) {
		case KeyEvent.KEYCODE_VOLUME_UP:
			if (action == KeyEvent.ACTION_UP) {
				if (player.audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) != 0) {
					player.volume = player.audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
				}
				if (player.mute && player.audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) != 0) {
					player.unmute();
				}
			}
			return true;
		case KeyEvent.KEYCODE_VOLUME_DOWN:
			if (action == KeyEvent.ACTION_UP) {
				// Volume down key detected
				if (player.audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) != 0) {
					player.volume = player.audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
				}
				if (!player.mute && player.audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) == 0) {
					player.mute();
				}
				return true;
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean onKeyOther(View view, Editable text, KeyEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void clearMetaKeyState(View view, Editable content, int states) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		int action = event.getAction();
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (action == KeyEvent.ACTION_DOWN) {
				if (!isAudioRecording && !isVideoRecording) {
					this.finish();
					return true;
				} else {
					Toast.makeText(SheetMusicActivity.this, "Stop recording before exiting", Toast.LENGTH_SHORT).show();
				}
			}
		}
		return false;
	}

	@Override
	public boolean onKeyDown(View view, Editable text, int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onKeyUp(View view, Editable text, int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

	/*** End of Mute Button Functions ***/

	/*** Metronome Functions ***/

	private void updateTempoView() {
		TextView tempoView = ((TextView) findViewById(R.id.tempo));
		tempoView.setText("Tempo : " + metronomeController.getTempo() + "");
	}

	public void start(View view) {
		metronomeController.startMetronome();
	}

	public void stop(View view) {
		metronomeController.stopMetronome();
	}

	public void updateTempo(View view) {
		SeekBar slider = (SeekBar) findViewById(R.id.slider);
		int newTempo = slider.getProgress();
		metronomeController.setTempo(newTempo);
		updateTempoView();
	}

	private void setSliderListener() {
		SeekBar slider = (SeekBar) findViewById(R.id.slider);
		slider.setMax(200);
		slider.setProgress(metronomeController.getTempo());
		slider.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				metronomeController.startMetronome();
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				metronomeController.stopMetronome();
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

				metronomeController.setTempo(progress);
				updateTempoView();
			}
		});
	}

	/*** End of Metronome Functions ***/

	/**********************************************************************************************************/
	/**********************************************************************************************************/
	/************************************* END OF FUNCTIONS FOR ADD-UPS ***************************************/
	/**********************************************************************************************************/
	/**********************************************************************************************************/
	/**********************************************************************************************************/

} // END !!

