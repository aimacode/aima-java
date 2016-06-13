package aima.core.search.api;

/**
 * Artificial Intelligence A Modern Approach (4th Edition): Figure ??, page ??.
 * <br>
 *
 * Figure ?? Nodes are the data structures from which the search tree is
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
 * @param <A>
 *            the type of the action.
 * @param <S>
 *            the type of the state that node contains.
 *
 * @author Ciaran O'Reilly
 * @author Ravi Mohan
 * @author Mike Stampone
 */
public interface Node<A, S> {

	/**
	 *
	 * @return the state in the state space to which the node corresponds.
	 */
	S state();

	/**
	 *
	 * @return the node in the search tree that generated this node.
	 */
	Node<A, S> parent();

	/**
	 *
	 * @return the action that was applied to the parent to generate the node.
	 */
	A action();

	/**
	 *
	 * @return the cost, traditionally denoted by <em>g(n)</em>, of the path
	 *         from the initial state to the node, as indicated by the parent
	 *         pointers.
	 */
	double pathCost();
}
