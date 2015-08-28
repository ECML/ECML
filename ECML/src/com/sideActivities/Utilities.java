package com.sideActivities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.ecml.R;
import com.metronome.MetronomeActivity;

public class Utilities extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utilities);

        // Metronome button
		ImageView metronome = (ImageView) findViewById(R.id.metronome);
		metronome.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent goToMetronome = new Intent(getApplicationContext(), MetronomeActivity.class);
				startActivity(goToMetronome);
			}
		});

		// Tuning fork button
		ImageView tuning = (ImageView) findViewById(R.id.tuning);
		tuning.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent tuningFork = new Intent(getApplicationContext(), TuningForkActivity.class);
				startActivity(tuningFork);
			}
		});

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_utilities, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
