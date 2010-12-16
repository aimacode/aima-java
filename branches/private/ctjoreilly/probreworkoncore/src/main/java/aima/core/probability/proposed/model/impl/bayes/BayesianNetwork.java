package aima.core.probability.proposed.model.impl.bayes;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 510.
 * 
 * Bayesian Networks are used to represent the dependencies among Random Variables.
 * They can represent essentially any full joint probability distribution and in many
 * cases can do so very concisely. A Bayesian network is a directed graph in which each
 * node is annotated with quantitative information. The full specification is as follows:
 * 
 * 1. Each node corresponds to a random variable, which may be discrete or continuous.
 * 
 * 2. A set of directed links or arrows connects pairs of nodes. If there is an arrow
 * from node X to node Y, X is said to be a parent of Y. The graph has no directed cycles
 * (and hence is a directed acyclic graph, or DAG.
 * 
 * 3. Each node Xi has a conditional probability distribution P(X, | Parent(Xi)) that
 * quantifies the effect of the parents on the node.
 * 
 * The topology of the network - the set of nodes and links - specifies the conditional
 * independence relationships that hold in the domain.
 * 
 * Note(1): "Bayesian Network" is the most common name used, but there are many synonyms,
 * including "belief network", "probabilistic network", "causal network", and "knowledge map". 
 * 
 * @author Ciaran O'Reilly
 * @author Ravi Mohan
 */
public class BayesianNetwork {

	private Set<Node> rootNodes = new LinkedHashSet<Node>(); 
	
	public BayesianNetwork(Node... rootNodes) {
		if (null == rootNodes) {
			throw new IllegalArgumentException("Root Nodes need to be specified.");
		}
		for (Node n : rootNodes) {
			this.rootNodes.add(n);
		}
		if (this.rootNodes.size() != rootNodes.length) {
			throw new IllegalArgumentException("Duplicate Root Nodes Passed in.");
		}
		// Ensure is a DAG
		HashSet<Node> seenAlready = new HashSet<Node>();
		for (Node n : this.rootNodes) {
			checkIsDAG(n, seenAlready);
		}
	}

	//
	// PRIVATE METHODS
	//
	private void checkIsDAG(Node n, Set<Node> seenAlready) {
		if (seenAlready.contains(n)) {
			throw new IllegalArgumentException("Network contains a cycle and is therefore not a DAG:"+n);
		}
		seenAlready.add(n);
		for (Node c : n.getChildren()) {
			checkIsDAG(c, seenAlready);
		}
	}
}
