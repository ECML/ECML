package com.game;

import com.ecml.ChooseSongActivity;
import com.ecml.ECML;
import com.ecml.R;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

public class ReadingGameModeActivity extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reading_game_mode);
		
		// Back to the score button
		Button backToScore = (Button) findViewById(R.id.back);
		backToScore.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (ECML.song != null) {
					ECML.intent.putExtra(ChooseSongActivity.mode,"chooseSong");
					ChooseSongActivity.openFile(ECML.song);
				}
				else {
					ECML.intent = new Intent(getApplicationContext(), ChooseSongActivity.class);
					ECML.intent.putExtra(ChooseSongActivity.mode,"chooseSong");
					startActivity(ECML.intent);
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

		// Beginner button
		Button beginner = (Button) findViewById(R.id.beginner);
		beginner.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), ChooseSongActivity.class);
				intent.putExtra(ChooseSongActivity.mode,"readingBeginner");
				startActivity(intent);
			}
		});

		// Normal button
		Button normal = (Button) findViewById(R.id.normal);
		normal.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
					Intent intent = new Intent(getApplicationContext(), ReadingGameNormal.class);
					startActivity(intent);
			}
		});
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
