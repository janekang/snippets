package com.example.socketclientimageview;

import java.util.ArrayList;

import com.example.socketclientimageview.R;
import com.google.android.glass.widget.CardBuilder;

/**
 * Lao Zi quotes
 */
public class QuotesLaoZiActivity extends QuotesActivity {

	@Override
	protected ArrayList<CardBuilder> setupCards() {
		ArrayList<CardBuilder> cards = new ArrayList<CardBuilder>();
		
		cards.add(new CardBuilder(this, CardBuilder.Layout.TITLE)
			.setText("ß¾à¼å´â©. \nEminent goodness is like water.")
			.addImage(R.drawable.pool_water));
		cards.add(new CardBuilder(this, CardBuilder.Layout.TEXT)
			.setText("Whoever can bear the disgrace of the country is the ruler of the country. " +
					"Whoever can bear the misfortune of the world is the ruler of the world. " +
					"Truthful speech seems paradoxical."));
		cards.add(new CardBuilder(this, CardBuilder.Layout.COLUMNS)
			.setText("The name that can be named is not the eternal name.")
			.addImage(R.drawable.name_tag));
		cards.add(new CardBuilder(this, CardBuilder.Layout.TEXT)
			.setText("A sound man by not advancing himself stays the further ahead of himself. \n" +
					"By not confining himself to himself, he sustains himself outside himself; \n" +
					"By never being an end in himself he endlessly becomes himself."));
		cards.add(new CardBuilder(this, CardBuilder.Layout.ALERT)
			.setText("To be worn out is to be renewed.")
			.setIcon(R.drawable.light_bulb_sprout));
		
		CardBuilder card = null;
		int cardsSize = cards.size();
		for (int i = 1; i < cardsSize-1; i++) {
			card = cards.get(i);
			card.setFootnote("Lao Zi");
			card.setTimestamp("Index: " +i);
		}
		
		return cards;
	}
}
