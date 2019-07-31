package aima.core.environment.map;

import aima.core.agent.Notifier;
import aima.core.agent.impl.DynamicPercept;
import aima.core.agent.impl.DynamicState;
import aima.core.search.agent.ProblemSolvingAgent;
import aima.core.search.framework.Node;
import aima.core.search.framework.SearchForActions;
import aima.core.search.framework.problem.Problem;
import aima.core.search.informed.Informed;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;

/**
 * Variant of {@link aima.core.environment.map.SimpleMapAgent} which works
 * correctly for A* and other best-first search implementations. It can be
 * extended for scenarios, in which the agent faces unforeseen events during
 * plan execution. When using informed search and more then one goal, make
 * sure, that a heuristic function factory is provided!
 *
 * @author Ruediger Lunde
 */
public class MapAgent extends ProblemSolvingAgent<DynamicPercept, String, MoveToAction> {

    protected final Map map;
    protected final DynamicState state = new DynamicState();
    protected final List<String> goals = new ArrayList<>();
    protected int nextGoalPos = 0;

    private SearchForActions<String, MoveToAction> search;
    private Function<String, ToDoubleFunction<Node<String, MoveToAction>>> hFnFactory;
    protected Notifier notifier;

    public MapAgent(Map map, SearchForActions<String, MoveToAction> search, String goal) {
        this.map = map;
        this.search = search;
        goals.add(goal);
    }

    public MapAgent(Map map, SearchForActions<String, MoveToAction> search, List<String> goals) {
        this.map = map;
        this.search = search;
        this.goals.addAll(goals);
    }

    /**
     * Constructor.
     * @param map Information about the environment
     * @param search Search strategy to be used
     * @param goals List of locations to be visited
     * @param hFnFactory Factory, mapping goals to heuristic functions. When using
     *                   informed search, the agent must be able to estimate remaining costs for
     *                   the goals he has selected.
     */
    public MapAgent(Map map, SearchForActions<String, MoveToAction> search, List<String> goals,
                    Function<String, ToDoubleFunction<Node<String, MoveToAction>>> hFnFactory) {
        this(map, search, goals);
        this.hFnFactory = hFnFactory;
    }

    /**  Sets a notifier which gets informed about decisions of the agent */
    public MapAgent setNotifier(Notifier notifier) {
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

    @SuppressWarnings("unchecked")
    @Override
    protected Optional<Object> formulateGoal() {
        String goal = null;
        if (nextGoalPos < goals.size()) {
            goal = goals.get(nextGoalPos++);
            if (hFnFactory != null && search instanceof Informed)
                ((Informed<String, MoveToAction>) search).setHeuristicFunction(hFnFactory.apply(goal));
            if (notifier != null)
                notifier.notify("Current location: In(" + state.getAttribute(AttNames.AGENT_LOCATION)
                        + "), Goal: In(" + goal + ")");
        }
        return Optional.ofNullable(goal);
    }

    @Override
    protected Problem<String, MoveToAction> formulateProblem(Object goal) {
        return new BidirectionalMapProblem(map, (String) state.getAttribute(AttNames.AGENT_LOCATION),
                (String) goal);
    }

    @Override
    protected Optional<List<MoveToAction>> search(Problem<String, MoveToAction> problem) {
        Optional<List<MoveToAction>> result = search.findActions(problem);
        if (notifier != null)
            notifier.notify("Search" + search.getMetrics());
        return result;
    }
}
