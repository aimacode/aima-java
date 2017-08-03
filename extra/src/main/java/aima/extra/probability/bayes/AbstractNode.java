package aima.extra.probability.bayes;

import java.util.ArrayList;
import java.util.List;
import aima.extra.probability.RandomVariable;

public class AbstractNode implements Node {

	// Internal fields

	private RandomVariable nodeVariable;

	private List<Node> parents;

	private List<Node> children;

	public AbstractNode(RandomVariable var) {
		this(var, null);
	}

	public AbstractNode(RandomVariable var, List<Node> parents) {
		nodeVariable = var;
		parents.stream().forEach(e -> ((AbstractNode)e).addChild(this));
		this.parents = new ArrayList<Node>(parents);
	}

	@Override
	public RandomVariable getNodeVariable() {
		return nodeVariable;
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
		return null;
	}

	protected void addChild(Node childNode) {
	}

}
