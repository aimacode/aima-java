package aima.core.probability;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import aima.core.util.Util;

/**
 * @author Ravi Mohan
 * 
 */
public class BayesNet {
	private List<BayesNetNode> roots = new ArrayList<BayesNetNode>();

	private List<BayesNetNode> variableNodes;

	public BayesNet(BayesNetNode root) {
		roots.add(root);
	}

	public BayesNet(BayesNetNode root1, BayesNetNode root2) {
		this(root1);
		roots.add(root2);
	}

	public BayesNet(BayesNetNode root1, BayesNetNode root2, BayesNetNode root3) {
		this(root1, root2);
		roots.add(root3);
	}

	public BayesNet(List<BayesNetNode> rootNodes) {
		roots = rootNodes;
	}

	public List<String> getVariables() {
		variableNodes = getVariableNodes();
		List<String> variables = new ArrayList<String>();
		for (BayesNetNode variableNode : variableNodes) {
			variables.add(variableNode.getVariable());
		}
		return variables;
	}

	public double probabilityOf(String Y, Boolean value,
			Hashtable<String, Boolean> evidence) {
		BayesNetNode y = getNodeOf(Y);
		if (y == null) {
			throw new RuntimeException("Unable to find a node with variable "
					+ Y);
		} else {
			List<BayesNetNode> parentNodes = y.getParents();
			if (parentNodes.size() == 0) {// root nodes
				Hashtable<String, Boolean> YTable = new Hashtable<String, Boolean>();
				YTable.put(Y, value);

				double prob = y.probabilityOf(YTable);
				return prob;

			} else {// non rootnodes
				Hashtable<String, Boolean> parentValues = new Hashtable<String, Boolean>();
				for (BayesNetNode parent : parentNodes) {
					parentValues.put(parent.getVariable(), evidence.get(parent
							.getVariable()));
				}
				double prob = y.probabilityOf(parentValues);
				if (value.equals(Boolean.TRUE)) {
					return prob;
				} else {
					return (1.0 - prob);
				}

			}

		}
	}

	public Hashtable getPriorSample(Randomizer r) {
		Hashtable<String, Boolean> h = new Hashtable<String, Boolean>();
		List<BayesNetNode> variableNodes = getVariableNodes();
		for (BayesNetNode node : variableNodes) {
			h.put(node.getVariable(), node.isTrueFor(r.nextDouble(), h));
		}
		return h;
	}

	public Hashtable getPriorSample() {
		return getPriorSample(new JavaRandomizer());
	}

	public double[] rejectionSample(String X, Hashtable evidence,
			int numberOfSamples, Randomizer r) {
		double[] retval = new double[2];
		for (int i = 0; i < numberOfSamples; i++) {
			Hashtable sample = getPriorSample(r);
			if (consistent(sample, evidence)) {
				boolean queryValue = ((Boolean) sample.get(X)).booleanValue();
				if (queryValue) {
					retval[0] += 1;
				} else {
					retval[1] += 1;
				}
			}
		}
		return Util.normalize(retval);
	}

	public double[] likelihoodWeighting(String X,
			Hashtable<String, Boolean> evidence, int numberOfSamples,
			Randomizer r) {
		double[] retval = new double[2];
		for (int i = 0; i < numberOfSamples; i++) {
			Hashtable<String, Boolean> x = new Hashtable<String, Boolean>();
			double w = 1.0;
			List<BayesNetNode> variableNodes = getVariableNodes();
			for (BayesNetNode node : variableNodes) {
				if (evidence.get(node.getVariable()) != null) {
					w *= node.probabilityOf(x);
					x.put(node.getVariable(), evidence.get(node.getVariable()));
				} else {
					x
							.put(node.getVariable(), node.isTrueFor(r
									.nextDouble(), x));
				}
			}
			boolean queryValue = (x.get(X)).booleanValue();
			if (queryValue) {
				retval[0] += w;
			} else {
				retval[1] += w;
			}

		}
		return Util.normalize(retval);
	}

	public double[] mcmcAsk(String X, Hashtable<String, Boolean> evidence,
			int numberOfVariables, Randomizer r) {
		double[] retval = new double[2];
		List nonEvidenceVariables = nonEvidenceVariables(evidence, X);
		Hashtable<String, Boolean> event = createRandomEvent(
				nonEvidenceVariables, evidence, r);
		for (int j = 0; j < numberOfVariables; j++) {
			Iterator iter = nonEvidenceVariables.iterator();
			while (iter.hasNext()) {
				String variable = (String) iter.next();
				BayesNetNode node = getNodeOf(variable);
				List<BayesNetNode> markovBlanket = markovBlanket(node);
				Hashtable mb = createMBValues(markovBlanket, event);
				// event.put(node.getVariable(), node.isTrueFor(
				// r.getProbability(), mb));
				event.put(node.getVariable(), truthValue(rejectionSample(node
						.getVariable(), mb, 100, r), r));
				boolean queryValue = (event.get(X)).booleanValue();
				if (queryValue) {
					retval[0] += 1;
				} else {
					retval[1] += 1;
				}
			}
		}
		return Util.normalize(retval);
	}

