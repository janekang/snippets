package com.example.voicecast_gglasssample;

import java.util.ArrayList;

import android.view.View;
import android.view.ViewGroup;

import com.google.android.glass.app.Card;
import com.google.android.glass.widget.CardScrollAdapter;

public class DeviceCardScrollAdapter extends CardScrollAdapter {
	private ArrayList<Card> mCards;
	
	public void setCardList(ArrayList<Card> cardList) {
		mCards = cardList;
	}
	
	@Override
    public int getPosition(Object item) {
		assert mCards != null;
        return mCards.indexOf(item);
    }

    @Override
    public int getCount() {
    	assert mCards != null;
        return mCards.size();
    }

    @Override
    public Object getItem(int position) {
    	assert mCards != null;
        return mCards.get(position);
    }

    @Override
    public int getViewTypeCount() {
        return Card.getViewTypeCount();
    }

    @Override
    public int getItemViewType(int position){
    	assert mCards != null;
        return mCards.get(position).getItemViewType();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	assert mCards != null;
        return mCards.get(position).getView(convertView, parent);
    }

}
