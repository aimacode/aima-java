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
 *  reached &lt;- a table of {state: node}; initially empty
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
	 * Receives a problem and a queue implementing the search strategy and
	 * computes a node referencing a goal state, if such a state was found.
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

		/// frontier <- a queue initially containing one path, for the problem's initial state
		/// reached <- a table of {state: node}; initially empty
		/// solution <- failure
		Hashtable<S, Node<S, A>> reached = new Hashtable<>();
		Node<S, A> solution = null;
		Node<S, A> root = nodeFactory.createNode(problem.getInitialState());
		addToFrontier(root);
		// missing in pseudocode but necessary if the initial state is a goal state.
		if (problem.testSolution(root))
			return asOptional(root);

		// while frontier is not empty and solution can possibly be improved do
		while (!frontier.isEmpty() && !Tasks.currIsCancelled() && isCheaper(frontier.element(), solution)) {
			/// parent <- some node that we choose to remove from frontier
			Node<S, A> parent = removeFromFrontier();
			/// for child in EXPAND(parent) do
			for (Node<S, A> child : nodeFactory.getSuccessors(parent, problem)) {
				/// s <- child.state
				S s = child.getState();
				/// if s is not in reached or child is a cheaper path than reached[s] then
				Node<S, A> reachedNode = reached.get(s);
				if (reachedNode == null || isCheaper(child, reachedNode)) {
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
	 * Primitive operation which inserts the node at the tail of the frontier.
	 */
	protected void addToFrontier(Node<S, A> node) {
		frontier.add(node);
		updateMetrics(frontier.size());
	}

	/**
	 * Primitive operation which removes and returns the node at the head of the frontier.
	 *
	 * @return the node at the head of the frontier.
	 */
	protected Node<S, A> removeFromFrontier() {
		Node<S, A> result = frontier.remove();
		updateMetrics(frontier.size());
		return result;
	}

	public boolean isCheaper(Node<S, A> node1, Node<S, A> node2) {
		return node2 == null || nodeComparator != null && nodeComparator.compare(node1, node2) < 0;
	}
}