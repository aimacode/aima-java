package aima.extra.probability.bayes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import aima.extra.probability.RandomVariable;

/**
 * Base implementation of the Node interface. AbstractNode is immutable.
 * 
 * @author Ciaran O'Reilly
 * @author Ravi Mohan
 * @author Nagaraj Poti
 */
public abstract class AbstractNode implements Node {

	// Internal fields

	/**
	 * Random variable represented by the node.
	 */
	protected RandomVariable nodeVariable;

	/**
	 * Parent nodes of this node.
	 */
	protected List<Node> parents;

	/**
	 * Child nodes of this nde.
	 */
	protected List<Node> children;

	/**
	 * Markov blanket of a node consists of this node's parent nodes, child
	 * nodes and other parent nodes of the child nodes.
	 */
	protected List<Node> markovBlanket;

	// Constructors

	/**
	 * @param var
	 *            is the random variable represented by this node.
	 */
	public AbstractNode(RandomVariable var) {
		this(var, null);
	}

	/**
	 * @param var
	 *            is the random variable represented by this node.
	 * @param parents
	 *            of this node.
	 */
	public AbstractNode(RandomVariable var, List<Node> parents) {
		Objects.requireNonNull(var, "Node must be associated with a random variable.");
		this.nodeVariable = var;
		if (null != parents) {
			parents.stream().forEach(parentNode -> ((AbstractNode) parentNode).addChild(this));
			this.parents = new ArrayList<Node>(parents);
		} else {
			this.parents = new ArrayList<Node>();
		}
		this.children = new ArrayList<Node>();
		generateMarkovBlanket();
	}

	// Public methods

	// START-Node

	@Override
	public RandomVariable getNodeVariable() {
		return this.nodeVariable;
	}

	@Override
	public List<Node> getParents() {
		return Collections.unmodifiableList(this.parents);
	}

	@Override
	public List<Node> getChildren() {
		return Collections.unmodifiableList(this.children);
	}

	@Override
	public List<Node> getMarkovBlanket() {
		return Collections.unmodifiableList(this.markovBlanket);
	}

	// END-Node

	@Override
	public int hashCode() {
		return this.nodeVariable.hashCode();
	}

	/**
	 * Two nodes are equal if they represent the same randomvariable and the
	 * parents and children randomvariables are equal as well.
	 */
	@Override
	public boolean equals(Object o) {
		if (null == o) {
			return false;
		} else if (o == this) {
			return true;
		} else if (!(o instanceof Node)) {
			return false;
		}
		Node other = (Node) o;
		boolean thisNodeCheck = this.nodeVariable.equals(other.getNodeVariable());
		Set<RandomVariable> thisParentVariables = this.parents.stream().map(node -> node.getNodeVariable())
				.collect(Collectors.toSet());
		Set<RandomVariable> otherParentVariables = other.getParents().stream().map(node -> other.getNodeVariable())
				.collect(Collectors.toSet());
		boolean parentNodesCheck = thisParentVariables.equals(otherParentVariables);
		Set<RandomVariable> thisChildVariables = this.children.stream().map(node -> node.getNodeVariable())
				.collect(Collectors.toSet());
		Set<RandomVariable> otherChildVariables = other.getChildren().stream().map(node -> other.getNodeVariable())
				.collect(Collectors.toSet());
		boolean childNodesCheck = thisChildVariables.equals(otherChildVariables);
		return thisNodeCheck && parentNodesCheck && childNodesCheck;
	}

	@Override
	public String toString() {
		return this.nodeVariable.getName();
	}

	// Protected methods
	
	protected void addChild(Node childNode) {
		this.children.add(childNode);
	}
	
	// Private methods

	/**
	 * Get the markov blanket of this node.
	 */
	private void generateMarkovBlanket() {
		Set<Node> mb = new LinkedHashSet<Node>();
		// Given this node's parents
		mb.addAll(this.parents);
		// children
		mb.addAll(this.children);
		// and children's other parents
		this.children.stream().forEach(childNode -> mb.addAll(childNode.getParents()));
		this.markovBlanket = new ArrayList<Node>(mb);
	}
}
