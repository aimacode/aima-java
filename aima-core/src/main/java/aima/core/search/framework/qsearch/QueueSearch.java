package aima.core.search.framework.qsearch;

import aima.core.search.framework.Metrics;
import aima.core.search.framework.Node;
import aima.core.search.framework.NodeFactory;
import aima.core.search.framework.problem.Problem;

import java.util.Optional;
import java.util.Queue;

/**
 * Base class for queue-based search implementations, especially for 
 * {@link TreeSearch}, {@link GraphSearch}, and {@link BidirectionalSearch}. It
 * defines an abstract method for controlling search execution and provides some
 * infrastructure for performance analysis and node creation.
 *
 * @param <S> The type used to represent states
 * @param <A> The type of the actions to be used to navigate through the state space
 *
 * @author Ruediger Lunde
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 * @author Mike Stampone
 */
public abstract class QueueSearch<S, A> {
	public static final String METRIC_NODES_EXPANDED = "nodesExpanded";
	public static final String METRIC_QUEUE_SIZE = "queueSize";
	public static final String METRIC_MAX_QUEUE_SIZE = "maxQueueSize";
	public static final String METRIC_PATH_COST = "pathCost";

	final protected NodeFactory<S, A> nodeFactory;
	protected boolean earlyGoalTest = false;
	protected Metrics metrics = new Metrics();

	/** Stores the provided node expander and adds a node listener to it. */
	protected QueueSearch(NodeFactory<S, A> nodeFactory) {
		this.nodeFactory = nodeFactory;
		nodeFactory.addNodeListener((node) -> metrics.incrementInt(METRIC_NODES_EXPANDED));
	}

	/**
	 * Receives a problem and a queue implementing the search strategy and
	 * computes a node referencing a goal state, if such a state was found.
	 *
	 * @param problem
	 *            the search problem
	 * @param frontier
	 *            the data structure for nodes that are waiting to be expanded
	 *
	 * @return a node referencing a goal state, if the goal was found, otherwise empty;
	 */
	abstract public Optional<Node<S, A>> findNode(Problem<S, A> problem, Queue<Node<S, A>> frontier);


	//
	// helper methods to be used in subclasses

	/**
	 * Enables optimization for FIFO queue based search, especially breadth first search.
	 */
	public void setEarlyGoalTest(boolean b) {
		earlyGoalTest = b;
	}

	public NodeFactory<S, A> getNodeFactory() {
		return nodeFactory;
	}

	/**
	 * Returns all the search metrics.
	 */
	public Metrics getMetrics() {
		return metrics;
	}

	/**
	 * Sets all metrics to zero.
	 */
	protected void clearMetrics() {
		metrics.set(METRIC_NODES_EXPANDED, 0);
		metrics.set(METRIC_QUEUE_SIZE, 0);
		metrics.set(METRIC_MAX_QUEUE_SIZE, 0);
		metrics.set(METRIC_PATH_COST, 0);
	}

	protected void updateMetrics(int queueSize) {
		metrics.set(METRIC_QUEUE_SIZE, queueSize);
		int maxQSize = metrics.getInt(METRIC_MAX_QUEUE_SIZE);
		if (queueSize > maxQSize) {
			metrics.set(METRIC_MAX_QUEUE_SIZE, queueSize);
		}
	}

	protected Optional<Node<S, A>> asOptional(Node<S, A> node) {
		if (node != null)
			metrics.set(METRIC_PATH_COST, node.getPathCost());
		return Optional.ofNullable(node);
	}
}