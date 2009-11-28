package aima.core.search.framework;

import java.util.ArrayList;
import java.util.List;

import aima.core.agent.Action;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): Figure 3.10, page 79.<br>
 * 
 * Figure 3.10 Nodes are the data structures from which the search tree is constructed. Each
 * has a parent, a state, and various bookkeeping fields. Arrows point from child to parent.<br>
 * <br>
 * Search algorithms require a data structure to keep track of the search tree that is being
 * constructed. For each node n of the tree, we have a structure that contains four components:
 * <ul>
 * <li>n.STATE: the state in the state space to which the node corresponds;</li>
 * <li>n.PARENT: the node in the search tree that generated this node;</li>
 * <li>n.ACTION: the action that was applied to the parent to generate the node;</li>
 * <li>n.PATH-COST: the cost, traditionally denoted by g(n), of the path from the initial state
 * to the node, as indicated by the parent pointers.</li>
 * </ul>
 */

/**
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 */
public class Node {

	// n.STATE: the state in the state space to which the node corresponds;
	private Object state;

	// n.PARENT: the node in the search tree that generated this node;
	private Node parent;

	// n.ACTION: the action that was applied to the parent to generate the node;
	private Action action;

	// n.PATH-COST: the cost, traditionally denoted by g(n), of the path from
	// the initial state to the node, as indicated by the parent pointers.
	private double pathCost;

	public Node(Object state) {
		this.state = state;
		this.pathCost = 0.0;
	}

	public Node(Object state, Node parent, Action action, double stepCost) {
		this(state);
		this.parent = parent;
		this.action = action;
		this.pathCost = parent.pathCost + stepCost;
	}

	public Object getState() {
		return state;
	}

	public Node getParent() {
		return parent;
	}

	public Action getAction() {
		return action;
	}

	public double getPathCost() {
		return pathCost;
	}

	public boolean isRootNode() {
		return parent == null;
	}

	public List<Node> getPathFromRoot() {
		List<Node> path = new ArrayList<Node>();
		Node current = this;
		while (!current.isRootNode()) {
			path.add(0, current);
			current = current.getParent();
		}
		// ensure the root node is added
		path.add(0, current);
		return path;
	}

	@Override
	public String toString() {
		return "[parent=" + parent + ", action=" + action + ", state="
				+ getState() + ", pathCost=" + pathCost + "]";
	}
}