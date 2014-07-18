package com.ecml;



import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SpeedGameModeActivity extends Activity {

	/** Called when the activity is first created. */
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setTheme(android.R.style.Theme_Holo_Light);
			setContentView(R.layout.speed_game_mode);
			ActionBar ab = getActionBar();
			ColorDrawable colorDrawable = new ColorDrawable(getResources().getColor(R.color.orange));
			ab.setBackgroundDrawable(colorDrawable);

			// Back to the score button
			Button score = (Button) findViewById(R.id.back);
			score.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					ChooseSongGameActivity.openFile(ECML.song);
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
			
			// lvl1 button
			Button lvl1 = (Button) findViewById(R.id.lvl1);
			lvl1.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getApplicationContext(), ChooseSongGameActivity.class);
					startActivity(intent);
				}
			});
			
			// lvl2 button
			Button lvl2 = (Button) findViewById(R.id.lvl2);
			lvl2.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getApplicationContext(), ChooseSongGameActivity.class);
					startActivity(intent);
				}
			});
			
			// lvl3 button
			Button lvl3 = (Button) findViewById(R.id.lvl3);
			lvl3.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getApplicationContext(), ChooseSongGameActivity.class);
					startActivity(intent);
				}
			});
			
			// lvl4 button
			Button lvl4 = (Button) findViewById(R.id.lvl4);
			lvl4.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getApplicationContext(), ChooseSongGameActivity.class);
					startActivity(intent);
				}
			});

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

