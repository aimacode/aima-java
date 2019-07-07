package aima.core.search.framework.qsearch;

import java.util.*;

import aima.core.search.framework.Node;
import aima.core.search.framework.NodeFactory;
import aima.core.search.framework.problem.Problem;
import aima.core.util.Tasks;

/**
 * Artificial Intelligence A Modern Approach (4th Edition): ??
 * <br>
 *
 * <pre>
 * function GRAPH-SEARCH(problem) returns a solution, or failure
 *  frontier &lt;- a queue initially containing one path, for the problem's initial state
 *  reached &lt;- a table of {state: node}; initially empty (RLu: ??)
 *  solution &lt;- failure
 *  while frontier is not empty and solution can possibly be improved do
 *    parent &lt;- some node that we choose to remove from frontier
 *    for child in EXPAND(parent) do
 *      s &lt;- child.state
 *      if s is not in reached or child is a cheaper path than reached[s] then
 *        reached[s] &lt;- child
 *        add child to frontier
 *        if s is a goal and child is cheaper than solution then
 *          solution = child
 *  return solution
 * </pre>
 *
 * Figure ?? In the GRAPH-SEARCH algorithm, we keep track of the best solution found so far,
 * as well as the states that we have already reached, and a frontier of paths from which we
 * will choose the next path to expand. In any specific search algorithm, we specify
 * (1) the criteria for ordering the paths in the frontier, and
 * (2) the procedure for determining when it is no longer possible to improve on a solution.
 *
 * <br>
 *
 * @param <S> The type used to represent states
 * @param <A> The type of the actions to be used to navigate through the state space
 *
 * @author Ruediger Lunde
 */
public class GraphSearch4e<S, A> extends QueueSearch<S, A> {

	private Queue<Node<S, A>> frontier;
	private Comparator<? super Node<S, A>> nodeComparator = null;

	public GraphSearch4e() {
		this(new NodeFactory<>());
	}

	public GraphSearch4e(NodeFactory<S, A> nodeFactory) {
		super(nodeFactory);
	}

	/**
	 * Template method which receives a problem and a queue implementing the search strategy
	 * and computes a node referencing a goal state, if such a state was found.
	 *
	 * @param problem
	 *            the search problem
	 * @param frontier
	 *            the data structure for nodes that are waiting to be expanded
	 *
	 * @return a node referencing a goal state, if the goal was found, otherwise empty;
	 */
	@Override
	public Optional<Node<S, A>> findNode(Problem<S, A> problem, Queue<Node<S, A>> frontier) {
		clearMetrics();
		this.frontier = frontier;
		nodeComparator = (frontier instanceof PriorityQueue<?>) ?
				((PriorityQueue<Node<S, A>>) frontier).comparator() : null;
		Node<S, A> root = nodeFactory.createNode(problem.getInitialState());

		/// frontier <- a queue initially containing one path, for the problem's initial state
		/// reached <- a table of {state: node}; initially empty
		/// solution <- failure
		addToFrontier(root);
		Hashtable<S, Node<S, A>> reached = new Hashtable<>();
		Node<S, A> solution = null;

		// missing in pseudocode...
		reached.put(root.getState(), root); // initial state has been reached!
		if (problem.testSolution(root)) // initial state can be a goal state
			return asOptional(root);

		/// while frontier is not empty and solution can possibly be improved do
		while (!frontier.isEmpty() && canPossiblyBeImproved(solution) && !Tasks.currIsCancelled()) {
			/// parent <- some node that we choose to remove from frontier
			Node<S, A> parent = removeFromFrontier();

			// missing in pseudocode (a better path might have been found for the state)
			if (reached.get(parent.getState()) != parent)
				continue;

			/// for child in EXPAND(parent) do
			for (Node<S, A> child : nodeFactory.getSuccessors(parent, problem)) {
				/// s <- child.state
				S s = child.getState();
				/// if s is not in reached or child is a cheaper path than reached[s] then
				if (isCheaper(child, reached.get(s))) {
					/// reached[s] <- child
					reached.put(s, child);
					/// add child to frontier
					addToFrontier(child);
					/// if s is a goal and child is cheaper than solution then
					if (problem.testSolution(child) && isCheaper(child, solution))
						/// solution = child
						solution = child;
				}
			}
		}
		/// return solution
		return asOptional(solution);
	}

	/**
	 * Inserts the node at the tail of the frontier.
	 */
	private void addToFrontier(Node<S, A> node) {
		frontier.add(node);
		updateMetrics(frontier.size());
	}

	/**
	 * Removes and returns the node at the head of the frontier.
	 *
	 * @return the node at the head of the frontier.
	 */
	private Node<S, A> removeFromFrontier() {
		Node<S, A> result = frontier.remove();
		updateMetrics(frontier.size());
		return result;
	}

	/**
	 * Primitive operation which tests whether it makes sense to continue search for better solutions.
	 * This implementation tests whether the first element of the frontier is cheaper than the
	 * solution. This is sufficient for priority queues which evaluate nodes in a non-decreasing way
	 * on all paths. It is assumed that the frontier contains at least one node.
	 */
	protected boolean canPossiblyBeImproved(Node<S, A> solution) {
		return isCheaper(frontier.element(), solution);
	}

	/**
	 * Primitive operation which compares <code>node1</code> and <code>node2</code> with the comparator used in the frontier
	 * if possible. If no comparator is given or <code>node2</code> is null, value true is returned.
	 * @param node1 A node.
	 * @param node2 A node, possibly null.
	 */
	protected boolean isCheaper(Node<S, A> node1, Node<S, A> node2) {
		return node2 == null || nodeComparator != null && nodeComparator.compare(node1, node2) < 0;
	}
}