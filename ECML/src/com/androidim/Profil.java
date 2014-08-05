package com.androidim;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.ecml.R;

public class Profil extends Activity {
	   /** Called when the activity is first created. */	
		@Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);  
	        setContentView(R.layout.fill_in_profil_screen);
	        setTitle("Your Profil");
	        Spinner spinner = (Spinner) findViewById(R.id.instruments_spinner);
	     // Create an ArrayAdapter using the string array and a default spinner LAYOUT
	     ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
	             R.array.instruments_array, android.R.layout.simple_spinner_item);
	     // Specify the layout to use when THE LIST of choices appears
	     adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	     // APPLY the adapter to the spinner
	     spinner.setAdapter(adapter);
	        }
		

}
