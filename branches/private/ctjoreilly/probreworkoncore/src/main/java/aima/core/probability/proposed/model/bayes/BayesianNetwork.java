package aima.core.probability.proposed.model.bayes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import aima.core.probability.proposed.model.RandomVariable;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 510.<br>
 * <br>
 * Bayesian Networks are used to represent the dependencies among Random
 * Variables. They can represent essentially any full joint probability
 * distribution and in many cases can do so very concisely. A Bayesian network
 * is a directed graph in which each node is annotated with quantitative
 * information. The full specification is as follows:<br>
 * <br>
 * 1. Each node corresponds to a random variable, which may be discrete or
 * continuous.<br>
 * <br>
 * 2. A set of directed links or arrows connects pairs of nodes. If there is an
 * arrow from node X to node Y, X is said to be a parent of Y. The graph has no
 * directed cycles (and hence is a directed acyclic graph, or <b>DAG</b>.<br>
 * <br>
 * 3. Each node X<sub>i</sub> has a conditional probability distribution
 * P(X<sub>i</sub> | Parents(X<sub>i</sub>)) that quantifies the effect of the
 * parents on the node.<br>
 * <br>
 * The topology of the network - the set of nodes and links - specifies the
 * conditional independence relationships that hold in the domain.<br>
 * <br>
 * <b>Note(1)</b>: "Bayesian Network" is the most common name used, but there are many
 * synonyms, including "belief network", "probabilistic network",
 * "causal network", and "knowledge map".
 * 
 * @author Ciaran O'Reilly
 * @author Ravi Mohan
 */
public class BayesianNetwork {

	private Set<Node> rootNodes = new LinkedHashSet<Node>();
	private List<RandomVariable> variables = new ArrayList<RandomVariable>();
	private Map<RandomVariable, Node> varToNodeMap = new HashMap<RandomVariable, Node>();

	public BayesianNetwork(Node... rootNodes) {
		if (null == rootNodes) {
			throw new IllegalArgumentException(
					"Root Nodes need to be specified.");
		}
		for (Node n : rootNodes) {
			this.rootNodes.add(n);
		}
		if (this.rootNodes.size() != rootNodes.length) {
			throw new IllegalArgumentException(
					"Duplicate Root Nodes Passed in.");
		}
		// Ensure is a DAG
		checkIsDAGAndCollectVariablesInTopologicalOrder();
		variables = Collections.unmodifiableList(variables);
	}

	/**
	 * @return a list of the Random Variables, in topological order, contained
	 *         within the network.
	 */
	public List<RandomVariable> getVariablesInTopologicalOrder() {
		return variables;
	}

	/**
	 * 
	 * @param rv
	 *            the RandomVariable whose corresponding Node is to be
	 *            retrieved.
	 * @return the Node associated with the random variable in this Bayesian
	 *         Network.
	 */
	public Node getNode(RandomVariable rv) {
		return varToNodeMap.get(rv);
	}

	//
	// PRIVATE METHODS
	//
	private void checkIsDAGAndCollectVariablesInTopologicalOrder() {

		// Topological sort based on logic described at:
		// http://en.wikipedia.org/wiki/Topoligical_sorting
		Set<Node> seenAlready = new HashSet<Node>();
		Map<Node, List<Node>> incomingEdges = new HashMap<Node, List<Node>>();
		Set<Node> s = new LinkedHashSet<Node>();
		for (Node n : this.rootNodes) {
			walkNode(n, seenAlready, incomingEdges, s);
		}
		while (!s.isEmpty()) {
			Node n = s.iterator().next();
			s.remove(n);
			variables.add(n.getRandomVariable());
			varToNodeMap.put(n.getRandomVariable(), n);
			for (Node m : n.getChildren()) {
				List<Node> edges = incomingEdges.get(m);
				edges.remove(n);
				if (edges.isEmpty()) {
					s.add(m);
				}
			}
		}

		for (List<Node> edges : incomingEdges.values()) {
			if (!edges.isEmpty()) {
				throw new IllegalArgumentException(
						"Network contains at least one cycle in it, must be a DAG.");
			}
		}
	}

	private void walkNode(Node n, Set<Node> seenAlready,
			Map<Node, List<Node>> incomingEdges, Set<Node> rootNodes) {
		if (!seenAlready.contains(n)) {
			seenAlready.add(n);
			// Check if has no incoming edges
			if (n.isRoot()) {
				rootNodes.add(n);
			}
			incomingEdges.put(n, new ArrayList<Node>(n.getParents()));
			for (Node c : n.getChildren()) {
				walkNode(c, seenAlready, incomingEdges, rootNodes);
			}
		}
	}
}
