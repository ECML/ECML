package com.sideActivities;

import java.io.File;
import java.io.IOException;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.ecml.R;

/** @class VideoRecordingActivity
 * 
 * @author Nicolas and Anaïs
 * <br>
 * This Activity uses a Media Recorder that handles Recording<br>
 * When replaying, this activity finishes (this needs to be fixed)<br>
 * Main functions :
 * <ul>
 * 		<li>startRecording()</li>
 * 		<li>stopRecording()</li>
 * 		<li>replayAudio()</li>
 * 		<li>pauseAudio()</li>
 * </ul>
 */
public class VideoRecordingActivity extends Activity implements SurfaceHolder.Callback {

	private SurfaceView surfaceView;			/* The Surface View needed for the Camera */
	private SurfaceHolder surfaceHolder;		/* The Surface Holder needed for the Camera */
	private MediaRecorder mediaRecorder;	/* Video Media Recorder */
	private Camera camera;						/* The Camera */
	private static final String VIDEO_RECORDER_FOLDER = "VideoRecords";	/* Video Records folder name */
	private String path;				/* Path of last Video Record */
	private boolean existVideoRecord;	/* Whether or not an Video Record already exists */
	private boolean isRecording;		/* Whether or not the Video Media Recorder is recording */
	private boolean front = true;		/* Whether the front camera or the back camera is used */
	private String cameraSide;			/* The String telling which camera is used accordingly to 'front' */
	
	private long fileName;						/* File name of last Video Record */
	private String ext = ".mp4";				/* Extension of Audio and VIDEO Record files */
	private static String ECMLPath = "ECML/";	/* Path to the ECML folder from the sdcard */

	final Context context = this;	/* The context of this activity */

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.videorecording);

		
		surfaceView = (SurfaceView) findViewById(R.id.surface_camera2);
		surfaceHolder = surfaceView.getHolder();
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

		// Start recording button
		ImageView startVideoRecording = (ImageView) findViewById(R.id.startVideoRecording);
		startVideoRecording.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					startVideoRecording();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		});

		// Stop recording button
		ImageView stopVideoRecording = (ImageView) findViewById(R.id.stopVideoRecording);
		stopVideoRecording.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				stopVideoRecording();
			}
			
		});

		// Replay last video
		ImageView replayVideoRecording = (ImageView) findViewById(R.id.replayVideoRecording);
		replayVideoRecording.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				replayVideoRecording();
			}
		
		});

		// Switch camera
		ImageView switchCamera = (ImageView) findViewById(R.id.switchCamera);
		switchCamera.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				switchCamera();
			}
			
		});
	}

	/** Get the Filename of the next Video Record, also updates the path */
	private String getFilenameVideo() {
		String filepath = Environment.getExternalStorageDirectory().getPath();
		File file = new File(filepath, ECMLPath + VIDEO_RECORDER_FOLDER);
		if (!file.exists()) {
			file.mkdirs();
		}
		fileName = System.currentTimeMillis();
		path = file.getAbsolutePath();
		return (path + "/" + fileName + ext);
	}
	
	/** Start Video Recording if not recording audio or video yet */
	protected void startVideoRecording() throws IOException {
		if (!isRecording) {
			if (front == true) {
				camera = openFrontFacingCamera();
			} else {
				camera = Camera.open();
			}
			mediaRecorder = new MediaRecorder(); // Works well
			camera.stopPreview();
			camera.unlock();
			mediaRecorder.setCamera(camera);

			mediaRecorder.setPreviewDisplay(surfaceHolder.getSurface());
			mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
			mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);

			if (front == true) {
				mediaRecorder.setProfile(CamcorderProfile.get(Camera.CameraInfo.CAMERA_FACING_FRONT, CamcorderProfile.QUALITY_HIGH));
			} else {
				mediaRecorder.setProfile(CamcorderProfile.get(Camera.CameraInfo.CAMERA_FACING_BACK, CamcorderProfile.QUALITY_HIGH));
			}

			mediaRecorder.setOutputFile(getFilenameVideo());
			mediaRecorder.setVideoFrameRate(10);

			mediaRecorder.prepare();
			isRecording = true;
			mediaRecorder.start();
		} else {
			Toast.makeText(context, "Stop Recording first", Toast.LENGTH_SHORT).show();
		}
	}

	/** Stop Video Recording if currently recording,
	 * and release video media recorder
	 * and release camera for other apps
	 */
	protected void stopVideoRecording() {
		if (isRecording) {
			isRecording = false;
			existVideoRecord = true;
			mediaRecorder.stop();
			releaseMediaRecorder();
			releaseCamera();
		} else {
			Toast.makeText(context, "Not Recording", Toast.LENGTH_SHORT).show();
		}
	}

	/** Release MediaRecorder to save memory
	 * and to avoid that other apps get unable
	 * to use this ressource
	 */
	private void releaseMediaRecorder() {
		if (mediaRecorder != null) {
			mediaRecorder.reset(); // clear recorder configuration
			mediaRecorder.release(); // release the recorder object
			mediaRecorder = null;
			camera.lock(); // lock camera for later use
		}
	}

	/** Release the camera for other apps */
	private void releaseCamera() {
		if (camera != null) {
			camera.release(); // release the camera for other applications
			if (front == true) {
				camera = openFrontFacingCamera();
				camera.release();
			} else {
				camera = Camera.open();
				camera.release();
			}
		}
	}

	/** Replay last Audio Record
	 * if not currently recording
	 * and if there is one to replay
	 */
	private void replayVideoRecording() {
		if (!isRecording) {
			if (existVideoRecord) {
				String filename = fileName + ext;
				String lastvideo = path + "/" + filename;
				Intent intentToPlayVideo = new Intent(Intent.ACTION_VIEW);
				intentToPlayVideo.setDataAndType(Uri.parse(lastvideo), "video/*");
				startActivity(intentToPlayVideo);
				Toast.makeText(context, "Playing Last Video Record", Toast.LENGTH_SHORT).show();
				this.finish();
			} else {
				Toast.makeText(context, "No Recent Video Record", Toast.LENGTH_SHORT).show();
			}
		} else {
			Toast.makeText(context, "Stop Recording First", Toast.LENGTH_SHORT).show();
		}
	}

	/** Necessary function for the camera */
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
	}

	/** Necessary function for the camera */
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
	}

	/** Necessary function for the camera */
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
	}

	private Camera openFrontFacingCamera() {
		int cameraCount = 0;
		Camera cam = null;
		Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
		cameraCount = Camera.getNumberOfCameras();
		for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
			Camera.getCameraInfo(camIdx, cameraInfo);
			if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
				try {
					cam = Camera.open(camIdx);
				} catch (RuntimeException e) {

				}
			}
		}

		return cam;

	}
	
	/** Switch the camera from front to back and vice versa */
	private void switchCamera() {
		// We should add a test here
//		if (Camera.getNumberOfCameras() > 1) {
			front = !front;
			if (front) {
				cameraSide = "Front";
			}
			else {
				cameraSide = "Back";
			}
			Toast.makeText(context, "Camera Switched: Now using " + cameraSide + " Camera" , Toast.LENGTH_SHORT).show();
//		} else {
//			Toast.makeText(context, "Cannot Switch Camera: Now using " + cameraSide + " Camera" , Toast.LENGTH_SHORT).show();
//		}
	}

}
