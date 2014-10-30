package com.example.gglassqrcodescanner;

import java.io.IOException;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.AutoFocusCallback;
import android.util.Log;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

/**
 * A basic Camera preview class 
 * http://thorbek.net/online/2013/10/11/an-integrated-qr-scanner-in-android-activity/
 */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

	private SurfaceHolder mHolder;
    private Camera mCamera;
    private PreviewCallback previewCallback;
    private AutoFocusCallback autoFocusCallback;
 
    
    public CameraPreview(Context context, Camera camera, PreviewCallback previewCb,
    		AutoFocusCallback autoFocusCb) {
        super(context);
        mCamera = camera;
        previewCallback = previewCb;
        autoFocusCallback = autoFocusCb;
        
        mHolder = getHolder();
        mHolder.addCallback(this);
    }
 
    /*
     * The Surface has been created
     * Tell the camera where to draw the preview
     */
    public void surfaceCreated(SurfaceHolder holder) {
    	if (mCamera != null) {
	        try {
	            mCamera.setPreviewDisplay(holder);
	        } catch (IOException e) {
	            Log.d(FetchQRDataActivity.PACKAGE_NAME, "Error setting camera preview: " + e.getMessage());
	        }
    	}
    }
 
    /*
     * Camera preview released in activity
     */
    public void surfaceDestroyed(SurfaceHolder holder) {
    	if (mCamera != null) {
			try {
				this.getHolder().removeCallback(this);
				mCamera.stopPreview();
				mCamera.release();
			} catch (Exception e) {
				// Do nothing
			}
    	}
    }
 
    /*
     * If your preview can change or rotate, take care of those events
     * Make sure to stop the preview before resizing or reformatting
     */
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (mHolder.getSurface() == null){ // Preview surface does not exist
          return;
        }
 
        try {
            mCamera.stopPreview();
        } catch (Exception e){
          // Ignore, tried to stop a non-existent preview
        }

        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.setPreviewCallback(previewCallback);
            
            Camera.Parameters parameters = mCamera.getParameters();  
            parameters.setPreviewSize(640, 480);
            mCamera.setParameters(parameters);
            
            mCamera.startPreview();
            mCamera.autoFocus(autoFocusCallback);
        } catch (Exception e){
            Log.d(FetchQRDataActivity.PACKAGE_NAME, "Error starting camera preview: " + e.getMessage());
        }
    }
}
