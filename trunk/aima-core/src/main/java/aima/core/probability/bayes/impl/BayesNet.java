package aima.core.probability.bayes.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import aima.core.probability.RandomVariable;
import aima.core.probability.bayes.BayesianNetwork;
import aima.core.probability.bayes.Node;

/**
 * Default implementation of the BayesianNetwork interface.
 * 
 * @author Ciaran O'Reilly
 * @author Ravi Mohan
 */
public class BayesNet implements BayesianNetwork {
	protected Set<Node> rootNodes = new LinkedHashSet<Node>();
	protected List<RandomVariable> variables = new ArrayList<RandomVariable>();
	protected Map<RandomVariable, Node> varToNodeMap = new HashMap<RandomVariable, Node>();

	public BayesNet(Node... rootNodes) {
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

	//
	// START-BayesianNetwork
	@Override
	public List<RandomVariable> getVariablesInTopologicalOrder() {
		return variables;
	}

	@Override
	public Node getNode(RandomVariable rv) {
		return varToNodeMap.get(rv);
	}

	// END-BayesianNetwork
	//

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
