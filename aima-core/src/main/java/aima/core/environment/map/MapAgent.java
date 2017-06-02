package aima.core.environment.map;

import aima.core.agent.Action;
import aima.core.agent.EnvironmentViewNotifier;
import aima.core.agent.Percept;
import aima.core.agent.impl.DynamicPercept;
import aima.core.agent.impl.DynamicState;
import aima.core.search.framework.ProblemSolvingAgent;
import aima.core.search.framework.SearchForActions;
import aima.core.search.framework.problem.Problem;
import aima.core.search.informed.HeuristicFunctionFactory;
import aima.core.search.informed.Informed;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Variant of {@link aima.core.environment.map.SimpleMapAgent} which works
 * correctly also for A* and other best-first search implementations. It can be
 * extended also for scenarios, in which the agent faces unforeseen events. When
 * using informed search and more then one goal, make sure, that a heuristic
 * function factory is provided!
 * 
 * @author Ruediger Lunde
 *
 */
public class MapAgent extends ProblemSolvingAgent<String, MoveToAction> {

	protected final Map map;
	protected final DynamicState state = new DynamicState();
	protected final List<String> goals = new ArrayList<>();
	protected int currGoalIdx = -1;

	// possibly null...
	protected EnvironmentViewNotifier notifier = null;
	private SearchForActions<String, MoveToAction> search = null;
	private HeuristicFunctionFactory<String, MoveToAction> hfFactory;

	public MapAgent(Map map, SearchForActions<String, MoveToAction> search, String goal) {
		this.map = map;
		this.search = search;
		goals.add(goal);
	}

	public MapAgent(Map map, SearchForActions<String, MoveToAction> search, String goal, EnvironmentViewNotifier notifier) {
		this(map, search, goal);
		this.notifier = notifier;
	}

	public MapAgent(Map map, SearchForActions<String, MoveToAction> search, List<String> goals) {
		this.map = map;
		this.search = search;
		this.goals.addAll(goals);
	}

	public MapAgent(Map map, SearchForActions<String, MoveToAction> search, List<String> goals,
					EnvironmentViewNotifier notifier) {
		this(map, search, goals);
		this.notifier = notifier;
	}

	public MapAgent(Map map, SearchForActions<String, MoveToAction> search, List<String> goals,
					EnvironmentViewNotifier notifier, HeuristicFunctionFactory<String, MoveToAction> hfFactory) {
		this(map, search, goals, notifier);
		this.hfFactory = hfFactory;
	}

	//
	// PROTECTED METHODS
	//
	@Override
	protected void updateState(Percept p) {
		DynamicPercept dp = (DynamicPercept) p;
		state.setAttribute(DynAttributeNames.AGENT_LOCATION, dp.getAttribute(DynAttributeNames.PERCEPT_IN));
	}

	@Override
	protected Object formulateGoal() {
		String goal = null;
		if (currGoalIdx < goals.size() - 1) {
			goal = goals.get(++currGoalIdx);
			if (notifier != null)
				notifier.notifyViews("CurrentLocation=In(" + state.getAttribute(DynAttributeNames.AGENT_LOCATION)
						+ "), Goal=In(" + goal + ")");
			modifyHeuristicFunction(goal);
		}
		return goal;
	}

	@Override
	protected Problem<String, MoveToAction> formulateProblem(Object goal) {
		return new BidirectionalMapProblem(map, (String) state.getAttribute(DynAttributeNames.AGENT_LOCATION),
				(String) goal);
	}

	@Override
	protected List<MoveToAction> search(Problem<String, MoveToAction> problem) {
		List<MoveToAction> result = new ArrayList<>();
		try {
			result.addAll(search.findActions(problem));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		notifyViewOfMetrics();
		return result;
	}

	protected void notifyViewOfMetrics() {
		if (notifier != null) {
			Set<String> keys = search.getMetrics().keySet();
			for (String key : keys) {
				notifier.notifyViews("METRIC[" + key + "]=" + search.getMetrics().get(key));
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void modifyHeuristicFunction(String goal) {
		if (hfFactory != null && search instanceof Informed) {
			((Informed<String, MoveToAction>) search).setHeuristicFunction(hfFactory.createHeuristicFunction(goal));
		}
	}
}
