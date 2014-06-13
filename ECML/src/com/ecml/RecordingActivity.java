package com.ecml;

import android.app.Activity;
import android.app.AlertDialog;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.Button;
import android.view.View;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.media.MediaRecorder;
import android.media.MediaPlayer;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.ecml.R;

import java.io.File;
import java.io.IOException;

public class RecordingActivity extends Activity {

	private static final String AUDIO_RECORDER_FILE_EXT_3GP = ".3gp";
	private static final String AUDIO_RECORDER_FILE_EXT_MP4 = ".mp4";
	private static final String AUDIO_RECORDER_FOLDER = "AudioRecorder";
	private MediaRecorder recorder = null;
	private int currentFormat = 0;
	private int output_formats[] = { MediaRecorder.OutputFormat.MPEG_4, MediaRecorder.OutputFormat.THREE_GPP };
	private String file_exts[] = { AUDIO_RECORDER_FILE_EXT_MP4, AUDIO_RECORDER_FILE_EXT_3GP };


	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setTheme(android.R.style.Theme_Holo_Light);
	    setContentView(R.layout.recording);
	    setButtonHandlers();
	    enableButtons(false);
	    setFormatButtonCaption();
	    
	    
	    
	    
	}
	
	
	

	private void setButtonHandlers() {
	    ((Button) findViewById(R.id.btnStart)).setOnClickListener(btnClick);
	    ((Button) findViewById(R.id.btnStop)).setOnClickListener(btnClick);
	    ((Button) findViewById(R.id.btnFormat)).setOnClickListener(btnClick);
	}

	private void enableButton(int id, boolean isEnable) {
	    ((Button) findViewById(id)).setEnabled(isEnable);
	}

	private void enableButtons(boolean isRecording) {
	    enableButton(R.id.btnStart, !isRecording);
	    enableButton(R.id.btnFormat, !isRecording);
	    enableButton(R.id.btnStop, isRecording);
	}

	private void setFormatButtonCaption() {
	    ((Button) findViewById(R.id.btnFormat)).setText(getString(R.string.audio_format_str) + " (" + file_exts[currentFormat] + ")");
	}

	private String getFilename() {
	    String filepath = Environment.getExternalStorageDirectory().getPath();
	    File file = new File(filepath, AUDIO_RECORDER_FOLDER);
	    if (!file.exists()) {
	        file.mkdirs();
	    }
	    return (file.getAbsolutePath() + "/" + System.currentTimeMillis() + file_exts[currentFormat]);
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
	    builder.setTitle(getString(R.string.choose_title_str)).setSingleChoiceItems(formats, currentFormat, new DialogInterface.OnClickListener() {
	         @Override
	         public void onClick(DialogInterface dialog, int which) {
	             currentFormat = which;
	             setFormatButtonCaption();
	             dialog.dismiss();
	         }
	     }).show();
	}
	
	private void play(String path, String fileName){
		    //set up MediaPlayer    
		    MediaPlayer mp = new MediaPlayer();
		 
		    try {
		        mp.setDataSource(path+"/"+fileName);
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
	        Toast.makeText(RecordingActivity.this, "Error: " + what + ", " + extra, Toast.LENGTH_SHORT).show();
	    }
	};

	private MediaRecorder.OnInfoListener infoListener = new MediaRecorder.OnInfoListener() {
	    @Override
	    public void onInfo(MediaRecorder mr, int what, int extra) {
	        Toast.makeText(RecordingActivity.this, "Warning: " + what + ", " + extra, Toast.LENGTH_SHORT).show();
	    }
	};

	private View.OnClickListener btnClick = new View.OnClickListener() {
	    @Override
	    public void onClick(View v) {
	        switch (v.getId()) {
	            case R.id.btnStart: {
	                Toast.makeText(RecordingActivity.this, "Start Recording", Toast.LENGTH_SHORT).show();
	                enableButtons(true);
	                startRecording();
	                break;
	            }
	            case R.id.btnStop: {
	                Toast.makeText(RecordingActivity.this, "Stop Recording", Toast.LENGTH_SHORT).show();
	                enableButtons(false);
	                stopRecording();
	                break;
	            }
	        /*    case R.id.btnPlay: {
	                play();
	                break;
	            } */
	            case R.id.btnFormat: {
	                displayFormatDialog();
	                break;
	            }
	        }
	    }
	};
}

	


