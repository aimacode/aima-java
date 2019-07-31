package aima.core.environment.map;

import aima.core.agent.Notifier;
import aima.core.agent.impl.DynamicPercept;
import aima.core.agent.impl.DynamicState;
import aima.core.search.agent.SimpleProblemSolvingAgent;
import aima.core.search.framework.SearchForActions;
import aima.core.search.framework.problem.Problem;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
public class SimpleMapAgent extends SimpleProblemSolvingAgent<DynamicPercept, String, MoveToAction> {

	protected Map map;
	protected DynamicState state = new DynamicState();

	private SearchForActions<String, MoveToAction> search;
	private List<String> goals;
	private int nextGoalPos = 0;
	private Notifier notifier;

	/** Randomly generates goals forever. */
	public SimpleMapAgent(Map map, SearchForActions<String, MoveToAction> search) {
		this.map = map;
		this.search = search;
	}

	public SimpleMapAgent(Map map, SearchForActions<String, MoveToAction> search, String goal) {
		this.map = map;
		this.search = search;
		goals = new ArrayList<>();
		goals.add(goal);
	}

	public SimpleMapAgent(Map map, SearchForActions<String, MoveToAction> search, List<String> goals) {
		this.map = map;
		this.search = search;
		this.goals = goals;
	}

	/**  Sets a notifier which gets informed about decisions of the agent */
	public SimpleMapAgent setNotifier(Notifier notifier) {
		this.notifier = notifier;
		return this;
	}

	//
	// PROTECTED METHODS
	//
	@Override
	protected void updateState(DynamicPercept percept) {
		state.setAttribute(AttNames.AGENT_LOCATION, percept.getAttribute(AttNames.PERCEPT_IN));
	}

	@Override
	protected Optional<Object> formulateGoal() {
		Object goal = null;
		if (goals == null)
			goal = map.randomlyGenerateDestination();
		else if (nextGoalPos < goals.size())
			goal = goals.get(nextGoalPos++);
		if (goal != null && notifier != null)
			notifier.notify("CurrentLocation=In(" + state.getAttribute(AttNames.AGENT_LOCATION)
					+ "), Goal=In(" + goal + ")");
		return Optional.ofNullable(goal);
	}

	@Override
	protected Problem<String, MoveToAction> formulateProblem(Object goal) {
		return new BidirectionalMapProblem(map, (String) state.getAttribute(AttNames.AGENT_LOCATION), (String) goal);
	}

	@Override
	protected Optional<List<MoveToAction>> search(Problem<String, MoveToAction> problem) {
		Optional<List<MoveToAction>> result = search.findActions(problem);
		if (notifier != null)
			notifier.notify("Search" + search.getMetrics());
		return result;
	}
}
