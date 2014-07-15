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

import java.io.IOException;

import android.app.ActionBar;
import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import android.content.*;
import android.content.res.*;
import android.graphics.*;
import android.graphics.drawable.ColorDrawable;

import com.ecml.R;

/**
 * @class ECMLActivity
 *        This is the launch activity for ECML.
 *        It simply displays the splash screen with buttons leading to the
 *        differents activities.
 */
public class ECMLActivity extends Activity {

	final Context context = this;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(android.R.style.Theme_Holo_Light);
		setContentView(R.layout.main);

		// Set Actionbar color
		ActionBar ab = getActionBar();
		ColorDrawable colorDrawable = new ColorDrawable(getResources().getColor(R.color.orange));
		ab.setBackgroundDrawable(colorDrawable);

		// Choose song button
		ImageView chooseSong = (ImageView) findViewById(R.id.choose_song);
		chooseSong.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), ChooseSongActivity.class);
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


}
