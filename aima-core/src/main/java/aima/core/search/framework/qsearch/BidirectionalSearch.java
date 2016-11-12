package aima.core.search.framework.qsearch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import aima.core.agent.Action;
import aima.core.search.framework.Node;
import aima.core.search.framework.NodeExpander;
import aima.core.search.framework.problem.BidirectionalProblem;
import aima.core.search.framework.problem.Problem;
import aima.core.util.CancelableThread;

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
 * BreadthFirstSearch, UniformCostSearch, or even AStarSearch.
 * 
 * @author Ruediger Lunde
 */
public class BidirectionalSearch extends QueueSearch {

	private final static int ORG_P_IDX = 0;
	private final static int REV_P_IDX = 1;

	/**
	 * Controls whether all actions of the reverse problem are tested to be
	 * reversible. This shouldn't be necessary for a correctly implemented
	 * bidirectional problem. But in case this is not guaranteed, the test is
	 * helpful to avoid failures.
	 */
	private boolean isReverseActionTestEnabled = true;

	// index 0: original problem, index 2: reverse problem
	private List<Map<Object, ExtendedNode>> explored;
	private ExtendedNode goalStateNode;

	public BidirectionalSearch() {
		this(new NodeExpander());
	}

	public BidirectionalSearch(NodeExpander nodeExpander) {
		super(nodeExpander);
		explored = new ArrayList<Map<Object, ExtendedNode>>(2);
		explored.add(new HashMap<Object, ExtendedNode>());
		explored.add(new HashMap<Object, ExtendedNode>());
	}

	/**
	 * Implements an approximation algorithm for bidirectional problems with
	 * exactly one initial and one goal state. The algorithm guarantees the
	 * following: If the queue is ordered by path costs (uniform cost search),
	 * the path costs of the solution will be less or equal to the costs of the
	 * best solution multiplied with two. Especially, if all step costs are
	 * equal and the reverse problem provides reverse actions for all actions of
	 * the original problem, the path costs of the result will exceed the
	 * optimal path by the costs of one step at maximum.
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
	public Node findNode(Problem problem, Queue<Node> frontier) {
		assert (problem instanceof BidirectionalProblem);
		
		nodeExpander.useParentLinks(true); // bidirectional search needs parents!
		this.frontier = frontier;
		clearInstrumentation();
		explored.get(ORG_P_IDX).clear();
		explored.get(REV_P_IDX).clear();

		Problem orgP = ((BidirectionalProblem) problem).getOriginalProblem();
		Problem revP = ((BidirectionalProblem) problem).getReverseProblem();
		ExtendedNode initStateNode;
		initStateNode = new ExtendedNode(nodeExpander.createRootNode(orgP.getInitialState()), ORG_P_IDX);
		goalStateNode = new ExtendedNode(nodeExpander.createRootNode(revP.getInitialState()), REV_P_IDX);

		if (orgP.getInitialState().equals(revP.getInitialState()))
			return getSolution(orgP, initStateNode, goalStateNode);

		// initialize the frontier using the initial state of the problem
		addToFrontier(initStateNode);
		addToFrontier(goalStateNode);

		while (!isFrontierEmpty() && !CancelableThread.currIsCanceled()) {
			// choose a leaf node and remove it from the frontier
			ExtendedNode nodeToExpand = (ExtendedNode) removeFromFrontier();
			ExtendedNode nodeFromOtherProblem;

			// if the node contains a goal state then return the
			// corresponding solution
			if (!earlyGoalTest && (nodeFromOtherProblem = getCorrespondingNodeFromOtherProblem(nodeToExpand)) != null)
				return getSolution(orgP, nodeToExpand, nodeFromOtherProblem);

			// expand the chosen node, adding the resulting nodes to the
			// frontier
			for (Node s : nodeExpander.expand(nodeToExpand, problem)) {
				ExtendedNode successor = new ExtendedNode(s, nodeToExpand.getProblemIndex());
				if (!isReverseActionTestEnabled || nodeToExpand.getProblemIndex() == ORG_P_IDX
						|| getReverseAction(orgP, successor) != null) {

					if (earlyGoalTest
							&& (nodeFromOtherProblem = getCorrespondingNodeFromOtherProblem(successor)) != null)
						return getSolution(orgP, successor, nodeFromOtherProblem);

					addToFrontier(successor);
				}
			}
		}
		// if the frontier is empty then return failure
		return null;
	}

	/**
	 * Enables a check for all actions offered by the reverse problem whether
	 * there exists a corresponding action of the original problem. Default
	 * value is true.
	 */
	public void setReverseActionTestEnabled(boolean state) {
		isReverseActionTestEnabled = state;
	}

