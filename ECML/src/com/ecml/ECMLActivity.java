/*
 * Copyright (c) 2011-2012 Madhav Vaidyanathan
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License version 2.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 */

package com.ecml;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.calendar.CalendarActivity;
import com.game.GameActivity;
import com.metronome.MetronomeActivity;
import com.sideActivities.AudioRecordingActivity;
import com.sideActivities.TuningForkActivity;
import com.sideActivities.VideoRecordingActivity;
import com.sideActivities.YoutubeActivity;

/**
 * @class ECMLActivity
 * <br>
 *        This is the launch activity for ECML.
 *        It displays the splash screen with buttons leading to the
 *        different activities :
 *        <ul>
 *        	<li>Choose Song (which leads to SheetMusicActivity)</li>
 *        	<li>Audio Recording</li>
 *        	<li>Video Recording</li>
 *        	<li>Calendar</li>
 *        	<li>Game</li>
 *        	<li>ECML Messenger</li>
 *        	<li>Metronome</li>
 *        	<li>Tuning Fork</li>
 *        	<li>Youtube</li>
 *        </ul>
 *  
 *  TODO
 *	It can also display a sequence screen of activities that are suggested
 *  to the user or set by the teacher.
 */
public class ECMLActivity extends Activity {

	final Context context = this;
	private ArrayList<String> list = new ArrayList<String>(4); /* The list of the activities to do */
	public static final String PRACTICE_ALONE = "PI";
	public static final String CHECK_WITH_YOUR_TEACHER = "C";
	public static final String READING_OF_NOTES = "R";
	public static final String SPEED_GAME = "S";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(android.R.style.Theme_Holo_Light);
		setContentView(R.layout.main);

		// Example of a list a teacher could send
		// TODO : make this automatic using a server
		// or using a virtual teacher with a pre-programmed schedules
		list.add(PRACTICE_ALONE);
		list.add(CHECK_WITH_YOUR_TEACHER);
		list.add(READING_OF_NOTES);
		list.add(SPEED_GAME);
		
		sequenceOfActivities();
		
		// Set Action Bar color
		ActionBar ab = getActionBar();
		ColorDrawable colorDrawable = new ColorDrawable(getResources().getColor(R.color.orange));
		ab.setBackgroundDrawable(colorDrawable);

		// Choose song button
		ImageView chooseSong = (ImageView) findViewById(R.id.choose_song);
		chooseSong.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), ChooseSongActivity.class);
				intent.putExtra(ChooseSongActivity.niveau,"chooseSong");
				startActivity(intent);
			}
		});

		// Audio Recording button
		ImageView audioRecording = (ImageView) findViewById(R.id.audiorecording);
		audioRecording.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent goToAudio = new Intent(getApplicationContext(), AudioRecordingActivity.class);
				startActivity(goToAudio);
			}
		});

		// Video Recording button
		ImageView videoRecording = (ImageView) findViewById(R.id.videorecording);
		videoRecording.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent goToVideo = new Intent(getApplicationContext(), VideoRecordingActivity.class);
				startActivity(goToVideo);
			}
		});

		// Calendar button
		ImageView calendar = (ImageView) findViewById(R.id.calendar);
		calendar.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent goToCalendar = new Intent(getApplicationContext(), CalendarActivity.class);
				startActivity(goToCalendar);
			}
		});

		// Game button
		ImageView game = (ImageView) findViewById(R.id.game);
		game.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent goToGame = new Intent(getApplicationContext(), GameActivity.class);
				startActivity(goToGame);
			}
		});

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

		// Youtube button
		ImageView youtube = (ImageView) findViewById(R.id.youtube);
		youtube.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent goToYoutube = new Intent(getApplicationContext(), YoutubeActivity.class);
				startActivity(goToYoutube);
			}
		});
	}
	
	/** Display the sequence of activities accordingly to the received list */
	private void sequenceOfActivities() {
		for (int i = 0 ; i < list.size() ; i++) {
			addView(list.get(i));
		}
	}

	/** Add the view of the activity given in the sequence of activities */
	private void addView(String task) {
		if (task == PRACTICE_ALONE) {
			
		} else if (task == CHECK_WITH_YOUR_TEACHER) {
			
		} else if (task == READING_OF_NOTES) {
			
		} else if (task == SPEED_GAME) {
			
		}
	}

}
