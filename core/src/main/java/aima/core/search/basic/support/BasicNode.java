package aima.core.search.basic.support;

import java.util.StringJoiner;

import aima.core.search.api.Node;

/**
 * Basic implementation of the Node interface.
 *
 * @author Ciaran O'Reilly
 */
public class BasicNode<A, S> implements Node<A, S> {
	private S state;
	private Node<A, S> parent;
	private A action;
	private double pathCost;

	public static <A, S> int depth(Node<A, S> n) {
		int level = 0;
		while ((n = n.parent()) != null) {
			level++;
		}
		return level;
	}

	@Override
	public S state() {
		return state;
	}

	@Override
	public Node<A, S> parent() {
		return parent;
	}

	@Override
	public A action() {
		return action;
	}

	@Override
	public double pathCost() {
		return pathCost;
	}

	/**
	 * @return json like representation of the node with its parents, recursively till root node
	 */
	@Override
	public String toString() {
		StringJoiner sj = new StringJoiner(", ", "{", "}");
		sj.add("parent: " + (parent == null ? "null" : parent.toString()));
		sj.add("state: '" + state.toString() + "'");
		sj.add("fromAction: " + (action == null ? "null" : "'" + action.toString() + "'"));
		sj.add("withPathCost: "+pathCost);
		return sj.toString();
	}

	//
	// Package Protected
	BasicNode(S state, double pathCost) {
		this(state, null, null, pathCost);
	}

	public BasicNode(S state, Node<A, S> parent, A action, double pathCost) {
		this.state = state;
		this.parent = parent;
		this.action = action;
		this.pathCost = pathCost;
	}
}
