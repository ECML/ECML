package com.ecml;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

public class VideoRecordingActivity extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setTheme(android.R.style.Theme_Holo_Light);
	    ActionBar ab = getActionBar();
		ColorDrawable colorDrawable = new ColorDrawable(getResources().getColor(R.color.orange));
		ab.setBackgroundDrawable(colorDrawable);
	}

}
