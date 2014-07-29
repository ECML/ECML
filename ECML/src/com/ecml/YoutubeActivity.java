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
		ColorDrawable colorDrawable = new ColorDrawable(getResources().getColor(R.color.orange));
		ab.setBackgroundDrawable(colorDrawable);

		// Go on Youtube button
		TextView youtube = (TextView) findViewById(R.id.youtubesearch);
		youtube.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent myWebLink = new Intent(android.content.Intent.ACTION_VIEW);
				myWebLink.setData(Uri.parse("http://www.youtube.com"));
				startActivity(myWebLink);
			}

		});


		// Upload on Youtube button
		TextView upload = (TextView) findViewById(R.id.youtubeshare);
		upload.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent myWebLink = new Intent(android.content.Intent.ACTION_VIEW);
				myWebLink.setData(Uri.parse("http://www.youtube.com/upload"));
				startActivity(myWebLink);
			}

		});

	}

}
