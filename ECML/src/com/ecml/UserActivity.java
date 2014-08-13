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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.TextureView;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.calendar.CalendarActivity;
import com.game.GameActivity;
import com.login.Login;
import com.login.Messenger;
import com.login.User;
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
 *	It can also display a sequence screen of activities that are suggested
 *  to the user or set by the teacher.
 */
public class UserActivity extends Messenger {

	final Context context = this;
	private Menu menu;
	
	private User user ;
	private ArrayList<String> UserList = new ArrayList<String>(); /* The list of the activities to do */

	public static final String PRACTICE_ALONE = "PI";
	public static final String PRACTICE_WITH_ACCOMPANIMENT = "PA";
	public static final String CHECK_WITH_YOUR_TEACHER = "C";
	public static final String READING_OF_NOTES = "R";
	public static final String SPEED_GAME = "S";
	
	
	public static final String CHOOSE_SONG = "chooseSong";
	public static final String READING_OF_NOTES_BEGINNER = "readingBeginner";
	
	private static Bitmap playAloneImage;			/* The Play Alone image */
	private static Bitmap playAccompaniedImage;		/* The Play with Accompaniment image */
	private static Bitmap readingOfNotesImage;		/* The Reading of Notes image */
	private static Bitmap checkWithTeacherImage;	/* TODO The Check with your Teacher image */
	private static Bitmap leftImage;				/* The left triangle for scrolling image */
	private static Bitmap rightImage;				/* The right triangle for scrolling image */
	
	private boolean displayed;						/* Whether the sequence of activities has already been displayed or not */
	private LinearLayout sequenceOfActivities;		/* The Linear Layout used for the dynamic Sequence of Activies */
	private int stripeHeight;		/* The Stripe Height used to resize the icons */
	private int iconWidth;			/* The Icon Width (varies depending of the icon added) */
	private int iconHeight;			/* The Icon Height (varies depending of the icon added) */
	private int leftMargin;			/* The Left Margin before the sequence of activities (currently depending on the choose song button) */

	private String song;			/* TODO This variable will be used to open the song passed by the teacher to the student */
	private ImageView chooseSong;	/* The choose song button (used for alignment purposes aswell) */
    private String username;
    
    // Handling the xml object 
    ReadXMLFile r = new ReadXMLFile();
	// Userr.read();
  // private HandleXML obj;
	
	/** Called when the activity is first created.
	 * Set the main activity buttons.
	 * Also set the sequence of activities if there is one to display
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_user);
		loadImages(this);
		
		Bundle EXTRAS = getIntent().getExtras();
		username =  EXTRAS.getString("username");
		this.setTitle( "Welcome "+ username);
		this.start();
		//int actId = usersdb.getForeignId(EXTRAS.getString("username"));
		
	 

		// Choose song button
		chooseSong = (ImageView) findViewById(R.id.choose_song);
		chooseSong.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), ChooseSongActivity.class);
				intent.putExtra(ChooseSongActivity.mode, "chooseSong");
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


		// Communication button
		ImageView communication = (ImageView) findViewById(R.id.communication);
		communication.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent goToFacebook = new Intent(getApplicationContext(), FacebookActivity.class);
				startActivity(goToFacebook);
			}
		});


		// Messenger service button
		ImageView messenger = (ImageView) findViewById(R.id.messenger);
		messenger.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent goToMessenger = new Intent(getApplicationContext(), com.androidim.Login.class);
				startActivity(goToMessenger);
			}
		});

		// ADD THE ACTIVITIES TO THE DATABASE : SHOULD NT BE DONE HERE ; JUST FOR TESTS
		/*System.out.println("before adding");
		//usersdb.addActivity(username,PRACTICE_ALONE);
		System.out.println("after adding");
		//usersdb.addActivity(username,PRACTICE_WITH_ACCOMPANIMENT);
		// GET THE LIST OF ACTIVITIES FROM THE DATABASE
		//UserList = usersdb.getAct(username);
		String acti =usersdb.getAct(username);
			UserList.add(acti);*/
		
		
		
		/*UserList.add(PRACTICE_ALONE);
		UserList.add(CHECK_WITH_YOUR_TEACHER);
		UserList.add(PRACTICE_WITH_ACCOMPANIMENT);
		UserList.add(READING_OF_NOTES);
		UserList.add(READING_OF_NOTES);
		*/
		/*UserList.add(PRACTICE_WITH_ACCOMPANIMENT);
		UserList.add(PRACTICE_WITH_ACCOMPANIMENT);
		UserList.add(READING_OF_NOTES);
		/*UserList.add(READING_OF_NOTES);
		UserList.add(PRACTICE_WITH_ACCOMPANIMENT);
		UserList.add(READING_OF_NOTES);
		UserList.add(PRACTICE_WITH_ACCOMPANIMENT);*/
         ArrayList<String> myList = r.read(this);
         handleList(myList);
		// Get the Linear Layout used for the sequence of activities
		sequenceOfActivities = (LinearLayout) findViewById(R.id.seqOfActivities);
		// Get the icon choose song as a reference for alignment
		chooseSong = (ImageView) findViewById(R.id.choose_song);

		// We cannot get the height of a view before it's drawn
		// so we wait for it to be drawn, and when we see it's drawn
		// we make the measurements so that we can add the icons at the right
		// sizes
		sequenceOfActivities.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