	public double[] mcmcAsk(String X, Hashtable<String, Boolean> evidence,
			int numberOfVariables) {
		return mcmcAsk(X, evidence, numberOfVariables, new JavaRandomizer());
	}

	public double[] likelihoodWeighting(String X,
			Hashtable<String, Boolean> evidence, int numberOfSamples) {
		return likelihoodWeighting(X, evidence, numberOfSamples,
				new JavaRandomizer());
	}

	public double[] rejectionSample(String X,
			Hashtable<String, Boolean> evidence, int numberOfSamples) {
		return rejectionSample(X, evidence, numberOfSamples,
				new JavaRandomizer());
	}

	//
	// PRIVATE METHODS
	//

	private List<BayesNetNode> getVariableNodes() {
		// TODO dicey initalisation works fine but unclear . clarify
		if (variableNodes == null) {
			List<BayesNetNode> newVariableNodes = new ArrayList<BayesNetNode>();
			List<BayesNetNode> parents = roots;
			List<BayesNetNode> traversedParents = new ArrayList<BayesNetNode>();

			while (parents.size() != 0) {
				List<BayesNetNode> newParents = new ArrayList<BayesNetNode>();
				for (BayesNetNode parent : parents) {
					// if parent unseen till now
					if (!(traversedParents.contains(parent))) {
						newVariableNodes.add(parent);
						// add any unseen children to next generation of parents
						List<BayesNetNode> children = parent.getChildren();
						for (BayesNetNode child : children) {
							if (!newParents.contains(child)) {
								newParents.add(child);
							}
						}
						traversedParents.add(parent);
					}
				}

				parents = newParents;
			}
			variableNodes = newVariableNodes;
		}

		return variableNodes;
	}

	private BayesNetNode getNodeOf(String y) {
		List<BayesNetNode> variableNodes = getVariableNodes();
		for (BayesNetNode node : variableNodes) {
			if (node.getVariable().equals(y)) {
				return node;
			}
		}
		return null;
	}

	private boolean consistent(Hashtable sample, Hashtable evidence) {
		Iterator iter = evidence.keySet().iterator();
		while (iter.hasNext()) {
			String key = (String) iter.next();
			Boolean value = (Boolean) evidence.get(key);
			if (!(value.equals(sample.get(key)))) {
				return false;
			}
		}
		return true;
	}

	private Boolean truthValue(double[] ds, Randomizer r) {
		double value = r.nextDouble();
		if (value < ds[0]) {
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}

	}

	private Hashtable<String, Boolean> createRandomEvent(
			List nonEvidenceVariables, Hashtable<String, Boolean> evidence,
			Randomizer r) {
		Hashtable<String, Boolean> table = new Hashtable<String, Boolean>();
		List<String> variables = getVariables();
		for (String variable : variables) {

			if (nonEvidenceVariables.contains(variable)) {
				Boolean value = r.nextDouble() <= 0.5 ? Boolean.TRUE
						: Boolean.FALSE;
				table.put(variable, value);
			} else {
				table.put(variable, evidence.get(variable));
			}
		}
		return table;
	}

	private List nonEvidenceVariables(Hashtable<String, Boolean> evidence,
			String query) {
		List<String> nonEvidenceVariables = new ArrayList<String>();
		List<String> variables = getVariables();
		for (String variable : variables) {

			if (!(evidence.keySet().contains(variable))) {
				nonEvidenceVariables.add(variable);
			}
		}
		return nonEvidenceVariables;
	}

	private List<BayesNetNode> markovBlanket(BayesNetNode node) {
		return markovBlanket(node, new ArrayList<BayesNetNode>());
	}

	private List<BayesNetNode> markovBlanket(BayesNetNode node,
			List<BayesNetNode> soFar) {
		// parents
		List<BayesNetNode> parents = node.getParents();
		for (BayesNetNode parent : parents) {
			if (!soFar.contains(parent)) {
				soFar.add(parent);
			}
		}
		// children
		List<BayesNetNode> children = node.getChildren();
		for (BayesNetNode child : children) {
			if (!soFar.contains(child)) {
				soFar.add(child);
				List<BayesNetNode> childsParents = child.getParents();
				for (BayesNetNode childsParent : childsParents) {
					;
					if ((!soFar.contains(childsParent))
							&& (!(childsParent.equals(node)))) {
						soFar.add(childsParent);
					}
				}// childsParents
			}// end contains child

		}// end child

		return soFar;
	}

	private Hashtable createMBValues(List<BayesNetNode> markovBlanket,
			Hashtable<String, Boolean> event) {
		Hashtable<String, Boolean> table = new Hashtable<String, Boolean>();
		for (BayesNetNode node : markovBlanket) {
			table.put(node.getVariable(), event.get(node.getVariable()));
		}
		return table;
	}
}