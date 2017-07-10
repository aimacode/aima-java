package aima.core.environment.map;

import aima.core.agent.EnvironmentViewNotifier;
import aima.core.agent.Percept;
import aima.core.agent.impl.DynamicPercept;
import aima.core.agent.impl.DynamicState;
import aima.core.search.framework.Node;
import aima.core.search.framework.SearchForActions;
import aima.core.search.agent.ProblemSolvingAgent;
import aima.core.search.framework.problem.Problem;
import aima.core.search.informed.Informed;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;

/**
 * Variant of {@link aima.core.environment.map.SimpleMapAgent} which works
 * correctly also for A* and other best-first search implementations. It can be
 * extended also for scenarios, in which the agent faces unforeseen events. When
 * using informed search and more then one goal, make sure, that a heuristic
 * function factory is provided!
 *
 * @author Ruediger Lunde
 */
public class MapAgent extends ProblemSolvingAgent<String, MoveToAction> {

    protected final Map map;
    protected final DynamicState state = new DynamicState();
    protected final List<String> goals = new ArrayList<>();
    protected int currGoalIdx = -1;

    // possibly null...
    protected EnvironmentViewNotifier notifier = null;
    private SearchForActions<String, MoveToAction> search = null;
    private Function<String, ToDoubleFunction<Node<String, MoveToAction>>> hFnFactory;

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

    /**
     * Constructor.
     * @param map Information about the environment
     * @param search Search strategy to be used
     * @param goals List of locations to be visited
     * @param notifier Gets informed about decisions of the agent
     * @param hFnFactory Factory, mapping goals to heuristic functions. When using
     *                   informed search, the agent must be able to estimate remaining costs for
     *                   the goals he has selected.
     */
    public MapAgent(Map map, SearchForActions<String, MoveToAction> search, List<String> goals,
                    EnvironmentViewNotifier notifier,
                    Function<String, ToDoubleFunction<Node<String, MoveToAction>>> hFnFactory) {
        this(map, search, goals, notifier);
        this.hFnFactory = hFnFactory;
    }

    //
    // PROTECTED METHODS
    //
    @Override
    protected void updateState(Percept p) {
        DynamicPercept dp = (DynamicPercept) p;
        state.setAttribute(DynAttributeNames.AGENT_LOCATION, dp.getAttribute(DynAttributeNames.PERCEPT_IN));
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Optional<Object> formulateGoal() {
        String goal = null;
        if (currGoalIdx < goals.size() - 1) {
            goal = goals.get(++currGoalIdx);
            if (hFnFactory != null && search instanceof Informed)
                ((Informed<String, MoveToAction>) search).setHeuristicFunction(hFnFactory.apply(goal));

            if (notifier != null)
                notifier.notifyViews("Current location: In(" + state.getAttribute(DynAttributeNames.AGENT_LOCATION)
                        + "), Goal: In(" + goal + ")");
        }
        return goal != null ? Optional.of(goal) : Optional.empty();
    }

    @Override
    protected Problem<String, MoveToAction> formulateProblem(Object goal) {
        return new BidirectionalMapProblem(map, (String) state.getAttribute(DynAttributeNames.AGENT_LOCATION),
                (String) goal);
    }

    @Override
    protected Optional<List<MoveToAction>> search(Problem<String, MoveToAction> problem) {
        Optional<List<MoveToAction>> result = search.findActions(problem);
        notifyViewOfMetrics();
        return result;
    }

    protected void notifyViewOfMetrics() {
        if (notifier != null)
            notifier.notifyViews("Search metrics: " + search.getMetrics());
    }
}
