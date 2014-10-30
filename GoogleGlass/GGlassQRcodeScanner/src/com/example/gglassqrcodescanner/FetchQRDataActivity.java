package com.example.gglassqrcodescanner;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.gglassqrcodescanner.R;
import com.google.android.glass.widget.CardBuilder;

/**
 * Handle QR code scanning
 * Must import zbar libraries to function
 * Modified to fit the Google Glass, original code was for Android phone
 * @author Jane Kang, Software Engineering Intern, Augmedix
 * 
 * Copyright held by Augmedix
 * http://thorbek.net/online/2013/10/11/an-integrated-qr-scanner-in-android-activity/
 */
public class FetchQRDataActivity extends Activity {
	public static final String DEBUG = "debug";
	public static String PACKAGE_NAME;

	private Camera mCamera;
	private CameraPreview mPreview;
	private Handler mAutoFocusHandler;
	private ImageScanner mImageScanner;
	private TextView mQRTitleTextview;
	private boolean previewing;
	
	static { // PART OF ZBAR LIBRARY
		System.loadLibrary("iconv");
	}

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qr_code_reader);
		PACKAGE_NAME = getPackageName();
		previewing = true;
		
		mQRTitleTextview = (TextView) findViewById(R.id.QR_CODE_SCANNER);
		mQRTitleTextview.setText(getResources().getString(R.string.scan_instruction));
		
		mAutoFocusHandler = new Handler();
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		for (int i = 0; i < 3; i++) {
			mCamera = getCameraInstance();
			if (mCamera != null) {
				break;
			}
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				Log.e(DEBUG, e.toString());
			}
		}
		if (mCamera == null) {
			try {
				releaseCamera();
			} catch (Exception e) {
				Log.e(DEBUG, e.toString());
			}
			
			// No camera, warn user
			CardBuilder notification = new CardBuilder(this, CardBuilder.Layout.ALERT);
			notification.addImage(R.drawable.ic_warning_150);
			notification.setText("Camera Locked");
			
			ViewGroup rootView = (ViewGroup) getWindow().getDecorView().findViewById(android.R.id.content);
			rootView.addView(notification.getView());
		}

		mImageScanner = new ImageScanner();
		mImageScanner.setConfig(0, Config.X_DENSITY, 3);
		mImageScanner.setConfig(0, Config.Y_DENSITY, 3);

		mPreview = new CameraPreview(this, mCamera, previewCb, autoFocusCB);
		FrameLayout preview = (FrameLayout) findViewById(R.id.QR_CODE_SCANNER_FL_CAMERA_PREVIEW);
		preview.addView(mPreview);
	}

	@Override
	protected void onPause() {
		super.onPause();
		releaseCamera();
		finish();
	}

	/*
	 * A safe way to get an instance of the Camera object.
	 */
	public static Camera getCameraInstance() {
		Camera camera = null;
		try {
			camera = Camera.open();
		} catch (Exception e) {
			Log.e(DEBUG, e.toString());
		}
		return camera;
	}

	private void releaseCamera() {
		if (mCamera != null) {
			previewing = false;
			mCamera.setPreviewCallback(null);
			mCamera.stopPreview();
			mCamera.release();
		}
	}

	private Runnable doAutoFocus = new Runnable() {
		public void run() {
			if (previewing) {
				mCamera.autoFocus(autoFocusCB);
			}
		}
	};

	PreviewCallback previewCb = new PreviewCallback() {
		public void onPreviewFrame(byte[] data, Camera camera) {
			Camera.Parameters parameters = camera.getParameters();
			Size size = parameters.getPreviewSize();
			Image barcode = new Image(size.width, size.height, "Y800");
			barcode.setData(data);
			int result = mImageScanner.scanImage(barcode);

			if (result != 0) {
				previewing = false;
				mCamera.setPreviewCallback(null);
				mCamera.stopPreview();

				SymbolSet syms = mImageScanner.getResults();
				for (Symbol sym : syms) {
					String QREncryptedText = sym.getData();
					Intent intent = new Intent();
					intent.putExtra(MainActivity.QR_RESPONSE, QREncryptedText);
					setResult(RESULT_OK, intent);
					finish();
				}
			}
		}

	};

	AutoFocusCallback autoFocusCB = new AutoFocusCallback() {
		public void onAutoFocus(boolean success, Camera camera) {
			mAutoFocusHandler.postDelayed(doAutoFocus, 1000);
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_CAMERA) {
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}