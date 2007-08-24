package aima.search.framework;

/**
 * Artificial Intelligence A Modern Approach (2nd Edition): Figure 3.7, page 70.
 * <code>
 * function TREE-SEARCH(problem, strategy) returns a solution, or failure
 *   initialize the search tree using the initial state of problem
 *   loop do
 *     if there are no candidates for expansion then return failure
 *     choose a leaf node for expansion according to strategy
 *     if the node contains a goal state then return the corresponding solution
 *     else expand the node and add the resulting nodes to the search tree.
 * </code>
 * Figure 3.7 An informal description of the general tree-search algorithm.
 * 
 * 
 * Artificial Intelligence A Modern Approach (2nd Edition): Figure 3.9, page 72.
 * <code>
 * function TREE-SEARCH(problem, fringe) returns a solution, or failure
 *   
 *   fringe <- INSERT(MAKE-NODE(INITIAL-STATE[problem]), fringe)
 *   loop do
 *     if EMPTY?(fringe) then return failure
 *     node <- REMOVE-FIRST(fringe)
 *     if GOAL-TEST[problem] applied to STATE[node] succeeds
 *       the return SOLUTION(node)
 *     fringe <- INSERT-ALL(EXPAND(node, problem), fringe)
 * ---------------------------------------------------------------------
 * function EXPAND(node, problem) returns a set of nodes
 * 
 *   successors <- empty set
 *   for each <action, result> in SUCCESSOR-FN[problem](STATE[node]) do
 *     s <- a new NODE
 *     STATE[s] <- result
 *     PARENT-NODE[s] <- node
 *     ACTION[s] <- action
 *     PATH-COST[s] <- PATH-COST[node] + STEP-COSTS(STATE[node], action, result)
 *     DEPTH[s] <- DEPTH[node] + 1
 *     add s to successor
 *   return successors
 * </code>
 * Figure 3.9 The general tree-search algorithm. (Note that the fringe argument must be an
 * empty queue, and the type of the queue will affect the order of the search.) The SOLUTION
 * function returns the sequence of actions obtained by following parent pointers back to the
 * root.
 */

/**
 * @author Ravi Mohan
 * 
 */

public class TreeSearch extends QueueSearch {

	@Override
	public void addExpandedNodesToFringe(NodeStore fringe, Node node,
			Problem problem) {
		fringe.add(expandNode(node, problem));
	}

}