package aima.core.search.basic.uninformed;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.function.Supplier;

import aima.core.search.api.NodeFactory;
import aima.core.search.basic.QueuedFirstGraphSearch;
import aima.core.search.basic.support.BasicFrontierQueueWithStateTracking;

/**
 * Artificial Intelligence A Modern Approach (4th Edition): Figure ??, page ??.<br>
 * <br>
 *
 * <pre>
 * function BREADTH-FIRST-GRAPH-SEARCH(problem) returns a solution, or failure
 *   node &lt;- a node with STATE = problem.INITIAL-STATE, PATH-COST=0
 *   if problem.GOAL-TEST(node.STATE) then return SOLUTION(node)
 *   frontier &lt;- a FIFO queue with node as the only element
 *   explored &lt;- an empty set
 *   loop do
 *      if EMPTY?(frontier) then return failure
 *      node &lt;- POP(frontier) // chooses the shallowest node in frontier
 *      add node.STATE to explored
 *      for each action in problem.ACTIONS(node.STATE) do
 *          child &lt;- CHILD-NODE(problem, node, action)
 *          if child.STATE is not in explored or frontier then
 *              if problem.GOAL-TEST(child.STATE) then return SOLUTION(child)
 *              frontier &lt;- INSERT(child, frontier)
 * </pre>
 *
 * Figure ?? Breadth-first search on a graph.<br>
 *
 * An instance of the general graph-search algorithm (Figure ?.?) in which the
 * shallowest unexpanded node is chosen for expansion. This is achieved very simply
 * by using a FIFO queue for the frontier. Thus, new nodes (which are always deeper
 * than their parent) go to the back of the queue, and old nodes, which are shallower
 * than the new nodes, get expanded first. There is one slight tweak on the general
 * graph-search algorithm, which is that the goal test is applied to each node
 * when it is generated rather than when it is selected for expansion.
 *
 * @author Ciaran O'Reilly
 * @author Ruediger Lunde
 */
public class BreadthFirstGraphSearch<A, S> extends QueuedFirstGraphSearch<A, S> {
	public BreadthFirstGraphSearch() {
		super(() -> new BasicFrontierQueueWithStateTracking<A, S>(LinkedList::new, HashSet::new));
	}
	
	public BreadthFirstGraphSearch(NodeFactory<A, S> nodeFactory, Supplier<Set<S>> exploredSupplier) {
		super(nodeFactory, () -> new BasicFrontierQueueWithStateTracking<A, S>(LinkedList::new, HashSet::new), exploredSupplier);
	}
}