package com.calendar;

import com.ecml.R;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class CalendarRehearsalProgramActivity extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calendar_listview);
		/**Create the list view*/
		populateListView();
		/**Allowed to add an action when on item from the list view is selected */
		registerClickCallback();
		
	}

	private void registerClickCallback() {
		ListView list = (ListView) findViewById(R.id.listView1);
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				if (arg2 == 0) {

				}
				if (arg2 == 1) {

				}
			}
		});
	}

	private void populateListView() {
		// Create list of items
		String[] myItems = { "Normal rehearsal program for beginner", "Intensive rehearsal program for beginner",
				"Normal rehearsal program for intermediate student", "Intensive rehearsal program for intermediate student",
				"Normal rehearsal program for advanced student", "Intensive rehearsal program for advanced student" };

		// Build Adapter
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, 							// Context for the activity
																R.layout.calendar_items, 		// Layout to use
																myItems); 						// Items to be displayed

		// Configure the list view.
		ListView list = (ListView) findViewById(R.id.listView1);
		list.setAdapter(adapter);
	}

	
	
}
