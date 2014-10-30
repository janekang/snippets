package com.example.gglassqrcodescanner;

import java.util.Calendar;
import com.google.android.glass.view.WindowUtils;
import com.google.android.glass.widget.CardBuilder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;


/**
 * Scan QR code, display the content on the main card
 * Assume QR code contents to be strings
 * @author Jane Kang, Software Engineering Intern, Augmedix
 */
public class MainActivity extends Activity {
	
	public static final int SHOW_DEMO = 47;
	public static final int FETCH_IP = 36;
	public static final String DEBUG = "debug";
	public static final String QR_RESPONSE = "qr_response";
	
	private CardBuilder mainCard;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(WindowUtils.FEATURE_VOICE_COMMANDS);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		mainCard = new CardBuilder(this, CardBuilder.Layout.TEXT);
		mainCard.setFootnote("QR code content");
		mainCard.setText("");
		mainCard.setTimestamp("No QR code scanned");
		setContentView(mainCard.getView());
	}
	
    @Override
    public boolean onCreatePanelMenu(int featureId, Menu menu) {
        if (featureId == WindowUtils.FEATURE_VOICE_COMMANDS
        		|| featureId == Window.FEATURE_OPTIONS_PANEL) {        	
            getMenuInflater().inflate(R.menu.main, menu);
            return true;
        }

        return super.onCreatePanelMenu(featureId, menu);
    }
    
    @Override
    public boolean onPreparePanel(int featureId, View view, Menu menu) {
        if (featureId == WindowUtils.FEATURE_VOICE_COMMANDS
        		|| featureId == Window.FEATURE_OPTIONS_PANEL) {
        	// Nothing to do
        }
        
        return super.onPreparePanel(featureId, view, menu);
    }
    
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        if (featureId == WindowUtils.FEATURE_VOICE_COMMANDS
        		|| featureId == Window.FEATURE_OPTIONS_PANEL) {
            switch (item.getItemId()) {      
            	case R.id.scan_qr_code_menu_item: // Launch QR code reader
            		Intent qrcodeIntent = new Intent(MainActivity.this, FetchQRDataActivity.class);
            		startActivityForResult(qrcodeIntent, FETCH_IP);
            		break;
            	
            	case R.id.clear_menu_item:
            		mainCard.setText("");
            		mainCard.setTimestamp("No QR code scanned");
        			setContentView(mainCard.getView());
            		break;
                	
                default:
                    return true;
            }
            return true;
        }

        return super.onMenuItemSelected(featureId, item);
    }
    
    /*
     * If user enabled casting in demo cards and did not quit it, continue casting
     * 
     * @param requestCode: tag for intent type
     * @param resultCode: either RESULT_OK or RESULT_CANCELLED
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if (requestCode == FETCH_IP) {
    		if (resultCode == RESULT_OK) {
    			String result = data.getStringExtra(QR_RESPONSE);
    			mainCard.setText(result);
    			mainCard.setTimestamp(currentDateTime());
    			setContentView(mainCard.getView());
    			Log.d(DEBUG, "QR code scanned: "+result);
        	}
    		else {
    			postToast("Failed to read QR code");
    			Log.e(DEBUG, "Comeback from QR code reader failed");
    		}
    	}
    }
    
    /*
     * @return current date and time on device
     * ISO 8601 format, local time
     */
    private String currentDateTime() {
    	String currentDate = "";
    	String currentTime = "";
    	int temp = -1;
    	
    	Calendar c = Calendar.getInstance();
    	currentDate += c.get(Calendar.YEAR)+"-";
    	
    	temp = c.get(Calendar.MONTH);
    	if (temp < 10) {
    		currentDate += "0";
    	}
    	currentDate += temp+"-";
    	
    	temp = c.get(Calendar.DATE);
    	if (temp < 10) {
    		currentDate += "0";
    	}
    	currentDate += temp;
    	
    	temp = c.get(Calendar.HOUR_OF_DAY);
    	if (temp < 10) {
    		currentTime += "0";
    	}
    	currentTime += temp+":";
    	
    	temp = c.get(Calendar.MINUTE);
    	if (temp < 10) {
    		currentTime += "0";
    	}
    	currentTime += temp+":";
    	
    	temp = c.get(Calendar.SECOND);
    	if (temp < 10) {
    		currentTime += "0";
    	}
    	currentTime += temp;
    	
    	return currentDate+"T"+currentTime;
    }
    
	/*
	 * Post toasts 
	 */
	private void postToast(String message) {
		Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
		toast.show();
	}
}
