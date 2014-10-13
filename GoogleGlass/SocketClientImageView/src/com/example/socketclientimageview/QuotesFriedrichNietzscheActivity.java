package com.example.socketclientimageview;

import java.util.ArrayList;

import com.google.android.glass.widget.CardBuilder;

/**
 * Nietzsche quotes
 */
public class QuotesFriedrichNietzscheActivity extends QuotesActivity {

	@Override
	protected ArrayList<CardBuilder> setupCards() {
		ArrayList<CardBuilder> cards = new ArrayList<CardBuilder>();
		
		cards.add(new CardBuilder(this, CardBuilder.Layout.CAPTION)
			.setText("When you gaze long into an abyss,\nthe abyss also gazes into you.")
			.addImage(R.drawable.abyss));
		cards.add(new CardBuilder(this, CardBuilder.Layout.COLUMNS)
			.setText("He who fights with monsters should look to it \n" +
					"that he himself does not become a monster.")
			.addImage(R.drawable.black_swan_poster));
		cards.add(new CardBuilder(this, CardBuilder.Layout.TEXT)
			.setText("There are no facts, only interpretations."));
		cards.add(new CardBuilder(this, CardBuilder.Layout.TEXT)
			.setText("The mother of excess is not joy but joylessness."));
		cards.add(new CardBuilder(this, CardBuilder.Layout.TEXT)
			.setText("Convictions are more dangerous enemies of truth than lies."));
		
		CardBuilder card = null;
		int cardsSize = cards.size();
		for (int i = 0; i < cardsSize; i++) {
			card = cards.get(i);
			card.setFootnote("Friedrich Nietzsche");
			card.setTimestamp("Index: " +i);
		}
		
		return cards;
	}

}
