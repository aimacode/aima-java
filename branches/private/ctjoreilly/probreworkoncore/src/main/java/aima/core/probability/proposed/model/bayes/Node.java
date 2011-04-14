package aima.core.probability.proposed.model.bayes;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import aima.core.probability.proposed.model.RandomVariable;

/**
 * @author Ciaran O'Reilly
 * @author Ravi Mohan
 */

// TODO-AIMA3e pg. 518, 519, consider support for deterministic, noisy-OR
// and noisy-MAX Nodes. In particular deterministic nodes can be 
// auto-generated for derived propositions by iterating over their 
// scope variables in order to construct their CPT - this could be done dynamically
// by an instance of a model. The noisy nodes could use a dynamic CPT
// interface to handle dynamically constructing their values.
public abstract class Node {

	private RandomVariable variable = null;
	private Set<Node> parents = null;
	private Set<Node> children = null;

	public Node(RandomVariable var) {
		this(var, (Node[]) null);
	}

	public Node(RandomVariable var, Node... parents) {
		if (null == var) {
			throw new IllegalArgumentException(
					"Random Variable for Node must be specified.");
		}
		this.variable = var;
		this.parents = new LinkedHashSet<Node>();
		if (null != parents) {
			for (Node p : parents) {
				p.addChild(this);
				this.parents.add(p);
			}
		}
		this.parents = Collections.unmodifiableSet(this.parents);
		this.children = Collections.unmodifiableSet(new LinkedHashSet<Node>());
	}

	public RandomVariable getRandomVariable() {
		return variable;
	}

	public boolean isRoot() {
		return 0 == getParents().size();
	}

	public Set<Node> getParents() {
		return parents;
	}

	public Set<Node> getChildren() {
		return children;
	}

	public Set<Node> getMarkovBlanket() {
		// TOOD-pg. 517
		return null;
	}

	@Override
	public String toString() {
		return getRandomVariable().getName();
	}

	@Override
	public boolean equals(Object o) {
		if (null == o) {
			return false;
		}
		if (o == this) {
			return true;
		}

		if (o instanceof Node) {
			Node n = (Node) o;

			return getRandomVariable().equals(n.getRandomVariable());
		}

		return false;
	}

	@Override
	public int hashCode() {
		return variable.hashCode();
	}

	//
	// PROTECTED METHODS
	//
	protected void addChild(Node childNode) {
		children = new LinkedHashSet<Node>(children);

		children.add(childNode);

		children = Collections.unmodifiableSet(children);
	}
}
