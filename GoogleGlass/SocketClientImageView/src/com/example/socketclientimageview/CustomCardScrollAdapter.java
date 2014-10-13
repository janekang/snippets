package com.example.socketclientimageview;

import java.util.ArrayList;

import android.view.View;
import android.view.ViewGroup;

import com.google.android.glass.widget.CardBuilder;
import com.google.android.glass.widget.CardScrollAdapter;


/**
 * Card Scroll Adapter
 */
public class CustomCardScrollAdapter extends CardScrollAdapter {
	
	private ArrayList<CardBuilder> mCards;
	
	
	public CustomCardScrollAdapter(ArrayList<CardBuilder> cards) {
		mCards = cards;
	}

	@Override
	public int getCount() {
		return mCards.size();
	}

	@Override
	public Object getItem(int position) {
		return mCards.get(position);
	}

	@Override
	public int getPosition(Object arg0) {
		return mCards.indexOf(arg0);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return mCards.get(position).getView(convertView, parent);
	}
}
