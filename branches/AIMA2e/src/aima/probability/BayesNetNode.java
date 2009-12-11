/*
 * Created on Jan 31, 2005
 *
 */
package aima.probability;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * @author Ravi Mohan
 * 
 */

public class BayesNetNode {
	private String variable;

	List<BayesNetNode> parents, children;

	ProbabilityDistribution distribution;

	public BayesNetNode(String variable) {
		this.variable = variable;
		parents = new ArrayList<BayesNetNode>();
		children = new ArrayList<BayesNetNode>();
		distribution = new ProbabilityDistribution(variable);
	}

	private void addParent(BayesNetNode node) {
		if (!(parents.contains(node))) {
			parents.add(node);
		}
	}

	private void addChild(BayesNetNode node) {
		if (!(children.contains(node))) {
			children.add(node);
		}
	}

	public void influencedBy(BayesNetNode parent1) {
		addParent(parent1);
		parent1.addChild(this);
		distribution = new ProbabilityDistribution(parent1.getVariable());
	}

	public void influencedBy(BayesNetNode parent1, BayesNetNode parent2) {
		influencedBy(parent1);
		influencedBy(parent2);
		distribution = new ProbabilityDistribution(parent1.getVariable(),
				parent2.getVariable());
	}

	public void setProbability(boolean b, double d) {
		distribution.set(b, d);
		if (isRoot()) {
			distribution.set(!b, 1.0 - d);
		}

	}

	private boolean isRoot() {
		return (parents.size() == 0);
	}

	public void setProbability(boolean b, boolean c, double d) {
		distribution.set(b, c, d);

	}

	public String getVariable() {
		return variable;
	}

	public List<BayesNetNode> getChildren() {
		return children;
	}

	public List<BayesNetNode> getParents() {
		return parents;
	}

	@Override
	public String toString() {
		return variable;
	}

	public double probabilityOf(Hashtable conditions) {
		return distribution.probabilityOf(conditions);
	}

	public Boolean isTrueFor(double probability,
			Hashtable<String, Boolean> modelBuiltUpSoFar) {
		Hashtable<String, Boolean> conditions = new Hashtable<String, Boolean>();
		if (isRoot()) {
			conditions.put(getVariable(), Boolean.TRUE);
		} else {
			for (int i = 0; i < parents.size(); i++) {
				BayesNetNode parent = parents.get(i);
				conditions.put(parent.getVariable(), modelBuiltUpSoFar
						.get(parent.getVariable()));
			}
		}
		double trueProbability = probabilityOf(conditions);
		if (probability <= trueProbability) {
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}
	}

	@Override
	public boolean equals(Object o) {

		if (this == o) {
			return true;
		}
		if ((o == null) || (this.getClass() != o.getClass())) {
			return false;
		}
		BayesNetNode another = (BayesNetNode) o;
		return variable.equals(another.variable);
	}

}