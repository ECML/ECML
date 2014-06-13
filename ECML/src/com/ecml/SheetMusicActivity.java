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
import java.net.*;

import android.net.Uri;
import android.widget.*;
import android.view.*;
import android.graphics.*;
import android.graphics.drawable.Drawable;
import android.content.*;
import android.content.res.*;
import android.media.*;

import java.util.zip.CRC32;



/***************************************************************************************************************
 ***************************************************************************************************************
 ****************************************************************************************************************/
/***************************************************************************************************************
***************************************************************************************************************
****************************************************************************************************************/
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;





import java.io.UnsupportedEncodingException;

import com.ecml.CalendarActivity;
import com.ecml.R;
import com.ecml.RecordingActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore.MediaColumns;
import android.util.Log;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
/**********************************************************************************************************
 ***********************************************************************************************************
 **********************************************************************************************************/
/**********************************************************************************************************
************************************************************************************************************
***********************************************************************************************************/




/** @class SheetMusicActivity
 *
 * The SheetMusicActivity is the main activity. The main components are:
 * - MidiPlayer : The buttons and speed bar at the top.
 * - Piano : For highlighting the piano notes during playback.
 * - SheetMusic : For highlighting the sheet music notes during playback.
 *
 */
public class SheetMusicActivity extends Activity implements  SurfaceHolder.Callback {

    public static final String MidiTitleID = "MidiTitleID";
    public static final int settingsRequestCode = 1;
    
    private MidiPlayer player;   /* The play/stop/rewind toolbar */
    private Piano piano;         /* The piano at the top */
    private SheetMusic sheet;    /* The sheet music */
    private LinearLayout layout; /* THe layout */
    private MidiFile midifile;   /* The midi file to play */
    private MidiOptions options; /* The options for sheet music and sound */
    private long midiCRC;      /* CRC of the midi bytes */

    /**********************************************************************************************************
     ***********************************************************************************************************
     **********************************************************************************************************/
    /**********************************************************************************************************
    ************************************************************************************************************
    ***********************************************************************************************************/
   
	private MediaRecorder videoRecorder;
    private File file = null;
    static final String VIDEO_PREFIX = "videorecord";
    static final String VIDEO_EXTENSION = ".mp4";
	private static final String TAG = null;
    private static String libraryPath = "sdcard/Library/";
   
 
    ImageButton myButton_start;
	ImageButton myButton_stop;
	ImageButton game ;
	Button stop_preview;
	Button start_preview;
	ImageButton full_preview ; 
	ImageButton full_sheet_button ;
	

	SurfaceView surfaceView;
	Camera camera_preview ;
	Camera camera_video ;
	SurfaceHolder surfaceHolder;
	boolean recording;
	boolean full ;
	boolean preview ;
	boolean full_sheet ;
	boolean midiPlayerVisible ; 
	boolean preview_button_layout_visible ;
		
	File videoFile = null ;
	SurfaceView myVideoView ;
	
	String stringPath = "/sdcard/samplevideo.3gp";
    
   
    private boolean isPlaying = false;
    private int totalSeek ;    
    private SeekBar seekBar; 
    private Handler mHandler = new Handler();
    private TextView CurrentDurationLabel;
    private TextView TotalDurationLabel;
    private Converter converter;
    final Context context = this ;
    
    
/*** Audio recording variables ***/
	
	private long fileName;
	private String pathAudio;
	private String ext;
	private boolean existLastRecord;

	private static final String AUDIO_RECORDER_FILE_EXT_3GP = ".3gp";
	private static final String AUDIO_RECORDER_FILE_EXT_MP4 = ".mp4";
	private static final String AUDIO_RECORDER_FOLDER = "AudioRecorder";
	private MediaRecorder recorder = null;
	private int currentFormat = 0;
	private int output_formats[] = { MediaRecorder.OutputFormat.MPEG_4, MediaRecorder.OutputFormat.THREE_GPP };
	private String file_exts[] = { AUDIO_RECORDER_FILE_EXT_MP4, AUDIO_RECORDER_FILE_EXT_3GP };
	
