package aima.core.search.basic.uninformed;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.function.Supplier;

import aima.core.search.api.NodeFactory;
import aima.core.search.basic.QueuedFirstGraphSearch;
import aima.core.search.basic.support.BasicFrontierQueueWithStateTracking;

/**
 * Artificial Intelligence A Modern Approach (4th Edition): page ??.<br>
 * <br>
 * The depth-first algorithm is an instance of the graph-search algorithm
 * in Figure ??; whereas breadth-first-search uses a FIFO queue, depth-first
 * search uses a LIFO queue.
 *
 * @author Ciaran O'Reilly
 * @author Ravi Mohan
 * @author Ruediger Lunde
 */

/**
 * Artificial Intelligence A Modern Approach (4th Edition): Figure ??, page ??.<br>
 * <br>
 *
 * <pre>
 * function DEPTH-FIRST-GRAPH-SEARCH(problem) returns a solution, or failure
 *   node &lt;- a node with STATE = problem.INITIAL-STATE, PATH-COST=0
 *   if problem.GOAL-TEST(node.STATE) then return SOLUTION(node)
 *   frontier &lt;- a LIFO queue with node as the only element
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
 *
 * The depth-first algorithm is an instance of the graph-search algorithm
 * in Figure ??; whereas breadth-first-search uses a FIFO queue, depth-first
 * search uses a LIFO queue.
 *
 * @author Ciaran O'Reilly
 * @author Ruediger Lunde
 */
public class DepthFirstGraphSearch<A, S> extends QueuedFirstGraphSearch<A, S> {
	public DepthFirstGraphSearch() {
		super(() -> new BasicFrontierQueueWithStateTracking<A, S>(() -> Collections.asLifoQueue(new LinkedList<>()), HashSet::new));
	}
	
	public DepthFirstGraphSearch(NodeFactory<A, S> nodeFactory, Supplier<Set<S>> exploredSupplier) {
		super(nodeFactory, () -> new BasicFrontierQueueWithStateTracking<A, S>(() -> Collections.asLifoQueue(new LinkedList<>()), HashSet::new), exploredSupplier);
	}
}
