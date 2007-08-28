package aima.search.uninformed;

import java.util.ArrayList;
import java.util.List;

import aima.search.framework.BidirectionalProblem;
import aima.search.framework.GraphSearch;
import aima.search.framework.Metrics;
import aima.search.framework.Node;
import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchUtils;
import aima.search.framework.Successor;
import aima.search.nodestore.CachedStateNodeStore;
import aima.search.nodestore.FIFONodeStore;

/**
 * Artificial Intelligence A Modern Approach (2nd Edition): page 79.
 * Bidirectional search.
 */

/**
 * @author Ciaran O'Reilly
 * 
 */
public class BidirectionalSearch implements Search {
	public enum SearchOutcome {
		PATH_FOUND_FROM_ORIGINAL_PROBLEM, PATH_FOUND_FROM_REVERSE_PROBLEM, PATH_FOUND_BETWEEN_PROBLEMS, PATH_NOT_FOUND
	};

	protected Metrics metrics;

	private SearchOutcome searchOutcome = SearchOutcome.PATH_NOT_FOUND;

	private static final String NODES_EXPANDED = "nodesExpanded";

	private static final String QUEUE_SIZE = "queueSize";

	private static final String MAX_QUEUE_SIZE = "maxQueueSize";

	private static final String PATH_COST = "pathCost";

	public BidirectionalSearch() {
		metrics = new Metrics();
	}

	public List<String> search(Problem p) throws Exception {

		assert (p instanceof BidirectionalProblem);

		searchOutcome = SearchOutcome.PATH_NOT_FOUND;

		clearInstrumentation();

		Problem op = ((BidirectionalProblem) p).getOriginalProblem();
		Problem rp = ((BidirectionalProblem) p).getReverseProblem();

		CachedStateNodeStore opFringe = new CachedStateNodeStore(
				new FIFONodeStore());
		CachedStateNodeStore rpFringe = new CachedStateNodeStore(
				new FIFONodeStore());

		GraphSearch ogs = new GraphSearch();
		GraphSearch rgs = new GraphSearch();
		// Ensure the instrumentation for these
		// are cleared down as their values
		// are used in calculating the overall
		// bidirectional metrics.
		ogs.clearInstrumentation();
		rgs.clearInstrumentation();

		Node opNode = new Node(op.getInitialState());
		Node rpNode = new Node(rp.getInitialState());
		opFringe.add(opNode);
		rpFringe.add(rpNode);

		setQueueSize(opFringe.size() + rpFringe.size());
		setNodesExpanded(ogs.getNodesExpanded() + rgs.getNodesExpanded());

		while (!(opFringe.isEmpty() && rpFringe.isEmpty())) {
			// Determine the nodes to work with and expand their fringes
			// in preparation for testing whether or not the two
			// searches meet or one or other is at the GOAL.
			if (!opFringe.isEmpty()) {
				opNode = opFringe.remove();
				ogs.addExpandedNodesToFringe(opFringe, opNode, op);
			} else {
				opNode = null;
			}
			if (!rpFringe.isEmpty()) {
				rpNode = rpFringe.remove();
				rgs.addExpandedNodesToFringe(rpFringe, rpNode, rp);
			} else {
				rpNode = null;
			}

			setQueueSize(opFringe.size() + rpFringe.size());
			setNodesExpanded(ogs.getNodesExpanded() + rgs.getNodesExpanded());

			//
			// First Check if either fringe contains the other's state
			if (null != opNode && null != rpNode) {
				Node popNode = null;
				Node prpNode = null;
				if (opFringe.containsNodeBasedOn(rpNode.getState())) {
					popNode = opFringe.getNodeBasedOn(rpNode.getState());
					prpNode = rpNode;
				} else if (rpFringe.containsNodeBasedOn(opNode.getState())) {
					popNode = opNode;
					prpNode = rpFringe.getNodeBasedOn(opNode.getState());
					// Need to also check whether or not the nodes that
					// have been taken off the fringe actually represent the
					// same state, otherwise there are instances whereby
					// the searches can pass each other by
				} else if (opNode.getState().equals(rpNode.getState())) {
					popNode = opNode;
					prpNode = rpNode;
				}
				if (null != popNode && null != prpNode) {
					List<String> actions = retrieveActions(op, rp, popNode,
							prpNode);
					// It may be the case that it is not in fact possible to
					// traverse from the original node to the goal node based on
					// the reverse path (i.e. unidirectional links: e.g.
					// InitialState(A)<->C<-Goal(B) )
					if (null != actions) {
						return actions;
					}
				}
			}

			//
			// Check if the original problem is at the GOAL state
			if (null != opNode && op.isGoalState(opNode.getState())) {
				// No need to check return value for null here
				// as an action path discovered from the goal
				// is guaranteed to exist
				return retrieveActions(op, rp, opNode, null);
			}
			//
			// Check if the reverse problem is at the GOAL state
			if (null != rpNode && rp.isGoalState(rpNode.getState())) {
				List<String> actions = retrieveActions(op, rp, null, rpNode);
				// It may be the case that it is not in fact possible to
				// traverse from the original node to the goal node based on
				// the reverse path (i.e. unidirectional links: e.g.
				// InitialState(A)<-Goal(B) )
				if (null != actions) {
					return actions;
				}
			}
		}

		// Empty List can indicate already at Goal
		// or unable to find valid set of actions
		return new ArrayList<String>();
	}