			@Override
			public void onGlobalLayout() {
				// We need to check if the sequence of activities is already
				// displayed or not to avoid
				// displaying it an infinite number of times
				if (!displayed) {
					displayed = true;
					stripeHeight = sequenceOfActivities.getHeight();
					iconHeight = stripeHeight * 4 / 5;
					leftMargin = (int) chooseSong.getX();
					// Display the sequence of activities if there's one
					sequenceOfActivities();
				}
			}

		});


	}


	/***********************************************************************************************************/
	/***********************************************************************************************************/
	/***********************************************************************************************************/
	/********************************************* ACTION BAR **************************************************/
	/***********************************************************************************************************/
	/***********************************************************************************************************/
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.loginactionbar, menu);
		this.menu = menu;
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.login) {
			launchLogin();
			return true;
		}
		return true;
	}

	private void launchLogin() {
		Intent i = new Intent(UserActivity.this, Login.class);
		startActivity(i);
	}
	
	/***********************************************************************************************************/
	/***********************************************************************************************************/
	/***********************************************************************************************************/
	/****************************************** END OF ACTION BAR **********************************************/
	/***********************************************************************************************************/
	/***********************************************************************************************************/

	/** Load the left and right triangle, the play alone, the play accompanied
	 * and the reading of notes images
	 */
	private static void loadImages(Context context) {
		// If it hasn't been done yet, then playAloneImage should be null
		if (playAloneImage == null) {
			Resources res = context.getResources();
			playAloneImage = BitmapFactory.decodeResource(res, R.drawable.play_alone);
			playAccompaniedImage = BitmapFactory.decodeResource(res, R.drawable.play_with_accompaniment);
			readingOfNotesImage = BitmapFactory.decodeResource(res, R.drawable.reading_of_notes);
			leftImage = BitmapFactory.decodeResource(res, R.drawable.triangle_left);
			rightImage = BitmapFactory.decodeResource(res, R.drawable.triangle_right);
		}
	}


	/** Display the sequence of activities accordingly to the received list */
	private void sequenceOfActivities() {
		if (!UserList.isEmpty()) {
			sequenceOfActivities.setPadding(leftMargin / 3, (stripeHeight - iconHeight) / 2, leftMargin / 3, 0);
			setTriangle(leftImage);
			for (int i = 0; i < UserList.size(); i++) {
				addButton(UserList.get(i));
			}
			setTriangle(rightImage);
		}
	}


	/** Add the view of the given activity in the sequence of activities */
	private void addButton(String task) {
		if (task == PRACTICE_ALONE) {
			setButton(playAloneImage, CHOOSE_SONG);
		} else if (task == PRACTICE_WITH_ACCOMPANIMENT) {
			setButton(playAccompaniedImage, CHOOSE_SONG);
		} else if (task == CHECK_WITH_YOUR_TEACHER) {
			// TODO
		} else if (task == READING_OF_NOTES) {
			setButton(readingOfNotesImage, READING_OF_NOTES_BEGINNER);
		} else if (task == SPEED_GAME) {
			// TODO
		}
	}

	/** Set the button according to the given activity */
	private void setButton(Bitmap image, final String toDo) {
		ImageButton nextButton = new ImageButton(this);
		nextButton.setImageBitmap(image);
		iconWidth = iconHeight * image.getWidth() / image.getHeight();
		nextButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), ChooseSongActivity.class);
				intent.putExtra(ChooseSongActivity.mode, toDo);
				// TODO Tempo ? Speed ? Which track to display and mute ? Need
				// to record ?
				startActivity(intent);
			}

		});

		nextButton.setScaleType(ImageView.ScaleType.FIT_XY);
		sequenceOfActivities.addView(nextButton);
		LinearLayout.LayoutParams params;
		params = new LinearLayout.LayoutParams(iconWidth, iconHeight);
		params.width = iconWidth;
		params.height = iconHeight;
		params.leftMargin = iconWidth / 12;
		nextButton.setLayoutParams(params);
	}

	/** Set the right or left triangle if they are needed to show the sequence of
	 * activities is scrollable
	 */
	private void setTriangle(Bitmap image) {
		// If there are more than 4 elements to display, then we need the
		// triangles
		if (UserList.size() > 4) {
			ImageView triangle = new ImageView(this);
			triangle.setImageBitmap(image);
			iconWidth = iconHeight * image.getWidth() / image.getHeight();
			triangle.setScaleType(ImageView.ScaleType.FIT_XY);
			sequenceOfActivities.addView(triangle);
			LinearLayout.LayoutParams params;
			params = new LinearLayout.LayoutParams(iconWidth, iconHeight);
			params.width = iconWidth;
			params.height = iconHeight;
			if (image == leftImage) {
				params.leftMargin = iconWidth / 12;
			} else {
				params.rightMargin = iconWidth / 12;
			}
			triangle.setLayoutParams(params);
		}
	}
	
 public void handleList(ArrayList<String> myList){
	 for(int i = 0 ; i<myList.size(); i++){
		 String act = myList.get(i);
		 if(act.equals(PRACTICE_ALONE)){
			 UserList.add(PRACTICE_ALONE);
		 }
		 else if(act.equals(PRACTICE_WITH_ACCOMPANIMENT)){
			 UserList.add(PRACTICE_WITH_ACCOMPANIMENT);
		 }
		 else if(act.equals(SPEED_GAME)){
			 UserList.add(SPEED_GAME);
		 }
		 else if(act.equals(CHECK_WITH_YOUR_TEACHER)){
			 UserList.add(CHECK_WITH_YOUR_TEACHER);
		 }
		 else if(act.equals(READING_OF_NOTES)){
			 UserList.add(READING_OF_NOTES);
		 }
	 }
	 
 }
 

}
