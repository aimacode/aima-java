package aima.core.environment.twoply;

import aima.core.environment.map.MoveToAction;
import aima.core.search.adversarial.Game;

import java.util.List;

public class TwoPlyGame implements Game<TwoPlyGameState, MoveToAction, String> {
	
	@Override
	public TwoPlyGameState getInitialState() {
		return new TwoPlyGameState("A");
	}
	
	@Override
	public String[] getPlayers() {
		return new String[]{"MAX", "MIN"};
	}
	
	@Override
	public String getPlayer(TwoPlyGameState state) {
		switch (state.getLocation()) {
			case "B":
			case "C":
			case "D":
				return "MIN";
			default:
				return "MAX";
		}
	}
	
	
	@Override
	public List<MoveToAction> getActions(TwoPlyGameState state) {
		return new TwoPlyGameTree().getActions(state);
	}
	
	@Override
	public TwoPlyGameState getResult(TwoPlyGameState state, MoveToAction action) {
		return new TwoPlyGameState(action.getToLocation());
	}
	
	@Override
	public boolean isTerminal(TwoPlyGameState state) {
		return (state.getLocation().charAt(0) > 'D');
	}
	
	@Override
	public double getUtility(TwoPlyGameState state, String player) {
		switch (state.getLocation()) {
			// B
			case "E":
				return 3;
			case "F":
				return 12;
			case "G":
				return 8;
			// C
			case "H":
				return 2;
			case "I":
				return 4;
			case "J":
				return 6;
			// D
			case "K":
				return 14;
			case "L":
				return 5;
			case "M":
				return 2;
			default:
				throw new IllegalArgumentException("State " + state.getLocation() + " unexpected.");
		}
	}
}
