package com.ecml;

import java.util.ArrayList;
import java.util.zip.CRC32;

import com.game.MicrophonePitchPoster;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;


public abstract class SpeedGameLvl extends Activity {


    protected enum KeyDisplay {
        DISPLAY_FLAT,
        DISPLAY_SHARP,
    }
    protected KeyDisplay keyDisplay = KeyDisplay.DISPLAY_SHARP;
    protected static final String noteNames[][] = {
        { "A", "Bb", "B", "C", "Db", "D", "Eb", "E", "F", "Gb", "G", "Ab" },
        { "A", "A#", "B", "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#" },
    };
	
	
	/*** MidiSheet variables ***/
	
	public static final String MidiTitleID = "MidiTitleID";
	
	protected SheetMusic sheet; /* The sheet music */
	protected LinearLayout layout; /* THe layout */
	protected Piano piano; /* The piano at the top */
	protected long midiCRC; /* CRC of the midi bytes */
	
	/*** End of MidiSheet variables ***/
	
	protected ArrayList<MidiTrack> Tracks;
	protected ArrayList<MidiNote> Notes;
	protected boolean search;
	View choice;
	View result;
	private View topLayout;
	
	
	protected MidiFile midifile; /* The midi file to play */
	protected MidiOptions options; /* The options for sheet music and sound */
	protected MidiPlayer player; /* The play/stop/rewind toolbar */
	public static int noteplace;
	protected MidiNote note;
	
	/*** PitchDetection variables ***/
	
	protected MicrophonePitchPoster pitchPoster;
	
	/*** End of PitchDetection variables ***/

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(android.R.style.Theme_Holo_Light);
		setContentView(R.layout.speedgamelvl1);
		ActionBar ab = getActionBar();
		ColorDrawable colorDrawable = new ColorDrawable(getResources().getColor(R.color.orange));
		ab.setBackgroundDrawable(colorDrawable);


		/*****************
		 * TOP VIEW WITH THE CHOICE OF NOTES AND THE HELP, BACK TO SCORE, CHANGE
		 * GAME BUTTON
		 **********/
		
		

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
		// If previous settings have been saved, use those

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

		
		player.mute();
		
		layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		choice = getLayoutInflater().inflate(R.layout.speedgamelvl1, layout, false);
		layout.addView(choice);
		setContentView(layout);

		// Back to the score button
		Button score = (Button) findViewById(R.id.back);
		score.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				
			}
		});

		// Help button
		Button help = (Button) findViewById(R.id.help);
		help.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
//				showHelpDialog();
			}
		});

		// Change game button
		Button game = (Button) findViewById(R.id.game);
		game.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), GameActivity.class);
				startActivity(intent);
			}
		});
		
		// Change stop button
		Button stop = (Button) findViewById(R.id.stop);
		stop.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
		        pitchPoster.stopSampling();
		        pitchPoster = null;
			}
		});
		
		result = getLayoutInflater().inflate(R.layout.reading_game_points, layout, false);
		layout.addView(result);
		result.setVisibility(View.GONE);
		setContentView(layout);
		
		createSheetMusic(options);
		
		/*sheet = new SheetMusic(this);
		sheet.init(midifile, options);
		sheet.setPlayer(player);*/
		/*layout.removeView(sheet);
		layout.addView(sheet);

		layout.requestLayout();
		sheet.callOnDraw();*/
		

	}

    
		
	/* Create the MidiPlayer and Piano views */
	void createView() {
		layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		player = new MidiPlayer(this);
		piano = new Piano(this);

		topLayout = getLayoutInflater().inflate(R.layout.main_top, layout, false);
		topLayout.setVisibility(View.GONE);
		layout.addView(topLayout);

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

		player.playAndRecordButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				//startAudioRecordingAndPlayingMusic();
			}
		});

		player.playRecordButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				//playAudio();
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
		piano.SetMidiFile(midifile, options, player);
		piano.SetShadeColors(options.shade1Color, options.shade2Color);
		player.SetMidiFile(midifile, options, sheet);
		layout.addView(sheet);
		layout.requestLayout();
		sheet.callOnDraw();
	}
	
	
}
