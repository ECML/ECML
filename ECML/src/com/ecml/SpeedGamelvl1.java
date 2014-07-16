package com.ecml;

import java.util.ArrayList;
import java.util.zip.CRC32;


import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.game.*;

public class SpeedGamelvl1 extends SpeedGameLvl {
	
    private enum KeyDisplay {
        DISPLAY_FLAT,
        DISPLAY_SHARP,
    }
    private KeyDisplay keyDisplay = KeyDisplay.DISPLAY_SHARP;
    private static final String noteNames[][] = {
        { "A", "Bb", "B", "C", "Db", "D", "Eb", "E", "F", "Gb", "G", "Ab" },
        { "A", "A#", "B", "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#" },
    };
	
	
	/*** MidiSheet variables ***/
	
	public static final String MidiTitleID = "MidiTitleID";
	
	private SheetMusic sheet; /* The sheet music */
	private LinearLayout layout; /* THe layout */
	private Piano piano; /* The piano at the top */
	private long midiCRC; /* CRC of the midi bytes */
	
	/*** End of MidiSheet variables ***/
	
	private ArrayList<MidiTrack> Tracks;
	private ArrayList<MidiNote> Notes;
	private boolean search;
	View choice;
	View result;
	private View topLayout;
	
	private MidiFile midifile; /* The midi file to play */
	private MidiOptions options; /* The options for sheet music and sound */
	private MidiPlayer player; /* The play/stop/rewind toolbar */
	public static int noteplace;
	private MidiNote note;
	
	/*** PitchDetection variables ***/
	
	private MicrophonePitchPoster pitchPoster;
	
	/*** End of PitchDetection variables ***/

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(android.R.style.Theme_Holo_Light);
		setContentView(R.layout.speedgamelvl1);
		ActionBar ab = getActionBar();
		ColorDrawable colorDrawable = new ColorDrawable(getResources().getColor(R.color.orange));
		ab.setBackgroundDrawable(colorDrawable);


		/*****************
		 * TOP VIEW WITH THE CHOICE OF NOTES AND THE HELP, BACK TO SCORE, CHANGE
		 * GAME BUTTON
		 **********/
		Tracks= midifile.getTracks();
		layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		choice = getLayoutInflater().inflate(R.layout.choice, layout, false);
		layout.addView(choice);
		setContentView(layout);
		
		
		// Parse the MidiFile from the raw bytes
		Uri uri = this.getIntent().getData();
		String title = this.getIntent().getStringExtra(MidiTitleID);
		if (title == null) {
			title = uri.getLastPathSegment();
		}
		FileUri file = new FileUri(uri, title);
		this.setTitle("ECML: " + title);
		byte[] data;
		try {
			data = file.getData(this);
			midifile = new MidiFile(data, title);
		} catch (MidiFileException e) {
			this.finish();
			return;
		}
		
		
		// Initialize the settings (MidiOptions).
		// If previous settings have been saved, use those

		options = new MidiOptions(midifile);

		CRC32 crc = new CRC32();
		crc.update(data);
		midiCRC = crc.getValue();
		SharedPreferences settings = getPreferences(0);
		options.scrollVert = settings.getBoolean("scrollVert", true);
		options.shade1Color = settings.getInt("shade1Color", options.shade1Color);
		options.shade2Color = settings.getInt("shade2Color", options.shade2Color);
		options.showPiano = settings.getBoolean("showPiano", true);
		String json = settings.getString("" + midiCRC, null);
		MidiOptions savedOptions = MidiOptions.fromJson(json);
		if (savedOptions != null) {
			options.merge(savedOptions);
		}
		createView();
		createSheetMusic(options);
		
		player.mute();

		// Back to the score button
		Button score = (Button) findViewById(R.id.back);
		score.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				ChooseSongActivity.openFile(ECML.song);
			}
		});

		// Help button
		Button help = (Button) findViewById(R.id.help);
		help.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
