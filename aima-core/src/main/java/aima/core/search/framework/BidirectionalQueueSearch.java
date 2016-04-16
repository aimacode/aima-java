package aima.core.search.framework;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import aima.core.agent.Action;
import aima.core.util.CancelableThread;
import aima.core.util.datastructure.Queue;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 90.<br>
 * <br>
 * Bidirectional search.<br>
 * <br>
 * The strategy of this search implementation is inspired by the description of
 * the bidirectional search algorithm i.e. 'Bidirectional search is implemented
 * by replacing the goal test with a check to see whether the frontiers of the
 * two searches intersect;'. But to gain some worst-case guarantees with respect
 * to solution quality (see below), the goal test of the original and the
 * reverse problem are replaced by a check, whether the node's state was already
 * explored in the other problem. Only one frontier is used which allows to use
 * the same queue search interface as known from other search implementations.
 * This implementation can be combined with many abstractions of search, e.g.
 * breadth-first-search or uniform-cost-search.
 * 
 * @author Ruediger Lunde
 */
public class BidirectionalQueueSearch extends QueueSearch {

	// last value should never occur!
	public enum SearchOutcome {
		PATH_FOUND, PATH_NOT_FOUND, PATH_FOUND_BUT_NO_REVERSE_ACTIONS
	};

	private final static int ORG_P_IDX = 0;
	private final static int REV_P_IDX = 1;

	private SearchOutcome searchOutcome = SearchOutcome.PATH_NOT_FOUND;

	// index 0: original problem, index 2: reverse problem
	private List<Map<Object, ExtendedNode>> explored;

	public BidirectionalQueueSearch() {
		explored = new ArrayList<Map<Object, ExtendedNode>>(2);
		explored.add(new HashMap<Object, ExtendedNode>());
		explored.add(new HashMap<Object, ExtendedNode>());
	}

	/**
	 * Implements an approximation algorithm for bidirectional problems with
	 * exactly one initial and one goal state. The algorithm guarantees the
	 * following: If the queue is ordered by path costs (uniform cost search),
	 * the path costs of the solution will be less or equal to the costs of the
	 * best solution multiplied with two. Especially, if all step costs are the
	 * same, the path costs of the result will exceed the optimal path by the
	 * costs of one step at maximum.
	 * 
	 * @param problem
	 *            a bidirectional search problem
	 * @param frontier
	 *            the data structure to be used to decide which node to be
	 *            expanded next
	 * 
	 * @return a list of actions to the goal if the goal was found, a list
	 *         containing a single NoOp Action if already at the goal, or an
	 *         empty list if the goal could not be found.
	 */
	public List<Action> search(Problem problem, Queue<Node> frontier) {
		assert (problem instanceof BidirectionalProblem);

		this.frontier = frontier;
		searchOutcome = SearchOutcome.PATH_NOT_FOUND;
		clearInstrumentation();

		clearInstrumentation();

		Problem orgP = ((BidirectionalProblem) problem).getOriginalProblem();
		Problem revP = ((BidirectionalProblem) problem).getReverseProblem();
		explored.get(ORG_P_IDX).clear();
		explored.get(REV_P_IDX).clear();

		// initialize the frontier using the initial state of the problem
		insertIntoFrontier(new ExtendedNode(new Node(orgP.getInitialState()), ORG_P_IDX));
		insertIntoFrontier(new ExtendedNode(new Node(revP.getInitialState()), REV_P_IDX));

		while (!isFrontierEmpty() && !CancelableThread.currIsCanceled()) {
			// choose a leaf node and remove it from the frontier
			ExtendedNode nodeToExpand = (ExtendedNode) popNodeFromFrontier();
			// if the node contains a goal state then return the
			// corresponding solution
			ExtendedNode nodeFromOtherProblem = explored.get(1 - nodeToExpand.getProblemIndex())
					.get(nodeToExpand.getState());
			if (nodeFromOtherProblem != null)
				return getSolution(orgP, nodeToExpand, nodeFromOtherProblem);
			// expand the chosen node, adding the resulting nodes to the
			// frontier
			for (Node successor : expandNode(nodeToExpand, problem)) {
				if (nodeToExpand.getProblemIndex() == ORG_P_IDX || getReverseAction(orgP, successor) != null)
					insertIntoFrontier(new ExtendedNode(successor, nodeToExpand.getProblemIndex()));
			}
		}
		// if the frontier is empty then return failure
		return failure();
	}

