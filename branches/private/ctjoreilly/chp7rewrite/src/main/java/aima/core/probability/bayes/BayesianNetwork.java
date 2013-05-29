package aima.core.probability.bayes;

import java.util.List;

import aima.core.probability.RandomVariable;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 510.<br>
 * <br>
 * Bayesian Networks are used to represent the dependencies among Random
 * Variables. They can represent essentially any full joint probability
 * distribution and in many cases can do so very concisely. A Bayesian network
 * is a directed graph in which each node is annotated with quantitative
 * probability information. The full specification is as follows:<br>
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
 * A network with both discrete and continuous variables is called a <b>hybrid
 * Bayesian network</b>.<br>
 * <br>
 * <b>Note(1)</b>: "Bayesian Network" is the most common name used, but there
 * are many synonyms, including "belief network", "probabilistic network",
 * "causal network", and "knowledge map".
 * 
 * @author Ciaran O'Reilly
 */
public interface BayesianNetwork {
	/**
	 * @return a list of the Random Variables, in topological order, contained
	 *         within the network.
	 */
	List<RandomVariable> getVariablesInTopologicalOrder();

	/**
	 * 
	 * @param rv
	 *            the RandomVariable whose corresponding Node is to be
	 *            retrieved.
	 * @return the Node associated with the random variable in this Bayesian
	 *         Network.
	 */
	Node getNode(RandomVariable rv);
}
