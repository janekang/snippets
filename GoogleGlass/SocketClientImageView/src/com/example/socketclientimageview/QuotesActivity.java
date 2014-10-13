package com.example.socketclientimageview;

import java.util.ArrayList;

import com.example.socketclientimageview.R;
import com.google.android.glass.view.WindowUtils;
import com.google.android.glass.widget.CardBuilder;
import com.google.android.glass.widget.CardScrollView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;

/**
 * Display quotes in card scroller
 */
public abstract class QuotesActivity extends Activity {
	
	protected static String PACKAGE_NAME;
	protected View rootView;
	
	protected boolean isCasting;
	
	protected ArrayList<CardBuilder> mCards;
	protected CardScrollView mCardScrollView;
	protected CustomCardScrollAdapter mCardScrollAdapter;
	
	
	//////////////////////////////////////////////////////////////////////////////////
	// METHODS TO BE OVERRIDEN
	//////////////////////////////////////////////////////////////////////////////////
	
	/*
	 * Configure the cards to be displayed in card scroller
	 */
	protected abstract ArrayList<CardBuilder> setupCards();
	
	
	//////////////////////////////////////////////////////////////////////////////////
	// IMPLEMENTED METHODS
	//////////////////////////////////////////////////////////////////////////////////
	
	/*
	 * Create scrolling card deck with quote cards
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(WindowUtils.FEATURE_VOICE_COMMANDS);
		getActionBar().hide();
		
		PACKAGE_NAME = getPackageName();
		rootView = getWindow().getDecorView().getRootView();
		
		mCards = setupCards();
		mCardScrollAdapter = new CustomCardScrollAdapter(mCards);
		
		mCardScrollView = new CardScrollView(this);
		mCardScrollView.setAdapter(mCardScrollAdapter);
		mCardScrollView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				openOptionsMenu();
			}
		});
		mCardScrollView.activate();
		setContentView(mCardScrollView);
	}
	
	/*
	 * Activity coming to the front, last function called before activity fully operating
	 */
	@Override
	protected void onResume() {
		super.onResume();
		
		// Check whether screenshot should be enabled immediately
		Bundle b = getIntent().getExtras();
        if (b != null)
        {
        	isCasting = b.getBoolean(MainActivity.CASTING);
        	Log.d(PACKAGE_NAME, "Casting passed on from main: "+isCasting);
        	
        	if (isCasting) {
        		Log.d(PACKAGE_NAME, "is quotes root view null: "+(rootView == null));
                BaseCastingNetworkActivity.switchActivity(rootView);
        	}
        }
        else {
        	isCasting = false;
        }
	}
	
	/*
	 * Communicate to MainActivity whether casting was on
	 */
	@Override
	protected void onStop() {
		// Notify MainActivity of the casting status
    	Intent resultIntent = new Intent().putExtra(MainActivity.CASTING, isCasting);
    	setResult(RESULT_OK, resultIntent);
    	Log.d(PACKAGE_NAME, "Back to main menu");
		
		super.onStop();
	}
	
	/*
	 * Back button pressed, activity closing
	 * Communicate to MainActivity whether casting was on
	 */
	@Override
	public void onBackPressed() {
		// Notify MainActivity of the casting status
    	Intent resultIntent = new Intent().putExtra(MainActivity.CASTING, isCasting);
    	setResult(RESULT_OK, resultIntent);
    	Log.d(PACKAGE_NAME, "Back to main menu");
		
		super.onBackPressed();
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
        // Pass through to super to setup touch menu.
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
        	
        	MenuItem seeQuotes = menu.findItem(R.id.see_quotes_menu_item);
        	seeQuotes.setEnabled(false);
        	seeQuotes.setVisible(false);
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
                    
                case R.id.return_to_main_menu_item:
                	finish();
                	break;
                	
                default:
                    return true;
            }
            return true;
        }
        // Good practice to pass through to super if not handled
        return super.onMenuItemSelected(featureId, item);
    }
}
