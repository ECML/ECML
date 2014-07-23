package com.ecml;

import java.util.ArrayList;
import java.util.zip.CRC32;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class ReadingGameBeginner extends Activity {

	private LinearLayout layout;
	View choice;
	View result;
	private SheetMusic sheet;
	private MidiFile midifile; /* The midi file to play */
	private MidiOptions options; /* The options for sheet music and sound */
	private MidiPlayer player; /* The play/stop/rewind toolbar */
	public static int noteplace;
	private MidiNote note;
	public static final String MidiTitleID = "MidiTitleID";
	

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(android.R.style.Theme_Holo_Light);

		ActionBar ab = getActionBar();
		ColorDrawable colorDrawable = new ColorDrawable(getResources().getColor(R.color.orange));
		ab.setBackgroundDrawable(colorDrawable);

		noteplace = 0;

		/*****************
		 * TOP VIEW WITH THE CHOICE OF NOTES AND THE HELP, BACK TO SCORE, CHANGE
		 * GAME BUTTON
		 **********/
		layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		choice = getLayoutInflater().inflate(R.layout.choice, layout, false);
		layout.addView(choice);
		setContentView(layout);

		// Back to the score button
		Button score = (Button) findViewById(R.id.back);
		score.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (ECML.song != null) {
					ChooseSongActivity.openFile(ECML.song);
				}
				else {
					Intent intent = new Intent(getApplicationContext(), ChooseSongActivity.class);
					intent.putExtra(ChooseSongActivity.niveau,"chooseSong");
					startActivity(intent);
				}
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
				Intent intent = new Intent(getApplicationContext(), GameActivity.class);
				startActivity(intent);
			}
		});

		// La button
		final Button la = (Button) findViewById(R.id.la);
		la.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isItALa()) {
					noteplace = noteplace + 1;
					la.setBackgroundColor(Color.GREEN);
				}
				la.setBackgroundColor(Color.RED);
			}
		});

		// si button
		Button si = (Button) findViewById(R.id.si);
		si.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				noteplace = noteplace + 1;
			}
		});

		// Do button
		Button donote = (Button) findViewById(R.id.donote);
		donote.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				noteplace = noteplace + 1;
			}
		});

		// Ré button
		Button re = (Button) findViewById(R.id.re);
		re.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				noteplace = noteplace + 1;
			}
		});

		// Mi button
		Button mi = (Button) findViewById(R.id.mi);
		mi.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				noteplace = noteplace + 1;
			}
		});

		// Fa button
		Button fa = (Button) findViewById(R.id.fa);
		fa.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				noteplace = noteplace + 1;
			}
		});

		// Sol button
		Button sol = (Button) findViewById(R.id.sol);
		sol.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				noteplace = noteplace + 1;
			}
		});

		/*****************
		 * END OF TOP VIEW WITH THE CHOICE OF NOTES AND THE HELP, BACK TO SCORE,
		 * CHANGE GAME BUTTON
		 **********/

		/********************* TOP VIEW WITH RESULTS AND APPRECIATION ***********************/
		result = getLayoutInflater().inflate(R.layout.reading_game_points, layout, false);
		layout.addView(result);
		result.setVisibility(View.GONE);
		setContentView(layout);

		// Retry button
		Button retry = (Button) findViewById(R.id.retry);
		retry.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), ReadingGameBeginner.class);
				startActivity(intent);
			}
		});
		/********************* END OF TOP VIEW WITH RESULTS AND APPRECIATION *****************/

		/*********************** BOTTOM VIEW WITH THE SHEET MUSIC ****************************/

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
		
		options = new MidiOptions(midifile);

		sheet = new SheetMusic(this);
		sheet.init(midifile, options);
		sheet.setPlayer(player);
		layout.addView(sheet);

		layout.requestLayout();
		sheet.callOnDraw();

		/*********************** END OF BOTTOM VIEW WITH THE SHEET MUSIC ****************************/

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

	public static boolean isItALa() {
//		ArrayList<MidiTrack>(0);
//		
//		if (.number == 0) {
//			return true;
//		}
		return false;

	}

}
