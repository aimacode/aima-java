package aima.core.search.map;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import aima.core.agent.Action;
import aima.core.agent.Model;
import aima.core.agent.Percept;
import aima.core.agent.impl.DynamicModel;
import aima.core.agent.impl.DynamicPercept;
import aima.core.search.framework.HeuristicFunction;
import aima.core.search.framework.Problem;
import aima.core.search.framework.Search;
import aima.core.search.framework.SimpleProblemSolvingAgent;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class MapAgent extends SimpleProblemSolvingAgent {
	private MapEnvironment mapEnvironment = null;
	
	private DynamicModel model = new DynamicModel();

	private Search search = null;

	private String[] goalTests = null;

	private int goalTestPos = 0;

	private HeuristicFunction heuristicFunction = null;

	public MapAgent(MapEnvironment mapEnvironment, Search search) {
		this.mapEnvironment = mapEnvironment;
		this.search = search;
	}

	public MapAgent(MapEnvironment mapEnvironment, Search search,
			int maxGoalsToFormulate) {
		super(maxGoalsToFormulate);
		this.mapEnvironment = mapEnvironment;
		this.search = search;
	}

	public MapAgent(MapEnvironment mapEnvironment, Search search,
			String[] goalTests) {
		super(goalTests.length);
		this.mapEnvironment = mapEnvironment;
		this.search = search;
		this.goalTests = new String[goalTests.length];
		System.arraycopy(goalTests, 0, this.goalTests, 0, goalTests.length);
	}

	public HeuristicFunction getHeuristicFunction() {
		return heuristicFunction;
	}

	public void setHeuristicFunction(HeuristicFunction heuristicFunction) {
		this.heuristicFunction = heuristicFunction;
	}

	//
	// PROTECTED METHODS
	//
	@Override
	protected Model updateState(Percept p) {
		DynamicPercept dp = (DynamicPercept) p;
		
		model.setAttribute(DynAttributeNames.AGENT_LOCATION, dp.getAttribute(DynAttributeNames.PERCEPT_IN));

		return model;
	}

	@Override
	protected Object formulateGoal() {
		Object goal = null;
		if (null == goalTests) {
			goal = mapEnvironment.getMap().randomlyGenerateDestination();
		} else {
			goal = goalTests[goalTestPos];
			goalTestPos++;
		}
		mapEnvironment.notifyViews("CurrentLocation=In(" + model.getAttribute(DynAttributeNames.AGENT_LOCATION)
				+ "), Goal=In(" + goal + ")");

		return goal;
	}

	@Override
	protected Problem formulateProblem(Object goal) {
		if (null == getHeuristicFunction()) {
			return new BidirectionalMapProblem(mapEnvironment.getMap(),
					(String) model.getAttribute(DynAttributeNames.AGENT_LOCATION), (String) goal);
		} else {
			return new BidirectionalMapProblem(mapEnvironment.getMap(),
					(String) model.getAttribute(DynAttributeNames.AGENT_LOCATION), (String) goal, getHeuristicFunction());
		}
	}

	@Override
	protected List<Action> search(Problem problem) {
		List<Action> actions = new ArrayList<Action>();
		try {
			List<Action> sactions = search.search(problem);
			for (Action action : sactions) {
				actions.add(action);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return actions;
	}

	@Override
	protected void notifyViewOfMetrics() {
		Set<String> keys = search.getMetrics().keySet();
		for (String key : keys) {
			mapEnvironment.notifyViews("METRIC[" + key + "]="
					+ search.getMetrics().get(key));
		}
	}
}
