package aima.extra.probability.bayes;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import aima.extra.probability.ProbabilityNumber;
import aima.extra.probability.RandomVariable;

/**
 * A probability distribution is a function that assigns probabilities to events
 * (sets of possible worlds). This probability distribution supports only a
 * finite number of random variables, although the domains of the variables may
 * be finite or infinite.
 * 
 * @see <a href="http://en.wikipedia.org/wiki/Probability_distribution"
 *      >Probability Distribution</a>
 * 
 * @author Ciaran O'Reilly
 * @author Nagaraj Poti
 */
public interface ProbabilityDistribution {

	/**
	 * @return a consistent ordered list of the random variables this
	 *         probability distribution is for.
	 */
	List<RandomVariable> getVariables();

	/**
	 * @param var
	 *            the random variable to be checked.
	 * 
	 * @return true if this distribution is for the passed in random variable,
	 *         false otherwise.
	 */
	boolean contains(RandomVariable var);

	/**
	 * Get the probability value for the provided (possibly compound)
	 * proposition for the random variables comprising the distribution.
	 * 
	 * @param eventProposition
	 *            is specified by a functional interface that takes an map of
	 *            random variables to objects that represent values
	 *            corresponding to random variables as input. When constructing
	 *            the proposition, the order of random variables does not
	 *            matter, but the size of the event must equal the random
	 *            variables specified by the distribution. Skipping random
	 *            variables in the proposition would result in undefined
	 *            behaviour.<br>
	 *            <br>
	 *            The function interface evaluates input values with the
	 *            provided proposition, and returns true / false.
	 * 
	 * @return the probability value for the possible world(s) (more than one
	 *         world may satisfy the proposition constraints depending on the
	 *         actual proposition specified) that satisfy the constraints of the
	 *         proposition for the random variables comprising the distribution.
	 */
	ProbabilityNumber getValue(Predicate<Map<RandomVariable, Object>> eventProposition);
}