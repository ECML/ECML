package com.ecml;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class YoutubeActivity extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(android.R.style.Theme_Holo_Light);
		setContentView(R.layout.youtube);

		// Set Actionbar color
		ActionBar ab = getActionBar();
		ColorDrawable colorDrawable = new ColorDrawable(getResources()
				.getColor(R.color.orange));
		ab.setBackgroundDrawable(colorDrawable);
		
		//Go on Youtube button
		TextView youtube = (TextView) findViewById(R.id.youtubesearch);
		youtube.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent myWebLink = new Intent(
						android.content.Intent.ACTION_VIEW);
				myWebLink.setData(Uri.parse("http://www.youtube.com"));
				startActivity(myWebLink);

			}
		});
		
		

		// Upload on Youtube button
		TextView upload = (TextView) findViewById(R.id.youtubeshare);
		upload.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent myWebLink = new Intent(
						android.content.Intent.ACTION_VIEW);
				myWebLink.setData(Uri.parse("http://www.youtube.com/upload"));
				startActivity(myWebLink);

			}
		});

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

}
