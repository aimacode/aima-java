package aima.core.search.local;

import java.util.List;
import java.util.Random;

import aima.core.agent.Action;
import aima.core.search.framework.Metrics;
import aima.core.search.framework.Node;
import aima.core.search.framework.NodeExpander;
import aima.core.search.framework.SearchForActions;
import aima.core.search.framework.SearchForStates;
import aima.core.search.framework.SearchUtils;
import aima.core.search.framework.evalfunc.HeuristicFunction;
import aima.core.search.framework.problem.Problem;
import aima.core.util.CancelableThread;
import aima.core.util.Util;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): Figure 4.5, page
 * 126.<br>
 * <br>
 * 
 * <pre>
 * function SIMULATED-ANNEALING(problem, schedule) returns a solution state
 *                    
 *   current &lt;- MAKE-NODE(problem.INITIAL-STATE)
 *   for t = 1 to INFINITY do
 *     T &lt;- schedule(t)
 *     if T = 0 then return current
 *     next &lt;- a randomly selected successor of current
 *     /\E &lt;- next.VALUE - current.value
 *     if /\E &gt; 0 then current &lt;- next
 *     else current &lt;- next only with probability e&circ;(/\E/T)
 * </pre>
 * 
 * Figure 4.5 The simulated annealing search algorithm, a version of stochastic
 * hill climbing where some downhill moves are allowed. Downhill moves are
 * accepted readily early in the annealing schedule and then less often as time
 * goes on. The schedule input determines the value of the temperature T as a
 * function of time.
 * 
 * @author Ravi Mohan
 * @author Mike Stampone
 * @author Ruediger Lunde
 */
public class SimulatedAnnealingSearch implements SearchForActions, SearchForStates {

	public enum SearchOutcome {
		FAILURE, SOLUTION_FOUND
	}

    public static final String METRIC_NODES_EXPANDED = "nodesExpanded";
	public static final String METRIC_TEMPERATURE = "temp";
	public static final String METRIC_NODE_VALUE = "nodeValue";
	
	private final HeuristicFunction hf;
	private final Scheduler scheduler;
	private final NodeExpander nodeExpander;
	
	private SearchOutcome outcome = SearchOutcome.FAILURE;
	private Object lastState = null;
	private Metrics metrics = new Metrics();

	/**
	 * Constructs a simulated annealing search from the specified heuristic
	 * function and a default scheduler.
	 * 
	 * @param hf
	 *            a heuristic function
	 */
	public SimulatedAnnealingSearch(HeuristicFunction hf) {
		this(hf, new Scheduler());
	}

	/**
	 * Constructs a simulated annealing search from the specified heuristic
	 * function and scheduler.
	 * 
	 * @param hf
	 *            a heuristic function
	 * @param scheduler
	 *            a mapping from time to "temperature"
	 */
	public SimulatedAnnealingSearch(HeuristicFunction hf, Scheduler scheduler) {
		this(hf, scheduler, new NodeExpander());
	}
	
	public SimulatedAnnealingSearch(HeuristicFunction hf, Scheduler scheduler, NodeExpander nodeExpander) {
		this.hf = hf;
		this.scheduler = scheduler;
		this.nodeExpander = nodeExpander;
	}
	
	@Override
	public List<Action> findActions(Problem p) {
		nodeExpander.useParentLinks(true);
		Node node = findNode(p);
		return node == null ? SearchUtils.failure() : SearchUtils.getSequenceOfActions(node);
	}
	
	@Override
	public Object findState(Problem p) {
		nodeExpander.useParentLinks(false);
		Node node = findNode(p);
		return node == null ? null : node.getState();
	}

	// function SIMULATED-ANNEALING(problem, schedule) returns a solution state
	public Node findNode(Problem p) {
		clearInstrumentation();
		outcome = SearchOutcome.FAILURE;
		lastState = null;
		// current <- MAKE-NODE(problem.INITIAL-STATE)
		Node current = nodeExpander.createRootNode(p.getInitialState());
		Node next = null;
		// for t = 1 to INFINITY do
		int timeStep = 0;
		while (!CancelableThread.currIsCanceled()) {
			// temperature <- schedule(t)
			double temperature = scheduler.getTemp(timeStep);
			timeStep++;
			lastState = current.getState();
			// if temperature = 0 then return current
			if (temperature == 0.0) {
				if (SearchUtils.isGoalState(p, current))
					outcome = SearchOutcome.SOLUTION_FOUND;
				return current;
			}

			updateMetrics(temperature, getValue(current));
			List<Node> children = nodeExpander.expand(current, p);
			if (children.size() > 0) {
				// next <- a randomly selected successor of current
				next = Util.selectRandomlyFromList(children);
				// /\E <- next.VALUE - current.value
				double deltaE = getValue(next) - getValue(current);

				if (shouldAccept(temperature, deltaE)) {
					current = next;
				}
			}
		}
		return null;
	}

	/**
	 * Returns <em>e</em><sup>&delta<em>E / T</em></sup>
	 * 
	 * @param temperature
	 *            <em>T</em>, a "temperature" controlling the probability of
	 *            downward steps
	 * @param deltaE
	 *            VALUE[<em>next</em>] - VALUE[<em>current</em>]
	 * @return <em>e</em><sup>&delta<em>E / T</em></sup>
	 */
	public double probabilityOfAcceptance(double temperature, double deltaE) {
		return Math.exp(deltaE / temperature);
	}

	public SearchOutcome getOutcome() {
		return outcome;
	}

	/**
	 * Returns the last state from which the simulated annealing search found a
	 * solution state.
	 * 
	 * @return the last state from which the simulated annealing search found a
	 *         solution state.
	 */
	public Object getLastSearchState() {
		return lastState;
	}

	@Override
	public NodeExpander getNodeExpander() {
		return nodeExpander;
	}
	
	/**
	 * Returns all the search metrics.
	 */
	@Override
	public Metrics getMetrics() {
		metrics.set(METRIC_NODES_EXPANDED, nodeExpander.getNumOfExpandCalls());
		return metrics;
	}
	
	private void updateMetrics(double temperature, double value) {
		metrics.set(METRIC_TEMPERATURE, temperature);
		metrics.set(METRIC_NODE_VALUE, value);
	}
	
	/**
	 * Sets all metrics to zero.
	 */
	private void clearInstrumentation() {
		nodeExpander.resetCounter();
		metrics.set(METRIC_NODES_EXPANDED, 0);
		metrics.set(METRIC_TEMPERATURE, 0);
		metrics.set(METRIC_NODE_VALUE, 0);
	}
	
	//
	// PRIVATE METHODS
	//

	// if /\E > 0 then current <- next
	// else current <- next only with probability e^(/\E/T)
	private boolean shouldAccept(double temperature, double deltaE) {
		return (deltaE > 0.0)
				|| (new Random().nextDouble() <= probabilityOfAcceptance(
						temperature, deltaE));
	}

	private double getValue(Node n) {
		// assumption greater heuristic value =>
		// HIGHER on hill; 0 == goal state;
		// SA deals with gardient DESCENT
		return -1 * hf.h(n.getState());
	}
}