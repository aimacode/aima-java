package aima.extra.probability.bayes;

import java.util.List;
import aima.extra.probability.RandomVariable;
import aima.extra.probability.bayes.ConditionalProbabilityDistribution;

/**
 * A node is annotated with quantitative probability information. Each node
 * corresponds to a random variable, which may be discrete or continuous. If
 * there is an arrow from node X to node Y in a Bayesian Network, X is said to
 * be a parent of Y and Y is a child of X. Each node X<sub>i</sub> has a
 * conditional probability distribution P(X<sub>i</sub> |
 * Parents(X<sub>i</sub>)) that quantifies the effect of the parents on the
 * node. <br>
 * 
 * @author Ciaran O'Reilly
 * @author Nagaraj Poti
 */
public interface Node {

	/**
	 * @return the random variable represented by this Node.
	 */
	RandomVariable getNodeVariable();

	/**
	 * @return true if this Node has no parents.
	 */
	default boolean isRoot() {
		 return (0 == getParents().size());
	}

	/**
	 * @return the parent Nodes for this Node.
	 */
	List<Node> getParents();

	/**
	 * @return the child Nodes for this Node.
	 */
	List<Node> getChildren();

	/**
	 * Get this Node's Markov Blanket:<br>
	 * 'A node is conditionally independent of all other nodes in the network,
	 * given its parents, children, and children's parents - that is, given its
	 * <b>MARKOV BLANKET</b>.
	 * 
	 * @return this Node's Markov Blanket.
	 */
	List<Node> getMarkovBlanket();

	/**
	 * @return the Conditional Probability Distribution associated with this
	 *         Node.
	 */
	ConditionalProbabilityDistribution getCPD();
}
