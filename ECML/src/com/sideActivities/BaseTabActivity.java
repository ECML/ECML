package com.sideActivities;

import android.app.TabActivity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.calendar.CalendarActivity;
import com.ecml.ChooseSongActivity;
import com.ecml.ECMLActivity;
import com.ecml.FacebookActivity;
import com.ecml.R;
import com.game.GameActivity;
import com.metronome.MetronomeActivity;

public class BaseTabActivity extends TabActivity {
	
	/** When the menu button is pressed, initialize the menu. */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_action_bar, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	/** When menu has dropped down, handle click on corresponding items */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.mainScreen:
			Intent mainScreen = new Intent(getApplicationContext(), ECMLActivity.class);
			// Go to the main screen and kill any other living activities
			mainScreen.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(mainScreen);
			return true;
		case R.id.chooseSongActivity:
			Intent chooseSongActivity = new Intent(getApplicationContext(), ChooseSongActivity.class);
			startActivity(chooseSongActivity);
			finish();
			return true;
		case R.id.calendarActivity:
			Intent calendarActivity = new Intent(getApplicationContext(), CalendarActivity.class);
			startActivity(calendarActivity);
			finish();
			return true;
		case R.id.audioActivity:
			Intent audioActivity = new Intent(getApplicationContext(), AudioRecordingActivity.class);
			startActivity(audioActivity);
			finish();
			return true;
		case R.id.videoActivity:
			Intent videoActivity = new Intent(getApplicationContext(), VideoRecordingActivity.class);
			startActivity(videoActivity);
			finish();
			return true;
		case R.id.gameActivity:
			Intent gameActivity = new Intent(getApplicationContext(), GameActivity.class);
			startActivity(gameActivity);
			finish();
			return true;
		case R.id.messengerActivity:
			Intent messengerActivity = new Intent(getApplicationContext(), com.androidim.Login.class);
			startActivity(messengerActivity);
			finish();
			return true;
		case R.id.youtubeActivity:
			Intent youtubeActivity = new Intent(getApplicationContext(), YoutubeActivity.class);
			startActivity(youtubeActivity);
			finish();
			return true;
		case R.id.metronomeActivity:
			Intent metronomeActivity = new Intent(getApplicationContext(), MetronomeActivity.class);
			startActivity(metronomeActivity);
			finish();
			return true;
		case R.id.tuningForkActivity:
			Intent tuningForkActivity = new Intent(getApplicationContext(), TuningForkActivity.class);
			startActivity(tuningForkActivity);
			finish();
			return true;
		case R.id.communicationActivity:
			Intent communicationActivity = new Intent(getApplicationContext(), FacebookActivity.class);
			startActivity(communicationActivity);
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
