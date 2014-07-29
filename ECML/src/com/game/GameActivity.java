package com.game;

import com.ecml.ChooseSongActivity;
import com.ecml.ECML;
import com.ecml.R;
import com.ecml.R.color;
import com.ecml.R.id;
import com.ecml.R.layout;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/** @class GameActivity
 * <br>
 * The Game Activity is the main screen activity for the different games :
 * <ul>
 * 		<li>Speed</li>
 * 		<li>Musical Ear</li>
 * 		<li>Reading of Notes</li>
 * 		<li>Technique</li>
 * </ul>
 * From this point, you can also choose to go back to the score.
 */
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
		
		
		// Start the speed part of the game
		Button speed = (Button) findViewById(R.id.speed);
		speed.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), SpeedGameModeActivity.class);
				startActivity(intent);
			}
		});

		// Back to the score button
		Button score = (Button) findViewById(R.id.backscore);
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
	}

}