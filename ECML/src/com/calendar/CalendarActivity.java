package com.calendar;



import com.ecml.R;

import android.app.ActionBar;
import android.app.Activity;
import android.app.TabActivity;
import android.content.ContentUris;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Calendars;
import android.provider.CalendarContract.Events;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;
import android.content.*;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;

public class CalendarActivity extends TabActivity {

	public static final String[] EVENT_PROJECTION = new String[] { Calendars._ID, // 0
			Calendars.ACCOUNT_NAME, // 1
			Calendars.CALENDAR_DISPLAY_NAME // 2

	};

	private static final int PROJECTION_DISPLAY_NAME_INDEX = 2;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setTheme(android.R.style.Theme_Holo_Light);
		setContentView(R.layout.calendar);

		ActionBar ab = getActionBar();
		ColorDrawable colorDrawable = new ColorDrawable(getResources().getColor(R.color.orange));
		ab.setBackgroundDrawable(colorDrawable);

		// create the TabHost that will contain the Tabs
		TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);

		TabSpec tab1 = tabHost.newTabSpec("First Tab");
		TabSpec tab2 = tabHost.newTabSpec("Second Tab");
		TabSpec tab3 = tabHost.newTabSpec("Third Tab");
		TabSpec tab4 = tabHost.newTabSpec("Fourth Tab");

		// Set the Tab name and Activity
		// that will be opened when particular Tab will be selected
		Intent intentCalendar;
		intentCalendar = new Intent().setClass(this, CalendarDisplayActivity.class);
		tab1.setIndicator("Display calendar");
		tab1.setContent(intentCalendar);

		tab2.setIndicator("Choose a Rehearsal program");
		tab2.setContent(new Intent(this, CalendarRehearsalProgramActivity.class));

		tab3.setIndicator("Add event");
		tab3.setContent(new Intent(this, CalendarAddEventsActivity.class));

		Intent intentPreferences;
		intentPreferences = new Intent().setClass(this, CalendarPreferencesActivity.class);
		tab4.setIndicator("Calendar preferences");
		tab4.setContent(intentPreferences);

		/** Add the tabs to the TabHost to display. */
		tabHost.addTab(tab1);
		tabHost.addTab(tab2);
		tabHost.addTab(tab3);
		tabHost.addTab(tab4);

	}
}
