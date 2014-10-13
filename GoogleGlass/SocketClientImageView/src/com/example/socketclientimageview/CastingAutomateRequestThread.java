package com.example.socketclientimageview;

import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;

/**
 * FOR GLASS
 * Regularly captures screen
 * If screen content changed, send request to screenshot
 */
public class CastingAutomateRequestThread extends Thread {
	
	private View rootView;
	private CastingNetworkHandler mHandler;
	
	
	/*
	 * Constructor
	 */
	public CastingAutomateRequestThread(View root, CastingNetworkHandler handler) {
		super("CastingAutomateRequestThread");
		
		if (root == null) {
			Log.e(BaseCastingNetworkActivity.PACKAGE_NAME, "Invalid root view");
			rootView = null;
		}
		else {
			rootView = root;
		}
		
		if (handler == null) {
			Log.e(BaseCastingNetworkActivity.PACKAGE_NAME, "Invalid network handler");
			mHandler = null;
		}
		else {
			mHandler = handler;
		}
	}
	
	/*
	 * Loop, capture screen
	 * If screen content changed, send data to network thread
	 */
	@Override
	public void run() {
		Log.d(BaseCastingNetworkActivity.PACKAGE_NAME, "AutomateRequestThread started");
		
		try {
			Bitmap lastScreenshot = null;
			
	    	while (rootView != null) {
	    		Bitmap screenshot = renderScreenshotBitmap(rootView);
	    		
	    		// Send screenshot request if new screenshot is different from old one
	    		if ((mHandler != null && mHandler.isCasting())
	    				&& (lastScreenshot == null
	    					|| !screenshot.sameAs(lastScreenshot))) {
	    			lastScreenshot = Bitmap.createBitmap(screenshot);

	        		Message msg = mHandler.obtainMessage(BaseCastingNetworkActivity.SEND_SCREENSHOT);
	    				Bundle bundle = new Bundle();
	    				bundle.putByteArray(CastingNetworkHandler.SCREENSHOT, bitmapToByteArray(lastScreenshot));
	    			msg.setData(bundle);
	    			mHandler.sendMessage(msg);
	    			Log.d(BaseCastingNetworkActivity.PACKAGE_NAME, "AutomateRequestThread sends request for screenshot");
	    		}
	    	}
		} catch (NullPointerException e) {
			// Casting cancelled by user, do nothing
			Log.d(BaseCastingNetworkActivity.PACKAGE_NAME, "AutomateRequestThread ended, cancelled by user\n"+e.toString());
		} catch (Exception e) {
			Log.e(BaseCastingNetworkActivity.PACKAGE_NAME, "AutomateRequestThread ended, Exception: "+e.toString());
		}
	}
	
	/*
	 * Quit the thread by quitting the loop in run()
	 */
	public void cancel() {
		mHandler = null;
		rootView = null;
	}
	
	/*
	 * Activity switched, update root view
	 */
	public void switchActivity(View root) {
		Log.d(BaseCastingNetworkActivity.PACKAGE_NAME, "AutomateRequestThread changed root view reference");
		rootView = root;
	}
	
	/*
	 * Take screenshot and make it bitmap format
	 */
	private Bitmap renderScreenshotBitmap(View view) {
		Bitmap bm = null;

		view.layout(0, 0, 640, 360);
		view.setDrawingCacheEnabled(true);
		bm = Bitmap.createBitmap(view.getDrawingCache()); // BUG DRAWING CACHE RETURNED AS NULL
		view.setDrawingCacheEnabled(false);

		// **LOG FOR DEBUG
		/*if (bm == null) {
			Log.d(BaseCastingNetworkActivity.PACKAGE_NAME, "Rendered bitmap is null");
		}
		else {
			Log.d(BaseCastingNetworkActivity.PACKAGE_NAME, "Rendered bitmap bytecount: "+bm.getByteCount());
		}*/
		
		return bm;
	}
	
	/*
	 * Make bitmap into byte array format
	 */
	private byte[] bitmapToByteArray(Bitmap bm) {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
		
		return byteArrayOutputStream.toByteArray();
	}
}