//				showHelpDialog();
			}
		});

		// Change game button
		Button game = (Button) findViewById(R.id.game);
		game.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), GameActivity.class);
				startActivity(intent);
			}
		});
		
		// Change game button
		Button stop = (Button) findViewById(R.id.stop);
		game.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
		        pitchPoster.stopSampling();
		        pitchPoster = null;
			}
		});
		
		// Launch the game = Play button
				Button play = (Button) findViewById(R.id.play);
				score.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						
						Notes = findNotes(Tracks,0);
						
				        pitchPoster = new MicrophonePitchPoster(60);
				        pitchPoster.setHandler(new UIUpdateHandler());
				        pitchPoster.start();
											
					}
				});
	}
	
	private final class UIUpdateHandler extends Handler {
        public void handleMessage(Message msg) {
            final MicrophonePitchPoster.PitchData data
                = (MicrophonePitchPoster.PitchData) msg.obj;
            
            int i = 0;

            if (data != null && data.decibel > -20) {
               /* frequencyDisplay.setText(String.format(data.frequency < 200 ? "%.1fHz" : "%.0fHz",
                                                       data.frequency));
                final String printNote = noteNames[keyDisplay.ordinal()][data.note % 12];
                noteDisplay.setText(printNote.substring(0, 1));
                final String accidental = printNote.length() > 1 ? printNote.substring(1) : "";
                flatDisplay.setVisibility("b".equals(accidental) ? View.VISIBLE : View.INVISIBLE);
                sharpDisplay.setVisibility("#".equals(accidental) ? View.VISIBLE : View.INVISIBLE);
                nextNote.setText(noteNames[keyDisplay.ordinal()][(data.note + 1) % 12]);
                prevNote.setText(noteNames[keyDisplay.ordinal()][(data.note + 11) % 12]);
                final int c = Math.abs(data.cent) > centThreshold
                        ? Color.rgb(255, 50, 50)
                        : Color.rgb(50, 255, 50);
                noteDisplay.setTextColor(c);
                flatDisplay.setTextColor(c);
                sharpDisplay.setTextColor(c);
                offsetCentView.setValue((int) data.cent);
                staffView.pushNote(data.note);*/
            	 if ( Notes.get(i).toString() == noteNames[keyDisplay.ordinal()][data.note % 12])
            	 {
            		 
            	 }
            	
            } else {
                // No valid data to display. Set most elements invisible.
                /*frequencyDisplay.setText("");
                final int ghostColor = Color.rgb(40,  40,  40);
                noteDisplay.setTextColor(ghostColor);
                flatDisplay.setTextColor(ghostColor);
                sharpDisplay.setTextColor(ghostColor);
                prevNote.setText("");
                nextNote.setText("");
                offsetCentView.setValue(100);  // out of range, not displayed.
                staffView.pushNote(-1);*/
            }
            if (data != null && data.decibel > -60) {
               /* decibelView.setVisibility(View.VISIBLE);
                decibelView.setText(String.format("%.0fdB", data.decibel));*/
            } else {
                //decibelView.setVisibility(View.INVISIBLE);
            }
        }
    }
		
	/* Create the MidiPlayer and Piano views */
	void createView() {
		layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		player = new MidiPlayer(this);
		piano = new Piano(this);

		topLayout = getLayoutInflater().inflate(R.layout.main_top, layout, false);
		topLayout.setVisibility(View.GONE);
		layout.addView(topLayout);

		player.pianoButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				options.showPiano = !options.showPiano;
				player.SetPiano(piano, options);
				SharedPreferences.Editor editor = getPreferences(0).edit();
				editor.putBoolean("showPiano", options.showPiano);
				String json = options.toJson();
				if (json != null) {
					editor.putString("" + midiCRC, json);
				}
				editor.commit();
			}
		});

		player.playAndRecordButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				//startAudioRecordingAndPlayingMusic();
			}
		});

		player.playRecordButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				//playAudio();
			}
		});

		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.x;
		int height = size.y;

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
		params.gravity = Gravity.CENTER_HORIZONTAL;

		layout.addView(piano, params);
		layout.addView(player);
		setContentView(layout);
		getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.orange));
		player.SetPiano(piano, options);
		layout.requestLayout();
	}

	/** Create the SheetMusic view with the given options */
	private void createSheetMusic(MidiOptions options) {
		if (sheet != null) {
			layout.removeView(sheet);
		}
		if (!options.showPiano) {
			piano.setVisibility(View.GONE);
		} else {
			piano.setVisibility(View.VISIBLE);
		}
		sheet = new SheetMusic(this);
		sheet.init(midifile, options);
		sheet.setPlayer(player);
		layout.addView(sheet);
		piano.SetMidiFile(midifile, options, player);
		piano.SetShadeColors(options.shade1Color, options.shade2Color);
		player.SetMidiFile(midifile, options, sheet);
		layout.requestLayout();
		sheet.callOnDraw();
	}
	
	private ArrayList<MidiNote> findNotes(ArrayList<MidiTrack> tracks, int instrument){
		
		int i = 0;
		search = true;
		while(search){
			if( instrument == Tracks.get(i).getInstrument()){
				search = false;
			}
			else{
				i++;
			}
		}
		return Tracks.get(i).getNotes();
	}

}
