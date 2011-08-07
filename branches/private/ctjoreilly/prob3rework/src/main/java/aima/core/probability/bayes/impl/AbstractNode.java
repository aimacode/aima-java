package aima.core.probability.bayes.impl;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import aima.core.probability.RandomVariable;
import aima.core.probability.bayes.ConditionalProbabilityDistribution;
import aima.core.probability.bayes.Node;

/**
 * Abstract base implementation of the Node interface.
 * 
 * @author Ciaran O'Reilly
 * @author Ravi Mohan
 */
public abstract class AbstractNode implements Node {
	private RandomVariable variable = null;
	private Set<Node> parents = null;
	private Set<Node> children = null;

	public AbstractNode(RandomVariable var) {
		this(var, (Node[]) null);
	}

	public AbstractNode(RandomVariable var, Node... parents) {
		if (null == var) {
			throw new IllegalArgumentException(
					"Random Variable for Node must be specified.");
		}
		this.variable = var;
		this.parents = new LinkedHashSet<Node>();
		if (null != parents) {
			for (Node p : parents) {
				((AbstractNode) p).addChild(this);
				this.parents.add(p);
			}
		}
		this.parents = Collections.unmodifiableSet(this.parents);
		this.children = Collections.unmodifiableSet(new LinkedHashSet<Node>());
	}

	//
	// START-Node
	@Override
	public RandomVariable getRandomVariable() {
		return variable;
	}

	@Override
	public boolean isRoot() {
		return 0 == getParents().size();
	}

	@Override
	public Set<Node> getParents() {
		return parents;
	}

	@Override
	public Set<Node> getChildren() {
		return children;
	}

	@Override
	public Set<Node> getMarkovBlanket() {
		LinkedHashSet<Node> mb = new LinkedHashSet<Node>();
		// Given its parents,
		mb.addAll(getParents());
		// children,
		mb.addAll(getChildren());
		// and children's parents
		for (Node cn : getChildren()) {
			mb.addAll(cn.getParents());
		}

		return mb;
	}

	public abstract ConditionalProbabilityDistribution getCPD();

	// END-Node
	//

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
