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


public abstract class SpeedGameLvl extends Activity {

	private LinearLayout layout;
	View choice;
	View result;
	private SheetMusic sheet;
	private MidiFile midifile; /* The midi file to play */
	private MidiOptions options; /* The options for sheet music and sound */
	private MidiPlayer player; /* The play/stop/rewind toolbar */
	public static int noteplace;
	private MidiNote note;
	

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(android.R.style.Theme_Holo_Light);

		ActionBar ab = getActionBar();
		ColorDrawable colorDrawable = new ColorDrawable(getResources().getColor(R.color.orange));
		ab.setBackgroundDrawable(colorDrawable);


		/*****************
		 * THE HELP, BACK TO SCORE, CHANGE
		 ***********/
		layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		choice = getLayoutInflater().inflate(R.layout.speed_game_lvl, layout, false);
		layout.addView(choice);
		setContentView(layout);

		// Back to the score button
		Button score = (Button) findViewById(R.id.back);
		score.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				ChooseSongActivity.openFile(ECML.song);
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
		
		result = getLayoutInflater().inflate(R.layout.speed_points, layout, false);
		layout.addView(result);
		result.setVisibility(View.GONE);
		setContentView(layout);

		// Retry button
		Button retry = (Button) findViewById(R.id.retry);
		retry.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), this.getClass());
				startActivity(intent);
			}
		});
		/********************* END OF TOP VIEW WITH RESULTS AND APPRECIATION *****************/

		/*********************** BOTTOM VIEW WITH THE SHEET MUSIC ****************************/
		// Parse the MidiFile from the raw bytes

		String title = "blabla";

		midifile = new MidiFile(ECML.song.getData(this), title);

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
		final View dialogView = inflator.inflate(R.layout.help_speed, null);

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
