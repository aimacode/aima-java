package aima.extra.probability.bayes;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import aima.extra.probability.RandomVariable;

/**
 * A conditional probability distribution on a RandomVariable X<sub>i</sub>:
 * P(X<sub>i</sub> | Parents(X<sub>i</sub>)) that quantifies the effect of the
 * parents on X<sub>i</sub>. Conditional probability distributions consisting of
 * more than one query variable must be restructured into computations involving
 * single query variable distributions (e.g P(X,Y|Z) = P(X|Y,Z)*P(Y|Z)).
 * 
 * @author Ciaran O'Reilly
 * @author Nagaraj Poti
 */
public interface ConditionalProbabilityDistribution extends ProbabilityDistribution {

	/**
	 * @return the random variable this conditional probability distribution is
	 *         on (i.e. the query random variable).
	 */
	RandomVariable getOn();

	/**
	 * @return a consistent ordered list of the parent random variables for this
	 *         conditional distribution.
	 */
	List<RandomVariable> getParents();

	/**
	 * A conditioning case is just a possible combination of values for the
	 * parent nodes - a miniature possible world, if you like. The returned
	 * distribution must sum to 1, because the entries represent an exhaustive
	 * set of cases for the random variable.
	 * 
	 * @param parentWorld
	 *            is a mapping of parent random variables with their domain
	 *            values for the conditioning case. Order of random variables
	 *            does not matter, but the size of the parentWorld must equal
	 *            the parent variables specified by the distribution.
	 * 
	 * @return the probability distribution for the random variable the
	 *         conditional probability distribution is on.
	 */
	ProbabilityDistribution getConditioningCase(Map<RandomVariable, Object> parentWorld);

	/**
	 * A conditioning case is just a possible combination of values for the
	 * parent nodes - a miniature possible world, if you like. The returned
	 * distribution must sum to 1, because the entries represent an exhaustive
	 * set of cases for the random variable.
	 * 
	 * @param parentWorldProposition
	 *            is specified by a functional interface that takes an array of
	 *            objects that represent values corresponding to parent random
	 *            variables as input. When constructing the proposition, it is
	 *            MANDATORY to respect the SIZE and ORDER of parent random
	 *            variables as specified by the probability distribution.
	 *            Skipping or changing the order of random variables in the
	 *            proposition would result in undefined behaviour.<br>
	 *            <br>
	 *            The function interface evaluates input values with the
	 *            provided proposition, and returns true / false.
	 * 
	 * @return the probability distribution for the random variable the
	 *         conditional probability distribution is on.
	 */
	ProbabilityDistribution getConditioningCase(Predicate<List<Object>> parentWorldProposition);
}