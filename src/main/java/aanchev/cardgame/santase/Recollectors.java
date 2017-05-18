package aanchev.cardgame.santase;

import static aanchev.cardgame.Randomness.chance;
import static java.util.Collections.reverse;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import aanchev.cardgame.model.Deck;
import aanchev.cardgame.santase.Santase.Player;
import aanchev.cardgame.santase.Santase.State;

public class Recollectors {

	public static <V> void let(V value, Consumer<V> use) {
		if (value != null)
			use.accept(value);
	}
	
	public static <V> List<List<V>> groupEvery(int n, List<V> of) {
		List<List<V>> grouped = new LinkedList<>();
		
		for (int i=0; i<of.size(); i+=n) {
			List<V> group = new ArrayList<>();
			
			for (int j=0; j<n; j++)
				group.add(of.get(i+j));
			
			grouped.add(group);
		}
		
		return grouped;
	}

	
	public static class CasualRecollector implements BiConsumer<Deck, State> {
		
		private double regularTakeChance;
		
		
		public CasualRecollector() {
			this(0d);
		}
		
		public CasualRecollector(double regularTakeChance) {
			this.regularTakeChance = regularTakeChance;
		}

		
		public void accept(Deck deck, State state) {
			List<Player> players = new LinkedList<Player>(state.playerHands.keySet());
			
			//shuffle(players);
			
			players.forEach(player -> {
				let(state.playerWinPiles.get(player),
					wonCards -> {
						groupEvery(2, wonCards).stream()
							.flatMap(cards -> {
								if (chance(regularTakeChance))
									reverse(cards);
								return cards.stream();
							})
							.forEach(deck::putOnTop);
					});
				
				let(state.playerHands.get(player),
					deck::putOnTop);
			});
		}
	}
}
