package com.game;

import java.util.ArrayList;
import java.util.zip.CRC32;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.ecml.ChooseSongActivity;
import com.ecml.ClefSymbol;
import com.ecml.ECML;
import com.ecml.FileUri;
import com.ecml.MidiFile;
import com.ecml.MidiFileException;
import com.ecml.MidiNote;
import com.ecml.MidiOptions;
import com.ecml.MidiPlayer;
import com.ecml.MidiTrack;
import com.ecml.Piano;
import com.ecml.R;
import com.ecml.SheetMusic;
import com.ecml.TimeSigSymbol;
import com.sideActivities.BaseActivity;

/* abstract class which define the main part of all the readinggame activities */

public abstract class ReadingGame extends BaseActivity {

	protected enum KeyDisplay {
		DISPLAY_FLAT, DISPLAY_SHARP,
	}

	protected KeyDisplay keyDisplay = KeyDisplay.DISPLAY_SHARP;
	protected static final String noteNames[][] = { { "A", "Bb", "B", "C", "Db", "D", "Eb", "E", "F", "Gb", "G", "Ab" },
			{ "A", "A#", "B", "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#" }, };

	protected boolean point = true;
	protected int counter = 0;
	protected int score = 0;

	public static int level = 1;

	/*** MidiSheet variables ***/

	public static final String MidiTitleID = "MidiTitleID";

	public static final int settingsRequestCode = 1;

	protected Thread playingthread;
	protected SheetMusic sheet; /* The sheet music */
	protected LinearLayout layout; /* THe layout */
	protected Piano piano; /* The piano at the top */
	protected long midiCRC; /* CRC of the midi bytes */

	/*** End of MidiSheet variables ***/

	protected ArrayList<MidiTrack> tracks;	/* The Tracks of the song */
	protected ArrayList<MidiNote> notes;	/* The Notes of the first Track (Track 0) */
	protected boolean search;
	static View choice;
	static View result;
	private View topLayout;

	protected MidiFile midifile; /* The midi file to play */
	protected MidiOptions options; /* The options for sheet music and sound */
	protected MidiPlayer player; /* The play/stop/rewind toolbar */
	public static int noteplace;
	protected MidiNote note;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choice);

		/*****************
		 * TOP VIEW WITH THE CHOICE OF NOTES AND THE HELP, BACK TO SCORE, CHANGE
		 * GAME BUTTON
		 **********/

		ClefSymbol.LoadImages(this);
		TimeSigSymbol.LoadImages(this);

		// Parse the MidiFile from the raw bytes
		Uri uri;
		String title;
		Log.i("level", "" + level);
		if (level == 1) {
			Log.i("levelboucle1", "" + level);
			uri = Uri.parse("file:///android_asset/Easy_Songs__Silent_Night.mid");
			title = this.getIntent().getStringExtra(MidiTitleID);
			if (title == null) {
				title = "Silent Night";
			}
		}
		else if (level == 2) {
			uri = Uri.parse("file:///android_asset/Bach__Invention_No._13.mid");
			title = this.getIntent().getStringExtra(MidiTitleID);
			if (title == null) {
				title = "Bach - Invention n�13";
			}
		}
		else {
			Log.i("levelboucle1", "" + level);
			uri = Uri.parse("file:///android_asset/Chopin__Nocturne_Op._9_No._1_in_B-flat_minor.mid");
			title = this.getIntent().getStringExtra(MidiTitleID);
			if (title == null) {
				title = "Chopin - Nocturn Op.9 N�1 in B flat minor";
			}
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
		options.tracks[0] = true;
		for (int i = 1; i < options.tracks.length; i++) {
			options.tracks[i] = false;
		}
		String json = settings.getString("" + midiCRC, null);
		MidiOptions savedOptions = MidiOptions.fromJson(json);
		if (savedOptions != null) {
			options.merge(savedOptions);
		}
		createView();

		player.mute();

		layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		choice = getLayoutInflater().inflate(R.layout.choice, layout, false);
		layout.addView(choice);
		setContentView(layout);

		// Back to the score button
		Button backToScore = (Button) findViewById(R.id.back);
		backToScore.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (ECML.song != null) {
					ECML.intent.putExtra(ChooseSongActivity.mode,"chooseSong");
					ChooseSongActivity.openFile(ECML.song);
				} else {
					ECML.intent = new Intent(getApplicationContext(), ChooseSongActivity.class);
					ECML.intent.putExtra(ChooseSongActivity.mode,"chooseSong");
					startActivity(ECML.intent);
				}
				finish();
			}
		});

		// Help button
		Button help = (Button) findViewById(R.id.help);
		help.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				showHelpDialog();
			}
			
		});

		// Change game button
		Button game = (Button) findViewById(R.id.game);
		game.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				score = 0;
				counter = 0;
				if (player != null) {
					player.Stop();
				}

				Intent intent = new Intent(getApplicationContext(), GameActivity.class);
				startActivity(intent);
				finish();
			}
		});

		result = getLayoutInflater().inflate(R.layout.reading_game_points, layout, false);
		layout.addView(result);
		result.setVisibility(View.GONE);
		setContentView(layout);

		createSheetMusic(options);


	}
	
	/** Make the result view appears*/
	public static void result() {
		choice.setVisibility(View.GONE);
		result.setVisibility(View.VISIBLE);
	}

	/** Create the MidiPlayer and Piano views */
	void createView() {
		layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		player = new MidiPlayer(this);
		piano = new Piano(this);

		topLayout = getLayoutInflater().inflate(R.layout.main_top, layout, false);
		topLayout.setVisibility(View.GONE);
		layout.addView(topLayout);

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
		sheet.init(midifile, options, false, 0, 2);
		if (level == 2) {sheet.init(midifile, options, true, 0, 45);}
		if (level == 3) {sheet.init(midifile, options, true, 0, 45);}
		sheet.setPlayer(player);
		layout.addView(sheet);
		piano.SetMidiFile(midifile, options, player);
		piano.SetShadeColors(options.shade1Color, options.shade2Color);
		player.SetMidiFile(midifile, options, sheet);
		layout.requestLayout();
		sheet.callOnDraw();

		sheet.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int action = event.getAction() & MotionEvent.ACTION_MASK;
				boolean result = sheet.getScrollAnimation().onTouchEvent(event);
				switch (action) {
				case MotionEvent.ACTION_DOWN:
					// If we touch while music is playing, stop the midi player
					if (player != null && player.playstate == player.playing) {
						player.Pause();
						sheet.getScrollAnimation().stopMotion();
					}
					return result;

				case MotionEvent.ACTION_MOVE:
					return result;

				case MotionEvent.ACTION_UP:
					return result;

				default:
					return false;
				}
			}
		});
	}

	protected void PauseEcoute() {
		point = false;
		player.Pause();
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
			player.unmute();
		}
		super.onPause();
	}

	private void showHelpDialog() {
		LayoutInflater inflator = LayoutInflater.from(this);
		final View dialogView = inflator.inflate(R.layout.help_reading_notes, null);

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("HELP");
		builder.setView(dialogView);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface builder, int whichButton) {

			}
		});
		AlertDialog dialog = builder.create();
		dialog.show();
	}

}
