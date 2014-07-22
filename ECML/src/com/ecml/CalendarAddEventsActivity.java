package com.ecml;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class CalendarAddEventsActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		     
		setContentView(R.layout.calendar_listview);
		populateListView();
		registerClickCallback();

	}

	private void registerClickCallback() {
		ListView list = (ListView) findViewById(R.id.listView1);
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				if (arg2 == 0) {
					addTeacherMeeting();
				}
				if (arg2 == 1) {
					addConcert();
				}

			}

		});

	}

	private void populateListView() {
		// Create list of items
		String[] myItems = { "Add a meeting with my music teacher", "Add a concert" };

		// Build Adapter
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, // Context for the activity
				R.layout.calendar_items, // Layout to use
				myItems); // Items to be displayed

		// Configure the list view.
		ListView list = (ListView) findViewById(R.id.listView1);
		list.setAdapter(adapter);
	}
	
	private void addTeacherMeeting() {

		Intent intent = new Intent(Intent.ACTION_INSERT);
		intent.setType("vnd.android.cursor.item/event");
		intent.putExtra(Events.TITLE, "Meeting with Music Teacher");
		intent.putExtra(Events.EVENT_LOCATION, "Music School");

		GregorianCalendar calDate = new GregorianCalendar(Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH);
		intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, calDate.getTimeInMillis());
		intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, calDate.getTimeInMillis() + 30 * 60 * 1000);

		// Make it private and show as busy
		intent.putExtra(Events.ACCESS_LEVEL, Events.ACCESS_PRIVATE);
		intent.putExtra(Events.AVAILABILITY, Events.AVAILABILITY_BUSY);

		startActivity(intent);

	}
		private void addConcert() {
			Intent intent = new Intent(Intent.ACTION_INSERT);
			intent.setType("vnd.android.cursor.item/event");
			intent.putExtra(Events.TITLE, "Music concert");

			GregorianCalendar calDate = new GregorianCalendar(Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH);
			intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, calDate.getTimeInMillis());
			intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, calDate.getTimeInMillis() + 30 * 60 * 1000);

			// Make it private and show as busy
			intent.putExtra(Events.ACCESS_LEVEL, Events.ACCESS_PRIVATE);
			intent.putExtra(Events.AVAILABILITY, Events.AVAILABILITY_BUSY);
			
			startActivity(intent);
		}
}
