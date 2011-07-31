package aima.core.probability.bayes;

/**
 * A node over a Random Variable that has a finite countable domain.
 * 
 * @author Ciaran O'Reilly
 * 
 */
public interface FiniteNode extends DiscreteNode {

	/**
	 * 
	 * @return the Conditional Probability Table detailing the finite set of
	 *         probabilities for this Node.
	 */
	ConditionalProbabilityTable getCPT();
}
