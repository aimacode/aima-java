package aima.search.map;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import aima.basic.Percept;
import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SimpleProblemSolvingAgent;

/**
 * A realization of the SimpleProblemSolvingAgent capable of navigating a MapEnvironment.
 */

/**
 * @author Ciaran O'Reilly
 * 
 */
public class MapAgent extends SimpleProblemSolvingAgent {
	private MapEnvironment mapEnvironment = null;
	private Search search = null;
	private String currentLocation = null;
	private String[] goalTests = null;
	private int goalTestPos = 0;
	
	public MapAgent(MapEnvironment mapEnvironment, Search search) {
		this.mapEnvironment = mapEnvironment;
		this.search = search;
	}
	
	public MapAgent(MapEnvironment mapEnvironment, Search search, int maxGoalsToFormulate) {
		super(maxGoalsToFormulate);
		this.mapEnvironment = mapEnvironment;
		this.search = search;
	}
	
	public MapAgent(MapEnvironment mapEnvironment, Search search, String[] goalTests) {
		super(goalTests.length);
		this.mapEnvironment = mapEnvironment;
		this.search = search;
		this.goalTests = new String[goalTests.length];
		System.arraycopy(goalTests, 0, this.goalTests, 0, goalTests.length);
	}
	
	//
	// PROTECTED METHODS
	//
	protected Object updateState(Percept p) {
		currentLocation = (String) p.getAttribute("In");
		
		return currentLocation;
	}
	
	protected Object formulateGoal() {
		Object goal = null;
		if (null == goalTests) {
			goal = mapEnvironment.randomlySelectDestination();
		} else {
			goal = goalTests[goalTestPos];
			goalTestPos++;
		}
		mapEnvironment.updateViews("CurrentLocation=In("+currentLocation+"), Goal=In("+goal+")");
		
		return goal;
	}
	
	protected Problem formulateProblem(Object goal) {
		return new Problem(currentLocation, 
				new MapSuccessorFunction(mapEnvironment.getMap()), 
				new MapGoalTest((String)goal),
				new MapStepCostFunction(mapEnvironment.getMap()));
	}
	
	protected List<String> search(Problem problem) {
		List<String> actions = new ArrayList<String>();
		try {
			List sactions = search.search(problem);
			for (Object action : sactions) {
				actions.add((String)action);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return actions;
	}
	
	protected void notifyViewOfMetrics() {
		Set keys = search.getMetrics().keySet();
		for (Object key : keys) {
			mapEnvironment.updateViews("METRIC[" + key + "]="+search.getMetrics().get((String)key));
		}
	}
}