	/*** End of Audio recording variables ***/

	
	/*** Audio Recording functions ***/

	private void enableButton(int id, boolean isEnable) {
		((Button) findViewById(id)).setEnabled(isEnable);
	}

	private void enableButtons(boolean isRecording) {
		enableButton(R.id.btnStart, !isRecording);
		enableButton(R.id.btnStop, isRecording);
		enableButton(R.id.btnPlay, (!isRecording && existLastRecord));
	}

	private String getFilename() {
		String filepath = Environment.getExternalStorageDirectory().getPath();
		File file = new File(filepath, AUDIO_RECORDER_FOLDER);
		if (!file.exists()) {
			file.mkdirs();
		}
		fileName = System.currentTimeMillis();
		pathAudio = file.getAbsolutePath();
		ext = file_exts[currentFormat];
		return (pathAudio + "/" + fileName + ext);
	}

	private void startRecording() {
		recorder = new MediaRecorder();
		recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		recorder.setOutputFormat(output_formats[currentFormat]);
		recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		recorder.setOutputFile(getFilename());
		recorder.setOnErrorListener(errorListener);
		recorder.setOnInfoListener(infoListener);
		try {
			recorder.prepare();
			recorder.start();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		existLastRecord = true;
	}

	private void stopRecording() {
		if (null != recorder) {
			recorder.stop();
			recorder.reset();
			recorder.release();
			recorder = null;
		}
	}

	private void displayFormatDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		String formats[] = { "MPEG 4", "3GPP" };
		builder.setTitle(getString(R.string.choose_title_str)).setSingleChoiceItems(formats, currentFormat,
			new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					currentFormat = which;
					// setFormatButtonCaption();
					dialog.dismiss();
				}
			}).show();
	}

	private void play(String path, String fileName) {
		// set up MediaPlayer
		MediaPlayer mp = new MediaPlayer();
		try {
			mp.setDataSource(path + "/" + fileName);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			mp.prepare();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mp.start();
	}

	private MediaRecorder.OnErrorListener errorListener = new MediaRecorder.OnErrorListener() {
		@Override
		public void onError(MediaRecorder mr, int what, int extra) {
			Toast.makeText(SheetMusicActivity.this, "Error: " + what + ", " + extra, Toast.LENGTH_SHORT).show();
		}
	};

	private MediaRecorder.OnInfoListener infoListener = new MediaRecorder.OnInfoListener() {
		@Override
		public void onInfo(MediaRecorder mr, int what, int extra) {
			Toast.makeText(SheetMusicActivity.this, "Warning: " + what + ", " + extra, Toast.LENGTH_SHORT).show();
		}
	};
	
	/*** End of Audio Recording Functions ***/
    
    
    /**********************************************************************************************************
     ***********************************************************************************************************
     **********************************************************************************************************/
    /**********************************************************************************************************
    ************************************************************************************************************
    ***********************************************************************************************************/
    
    
    
     /** Create this SheetMusicActivity.  
      * The Intent should have two parameters:
      * - data: The uri of the midi file to open.
      * - MidiTitleID: The title of the song (String)
      */
    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setTheme(android.R.style.Theme_Holo_Light);
        /****Audio recording*/
        
        /********************/
        
        ClefSymbol.LoadImages(this);
        TimeSigSymbol.LoadImages(this);
        MidiPlayer.LoadImages(this);

        // Parse the MidiFile from the raw bytes
        Uri uri = this.getIntent().getData();
        // Default music sheet = Easy_Songs__Brahms_Lullaby.mid
       //Uri uri = Uri.parse( "file:///android_asset/Easy_Songs__Brahms_Lullaby.mid" );
        String title = this.getIntent().getStringExtra(MidiTitleID);
        if (title == null) {
        	title = uri.getLastPathSegment(); //"Easy_Songs__Brahms_Lullaby.mid";
        }
        FileUri file = new FileUri(uri, title);
        this.setTitle("ECML: " + title);
        byte[] data;
        try {
            data = file.getData(this);
            midifile = new MidiFile(data, title);
        }
        catch (MidiFileException e) {
            this.finish();
            return;
        }

        // Initialize the settings (MidiOptions).
        // If previous settings have been saved, used those
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
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        /**********************************************************************************************************
         ***********************************************************************************************************
         **********************************************************************************************************/
        /**********************************************************************************************************
        ************************************************************************************************************
        ***********************************************************************************************************/
        
        recording = false; 
        preview = false ;
        full = false ;
        full_sheet = true ; 
        midiPlayerVisible = true ;
        preview_button_layout_visible = true ;
        
        // Create the library folder if it doesn't exist 
        File file_library = new File(libraryPath);
        if (!file_library.exists()) {
            if (!file_library.mkdirs()) {
                Log.e("TravellerLog :: ", "Problem creating the Library");
            }
        }
        
        // Create the folder containing the music sheets ( in the library)
        File musicSheets = new File(libraryPath.concat("MusicSheets"));
        if (!musicSheets.exists()) {
            if (!musicSheets.mkdirs()) {
                Log.e("TravellerLog :: ", "Problem creating the Music sheets folder");
            }
        }
        
        // Create the folder containing the records ( in the library)
        File records = new File(libraryPath.concat("AudioRecords"));
        if (!records.exists()) {
            if (!records.mkdirs()) {
                Log.e("TravellerLog :: ", "Problem creating the records folder");
            }
        }
        
        // create the folder containing the video records
        File videorecords = new File(libraryPath.concat("VideoRecords"));
        if (!videorecords.exists()) {
            if (!videorecords.mkdirs()) {
                Log.e("TravellerLog :: ", "Problem creating the video records folder");
            }
        }
       
        converter = new Converter();
        ImageButton startRecording = (ImageButton) findViewById(R.id.startBtn);
        ImageButton stopRecording = (ImageButton) findViewById(R.id.stopBtn);
        ImageButton startPlayBack = (ImageButton) findViewById(R.id.play_button);
        ImageButton stopPlayBack = (ImageButton) findViewById(R.id.stopPlayBackBtn);
        ImageButton youtube_btn = (ImageButton) findViewById(R.id.youtubeBtn);
        ImageButton calendar = (ImageButton) findViewById(R.id.calendar);
        ImageButton tuning = (ImageButton) findViewById(R.id.tuning);
        final LinearLayout preview_button_layout = (LinearLayout) findViewById(R.id.preview_button_layout);
        game = (ImageButton) findViewById(R.id.game);
        start_preview = (Button) findViewById(R.id.start_preview);
        stop_preview = (Button) findViewById(R.id.stop_preview);
        preview_button_layout.setVisibility(View.VISIBLE);
        full_preview = (ImageButton) findViewById(R.id.full_preview);
    
 
           
        calendar.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				try {
                	Intent goToCalendar = new Intent(getApplicationContext(), CalendarActivity.class);
					startActivity(goToCalendar);
                    
                } catch (Exception e) {
                    e.printStackTrace();
                }
				
			}
		});
        
        tuning.setOnClickListener (new View.OnClickListener() {
        	public void onClick(View arg0) {
        		Toast.makeText(SheetMusicActivity.this, "Tuning fork", Toast.LENGTH_SHORT).show();
        		MediaPlayer mPlayer = MediaPlayer.create(context, R.raw.tuning);
        		mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        		try {
					mPlayer.prepare();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        		mPlayer.start();
			}
        	
        });
        
        
        
        
full_preview.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			
				if(!full) {
				((LinearLayout) findViewById(R.id.other1)).setVisibility(View.GONE);
		     	((LinearLayout) findViewById(R.id.other2)).setVisibility(View.GONE);
				player.setVisibility(View.GONE);
				full_sheet_button.setVisibility(View.GONE);
				preview_button_layout.setVisibility(View.GONE);
				((LinearLayout) findViewById(R.id.camera_layout)).setLayoutParams(new LinearLayout.LayoutParams(1024,600));
				full = true ;
				} else {
				((LinearLayout) findViewById(R.id.camera_layout)).setLayoutParams(new LinearLayout.LayoutParams(356,250));
				((LinearLayout) findViewById(R.id.other1)).setVisibility(View.VISIBLE);
				((LinearLayout) findViewById(R.id.other2)).setVisibility(View.VISIBLE);
				player.setVisibility(View.VISIBLE);
				full_sheet_button.setVisibility(View.VISIBLE);
				preview_button_layout.setVisibility(View.GONE);
				full = false ;
				}
			}
		});
        
