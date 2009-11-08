package aima.core.search.uninformed;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import aima.core.agent.Action;
import aima.core.search.framework.Node;
import aima.core.search.framework.PrioritySearch;
import aima.core.search.framework.Problem;
import aima.core.search.framework.QueueSearch;
import aima.core.util.datastructure.Queue;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): Figure 3.14, page ??. 
 * <code>
 * function UNIFORM-COST-SEARCH(problem) returns a solution, or failure
 *   node <- a node with STATE = problem.INITIAL-STATE, PATH-COST = 0
 *   frontier <- a priority queue ordered by PATH-COST, with node as the only element
 *   explored <- an empty set
 *   loop do
 *      if EMPTY?(frontier) then return failure
 *      node <- POP(frontier) // chooses the lowest-cost node in frontier
 *      if problem.GOAL-TEST(node.STATE) then return SOLUTION(node)
 *      add node.STATE to explored
 *      for each action in problem.ACTIONS(node.STATE) do
 *          child <- CHILD-NODE(problem, node, action)
 *          if child.STATE is not in explored or frontier then
 *             frontier <- INSERT(child, frontier)
 *          else if child.STATE is in frontier with higher PATH-COST then
 *             replace that frontier node with child
 * </code> 
 * Figure 3.14 Uniform-cost search on a graph. The algorithm is identical to the general
 * graph search algorithm in Figure 3.7, except for the use of a priority queue and the addition
 * of an extra check in case a shorter path to a frontier state is discovered. The data structure
 * for frontier needs to support efficient membership testing, so it should combine the capabilities
 * of a priority queue and a hash table.
 */

/**
 * @author Ciaran O'Reilly
 * 
 */
public class UniformCostSearch extends PrioritySearch {

	public UniformCostSearch() {
		this.search = new UniformCostQueueSearch();
	}

	//
	// PROTECTED METHODS
	//
	@Override
	protected Comparator<Node> getComparator(Problem p) {
		return new Comparator<Node>() {
			public int compare(Node node1, Node node2) {
				return (new Double(node1.getPathCost()).compareTo(new Double(
						node2.getPathCost())));
			}
		};
	}
	
	//
	// PRIVATE
	//
	private class UniformCostQueueSearch extends QueueSearch {
		private Set<Object> explored = new HashSet<Object>();
		private Queue<Node> frontier = null;
		private Map<Object, Node> frontierState = new HashMap<Object, Node>();
		private List<Node> addToFrontier = new ArrayList<Node>();
		
		// Need to override search() method so that I can re-initialize
		// the explored set should multiple calls to search be made.
		@Override
		public List<Action> search(Problem problem, Queue<Node> frontier) {
			// Need to keep track of the frontier
			// as will need to remove items with higher path cost
			// to the same state.
			this.frontier = frontier; 
			// initialize the explored set to be empty
			explored.clear();
			frontierState.clear();			
			return super.search(problem, frontier);
		}
		
		@Override
		public Node removeNodeFromFrontier(Queue<Node> frontier) {
			Node toRemove = super.removeNodeFromFrontier(frontier);
			frontierState.remove(toRemove.getState());
			return toRemove;
		}

		@Override
		public List<Node> getResultingNodesToAddToFrontier(
				Node nodeToExpand, Problem problem) {

			addToFrontier.clear();
			// add the node to the explored set
			explored.add(nodeToExpand.getState());		
			// expand the chosen node, adding the resulting nodes to the frontier
			for (Node cfn : expandNode(nodeToExpand, problem)) {
				Node frontierNode = frontierState.get(cfn.getState());
				boolean yesAddToFrontier = false;
				// only if not in the frontier or explored set
				if (null == frontierNode && !explored.contains(cfn.getState())) {
					yesAddToFrontier = true;
				} else if (null != frontierNode && frontierNode.getPathCost() > cfn.getPathCost()) {
					yesAddToFrontier = true;
					// Want to replace the current frontier node with the child node
					// therefore mark the child to be added and remove the
					// current fontierNode
					frontier.remove(frontierNode);
					// Ensure removed from node to add to frontier as well
					// as 1 or more may reach the same state at the same time
					addToFrontier.remove(frontierNode);
				}
				
				if (yesAddToFrontier) {
					addToFrontier.add(cfn);
					frontierState.put(cfn.getState(), cfn);		
				}
			}
			
			return addToFrontier;
		}
	}
}