	/**
	 * Describes the success of the last search call.
	 */
	public SearchOutcome getSearchOutcome() {
		return searchOutcome;
	}

	/**
	 * Inserts the node at the tail of the frontier if the corresponding state
	 * is not yet explored.
	 */
	@Override
	protected void insertIntoFrontier(Node node) {
		if (!isExplored(node)) {
			frontier.insert(node);
			updateMetrics(frontier.size());
		}
	}

	/**
	 * Removes the node at the head of the frontier, adds it to the
	 * corresponding explored map, and returns the node.
	 * 
	 * @return the node at the head of the frontier.
	 */
	@Override
	protected Node popNodeFromFrontier() {
		Node result = frontier.pop();
		// add the node to the explored set of the corresponding problem
		setExplored(result);
		updateMetrics(frontier.size());
		return result;
	}

	/**
	 * Pops nodes of already explored states from the head of the frontier and
	 * checks whether there are still some nodes left.
	 */
	@Override
	protected boolean isFrontierEmpty() {
		while (!frontier.isEmpty()) {
			Node result = frontier.peek();
			if (!isExplored(result))
				break;
			frontier.pop();
			updateMetrics(frontier.size());
		}
		return frontier.isEmpty();
	}

	/**
	 * Not supported.
	 */
	public void setCheckGoalBeforeAddingToFrontier(boolean checkGoalBeforeAddingToFrontier) {
		// throw new UnsupportedOperationException();
	}

	/**
	 * Computes a list of actions by appending the list of actions corresponding
	 * to the provided node of the original problem and in reverse order the
	 * list of actions corresponding to the provided node of the reverse
	 * problem. Note that both nodes must be linked to the same state. Success
	 * is not guaranteed if some actions cannot be reversed.
	 */
	private List<Action> getSolution(Problem orgP, ExtendedNode node1, ExtendedNode node2) {
		assert node1.getState().equals(node2.getState());

		Node nodeOrg = node1.getProblemIndex() == ORG_P_IDX ? node1 : node2;
		Node nodeRev = node1.getProblemIndex() == REV_P_IDX ? node1 : node2;

		while (nodeRev.getParent() != null) {
			Action action = getReverseAction(orgP, nodeRev);
			if (action != null) {
				Object stateNext = nodeRev.getParent().getState();
				double stepCosts = orgP.getStepCostFunction().c(nodeRev.getState(), action, stateNext);
				nodeOrg = new Node(stateNext, nodeOrg, action, stepCosts);
				nodeRev = nodeRev.getParent();
			} else { // should never happen...
				searchOutcome = SearchOutcome.PATH_FOUND_BUT_NO_REVERSE_ACTIONS;
				return failure();
			}
		}
		metrics.set(METRIC_PATH_COST, nodeOrg.getPathCost());
		searchOutcome = SearchOutcome.PATH_FOUND;
		return SearchUtils.actionsFromNodes(nodeOrg.getPathFromRoot());
	}

	private Action getReverseAction(Problem orgP, Node node) {
		Object stateCurr = node.getState();
		Object stateNext = node.getParent().getState();

		for (Action action : orgP.getActionsFunction().actions(stateCurr)) {
			Object aResult = orgP.getResultFunction().result(stateCurr, action);
			if (stateNext.equals(aResult))
				return action;
		}
		return null;
	}

	private boolean isExplored(Node node) {
		ExtendedNode eNode = (ExtendedNode) node;
		return explored.get(eNode.getProblemIndex()).containsKey(eNode.getState());
	}

	private void setExplored(Node node) {
		ExtendedNode eNode = (ExtendedNode) node;
		explored.get(eNode.getProblemIndex()).put(eNode.getState(), eNode);
	}

	/**
	 * Maintains the usual node data and additionally the index of the problem
	 * to which the node belongs.
	 * 
	 * @author Ruediger Lunde
	 *
	 */
	static class ExtendedNode extends Node {

		int problemIndex;

		public ExtendedNode(Node node, int problemIndex) {
			super(node.getState(), node.getParent(), node.getAction(),
					node.getParent() != null ? node.getPathCost() - node.getParent().getPathCost() : 0.0);
			this.problemIndex = problemIndex;
		}

		public int getProblemIndex() {
			return problemIndex;
		}

		@Override
		public String toString() {
			return "[" + getState() + ":" + problemIndex + "]";
		}
	}
}
