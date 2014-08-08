package com.ecml;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class StudentActivities extends Activity {
	   /** Called when the activity is first created. */	
		@Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);  
	        setContentView(R.layout.student_activities);
	        setTitle("List of activities");
	        Spinner spinner = (Spinner) findViewById(R.id.activities_spinner);
	     // Create an ArrayAdapter using the string array and a default spinner LAYOUT
	     ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
	             R.array.activity_array, android.R.layout.simple_spinner_item);
	     // Specify the layout to use when THE LIST of choices appears
	     adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	     // APPLY the adapter to the spinner
	     spinner.setAdapter(adapter);
	        }
		

}
