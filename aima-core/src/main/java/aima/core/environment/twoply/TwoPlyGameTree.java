package aima.core.environment.twoply;

import aima.core.environment.map.ExtendableMap;
import aima.core.environment.map.Map;
import aima.core.environment.map.MoveToAction;

import java.util.ArrayList;
import java.util.List;

public class TwoPlyGameTree {
	Map aima3eFig5_2;
	
	public TwoPlyGameTree() {
		aima3eFig5_2 = new ExtendableMap() {
			{
				addUnidirectionalLink("A", "B", 1.0);
				addUnidirectionalLink("A", "C", 1.0);
				addUnidirectionalLink("A", "D", 1.0);
				addUnidirectionalLink("B", "E", 1.0);
				addUnidirectionalLink("B", "F", 1.0);
				addUnidirectionalLink("B", "G", 1.0);
				addUnidirectionalLink("C", "H", 1.0);
				addUnidirectionalLink("C", "I", 1.0);
				addUnidirectionalLink("C", "J", 1.0);
				addUnidirectionalLink("D", "K", 1.0);
				addUnidirectionalLink("D", "L", 1.0);
				addUnidirectionalLink("D", "M", 1.0);
			}
		};
	}
	
	public List<MoveToAction> getActions(TwoPlyGameState state) {
		List<MoveToAction> possibleActions = new ArrayList<>();
		List<String> nextPossibleLocations = aima3eFig5_2.getPossibleNextLocations(state.getLocation());
		for (String nextLocation : nextPossibleLocations) {
			MoveToAction action = new MoveToAction(nextLocation);
			possibleActions.add(action);
		}
		return possibleActions;
	}
	
}
