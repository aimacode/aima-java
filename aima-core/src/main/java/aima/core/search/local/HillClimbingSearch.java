package aima.core.search.local;

import aima.core.agent.Action;
import aima.core.search.framework.*;
import aima.core.search.framework.evalfunc.HeuristicFunction;
import aima.core.search.framework.problem.Problem;
import aima.core.util.CancelableThread;

import java.util.List;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): Figure 4.2, page
 * 122.<br>
 * <br>
 * 
 * <pre>
 * function HILL-CLIMBING(problem) returns a state that is a local maximum
 *                    
 *   current &lt;- MAKE-NODE(problem.INITIAL-STATE)
 *   loop do
 *     neighbor &lt;- a highest-valued successor of current
 *     if neighbor.VALUE &lt;= current.VALUE then return current.STATE
 *     current &lt;- neighbor
 * </pre>
 * 
 * Figure 4.2 The hill-climbing search algorithm, which is the most basic local
 * search technique. At each step the current node is replaced by the best
 * neighbor; in this version, that means the neighbor with the highest VALUE,
 * but if a heuristic cost estimate h is used, we would find the neighbor with
 * the lowest h.
 * 
 * @author Ravi Mohan
 * @author Mike Stampone
 * @author Ruediger Lunde
 */
public class HillClimbingSearch implements SearchForActions, SearchForStates, Informed {

	public enum SearchOutcome {
		FAILURE, SOLUTION_FOUND
	}

	public static final String METRIC_NODES_EXPANDED = "nodesExpanded";
	public static final String METRIC_NODE_VALUE = "nodeValue";

	private HeuristicFunction hf = null;
	private final NodeExpander nodeExpander;
	private SearchOutcome outcome = SearchOutcome.FAILURE;
	private Object lastState = null;
	private Metrics metrics = new Metrics();

	/**
	 * Constructs a hill-climbing search from the specified heuristic function.
	 * 
	 * @param hf
	 *            a heuristic function
	 */
	public HillClimbingSearch(HeuristicFunction hf) {
		this(hf, new NodeExpander());
	}
	
	public HillClimbingSearch(HeuristicFunction hf, NodeExpander nodeExpander) {
		this.hf = hf;
		this.nodeExpander = nodeExpander;
	}

	@Override
	public void setHeuristicFunction(HeuristicFunction hf) {
		this.hf = hf;
	}

	@Override
	public List<Action> findActions(Problem p) {
		nodeExpander.useParentLinks(true);
		Node node = searchNode(p);
		return node == null ? SearchUtils.failure() : SearchUtils.getSequenceOfActions(node);
	}
	
	@Override
	public Object findState(Problem p) {
		nodeExpander.useParentLinks(false);
		Node node = searchNode(p);
		return node == null ? null : node.getState();
	}

	/**
	 * Returns a list of actions to the local maximum if the local maximum was
	 * found, a list containing a single NoOp Action if already at the local
	 * maximum, or an empty list if the search was canceled by the user.
	 * 
	 * @param p
	 *            the search problem
	 * 
	 * @return a list of actions to the local maximum if the local maximum was
	 *         found, a list containing a single NoOp Action if already at the
	 *         local maximum, or an empty list if the search was canceled by the
	 *         user.
	 */
	// function HILL-CLIMBING(problem) returns a state that is a local maximum
	public Node searchNode(Problem p) {
		clearInstrumentation();
		outcome = SearchOutcome.FAILURE;
		// current <- MAKE-NODE(problem.INITIAL-STATE)
		Node current = nodeExpander.createRootNode(p.getInitialState());
		Node neighbor = null;
		// loop do
		while (!CancelableThread.currIsCanceled()) {
			lastState = current.getState();
			metrics.set(METRIC_NODE_VALUE, getValue(current));
			List<Node> children = nodeExpander.expand(current, p);
			// neighbor <- a highest-valued successor of current
			neighbor = getHighestValuedNodeFrom(children, p);
			
			// if neighbor.VALUE <= current.VALUE then return current.STATE
			if ((neighbor == null) || (getValue(neighbor) <= getValue(current))) {
				if (SearchUtils.isGoalState(p, current))
					outcome = SearchOutcome.SOLUTION_FOUND;
				return current;
			}
			// current <- neighbor
			current = neighbor;
		}
		return null;
	}
	
	/**
	 * Returns SOLUTION_FOUND if the local maximum is a goal state, or FAILURE
	 * if the local maximum is not a goal state.
	 * 
	 * @return SOLUTION_FOUND if the local maximum is a goal state, or FAILURE
	 *         if the local maximum is not a goal state.
	 */
	public SearchOutcome getOutcome() {
		return outcome;
	}

	/**
	 * Returns the last state from which the hill climbing search found the
	 * local maximum.
	 * 
	 * @return the last state from which the hill climbing search found the
	 *         local maximum.
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
	public Metrics getMetrics() {
		metrics.set(METRIC_NODES_EXPANDED, nodeExpander.getNumOfExpandCalls());
		return metrics;
	}
	
	/**
	 * Sets all metrics to zero.
	 */
	private void clearInstrumentation() {
		nodeExpander.resetCounter();
		metrics.set(METRIC_NODES_EXPANDED, 0);
		metrics.set(METRIC_NODE_VALUE, 0);
	}
	
	//
	// PRIVATE METHODS
	//

	private Node getHighestValuedNodeFrom(List<Node> children, Problem p) {
		double highestValue = Double.NEGATIVE_INFINITY;
		Node nodeWithHighestValue = null;
		for (int i = 0; i < children.size(); i++) {
			Node child = (Node) children.get(i);
			double value = getValue(child);
			if (value > highestValue) {
				highestValue = value;
				nodeWithHighestValue = child;
			}
		}
		return nodeWithHighestValue;
	}

	private double getValue(Node n) {
		// assumption greater heuristic value =>
		// HIGHER on hill; 0 == goal state;
		return -1 * hf.h(n.getState());
	}
}