	/**
	 * Inserts the node at the tail of the frontier if the corresponding state
	 * is not yet explored.
	 */
	@Override
	protected void addToFrontier(Node node) {
		if (!isExplored(node)) {
			frontier.add(node);
			updateMetrics(frontier.size());
		}
	}

	/**
	 * Cleans up the head of the frontier, removes the first node of a
	 * non-explored state from the head of the frontier, adds it to the
	 * corresponding explored map, and returns the node.
	 * 
	 * @return A node of a not yet explored state.
	 */
	@Override
	protected Node removeFromFrontier() {
		cleanUpFrontier(); // not really necessary because isFrontierEmpty
							// should be called before...
		Node result = frontier.remove();
		updateMetrics(frontier.size());
		// add the node to the explored set of the corresponding problem
		setExplored(result);
		return result;
	}

	/**
	 * Pops nodes of already explored states from the head of the frontier and
	 * checks whether there are still some nodes left.
	 */
	@Override
	protected boolean isFrontierEmpty() {
		cleanUpFrontier();
		updateMetrics(frontier.size());
		return frontier.isEmpty();
	}

	/**
	 * Helper method which removes nodes of already explored states from the
	 * head of the frontier.
	 */
	private void cleanUpFrontier() {
		while (!frontier.isEmpty() && isExplored(frontier.element()))
			frontier.remove();
	}

	/**
	 * Computes a node whose sequence of recursive parents corresponds to a
	 * sequence of actions which leads from the initial state of the original
	 * problem to the state of node1 and then to the initial state of the
	 * reverse problem, following reverse actions to parents of node2. Note that
	 * both nodes must be linked to the same state. Success is not guaranteed if
	 * some actions cannot be reversed.
	 */
	private Node getSolution(Problem orgP, ExtendedNode node1, ExtendedNode node2) {
		assert node1.getState().equals(node2.getState());
		
		Node orgNode = node1.getProblemIndex() == ORG_P_IDX ? node1 : node2;
		Node revNode = node1.getProblemIndex() == REV_P_IDX ? node1 : node2;

		while (revNode.getParent() != null) {
			Action action = getReverseAction(orgP, revNode);
			if (action != null) {
				Object nextState = revNode.getParent().getState();
				double stepCosts = orgP.getStepCostFunction().c(revNode.getState(), action, nextState);
				orgNode = nodeExpander.createNode(nextState, orgNode, action, stepCosts);
				revNode = revNode.getParent();
			} else {
				return null;
			}
		}
		metrics.set(METRIC_PATH_COST, orgNode.getPathCost());
		return orgNode;
	}

	/**
	 * Returns the action which leads from the state of <code>node</code> to the
	 * state of the node's parent, if such an action exists in problem
	 * <code>orgP</code>.
	 */
	private Action getReverseAction(Problem orgP, Node node) {
		Object currState = node.getState();
		Object nextState = node.getParent().getState();

		for (Action action : orgP.getActionsFunction().actions(currState)) {
			Object aResult = orgP.getResultFunction().result(currState, action);
			if (nextState.equals(aResult))
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

	private ExtendedNode getCorrespondingNodeFromOtherProblem(ExtendedNode node) {
		ExtendedNode result = explored.get(1 - node.getProblemIndex()).get(node.getState());

		// Caution: The goal test of the original problem should always include
		// the root node of the reverse problem as that node might not yet have
		// been explored yet. This is important if the reverse problem does not
		// provide reverse actions for all original problem actions.
		if (result == null && node.getProblemIndex() == ORG_P_IDX && node.getState() == goalStateNode.getState())
			result = goalStateNode;
		return result;
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
			super(node.getState(), node.getParent(), node.getAction(), node.getPathCost());
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
