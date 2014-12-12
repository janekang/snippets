/*
Copyright (c) 2011, Sony Mobile Communications Inc.
Copyright (c) 2014, Sony Corporation

 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions are met:

 * Redistributions of source code must retain the above copyright notice, this
 list of conditions and the following disclaimer.

 * Redistributions in binary form must reproduce the above copyright notice,
 this list of conditions and the following disclaimer in the documentation
 and/or other materials provided with the distribution.

 * Neither the name of the Sony Mobile Communications Inc.
 nor the names of its contributors may be used to endorse or promote
 products derived from this software without specific prior written permission.

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.example.sony.smarteyeglass.quotes;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.example.sony.smarteyeglass.quotes.R;
import com.sony.smarteyeglass.extension.util.SmartEyeglassControlUtils;
import com.sonyericsson.extras.liveware.aef.control.Control;
import com.sonyericsson.extras.liveware.aef.control.Control.Intents;
import com.sonyericsson.extras.liveware.extension.util.control.ControlExtension;
import com.sonyericsson.extras.liveware.extension.util.control.ControlListItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Displays a list of name cards
 * User can tap on a name card to see that person's quotes
 */
public final class QuotesControl extends ControlExtension {

    // String arrays with data to be displayed in gallery.
    private ArrayList<String> quotedPeople;
    private ArrayList<String> quotedPeopleIntro;
    private ArrayList<ArrayList<String>> quotes;

    // Control Utility, handles animation
    private final SmartEyeglassControlUtils utils;

    // Index position of displayed item
    private int quotePosition = 0;
    private int namePosition = 0;

    // Flag for name view or quote view
    private boolean showingDetail;

    /*
     * Creates Advanced Layout control.
     *
     * @param context: application context
     * @param hostAppPackageName: package name
     */
    public QuotesControl(final Context context, final String hostAppPackageName) {
        super(context, hostAppPackageName);
        utils = new SmartEyeglassControlUtils(hostAppPackageName, null);
        utils.activate(context);
        setup();
    }

    @Override
    public void onDestroy() {
        utils.deactivate();
    }

    @Override
    public void onResume() {
        int startPosition = 0;
        showingDetail = false;

        // Set the layout to display
        showLayout(R.layout.smarteyeglass_quotes_names_gallery, null);

        // Indicate total number of cards for the layout
        sendListCount(R.id.names_gallery, quotedPeople.size());

        // Save current index for future reference
        namePosition = startPosition;

        // Indicate card index to start
        sendListPosition(R.id.names_gallery, startPosition);

        // For scrollable text view
        utils.sendTextViewLayoutId(R.id.body);
    }

    @Override
    /*
     * @param layoutReference: current layout
     * @param listItemPosition: current card index
     * @return matching card list
     */
    public void onRequestListItem(
            final int layoutReference, final int listItemPosition) {
        Log.d(Constants.LOG_TAG,
                "onRequestListItem() - position " + listItemPosition);
        
        if (layoutReference != -1 && listItemPosition != -1) {
        	ControlListItem item = null;
        	
        	if (layoutReference == R.id.names_gallery) {
        		item = createControlListItem(listItemPosition);
        	}
        	else if (layoutReference == R.id.quotes_gallery) {
        		item = createQuoteListItem(namePosition, listItemPosition);
        	}
        	
        	if (item != null) {
        		sendListItem(item);
        	}
        }
    }

    /*
     * Save the last "selected" position
     * @param listItem: current card index
     */
    @Override
    public void onListItemSelected(final ControlListItem listItem) {
        super.onListItemSelected(listItem);
        
        if (!showingDetail) {
        	namePosition = listItem.listItemPosition;
        }
        else {
        	quotePosition = listItem.listItemPosition;
        }
    }

    /*
     * User clicked on the card
     * 
     * @param listItem: current card list
     * @param clickType: long or short click
     * @param itemLayoutReference: current layout id
     */
    @Override
    public void onListItemClick(final ControlListItem listItem, final int clickType,
            final int itemLayoutReference) {
    	
    	if (clickType == Intents.CLICK_TYPE_LONG) {
            return;
        }
    	
    	if (!showingDetail) {
	        namePosition = listItem.listItemPosition;
	        quotePosition = 0;
	
	        utils.moveLowerLayer(R.layout.smarteyeglass_quotes_quotes_gallary, null);
	        sendListCount(R.id.quotes_gallery, quotes.get(namePosition).size());
	        sendListPosition(R.id.quotes_gallery, quotePosition);
	        utils.sendTextViewLayoutId(R.id.body);
	        showingDetail = true;
    	}
    }

    /*
     * Control button action
     * 
     * @param action: press, release, etc.
     * @param keyCode: activated button type
     * @param timeStamp
     */
    @Override
    public void onKey(final int action, final int keyCode, final long timeStamp) {
        if (action == Intents.KEY_ACTION_RELEASE
                && keyCode == Control.KeyCodes.KEYCODE_BACK) {
            // User pressed back button
            
            if (!showingDetail) {
                stopRequest();
            }
            else { // Go back to name cards
    	        showingDetail = false;
    	        
    	        ControlListItem item = createControlListItem(namePosition);
                utils.moveUpperLayer(item.dataXmlLayout, item.layoutData);
                showLayout(R.layout.smarteyeglass_quotes_names_gallery, null);
                sendListCount(R.id.names_gallery, quotedPeople.size());
                sendListPosition(R.id.names_gallery, namePosition);
                utils.sendTextViewLayoutId(R.id.body);
                sendListItem(item);
            }
        }
    }