start_preview.setOnClickListener(new View.OnClickListener() {
	public void onClick(View v) {
		camera_preview = openFrontFacingCameraGingerbread() ;
		if (camera_preview != null) {
			try {
				camera_preview.setPreviewDisplay(surfaceHolder);
				camera_preview.startPreview();
				preview_button_layout.setVisibility(View.GONE);
				preview_button_layout_visible = false ;
				preview = true ; 
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
       }
   });
        
stop_preview.setOnClickListener(new View.OnClickListener() {
	public void onClick(View v) {
		if (camera_preview != null) {
			camera_preview.stopPreview();
			preview_button_layout.setVisibility(View.GONE);
			preview_button_layout_visible = false ;
			releaseCameraPreview();
			preview = false ;
		}
    }
});

       
       
        
        
        
        
        
        game.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                	Intent goToGameActivity = new Intent(getApplicationContext(), GameActivity.class);
					startActivity(goToGameActivity);
                    
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        
        
        
        final String songTitle = this.getIntent().getStringExtra(MidiTitleID);
        
        youtube_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myWebLink = new Intent(android.content.Intent.ACTION_VIEW);
                myWebLink.setData(Uri.parse("http://www.youtube.com/results?search_query=" + spaceToPlus(songTitle)));
                    startActivity(myWebLink);
             }
        });
        
  
        
        myVideoView = (SurfaceView)findViewById(R.id.surface_camera);
	       surfaceHolder = myVideoView.getHolder();
	       surfaceHolder.addCallback(this);
	       surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);	      
	       myButton_start = (ImageButton)findViewById(R.id.startcamerapreview);
	       myButton_stop = (ImageButton)findViewById(R.id.stopcamerapreview);
 
	       myVideoView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(preview_button_layout_visible) {
						preview_button_layout.setVisibility(View.GONE);	
						preview_button_layout_visible = false ;
					} else {
						preview_button_layout.setVisibility(View.VISIBLE);
						preview_button_layout_visible = true ;
					}
					
				}
			});
	       
	       
	     myButton_stop.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(recording){
					
					videoRecorder.stop();  // stop the recording
	                 // release the MediaRecorder object
	                releaseCameraVideo();
	                recording = false;	                
	            //    cameraLaunch();
	                
	               try {
	                	String path = "sdcard/Library/VideoRecords/";
	                	ContentValues values = new ContentValues(3);
	                    long current =  System.currentTimeMillis();
	                    values.put(MediaColumns.DATE_ADDED, (int) (current / 1000));
	            		String newFilePath = path.concat(values.toString()).concat(videoFile.getName());
	            		File newFile= new File(newFilePath);						
	        			try{	        		  		 
	        		  		  InputStream in = new FileInputStream(videoFile);
	        		  		  //For Overwrite the file.
	        		  		  OutputStream out = new FileOutputStream(newFile);

	        		  		  byte[] buf = new byte[1024];
	        		  		  int len;
	        		  		  while ((len = in.read(buf)) > 0){
	        		  		  out.write(buf, 0, len);
	        		  		  }
	        		  		  in.close();
	        		  		  out.close();
	        		  		  System.out.println("File copied.");
	        		  	      videoFile.delete();
	        		  		  }
	        		  		  catch(FileNotFoundException ex){
	        		  		  System.out.println(ex.getMessage() + " in the specified directory.");
	        		  		  }
	        		  		  catch(IOException e){
	        		  		  System.out.println(e.getMessage());  
	        		  		  }
	        			Toast.makeText(getApplicationContext(), " the file has been copied " + newFile.getCanonicalPath().toString(), Toast.LENGTH_LONG).show();
	        		} catch (IOException e) {			
	        			e.printStackTrace();
	        		} 
	               	
           }				
			}
		});
	     
	     
	     myButton_start.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(!recording){

						// initialize video camera
		                if (prepareMediaRecorder()) {
		                    // Camera is available and unlocked, MediaRecorder is prepared,
		                    // now you can start recording
		                    videoRecorder.start();

		                    // inform the user that recording has started
		               
		                   recording = true;
		                } else {
		                    // prepare didn't work, release the camera
		                    
		                    
		                   // stop the recording
		                   releaseMediaRecorder();
	                      
		                    // inform user
		                }
						
					  }
				}
			});
	     
	     
	     startRecording.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Toast.makeText(SheetMusicActivity.this, "Start Recording", Toast.LENGTH_SHORT).show();
					//enableButtons(true);
					startRecording();
					// TODO Auto-generated method stub
					
				}
			});
			
			stopRecording.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							Toast.makeText(SheetMusicActivity.this, "Stop Recording", Toast.LENGTH_SHORT).show();
							//enableButtons(false);
							stopRecording();
							// TODO Auto-generated method stub
							
						}
					});

			startPlayBack.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Toast.makeText(SheetMusicActivity.this, "Play Last Record", Toast.LENGTH_SHORT).show();
					String filename = fileName + ext;
					play(pathAudio, filename);
					// TODO Auto-generated method stub
					
				}
			});
	     

        
        /**********************************************************************************************************
         ***********************************************************************************************************
         **********************************************************************************************************/
        /**********************************************************************************************************
        ************************************************************************************************************
        ***********************************************************************************************************/
        
        
        
        
        
    }   // END ONCREATE
    
    
    
    
    
    /* Create the MidiPlayer and Piano views */
    void createView() {
        layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        player = new MidiPlayer(this);
        piano = new Piano(this);
        full_sheet_button = new ImageButton(this);
        final Drawable triangleUp = getResources().getDrawable(R.drawable.triangle_up);
        final Drawable triangleDown = getResources().getDrawable(R.drawable.triangle_down);
        full_sheet_button.setBackgroundDrawable(triangleUp);
        full_sheet_button.invalidate();
        full_sheet_button.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View arg0) {
        		if(preview) {
					camera_preview.stopPreview();
					releaseCameraPreview();
					preview = false ;
				}
        		// TODO Auto-generated method stub
        		if(!full_sheet) {
        			myVideoView.setVisibility(View.VISIBLE);
        			((LinearLayout) findViewById(R.id.main_top)).setVisibility(View.VISIBLE);
        			full_sheet_button.setBackgroundDrawable(triangleUp);
        	        full_sheet_button.invalidate();
        			full_sheet = true  ;
        		}
        		else {
        			((LinearLayout) findViewById(R.id.main_top)).setVisibility(View.GONE);
        			myVideoView.setVisibility(View.GONE);
        			full_sheet_button.setBackgroundDrawable(triangleDown);
        	        full_sheet_button.invalidate();
        			full_sheet = false ;
        		}					
        	}
        });
     
        View l = getLayoutInflater().inflate(R.layout.main_top, layout,false);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(100,20);
		params.leftMargin = (int) 460 ;
		
		layout.addView(l);
		layout.addView(full_sheet_button,params);
        layout.addView(player);
        layout.addView(piano);
        setContentView(layout);
        player.SetPiano(piano);
        layout.requestLayout();
    }

    /** Create the SheetMusic view with the given options */
    private void 
    createSheetMusic(MidiOptions options) {
        if (sheet != null) {
            layout.removeView(sheet);
        }
        if (!options.showPiano) {
            piano.setVisibility(View.GONE);
        }
        else {
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


    /** Always display this activity in landscape mode. */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    /** When the menu button is pressed, initialize the menus. */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (player != null) {
            player.Pause();
        }
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sheet_menu, menu);
        return true;
    }

    /** Callback when a menu item is selected.
     *  - Choose Song : Choose a new song
     *  - Song Settings : Adjust the sheet music and sound options
     *  - Save As Images: Save the sheet music as PNG images
     *  - Help : Display the HTML help screen
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
          case R.id.choose_song:
            chooseSong();
            return true;
          case R.id.song_settings:
            changeSettings();
            return true;
          case R.id.save_images:
            showSaveImagesDialog();
            return true;
          case R.id.help:
            showHelp();
            return true;
          case R.id.youtube:
          	showYoutube();
          	return true;
          case R.id.record:
          	showRecorder();
          	return true;
          case R.id.calendar:
        	showCalendar();
            return true;
          case R.id.game:
          	showGame();
          	return true;
          default:
            return super.onOptionsItemSelected(item);
        }
    }

    /** To choose a new song, simply finish this activity.
     *  The previous activity is always the ChooseSongActivity.
     */
    private void chooseSong() {
        this.finish();
        Intent intent = new Intent(this, ChooseSongActivity.class);
        startActivity(intent);
    }

    /** To change the sheet music options, start the SettingsActivity.
     *  Pass the current MidiOptions as a parameter to the Intent.
     *  Also pass the 'default' MidiOptions as a parameter to the Intent.
     *  When the SettingsActivity has finished, the onActivityResult()
     *  method will be called.
     */
    private void changeSettings() {
        MidiOptions defaultOptions = new MidiOptions(midifile);
        Intent intent = new Intent(this, SettingsActivity.class);
        intent.putExtra(SettingsActivity.settingsID, options);
        intent.putExtra(SettingsActivity.defaultSettingsID, defaultOptions);
        startActivityForResult(intent, settingsRequestCode);
    }


    /* Show the "Save As Images" dialog */
    private void showSaveImagesDialog() {
         LayoutInflater inflator = LayoutInflater.from(this);
         final View dialogView= inflator.inflate(R.layout.save_images_dialog, null);
         final EditText filenameView = (EditText)dialogView.findViewById(R.id.save_images_filename);
         filenameView.setText(midifile.getFileName().replace("_", " ") );
         AlertDialog.Builder builder = new AlertDialog.Builder(this);
         builder.setTitle(R.string.save_images_str);
         builder.setView(dialogView);
         builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
             public void onClick(DialogInterface builder, int whichButton) {
                 saveAsImages(filenameView.getText().toString());
             }
         });
         builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
             public void onClick(DialogInterface builder, int whichButton) {
             }
         });
         AlertDialog dialog = builder.create();
         dialog.show();
    }


    /* Save the current sheet music as PNG images. */
    private void saveAsImages(String name) {
        String filename = name;
        try {
            filename = URLEncoder.encode(name, "utf-8");
        }
        catch (UnsupportedEncodingException e) {
        }
        if (!options.scrollVert) {
            options.scrollVert = true;
            createSheetMusic(options);
        }
        try {
            int numpages = sheet.GetTotalPages();
            for (int page = 1; page <= numpages; page++) {
                Bitmap image= Bitmap.createBitmap(SheetMusic.PageWidth + 40, SheetMusic.PageHeight + 40, Bitmap.Config.ARGB_8888);
                Canvas imageCanvas = new Canvas(image);
                sheet.DrawPage(imageCanvas, page);
                File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/ECML");
                File file = new File(path, "" + filename + page + ".png");
                path.mkdirs();
                OutputStream stream = new FileOutputStream(file);
                image.compress(Bitmap.CompressFormat.PNG, 0, stream);
                image = null;
                stream.close();
    
                // Inform the media scanner about the file
                MediaScannerConnection.scanFile(this, new String[] { file.toString() }, null, null);
            }
        }
        catch (IOException e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Error saving image to file " + Environment.DIRECTORY_PICTURES + "/ECML/" + filename  + ".png");
            builder.setCancelable(false);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }
        catch (NullPointerException e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Ran out of memory while saving image to file " + Environment.DIRECTORY_PICTURES + "/ECML/" + filename  + ".png");
            builder.setCancelable(false);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }


    /** Show the HTML help screen. */
    private void showHelp() {
        Intent intent = new Intent(this, HelpActivity.class);
        startActivity(intent);
    }
    
    /** Change blanks and ":" to "+" in a String */
    private String spaceToPlus(String title) {
    	String new_title = "" ;
    	boolean last = false ; // tell whether last char is a '+' or not
    	for (int i = 0 ; i < title.length() ; i++) {
    		if ((title.charAt(i) == ' ' || title.charAt(i) == ':') && !last) {
        		new_title = new_title + "+" ;
        		last = true ;
    		}
    		else if (title.charAt(i) == ' ' || title.charAt(i) == ':')
    		{
    			
    		}
    		else {
    			new_title = new_title + title.charAt(i) ;
    			last = false ;
    		}
    	}
    	return new_title ;
    }
    
    /** Launch Youtube on Navigator and search for the current song */
    private void showYoutube() {
        String title = this.getIntent().getStringExtra(MidiTitleID);
    	Uri uri = Uri.parse("http://www.youtube.com/results?search_query=" + spaceToPlus(title));
  		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
  		startActivity(intent);
    }
    
    /** Launch the sound recorder */
    private void showRecorder() {
    	Intent goToRecorder = new Intent(this, RecordingActivity.class);
    	startActivity(goToRecorder);
    }
    
    /** Launch a Calendar */
    private void showCalendar() {
    	Intent goToCalendar = new Intent(getApplicationContext(), CalendarActivity.class);
		startActivity(goToCalendar);
    }
    
    /** Launch the game */
    private void showGame() {
    	
    }

    /** This is the callback when the SettingsActivity is finished.
     *  Get the modified MidiOptions (passed as a parameter in the Intent).
     *  Save the MidiOptions.  The key is the CRC checksum of the midi data,
     *  and the value is a JSON dump of the MidiOptions.
     *  Finally, re-create the SheetMusic View with the new options.
     */
    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent intent) {
        if (requestCode != settingsRequestCode) {
            return;
        }
        options = (MidiOptions) 
            intent.getSerializableExtra(SettingsActivity.settingsID);

        // Check whether the default instruments have changed.
        for (int i = 0; i < options.instruments.length; i++) {
            if (options.instruments[i] !=  
                midifile.getTracks().get(i).getInstrument()) {
                options.useDefaultInstruments = false;
            }
        }
        // Save the options. 
        SharedPreferences.Editor editor = getPreferences(0).edit();
        editor.putBoolean("scrollVert", options.scrollVert);
        editor.putInt("shade1Color", options.shade1Color);
        editor.putInt("shade2Color", options.shade2Color);
        editor.putBoolean("showPiano", options.showPiano);
        String json = options.toJson();
        if (json != null) {
            editor.putString("" + midiCRC, json);
        }
        editor.commit();

        // Recreate the sheet music with the new options
        createSheetMusic(options);
    }

    /** When this activity resumes, redraw all the views */
    @Override
    protected void onResume() {
        super.onResume();
        layout.requestLayout();
        player.invalidate();
        piano.invalidate();
        if (sheet != null) {
            sheet.invalidate();
        }
        layout.requestLayout();
    }

    /** When this activity pauses, stop the music */
    @Override
    protected void onPause() {
        if (player != null) {
            player.Pause();
        }
        releaseMediaRecorder();       // if you are using MediaRecorder, release it first
        releaseCameraVideo();
        super.onPause();
    } 
    
    
    
    /**********************************************************************************************************
     ***********************************************************************************************************
     **********************************************************************************************************/
    /**********************************************************************************************************
    ************************************************************************************************************
    ***********************************************************************************************************/
    
    
    
    
    
   


 
   

    
   
   

	
	private Camera openFrontFacingCameraGingerbread() {
	    int cameraCount = 0;
	    Camera cam = null;
	    Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
	    cameraCount = Camera.getNumberOfCameras();
	    for ( int camIdx = 0; camIdx < cameraCount; camIdx++ ) {
	        Camera.getCameraInfo( camIdx, cameraInfo );
	        if ( cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT  ) {
	            try {
	                cam = Camera.open( camIdx );
	            } catch (RuntimeException e) {
	                Log.e(TAG, "Camera failed to open: " + e.getLocalizedMessage());
	            }
	        }
	    }

	    return cam;
	}
	
	// VIDEO 
	
	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
	 // TODO Auto-generated method stub

	}
	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
	 // TODO Auto-generated method stub
	// prepareMediaRecorder();
	}
	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
	 // TODO Auto-generated method stub

	}


	private boolean prepareMediaRecorder() {
		videoRecorder = new MediaRecorder();
		camera_video = openFrontFacingCameraGingerbread();
		videoRecorder.setCamera(camera_video);
		camera_video.unlock();
		ContentValues values = new ContentValues(3);
      // long current =  System.currentTimeMillis();
        values.put(MediaColumns.TITLE, "My video record coucou ");
        //values.put(MediaColumns.DATE_ADDED, (int) (current / 1000));
       // values.put(MediaColumns.MIME_TYPE, "video.mp4");
        String prefix = VIDEO_PREFIX.concat(values.toString());
	 videoRecorder.setPreviewDisplay(surfaceHolder.getSurface());
	 videoRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
	// videoRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
     videoRecorder.setVideoSource(MediaRecorder.VideoSource.DEFAULT);
     CamcorderProfile camcorderProfile_HQ = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);
     videoRecorder.setProfile(camcorderProfile_HQ);
	 String path = "sdcard/Library/VideoRecords/";
	 String outputPath = prefix.concat(VIDEO_EXTENSION);

	 if (videoFile == null) {
         File rootDir = Environment.getExternalStorageDirectory();
         try {
			videoFile = File.createTempFile(VIDEO_PREFIX, VIDEO_EXTENSION, rootDir);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
     }
    
 
	 videoRecorder.setOutputFile(videoFile.getAbsolutePath());
  //   videoRecorder.setOutputFile(path.concat(outputPath));
     videoRecorder.setMaxDuration(60000); // Set max duration 60 sec.
     videoRecorder.setMaxFileSize(5000000); // Set max file size 5M
	 try {
	  videoRecorder.prepare();
	 } catch (IllegalStateException e) {
		 Log.d(TAG, "IllegalStateException preparing MediaRecorder: " + e.getMessage());
	        releaseMediaRecorder();
	        return false;
	 } catch (IOException e) {
		 Log.d(TAG, "IOException preparing MediaRecorder: " + e.getMessage());
	        releaseMediaRecorder();
	        return false;
	 }
	 return true ;
	}
	
	
	

    private void releaseMediaRecorder(){
        if (videoRecorder != null) {
            videoRecorder.reset();   // clear recorder configuration
            videoRecorder.release(); // release the recorder object
            videoRecorder = null;
           camera_video.lock();           // lock camera for later use
        }
    }

    private void releaseCameraVideo(){
        if (camera_video != null){
            camera_video.release();        // release the camera for other applications
            camera_video = null;
        }
    }
    
    private void releaseCameraPreview(){
        if (camera_preview != null){
            camera_preview.release();        // release the camera for other applications
            camera_preview = null;
        }
    }
	
	
	
	
    
    
    
    
    
    
    
    
    
}  // END !! 

