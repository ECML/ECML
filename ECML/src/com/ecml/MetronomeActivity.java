package com.ecml;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.metronome.MetronomeController;

public class MetronomeActivity extends Activity {

	MetronomeController metronomeController;	/* Metronome Controller */
	SeekBar slider;								/* Slider that sets the tempo */
	int accentBeep;								/* Time Signature */
	TextView timeSignature;						/* The View for the Time Signature */

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(android.R.style.Theme_Holo_Light);
		
		/* Action Bar */
		ActionBar ab = getActionBar();
		ColorDrawable colorDrawable = new ColorDrawable(getResources().getColor(R.color.orange));
		ab.setBackgroundDrawable(colorDrawable);
		/* End of Action bar */
		
		setContentView(R.layout.metronome);

		/* Buttons */
		TextView startMetronome = (TextView) findViewById(R.id.startMetronome);
		TextView stopMetronome = (TextView) findViewById(R.id.stopMetronome);
		ImageView minus = (ImageView) findViewById(R.id.minusTempo);
		ImageView plus = (ImageView) findViewById(R.id.plusTempo);
		timeSignature = (TextView) findViewById(R.id.timeSignature);
		updateTimeSignatureView();
		/* End of Buttons */
		
		/* Buttons' Listeners */
		startMetronome.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				metronomeController.startMetronome();
			}
		});

		stopMetronome.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				metronomeController.stopMetronome();
			}
		});

		minus.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				minus();
			}
		});

		plus.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				plus();
			}
		});

		timeSignature.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				metronomeController.stopMetronome();
				// Creating the instance of PopupMenu
				PopupMenu popup = new PopupMenu(MetronomeActivity.this, timeSignature);
				// Inflating the Popup using xml file
				popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
				
				// registering popup with OnMenuItemClickListener
				popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
					public boolean onMenuItemClick(MenuItem item) {
						if (item.getTitle() != "Off") {
							accentBeep = Character.getNumericValue(item.getTitle().charAt(0));
						}
						else {
							accentBeep = 0;
						}
						timeSignature.setText("Time Signature: " + item.getTitle());
						metronomeController.setAccentBeep(accentBeep);
						metronomeController.startMetronome();
						return true;
					}
				});
				
				popup.show();
			}
		});
		/* End of Buttons' Listeners */

		metronomeController = new MetronomeController(this);
		setSliderListener();
	}

	/** When this activity pasuses, stop the metronome */
	@Override
	protected void onPause() {
		super.onPause();
		metronomeController.stopMetronome();
	}

	/** Update the View for Tempo */
	private void updateTempoView() {
		TextView tempoView = ((TextView) findViewById(R.id.tempo));
		tempoView.setText("Tempo: " + metronomeController.getTempo() + " bpm");
	}
	
	/** Update the View for the Time Signature */
	private void updateTimeSignatureView() {
		if (accentBeep == 0) {
			timeSignature.setText("Time Signature: Off");
		}
		else {
			timeSignature.setText("Time Signature: " + accentBeep);
		}
	}

	/** SliderListener */
	private void setSliderListener() {
		slider = (SeekBar) findViewById(R.id.sliderMetronome);
		slider.setMax(200 - 1); // -1 to avoid reaching 0
		slider.setProgress(metronomeController.getTempo() - 1); // -1 to avoid reaching 0
		updateTempoView();
		
		slider.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				metronomeController.startMetronome();
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				metronomeController.stopMetronome();
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				metronomeController.setTempo(progress); // updates the Variable Tempo of the Metronome
				updateTempoView(); // updates the View
			}
		});
	}

	/** Add 1 to the tempo */
	void plus() {
		metronomeController.stopMetronome();
		slider.setProgress(slider.getProgress() + 1); // onProgressChanged updates the Metronome and the View automatically
		metronomeController.startMetronome();
	}

	/** Remove 1 from the tempo */
	void minus() {
		metronomeController.stopMetronome();
		slider.setProgress(slider.getProgress() - 1); // onProgressChanged updates the Metronome and the View automatically
		metronomeController.startMetronome();
	}

}
