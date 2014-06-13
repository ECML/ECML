package com.ecml;

import android.app.Activity;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.Toast;
import com.ecml.R;

public class CalendarActivity extends Activity {

	/** Called when the activity is first created. */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(android.R.style.Theme_Holo_Light);
		setContentView(R.layout.calendar);
	      CalendarView calendarView=(CalendarView) findViewById(R.id.calendarView1);
	        calendarView.setOnDateChangeListener(new OnDateChangeListener() {

	            @Override
	            public void onSelectedDayChange(CalendarView view, int year, int month,
	                    int dayOfMonth) {
	                 Toast.makeText(getApplicationContext(), ""+dayOfMonth, 0).show();// TODO Auto-generated method stub

	            }
	        });
	    }
}
