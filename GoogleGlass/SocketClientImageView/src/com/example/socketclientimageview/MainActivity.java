package com.example.socketclientimageview;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;

import com.example.socketclientimageview.R;
import com.google.android.glass.view.WindowUtils;
import com.google.android.glass.widget.CardBuilder;
import com.google.android.glass.widget.CardScrollView;

/**
 * FOR GLASS
 */
public class MainActivity extends Activity {
	
	// Tag for card type and casting status
	public static final int SHOW_QUOTES = 47;
	public static final String CASTING = "casting";
	protected boolean isCasting;
	
	protected static String PACKAGE_NAME;
	protected View rootView;
	
	protected final Handler mHandler = new Handler();
	
	protected ArrayList<CardBuilder> mCards;
	protected CardScrollView mCardScrollView;
	protected CustomCardScrollAdapter mCardScrollAdapter;
	
	
	/*
	 * Create scrolling card deck with two cards
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(WindowUtils.FEATURE_VOICE_COMMANDS);
		getActionBar().hide();
		
		PACKAGE_NAME = getPackageName();
		rootView = getWindow().getDecorView().getRootView();
		isCasting = false;
		
		mCards = new ArrayList<CardBuilder>();
		mCards.add(new CardBuilder(this, CardBuilder.Layout.TEXT)
			.setText("Quotes from Friedrich Nietzsche"));
		mCards.add(new CardBuilder(this, CardBuilder.Layout.TEXT)
			.setText("Quotes from Lao Zi"));

		mCardScrollAdapter = new CustomCardScrollAdapter(mCards);
		
		mCardScrollView = new CardScrollView(this);
		mCardScrollView.setAdapter(mCardScrollAdapter);
		mCardScrollView.activate();
		mCardScrollView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				openOptionsMenu();
			}
		});
		setContentView(mCardScrollView);
	}
	
	/*
	 * Make sure to close sockets and network connections on quitting
	 */
	@Override
	protected void onDestroy() {
		if (isCasting) {
			BaseCastingNetworkActivity.stopCasting();
		}
		
		super.onDestroy();
	}
	
    /*
     * Create option menu
     */
    @Override
    public boolean onCreatePanelMenu(int featureId, Menu menu) {
        if (featureId == WindowUtils.FEATURE_VOICE_COMMANDS
        		|| featureId == Window.FEATURE_OPTIONS_PANEL) {        	
            getMenuInflater().inflate(R.menu.main, menu);
            return true;
        }
        // Pass through to super to setup touch menu
        return super.onCreatePanelMenu(featureId, menu);
    }
    
    /*
     * Configure option menu
     */
    @Override
    public boolean onPreparePanel(int featureId, View view, Menu menu) {
        if (featureId == WindowUtils.FEATURE_VOICE_COMMANDS
        		|| featureId == Window.FEATURE_OPTIONS_PANEL) {
        	BaseCastingNetworkActivity.onPreparePanelForCasting(featureId, view, menu);
        	
        	MenuItem returnToMain = menu.findItem(R.id.return_to_main_menu_item);
        	returnToMain.setEnabled(false);
        	returnToMain.setVisible(false);
        }
        
        return super.onPreparePanel(featureId, view, menu);
    }
    
    /*
     * Handle starting and ending connection
     */
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        if (featureId == WindowUtils.FEATURE_VOICE_COMMANDS
        		|| featureId == Window.FEATURE_OPTIONS_PANEL) {
            switch (item.getItemId()) {            	
                case R.id.start_casting_menu_item:
            		BaseCastingNetworkActivity.startCasting(PACKAGE_NAME, rootView);
            		isCasting = true;
                    break;
                    
                case R.id.stop_casting_menu_item:
                	BaseCastingNetworkActivity.stopCasting();
                	isCasting = false;
                    break;
                    
                case R.id.see_quotes_menu_item:
                	switch (mCardScrollView.getSelectedItemPosition()) {
                		case 0: // Friedrich Nietzsche
                			Log.d(PACKAGE_NAME, "Immersion to Friedrich Nietzsche quotes");
                			
                			mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                	Intent intent = new Intent(MainActivity.this, QuotesFriedrichNietzscheActivity.class);
                                	intent.putExtra(CASTING, isCasting);
                                	startActivityForResult(intent, SHOW_QUOTES);
                                }
                            });
                			
                			break;
                			
                		case 1: // Lao Zi
                			Log.d(PACKAGE_NAME, "Immersion to Lao Zi quotes");
                			
                			mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                	Intent intent = new Intent(MainActivity.this, QuotesLaoZiActivity.class);
                                	intent.putExtra(CASTING, isCasting);
                                	startActivityForResult(intent, SHOW_QUOTES);
                                }
                            });
                			
                			break;
                	}
                	break;
                	
                default:
                    return true;
            }
            return true;
        }
        // Good practice to pass through to super if not handled
        return super.onMenuItemSelected(featureId, item);
    }
    
    /*
     * If user enabled casting in quote cards and did not quit it, continue casting
     * 
     * @param requestCode: tag for intent type
     * @param resultCode: either RESULT_OK or RESULT_CANCELLED
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if (requestCode == SHOW_QUOTES) {
    		if (resultCode == RESULT_OK) {

        		Bundle b = data.getExtras();
        		if (b != null) {
        			if (isCasting = b.getBoolean(CASTING)) {
        				BaseCastingNetworkActivity.switchActivity(rootView);
        			}
        			
        			//isCasting = b.getBoolean(CASTING);
        		}
    		}
    		else {
    			Log.e(PACKAGE_NAME, "Comeback from quote cards failed");
    		}
    	}
    }
    
}
