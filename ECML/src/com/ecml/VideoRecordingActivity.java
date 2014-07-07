package com.ecml;


import java.io.File;
import java.io.IOException;

import android.app.ActionBar;
import android.app.Activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.net.Uri;

import android.graphics.drawable.ColorDrawable;

import android.os.Bundle;
import android.os.Environment;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

public class VideoRecordingActivity extends Activity {
	
	SurfaceView surfaceView;
	SurfaceHolder surfaceHolder;
	public MediaRecorder mrec;
	private Camera mCamera;
	private static final String VIDEO_RECORDER_FOLDER = "VideoRecords";
	private String pathVideo;
	private boolean isVideoRecording;
	private boolean existVideoRecord;
	boolean front = true;

	private long fileName;
	private String ext;
	private static String ECMLPath = "ECML/";
	private int currentFormat = 0;
	private static final String VIDEO_RECORDER_FILE_EXT_MP4 = ".mp4";
	private String file_exts[] = { VIDEO_RECORDER_FILE_EXT_MP4};
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setTheme(android.R.style.Theme_Holo_Light);

		setContentView(R.layout.videorecording);

		ActionBar ab = getActionBar();
		ColorDrawable colorDrawable = new ColorDrawable(getResources()
				.getColor(R.color.orange));


	protected void startVideoRecording() throws IOException {
		mrec = new MediaRecorder(); // Works well
		mCamera.stopPreview();
		mCamera.unlock();
		mrec.setCamera(mCamera);

		mrec.setPreviewDisplay(surfaceHolder.getSurface());
		mrec.setVideoSource(MediaRecorder.VideoSource.CAMERA);
		mrec.setAudioSource(MediaRecorder.AudioSource.MIC);

		if (front == true) {
			mrec.setProfile(CamcorderProfile.get(Camera.CameraInfo.CAMERA_FACING_FRONT, CamcorderProfile.QUALITY_HIGH));
		} else {
			mrec.setProfile(CamcorderProfile.get(Camera.CameraInfo.CAMERA_FACING_BACK, CamcorderProfile.QUALITY_HIGH));
		}

		mrec.setOutputFile(getFilenameVideo());
		mrec.setVideoFrameRate(10);

		mrec.prepare();
		isVideoRecording = true;
		mrec.start();
	}

	protected void stopVideoRecording() {
		isVideoRecording = false;
		existVideoRecord = true;
		mrec.stop();
		releaseMediaRecorder();
		releaseCamera();
	}

	private void releaseMediaRecorder() {
		if (mrec != null) {
			mrec.reset(); // clear recorder configuration
			mrec.release(); // release the recorder object
			mrec = null;
			mCamera.lock(); // lock camera for later use
		}
	}

	private void releaseCamera() {
		if (mCamera != null) {
			mCamera.release(); // release the camera for other applications
			if (front == true) {
				mCamera = openFrontFacingCamera();
				mCamera.release();
			} else {
				mCamera = Camera.open();
				mCamera.release();
			}

		}
	}

	private void replayVideoRecording() {
		String filename = fileName + ext;
		String lastvideo = pathVideo + "/" + filename;
		Intent intentToPlayVideo = new Intent(Intent.ACTION_VIEW);
		intentToPlayVideo.setDataAndType(Uri.parse(lastvideo), "video/*");
		startActivity(intentToPlayVideo);
		this.finish();
	}

	private String getFilenameVideo() {
		String filepath = Environment.getExternalStorageDirectory().getPath();
		File file = new File(filepath, ECMLPath + VIDEO_RECORDER_FOLDER);
		if (!file.exists()) {
			file.mkdirs();
		}
		fileName = System.currentTimeMillis();
		pathVideo = file.getAbsolutePath();
		ext = file_exts[currentFormat];
		return (pathVideo + "/" + fileName + ext);
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
	}

	public void surfaceCreated(SurfaceHolder holder) {
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
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
	
}

