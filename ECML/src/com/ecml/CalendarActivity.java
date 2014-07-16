package com.ecml;

import java.util.GregorianCalendar;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentUris;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Calendars;
import android.provider.CalendarContract.Events;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.content.*;
import android.graphics.drawable.ColorDrawable;

public class CalendarActivity extends Activity {
	
	public static final String[] EVENT_PROJECTION = new String[] 
			{  
			        Calendars._ID, // 0  
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

		// Display calendar button
		Button calendar = (Button) findViewById(R.id.calendar);
		calendar.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// A date-time specified in milliseconds since the epoch.
				long startMillis = 0;
				Uri.Builder builder = CalendarContract.CONTENT_URI.buildUpon();
				builder.appendPath("time");
				ContentUris.appendId(builder, startMillis);
				Intent intent = new Intent(Intent.ACTION_VIEW).setData(builder.build());
				startActivity(intent);
			}
		});
		
		// Add event button
				Button event = (Button) findViewById(R.id.event);
				event.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(Intent.ACTION_INSERT);  
					    intent.setType("vnd.android.cursor.item/event");  
					    intent.putExtra(Events.TITLE, "ECML : Instrument practise");  
					    intent.putExtra(Events.EVENT_LOCATION, "Home sweet home");  
					    intent.putExtra(Events.DESCRIPTION, "ECML");  

					    GregorianCalendar calDate = new GregorianCalendar(2014, 07, 17);  
					    intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,  
					            calDate.getTimeInMillis());  
					    intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,  
					            calDate.getTimeInMillis());  

					    intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);  


					    intent.putExtra(Events.ACCESS_LEVEL, Events.ACCESS_PRIVATE);  
					    intent.putExtra(Events.AVAILABILITY, Events.AVAILABILITY_BUSY);  

					    startActivity(intent);  

					}
				});

	}
}
