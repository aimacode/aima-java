package aima.core.search.uninformed;

import aima.core.search.framework.*;
import aima.core.search.framework.problem.Problem;
import aima.core.util.Tasks;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): Figure 3.18, page
 * 89.<br>
 * <br>
 * 
 * <pre>
 * function ITERATIVE-DEEPENING-SEARCH(problem) returns a solution, or failure
 *   for depth = 0 to infinity  do
 *     result &lt;- DEPTH-LIMITED-SEARCH(problem, depth)
 *     if result != cutoff then return result
 * </pre>
 * 
 * Figure 3.18 The iterative deepening search algorithm, which repeatedly
 * applies depth-limited search with increasing limits. It terminates when a
 * solution is found or if the depth- limited search returns failure, meaning
 * that no solution exists.
 *
 * @author Ruediger Lunde
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 */
public class IterativeDeepeningSearch<S, A> implements SearchForActions<S, A>, SearchForStates<S, A> {
	public static final String METRIC_NODES_EXPANDED = "nodesExpanded";
	public static final String METRIC_PATH_COST = "pathCost";

	private final NodeFactory<S, A> nodeFactory;
	private final Metrics metrics;

	public IterativeDeepeningSearch() {
		this(new NodeFactory<>());
	}
	
	public IterativeDeepeningSearch(NodeFactory<S, A> nodeFactory) {
		this.nodeFactory = nodeFactory;
		this.metrics = new Metrics();
	}
	
	
	// function ITERATIVE-DEEPENING-SEARCH(problem) returns a solution, or
	// failure
	@Override
	public Optional<List<A>> findActions(Problem<S, A> p) {
		nodeFactory.useParentLinks(true);
		return SearchUtils.toActions(findNode(p));
	}

	@Override
	public Optional<S> findState(Problem<S, A> p) {
		nodeFactory.useParentLinks(false);
		return SearchUtils.toState(findNode(p));
	}

	/**
	 * Returns a solution node if a solution was found, empty if no solution is reachable or the task was cancelled
	 * by the user.
	 * @param p
	 * @return
	 */
	private Optional<Node<S, A>> findNode(Problem<S, A> p) {
		clearMetrics();
		// for depth = 0 to infinity do
		for (int i = 0; !Tasks.currIsCancelled(); i++) {
			// result <- DEPTH-LIMITED-SEARCH(problem, depth)
			DepthLimitedSearch<S, A> dls = new DepthLimitedSearch<>(i, nodeFactory);
			Optional<Node<S, A>> result = dls.findNode(p);
			updateMetrics(dls.getMetrics());
			// if result != cutoff then return result
			if (!dls.isCutoffResult(result))
				return result;
		}
		return Optional.empty();
	}
	
	@Override
	public Metrics getMetrics() {
		return metrics;
	}

	@Override
	public void addNodeListener(Consumer<Node<S, A>> listener)  {
		nodeFactory.addNodeListener(listener);
	}

	@Override
	public boolean removeNodeListener(Consumer<Node<S, A>> listener) {
		return nodeFactory.removeNodeListener(listener);
	}


	//
	// PRIVATE METHODS
	//

	/**
	 * Sets the nodes expanded and path cost metrics to zero.
	 */
	private void clearMetrics() {
		metrics.set(METRIC_NODES_EXPANDED, 0);
		metrics.set(METRIC_PATH_COST, 0);
	}

	private void updateMetrics(Metrics dlsMetrics) {
		metrics.set(METRIC_NODES_EXPANDED,
				metrics.getInt(METRIC_NODES_EXPANDED) + dlsMetrics.getInt(METRIC_NODES_EXPANDED));
		metrics.set(METRIC_PATH_COST, dlsMetrics.getDouble(METRIC_PATH_COST));
	}
}