package aima.extra.probability.bayes;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import aima.extra.probability.RandomVariable;

public class AbstractNode implements Node {

	// Internal fields

	private RandomVariable nodeVariable;

	private List<Node> parents;

	private List<Node> children;

	public AbstractNode(RandomVariable var) {
		this(var, null);
	}

	public AbstractNode(RandomVariable var, List<? extends Node> parents) {
		this.nodeVariable = var;
		parents.stream().forEach(e -> ((AbstractNode) e).addChild(this));
		this.parents = parents.stream().collect(Collectors.toCollection(ArrayList<Node>::new));
	}

	@Override
	public RandomVariable getNodeVariable() {
		return this.nodeVariable;
	}

	@Override
	public boolean isRoot() {
		boolean isRoot = (0 == parents.size());
		return isRoot;
	}

	@Override
	public List<Node> getParents() {
		return parents;
	}

	@Override
	public List<Node> getChildren() {
		return children;
	}

	@Override
	public List<Node> getMarkovBlanket() {
		return null;
	}

	@Override
	public ConditionalProbabilityDistribution getCPD() {
		// TODO Auto-generated method stub
		return null;
	}

	protected void addChild(Node childNode) {

	}

}
