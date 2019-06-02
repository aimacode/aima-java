package aima.core.search.framework;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): Figure 3.10, page
 * 79.<br>
 * 
 * Figure 3.10 Nodes are the data structures from which the search tree is
 * constructed. Each has a parent, a state, and various bookkeeping fields.
 * Arrows point from child to parent.<br>
 * <br>
 * Search algorithms require a data structure to keep track of the search tree
 * that is being constructed. For each node n of the tree, we have a structure
 * that contains four components:
 * <ul>
 * <li>n.STATE: the state in the state space to which the node corresponds;</li>
 * <li>n.PARENT: the node in the search tree that generated this node;</li>
 * <li>n.ACTION: the action that was applied to the parent to generate the node;
 * </li>
 * <li>n.PATH-COST: the cost, traditionally denoted by g(n), of the path from
 * the initial state to the node, as indicated by the parent pointers.</li>
 * </ul>
 *
 * @param <S> The type used to represent states
 * @param <A> The type of the actions to be used to navigate through the state space
 *
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 * @author Mike Stampone
 * @author Ruediger Lunde
 */
public class Node<S, A> {

	// n.STATE: the state in the state space to which the node corresponds;
	private final S state;

	// n.PARENT: the node in the search tree that generated this node;
	private final Node<S, A> parent;

	// n.ACTION: the action that was applied to the parent to generate the node;
	private final A action;

	// n.PATH-COST: the cost, traditionally denoted by g(n), of the path from
	// the initial state to the node, as indicated by the parent pointers.
	private final double pathCost;

	/**
	 * Constructs a root node for the specified state.
	 * 
	 * @param state
	 *            the state in the state space to which the node corresponds.
	 */
	public Node(S state) {
		this(state, null, null, 0.0);
	}

	/**
	 * Constructs a node with the specified state, parent, action, and path
	 * cost.
	 * 
	 * @param state
	 *            the state in the state space to which the node corresponds.
	 * @param parent
	 *            the node in the search tree that generated the node.
	 * @param action
	 *            the action that was applied to the parent to generate the
	 *            node.
	 * @param pathCost
	 *            full pathCost from the root node to here, typically
	 *            the root's path costs plus the step costs for executing
	 *            the the specified action.
	 */
	public Node(S state, Node<S, A> parent, A action, double pathCost) {
		this.state = state;
		this.parent = parent;
		this.action = action;
		this.pathCost = pathCost;
	}

	/**
	 * Returns the state in the state space to which the node corresponds.
	 * 
	 * @return the state in the state space to which the node corresponds.
	 */
	public S getState() {
		return state;
	}

	/**
	 * Returns this node's parent node, from which this node was generated.
	 * 
	 * @return the node's parenet node, from which this node was generated.
	 */
	public Node<S, A> getParent() {
		return parent;
	}

	/**
	 * Returns the action that was applied to the parent to generate the node.
	 * 
	 * @return the action that was applied to the parent to generate the node.
	 */
	public A getAction() {
		return action;
	}

	/**
	 * Returns the cost of the path from the initial state to this node as
	 * indicated by the parent pointers.
	 * 
	 * @return the cost of the path from the initial state to this node as
	 *         indicated by the parent pointers.
	 */
	public double getPathCost() {
		return pathCost;
	}

	/**
	 * Returns <code>true</code> if the node has no parent.
	 * 
	 * @return <code>true</code> if the node has no parent.
	 */
	public boolean isRootNode() {
		return parent == null;
	}

	@Override
	public String toString() {
		return "[parent=" + parent + ", action=" + action + ", state=" + getState() + ", pathCost=" + pathCost + "]";
	}
}