    /*
     * User tapped on the touchpad
     * 
     * @param action: single tap, etc.
     * @param timestamp
     */
    @Override
    public void onTap(final int action, final long timeStamp) {
        if (action == Control.TapActions.SINGLE_TAP) {
            Log.d(Constants.LOG_TAG, "tapactions:" + action);
            
            if (showingDetail) {
            	// Card immersion to quote cards
            	ControlListItem item = createQuoteListItem(namePosition, quotePosition);
                utils.moveLowerLayer(item.dataXmlLayout, item.layoutData);
                showLayout(R.layout.smarteyeglass_quotes_quotes_gallary, null);
                sendListCount(R.id.quotes_gallery, quotes.get(namePosition).size());
                sendListPosition(R.id.quotes_gallery, quotePosition);
                utils.sendTextViewLayoutId(R.id.body);
                sendListItem(item);

                showingDetail = false;
            }
        }
    }
    
    /*
     * Setup card contents
     */
    private void setup() {
        quotedPeople = new ArrayList<String>();
        quotedPeople.add("Friedrich Nietzsche");
        quotedPeople.add("Lao Tzu");
        quotedPeople.add("Ayn Rand");
        
        quotedPeopleIntro = new ArrayList<String>();
        quotedPeopleIntro.add("Beyond Good and Evil");
        quotedPeopleIntro.add("Tao Te Ching");
        quotedPeopleIntro.add("Atlas Shrugged");
        
        quotes = new ArrayList<ArrayList<String>>();
	        ArrayList<String> nietzscheQuotes = new ArrayList<String>();
	        nietzscheQuotes.add("When you gaze long into the abyss, the abyss also gazes into you");
	        nietzscheQuotes.add("He who fights with monsters should look to it that he himself does not become a monster");
	        ArrayList<String> laotzuQuotes = new ArrayList<String>();
	        laotzuQuotes.add("When I let go of what I am, I become what I might be");
	        laotzuQuotes.add("The softest things in the world overcome the hardest things in the world");
	        ArrayList<String> aynRandQuotes = new ArrayList<String>();
	        aynRandQuotes.add("If you don't know, the thing to do is not to get scared, but to learn");
	        aynRandQuotes.add("There are no contradictions. If you find one, check your premises");
	        aynRandQuotes.add("But you see, the measure of hell you're able to endure is the measure of your love");
	    quotes.add(nietzscheQuotes);
        quotes.add(laotzuQuotes);
        quotes.add(aynRandQuotes);
    }

    /*
     * Creates name cards list object for the specified position.
     *
     * @param position: card index
     * @return the name card at the indicated index
     */
    private ControlListItem createControlListItem(final int position) {
        Log.d(Constants.LOG_TAG, "position = " + position);

        ControlListItem item = new ControlListItem();
        item.layoutReference = R.id.names_gallery;
        item.dataXmlLayout = R.layout.smarteyeglass_item_gallery;
        item.listItemId = position;
        item.listItemPosition = position;

        List<Bundle> list = new ArrayList<Bundle>();

        // Header data
        Bundle headerBundle = new Bundle();
        headerBundle.putInt(Intents.EXTRA_LAYOUT_REFERENCE, R.id.title);
        headerBundle.putString(Intents.EXTRA_TEXT, quotedPeople.get(position));
        list.add(headerBundle);

        // Body data
        Bundle bodyBundle = new Bundle();
        bodyBundle.putInt(Intents.EXTRA_LAYOUT_REFERENCE, R.id.body);
        bodyBundle.putString(Intents.EXTRA_TEXT, quotedPeopleIntro.get(position));
        list.add(bodyBundle);

        item.layoutData = list.toArray(new Bundle[list.size()]);
        return item;
    }
    
    /*
     * Creates quote cards list object for the specified position.
     *
     * @param position: card index
     * @return the quote card at the indicated index
     */
    private ControlListItem createQuoteListItem(final int namePosition, final int position) {
        Log.d(Constants.LOG_TAG, "position = " + position);

        ControlListItem item = new ControlListItem();
        item.layoutReference = R.id.quotes_gallery;
        item.dataXmlLayout = R.layout.smarteyeglass_item_gallery;
        item.listItemId = position;
        item.listItemPosition = position;

        List<Bundle> list = new ArrayList<Bundle>();

        // Header data
        Bundle headerBundle = new Bundle();
        headerBundle.putInt(Intents.EXTRA_LAYOUT_REFERENCE, R.id.title);
        headerBundle.putString(Intents.EXTRA_TEXT, quotedPeople.get(namePosition));
        list.add(headerBundle);

        // Body data
        Bundle bodyBundle = new Bundle();
        bodyBundle.putInt(Intents.EXTRA_LAYOUT_REFERENCE, R.id.body);
        bodyBundle.putString(Intents.EXTRA_TEXT, quotes.get(namePosition).get(position));
        list.add(bodyBundle);

        item.layoutData = list.toArray(new Bundle[list.size()]);
        return item;
    }
}
