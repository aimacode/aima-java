package aima.core.environment.map;

import aima.core.agent.EnvironmentViewNotifier;
import aima.core.agent.Percept;
import aima.core.agent.impl.DynamicPercept;
import aima.core.agent.impl.DynamicState;
import aima.core.search.framework.SearchForActions;
import aima.core.search.agent.SimpleProblemSolvingAgent;
import aima.core.search.framework.problem.Problem;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Note: This implementation should be used with one predefined goal only or
 * with uninformed search. As the heuristic of the used search algorithm is
 * never changed, estimates for the second (or randomly created goal) will be
 * wrong.
 * 
 * @author Ciaran O'Reilly
 * @author Ruediger Lunde
 * 
 */
public class SimpleMapAgent extends SimpleProblemSolvingAgent<String, MoveToAction> {

	protected Map map = null;
	protected DynamicState state = new DynamicState();

	// possibly null...
	private EnvironmentViewNotifier notifier = null;
	private SearchForActions<String, MoveToAction> search = null;
	private String[] goals = null;
	private int goalTestPos = 0;

	public SimpleMapAgent(Map map, EnvironmentViewNotifier notifier, SearchForActions<String, MoveToAction> search) {
		this.map = map;
		this.notifier = notifier;
		this.search = search;
	}

	public SimpleMapAgent(Map map, EnvironmentViewNotifier notifier, SearchForActions<String, MoveToAction> search,
						  int maxGoalsToFormulate) {
		super(maxGoalsToFormulate);
		this.map = map;
		this.notifier = notifier;
		this.search = search;
	}

	public SimpleMapAgent(Map map, EnvironmentViewNotifier notifier, SearchForActions<String, MoveToAction> search,
						  String[] goals) {
		this(map, search, goals);
		this.notifier = notifier;
	}

	public SimpleMapAgent(Map map, SearchForActions<String, MoveToAction> search, String[] goals) {
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
	protected void updateState(Percept p) {
		DynamicPercept dp = (DynamicPercept) p;
		state.setAttribute(DynAttributeNames.AGENT_LOCATION, dp.getAttribute(DynAttributeNames.PERCEPT_IN));
	}

	@Override
	protected Object formulateGoal() {
		Object goal;
		if (goals == null) {
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
	protected Problem<String, MoveToAction> formulateProblem(Object goal) {
		return new BidirectionalMapProblem(map, (String) state.getAttribute(DynAttributeNames.AGENT_LOCATION),
				(String) goal);
	}

	@Override
	protected Optional<List<MoveToAction>> search(Problem<String, MoveToAction> problem) {
		return search.findActions(problem);
	}

	@Override
	protected void notifyViewOfMetrics() {
		if (notifier != null) {
			Set<String> keys = search.getMetrics().keySet();
			for (String key : keys)
				notifier.notifyViews("METRIC[" + key + "]=" + search.getMetrics().get(key));
		}
	}
}
