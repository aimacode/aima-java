package aima.core.search.local;

import aima.core.search.framework.*;
import aima.core.search.framework.problem.Problem;
import aima.core.util.Tasks;
import aima.core.util.Util;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.ToDoubleFunction;

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
public class SimulatedAnnealingSearch<S, A> implements SearchForActions<S, A>, SearchForStates<S, A> {

    public static final String METRIC_NODES_EXPANDED = "nodesExpanded";
	public static final String METRIC_TEMPERATURE = "temp";
	public static final String METRIC_NODE_VALUE = "nodeValue";
	
	private final ToDoubleFunction<Node<S, A>> h;
	private final Scheduler scheduler;
	private final NodeExpander<S, A> nodeExpander;

	private S lastState;
	private Metrics metrics = new Metrics();

	/**
	 * Constructs a simulated annealing search from the specified heuristic
	 * function and a default scheduler.
	 * 
	 * @param h
	 *            a heuristic function
	 */
	public SimulatedAnnealingSearch(ToDoubleFunction<Node<S, A>> h) {
		this(h, new Scheduler());
	}

	/**
	 * Constructs a simulated annealing search from the specified heuristic
	 * function and scheduler.
	 * 
	 * @param h
	 *            a heuristic function
	 * @param scheduler
	 *            a mapping from time to "temperature"
	 */
	public SimulatedAnnealingSearch(ToDoubleFunction<Node<S, A>> h, Scheduler scheduler) {
		this(h, scheduler, new NodeExpander<>());
	}
	
	public SimulatedAnnealingSearch(ToDoubleFunction<Node<S, A>> h, Scheduler scheduler, NodeExpander<S, A> nodeExpander) {
		this.h = h;
		this.scheduler = scheduler;
		this.nodeExpander = nodeExpander;
		nodeExpander.addNodeListener((node) -> metrics.incrementInt(METRIC_NODES_EXPANDED));
	}
	
	@Override
	public Optional<List<A>> findActions(Problem<S, A> p) {
		nodeExpander.useParentLinks(true);
		return SearchUtils.toActions(findNode(p));
	}
	
	@Override
	public Optional<S> findState(Problem<S, A> p) {
		nodeExpander.useParentLinks(false);
		return SearchUtils.toState(findNode(p));
	}

	/**
	 * Returns a node corresponding to a goal state or empty. Method {@link #getLastState()}
	 * provides the last explored state if result is empty.
	 */
	/// function SIMULATED-ANNEALING(problem, schedule) returns a solution state
	public Optional<Node<S, A>> findNode(Problem<S, A> p) {
		clearMetrics();
		/// current <- MAKE-NODE(problem.INITIAL-STATE)
		Node<S, A> current = nodeExpander.createRootNode(p.getInitialState());
		/// for t = 1 to INFINITY do
		int timeStep = 0;
		while (!Tasks.currIsCancelled()) {
			/// temperature <- schedule(t)
			double temperature = scheduler.getTemp(timeStep);
			timeStep++;
			lastState = current.getState();
			/// if temperature = 0 then return current
			if (temperature == 0.0) {
				lastState = current.getState();
				return Optional.ofNullable(p.testSolution(current) ? current : null);
			}

			updateMetrics(temperature, getValue(current));
			List<Node<S, A>> children = nodeExpander.expand(current, p);
			if (children.size() > 0) {
				/// next <- a randomly selected successor of current
				Node<S, A> next = Util.selectRandomlyFromList(children);
				/// /\E <- next.VALUE - current.value
				double deltaE = getValue(next) - getValue(current);
				/// if /\E &gt; 0 then current &lt;- next
				/// else current &lt;- next only with probability e&circ;(/\E/T)
				if (deltaE > 0.0 || Math.random() <= Math.exp(deltaE / temperature))
					current = next;
			}
		}
		lastState = current.getState();
		return Optional.empty();
	}

	/**
	 * Returns the last explored state.
	 */
	public S getLastState() {
		return lastState;
	}
	
	/**
	 * Returns all the search metrics.
	 */
	@Override
	public Metrics getMetrics() {
		return metrics;
	}
	
	private void updateMetrics(double temperature, double value) {
		metrics.set(METRIC_TEMPERATURE, temperature);
		metrics.set(METRIC_NODE_VALUE, value);
	}
	
	/**
	 * Sets all metrics to zero.
	 */
	private void clearMetrics() {
		metrics.set(METRIC_NODES_EXPANDED, 0);
		metrics.set(METRIC_TEMPERATURE, 0);
		metrics.set(METRIC_NODE_VALUE, 0);
	}

	@Override
	public void addNodeListener(Consumer<Node<S, A>> listener)  {
		nodeExpander.addNodeListener(listener);
	}

	@Override
	public boolean removeNodeListener(Consumer<Node<S, A>> listener) {
		return nodeExpander.removeNodeListener(listener);
	}
	
	//
	// PRIVATE METHODS
	//

	private double getValue(Node<S, A> n) {
		// assumption greater heuristic value =>
		// HIGHER on hill; 0 == goal state;
		// SA deals with gradient DESCENT
		return -1 * h.applyAsDouble(n);
	}
}