	public SearchOutcome getSearchOutcome() {
		return searchOutcome;
	}

	public Metrics getMetrics() {
		return metrics;
	}

	public void clearInstrumentation() {
		metrics.set(NODES_EXPANDED, 0);
		metrics.set(QUEUE_SIZE, 0);
		metrics.set(MAX_QUEUE_SIZE, 0);
		metrics.set(PATH_COST, 0.0);
	}

	public int getNodesExpanded() {
		return metrics.getInt(NODES_EXPANDED);
	}

	public void setNodesExpanded(int nodesExpanded) {
		metrics.set(NODES_EXPANDED, nodesExpanded);
	}

	public int getQueueSize() {
		return metrics.getInt(QUEUE_SIZE);
	}

	public void setQueueSize(int queueSize) {
		metrics.set(QUEUE_SIZE, queueSize);
		int maxQSize = metrics.getInt(MAX_QUEUE_SIZE);
		if (queueSize > maxQSize) {
			metrics.set(MAX_QUEUE_SIZE, queueSize);
		}
	}

	public int getMaxQueueSize() {
		return metrics.getInt(MAX_QUEUE_SIZE);
	}

	public double getPathCost() {
		return metrics.getDouble(PATH_COST);
	}

	public void setPathCost(Double pathCost) {
		metrics.set(PATH_COST, pathCost);
	}

	//
	// PRIVATE METHODS
	//	
	private List<String> retrieveActions(Problem op, Problem rp,
			Node originalPath, Node reversePath) {
		List<String> actions = new ArrayList<String>();

		if (null == reversePath) {
			// This is the simple case whereby the path has been found
			// from the original problem first
			setPathCost(originalPath.getPathCost());
			searchOutcome = SearchOutcome.PATH_FOUND_FROM_ORIGINAL_PROBLEM;
			actions = SearchUtils.actionsFromNodes(originalPath
					.getPathFromRoot());
		} else {
			List<Node> nodePath = new ArrayList<Node>();
			Object originalState = null;
			if (null != originalPath) {
				nodePath.addAll(originalPath.getPathFromRoot());
				originalState = originalPath.getState();
			}
			// Only append the reverse path if it is not the
			// GOAL state from the original problem (if you don't
			// you could end up appending a partial reverse path
			// that looks back on its initial state)
			if (!op.isGoalState(reversePath.getState())) {
				List<Node> rpath = reversePath.getPathFromRoot();
				for (int i = rpath.size() - 1; i >= 0; i--) {
					// Ensure do not include the node from the reverse path
					// that is the one that potentially overlaps with the
					// original path (i.e. if started in goal state or where
					// they meet in the middle).
					if (!rpath.get(i).getState().equals(originalState)) {
						nodePath.add(rpath.get(i));
					}
				}
			}

			if (!canTraversePathFromOriginalProblem(op, nodePath, actions)) {
				// This is where it is possible to get to the initial state
				// from the goal state (i.e. reverse path) but not the other way
				// round, null returned to indicate an invalid path found from
				// the reverse problem
				return null;
			}

			if (null == originalPath) {
				searchOutcome = SearchOutcome.PATH_FOUND_FROM_REVERSE_PROBLEM;
			} else {
				// Need to ensure that where the original and reverse paths
				// overlap, as they can link based on their fringes, that
				// the reverse path is actually capable of connecting to
				// the previous node in the original path (if not root).
				if (canConnectToOriginalFromReverse(rp, originalPath,
						reversePath)) {
					searchOutcome = SearchOutcome.PATH_FOUND_BETWEEN_PROBLEMS;
				} else {
					searchOutcome = SearchOutcome.PATH_FOUND_FROM_ORIGINAL_PROBLEM;
				}
			}
		}

		return actions;
	}

	private boolean canTraversePathFromOriginalProblem(Problem op,
			List<Node> path, List<String> actions) {
		boolean rVal = true;
		double pc = 0.0;

		for (int i = 0; i < (path.size() - 1); i++) {
			Object currentState = path.get(i).getState();
			Object nextState = path.get(i + 1).getState();
			List<Successor> successors = op.getSuccessorFunction()
					.getSuccessors(currentState);
			boolean found = false;
			for (Successor s : successors) {
				if (nextState.equals(s.getState())) {
					found = true;
					pc += op.getStepCostFunction().calculateStepCost(
							currentState, nextState, s.getAction());
					actions.add(s.getAction());
					break;
				}
			}

			if (!found) {
				rVal = false;
				break;
			}
		}

		setPathCost(true == rVal ? pc : 0.0);

		return rVal;
	}

	private boolean canConnectToOriginalFromReverse(Problem rp,
			Node originalPath, Node reversePath) {
		boolean rVal = true;

		// Only need to test if not already at root
		if (!originalPath.isRootNode()) {
			rVal = false;
			List<Successor> successors = rp.getSuccessorFunction()
					.getSuccessors(reversePath.getState());
			for (Successor s : successors) {
				if (originalPath.getParent().getState().equals(s.getState())) {
					rVal = true;
					break;
				}
			}
		}

		return rVal;
	}
}
