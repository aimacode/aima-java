package aima.extra.probability.bayes;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import aima.extra.probability.RandomVariable;

/**
 * Default implementation of the BayesianNetwork interface.
 * 
 * @author Ciaran O'Reilly
 * @author Ravi Mohan
 * @author Nagaraj Poti
 */
public class BayesNet implements BayesianNetwork {

	// Internal fields

	private Set<Node> rootNodes;

	private List<RandomVariable> randomVariables;

	private Map<RandomVariable, Node> varToNodeMap;

	// Constructor

	public BayesNet(List<Node> rootNodes) {
		Objects.requireNonNull(rootNodes, "Atleast one root node needs to be specified.");
		this.rootNodes = new HashSet<Node>(rootNodes);
		// Ensure is a DAG
		checkIsDAGAndCollectVariablesInTopologicalOrder();
		this.randomVariables = Collections.unmodifiableList(this.randomVariables);
	}

	// Public methods

	// START-BayesianNetwork

	@Override
	public List<RandomVariable> getVariablesInTopologicalOrder() {
		return this.randomVariables;
	}

	@Override
	public Node getNode(RandomVariable rv) {
		return null;
	}

	// END-BayesianNetwork

	// Private methods

	private void checkIsDAGAndCollectVariablesInTopologicalOrder() {
		Set<Node> globalSeenAlready = new HashSet<Node>();
		for (Node n : this.rootNodes) {
			if (globalSeenAlready.contains(n)) {
				throw new IllegalArgumentException("Root nodes specified have atleast one incoming edge.");
			}
			Set<Node> localSeenAlready = new HashSet<Node>();
			walkNode(n, globalSeenAlready, localSeenAlready);
		}
	}

	private void walkNode(Node n, Set<Node> globalSeenAlready, Set<Node> localSeenAlready) {
		globalSeenAlready.add(n);
		localSeenAlready.add(n);
		for (Node childNode : n.getChildren()) {
			if (localSeenAlready.contains(childNode)) {
				throw new IllegalArgumentException("Network contains at least one cycle in it, must be a DAG.");
			}
			if (!globalSeenAlready.contains(childNode)) {
				walkNode(childNode, globalSeenAlready, localSeenAlready);
			} else if (this.rootNodes.contains(childNode)) {
				throw new IllegalArgumentException("Root nodes specified have atleast one incoming edge.");
			}
		}
		this.randomVariables.add(n.getNodeVariable());
		this.varToNodeMap.put(n.getNodeVariable(), n);
	}
}