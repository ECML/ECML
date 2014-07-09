package com.ecml;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class GameActivity extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(android.R.style.Theme_Holo_Light);
		setContentView(R.layout.game);
		ActionBar ab = getActionBar();
		ColorDrawable colorDrawable = new ColorDrawable(getResources().getColor(R.color.orange));
		ab.setBackgroundDrawable(colorDrawable);

		// Start the reading of notes part of the game
		Button reading = (Button) findViewById(R.id.reading);
		reading.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), ReadingGameModeActivity.class);
				startActivity(intent);
			}
		});

		// Back to the score button
		Button score = (Button) findViewById(R.id.backscore);
		score.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				ChooseSongActivity.openFile(ECML.song);
			}
		});
	}

}
