package com.example.socketclientimageview;


import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import com.example.socketclientimageview.R;
import com.google.android.glass.view.WindowUtils;

/**
 * FOR GLASS
 * Empty static activity class to handle casting only
 * Creates a thread to handle network connection,
 * and another thread to send regular screenshot requests
 * 
 * No UI, no menus
 */
public class BaseCastingNetworkActivity extends Activity {
	
	public static final int START_CONNECTION = 1;
	public static final int SEND_SCREENSHOT = 2;
	public static final int QUIT_CONNECTION = 3;
	public static String PACKAGE_NAME;

	protected static CastingNetworkHandlerThread mHandlerThread;
	protected static CastingNetworkHandler mHandler;
	protected static CastingAutomateRequestThread mAutomateRequestThread;
	
    
    /*
     * Configure option menu for casting
     */
    public static void onPreparePanelForCasting(int featureId, View view, Menu menu) {
        if (featureId == WindowUtils.FEATURE_VOICE_COMMANDS
        		|| featureId == Window.FEATURE_OPTIONS_PANEL) {
        	MenuItem startCasting = menu.findItem(R.id.start_casting_menu_item);
        	MenuItem stopCasting = menu.findItem(R.id.stop_casting_menu_item);
        
            if (mHandler != null && mHandler.isCasting()) {
            	startCasting.setEnabled(false);
            	startCasting.setVisible(false);
            	stopCasting.setEnabled(true);
            	stopCasting.setVisible(true);
            }
            else {
            	startCasting.setEnabled(true);
            	startCasting.setVisible(true);
            	stopCasting.setEnabled(false);
            	stopCasting.setVisible(false);
            }
        }
    }
    
    /*
     * Start background thread, open connection to phone
     * Start background thread to send screenshot regularly
     */
    public static void startCasting(String packageName, View rootView) {
    	PACKAGE_NAME = packageName;
    	
		mHandlerThread = new CastingNetworkHandlerThread(); 
		mHandlerThread.start();
		mHandler = new CastingNetworkHandler(mHandlerThread.getLooper());
		
		Message msg = mHandler.obtainMessage(START_CONNECTION);
			Bundle bundle = new Bundle();
			bundle.putString("dst", "10.20.30.223:8080");
		msg.setData(bundle);
		mHandler.sendMessage(msg);
		
		// For regular screenshot
		mAutomateRequestThread = new CastingAutomateRequestThread(rootView, mHandler);
		mAutomateRequestThread.start();
    }
    
    /*
     * Close sockets and quit background threads
     */
    public static void stopCasting() {
    	if (mAutomateRequestThread != null) {
    		mAutomateRequestThread.cancel();
    		mAutomateRequestThread = null;
    	}
    	
    	if (mHandler != null) {
    		Message msg = mHandler.obtainMessage(QUIT_CONNECTION);
    		mHandler.sendMessage(msg);
    		mHandler = null;
    	}
    	if (mHandlerThread != null) {
			mHandlerThread.quitSafely();
			mHandlerThread = null;
    	}
    }
    
    /*
     * Activity changed, update screenshot request connection
     * Assume same package
     */
    public static void switchActivity(View rootView) {
    	if (mAutomateRequestThread != null) {
    		mAutomateRequestThread.switchActivity(rootView);
    	}
    }
}
