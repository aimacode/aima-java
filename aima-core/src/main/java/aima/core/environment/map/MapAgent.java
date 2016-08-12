package aima.core.environment.map;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import aima.core.agent.Action;
import aima.core.agent.EnvironmentViewNotifier;
import aima.core.agent.Percept;
import aima.core.agent.State;
import aima.core.agent.impl.DynamicPercept;
import aima.core.agent.impl.DynamicState;
import aima.core.search.framework.Search;
import aima.core.search.framework.SimpleProblemSolvingAgent;
import aima.core.search.framework.problem.Problem;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class MapAgent extends SimpleProblemSolvingAgent {

	protected Map map = null;
	protected DynamicState state = new DynamicState();

	// possibly null...
	private EnvironmentViewNotifier notifier = null;
	private Search search = null;
	private String[] goals = null;
	private int goalTestPos = 0;

	public MapAgent(Map map, EnvironmentViewNotifier notifier, Search search) {
		this.map = map;
		this.notifier = notifier;
		this.search = search;
	}

	public MapAgent(Map map, EnvironmentViewNotifier notifier, Search search, int maxGoalsToFormulate) {
		super(maxGoalsToFormulate);
		this.map = map;
		this.notifier = notifier;
		this.search = search;
	}

	public MapAgent(Map map, EnvironmentViewNotifier notifier, Search search, String[] goals) {
		this(map, search, goals);
		this.notifier = notifier;
	}

	public MapAgent(Map map, Search search, String[] goals) {
		super(goals.length);
		this.map = map;
		this.search = search;
		this.goals = new String[goals.length];
		System.arraycopy(goals, 0, this.goals, 0, goals.length);
	}

	//
	// PROTECTED METHODS
	//
	@Override
	protected State updateState(Percept p) {
		DynamicPercept dp = (DynamicPercept) p;

		state.setAttribute(DynAttributeNames.AGENT_LOCATION, dp.getAttribute(DynAttributeNames.PERCEPT_IN));

		return state;
	}

	@Override
	protected Object formulateGoal() {
		Object goal = null;
		if (null == goals) {
			goal = map.randomlyGenerateDestination();
		} else {
			goal = goals[goalTestPos];
			goalTestPos++;
		}
		if (notifier != null)
			notifier.notifyViews("CurrentLocation=In(" + state.getAttribute(DynAttributeNames.AGENT_LOCATION)
					+ "), Goal=In(" + goal + ")");

		return goal;
	}

	@Override
	protected Problem formulateProblem(Object goal) {
		return new BidirectionalMapProblem(map, (String) state.getAttribute(DynAttributeNames.AGENT_LOCATION),
				(String) goal);
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
		if (notifier != null) {
			Set<String> keys = search.getMetrics().keySet();
			for (String key : keys) {
				notifier.notifyViews("METRIC[" + key + "]=" + search.getMetrics().get(key));
			}
		}
	}
}
