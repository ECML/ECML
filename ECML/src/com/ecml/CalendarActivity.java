package com.ecml;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
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
		
		ActionBar ab = getActionBar();
		ColorDrawable colorDrawable = new ColorDrawable(getResources().getColor(R.color.orange));
		ab.setBackgroundDrawable(colorDrawable);
		
	    CalendarView calendarView=(CalendarView) findViewById(R.id.calendarView1);
	    
	    calendarView.setOnDateChangeListener(new OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                 Toast.makeText(getApplicationContext(), ""+dayOfMonth, 0).show();
            }
            
	    });
	}
}
