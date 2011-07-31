package aima.core.probability.bayes;

import java.util.Set;

import aima.core.probability.RandomVariable;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 511.<br>
 * <br>
 * A node is annotated with quantitative probability information. Each node
 * corresponds to a random variable, which may be discrete or continuous. If
 * there is an arrow from node X to node Y in a Bayesian Network, X is said to
 * be a parent of Y and Y is a child of X. Each node X<sub>i</sub> has a
 * conditional probability distribution P(X<sub>i</sub> |
 * Parents(X<sub>i</sub>)) that quantifies the effect of the parents on the
 * node. <br>
 * 
 * @author Ciaran O'Reilly
 */
public interface Node {

	/**
	 * 
	 * @return the Random Variable this Node is for/on.
	 */
	RandomVariable getRandomVariable();

	/**
	 * 
	 * @return true if this Node has no parents.
	 * 
	 * @see Node#getParents()
	 */
	boolean isRoot();

	/**
	 * 
	 * @return the parent Nodes for this Node.
	 */
	Set<Node> getParents();

	/**
	 * 
	 * @return the children Nodes for this Node.
	 */
	Set<Node> getChildren();

	/**
	 * Get this Node's Markov Blanket:<br>
	 * 'A node is conditionally independent of all other nodes in the network,
	 * given its parents, children, and children's parents - that is, given its
	 * <b>MARKOV BLANKET</b> (AIMA3e pg, 517).
	 * 
	 * @return this Node's Markov Blanket.
	 */
	Set<Node> getMarkovBlanket();

	/**
	 * 
	 * @return the Conditional Probability Distribution associated with this
	 *         Node.
	 */
	ConditionalProbabilityDistribution getCPD();
}
