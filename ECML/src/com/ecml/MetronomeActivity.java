package com.ecml;

import com.metronome.MetronomeController;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class MetronomeActivity extends Activity {
	
	MetronomeController metronomeController;
	SeekBar slider;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setTheme(android.R.style.Theme_Holo_Light);
	    
	    ActionBar ab = getActionBar();
		ColorDrawable colorDrawable = new ColorDrawable(getResources().getColor(R.color.orange));
		ab.setBackgroundDrawable(colorDrawable);
	    
		setContentView(R.layout.metronome);
	    
	    TextView startMetronome = (TextView) findViewById(R.id.startMetronome);
	    TextView stopMetronome = (TextView) findViewById(R.id.stopMetronome);
	    ImageView minus = (ImageView) findViewById(R.id.minusTempo);
	    ImageView plus = (ImageView) findViewById(R.id.plusTempo);
	    
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
	    
	    metronomeController = new MetronomeController(this);   
        updateTempoView();
        setSliderListener();   
	}
	
	/** When this activity pasuses, stop the metronome */
	@Override
	protected void onPause() {
		super.onPause();
		metronomeController.stopMetronome();
	}
	
	private void updateTempoView(){
        TextView tempoView = ((TextView) findViewById(R.id.tempo));
        tempoView.setText("Tempo: " + metronomeController.getTempo() + " bpm");
    }
    
    public void start(View view){
    	metronomeController.startMetronome();
    }
    
    public void stop(View view){
    	metronomeController.stopMetronome();
    }
    
    public void updateTempo(View view){
    	SeekBar slider = (SeekBar) findViewById(R.id.sliderMetronome);
    	int newTempo = slider.getProgress();
    	metronomeController.setTempo(newTempo);
    	updateTempoView();
    }
    
    private void setSliderListener(){
    	slider = (SeekBar) findViewById(R.id.sliderMetronome);
    	slider.setMax(200-1);
    	slider.setProgress(metronomeController.getTempo()-1);
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
		    	metronomeController.setTempo(progress);
		    	updateTempoView();
			}
		});
    }
    
    void plus() {
    	slider.setProgress(slider.getProgress()+1);
    }
    
    void minus() {
    	slider.setProgress(slider.getProgress()-1);
    }

}
