package aima.search.informed;

import java.util.List;

import aima.search.framework.Node;
import aima.search.framework.NodeExpander;
import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchUtils;

/**
 * Artificial Intelligence A Modern Approach (2nd Edition): Figure 4.11, page 112.
 * 
 * <code>
 * function HILL-CLIMBING(problem) returns a state that is a local maximum
 *   inputs: problem, a problem
 *   local variables: current, a node
 *                    neighbor, a node
 *                    
 *   current <- MAKE-NODE(INITIAL-STATE[problem])
 *   loop do
 *     neighbor <- a highest-valued successor of current
 *     if VALUE[neighbor] <= VALUE[current] then return STATE[current]
 *     current <- neighbor
 * </code>
 * Figure 4.11 The hill-climbing search algorithm (steepest ascent version), which is the
 * most basic local search technique. At each step the current node is replaced by the best
 * neighbor; in this version, that means the neighbor with the highest VALUE, but if a heuristic
 * cost estimate h is used, we would find the neighbor with the lowest h.
 */

/**
 * @author Ravi Mohan
 * 
 */
public class HillClimbingSearch extends NodeExpander implements Search {

	public enum SearchOutcome {
		FAILURE, SOLUTION_FOUND
	};

	private SearchOutcome outcome = SearchOutcome.FAILURE;

	private Object lastState = null;

	public HillClimbingSearch() {
	}

	// function HILL-CLIMBING(problem) returns a state that is a local maximum
	// inputs: problem, a problem
	public List search(Problem p) throws Exception {
		clearInstrumentation();
		outcome = SearchOutcome.FAILURE;
		lastState = null;
		// local variables: current, a node
		// neighbor, a node
		// current <- MAKE-NODE(INITIAL-STATE[problem])
		Node current = new Node(p.getInitialState());
		Node neighbor = null;
		// loop do
		while (true) {
			List children = expandNode(current, p);
			// neighbor <- a highest-valued successor of current
			neighbor = getHighestValuedNodeFrom(children, p);

			// if VALUE[neighbor] <= VALUE[current] then return STATE[current]
			if ((neighbor == null)
					|| (getValue(p, neighbor) <= getValue(p, current))) {
				if (p.isGoalState(current.getState())) {
					outcome = SearchOutcome.SOLUTION_FOUND;
				}
				lastState = current.getState();
				return SearchUtils.actionsFromNodes(current.getPathFromRoot());
			}
			// current <- neighbor
			current = neighbor;
		}
	}

	public SearchOutcome getOutcome() {
		return outcome;
	}

	public Object getLastSearchState() {
		return lastState;
	}

	private Node getHighestValuedNodeFrom(List children, Problem p) {
		double highestValue = Double.MIN_VALUE;
		Node nodeWithHighestValue = null;
		for (int i = 0; i < children.size(); i++) {
			Node child = (Node) children.get(i);
			double value = getValue(p, child);
			if (value > highestValue) {
				highestValue = value;
				nodeWithHighestValue = child;
			}
		}
		return nodeWithHighestValue;
	}

	private double getValue(Problem p, Node n) {
		return -1 * getHeuristic(p, n); // assumption greater heuristic value =>
		// HIGHER on hill; 0 == goal state;
	}

	private double getHeuristic(Problem p, Node aNode) {
		return p.getHeuristicFunction().getHeuristicValue(aNode.getState());
	}
}