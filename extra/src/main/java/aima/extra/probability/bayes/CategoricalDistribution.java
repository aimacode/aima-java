package aima.extra.probability.bayes;

import java.util.List;
import java.util.Map;
import aima.extra.probability.ProbabilityNumber;
import aima.extra.probability.RandomVariable;

/**
 * A probability distribution for discrete random variables with a finite set of
 * values. <br>
 * <br>
 * <b>Note:</b> This definition corresponds to that given in AIMA for a
 * Probability Distribution.
 * 
 * @author Ciaran O'Reilly
 * @author Nagaraj Poti
 */
public interface CategoricalDistribution extends ProbabilityMass {

	/**
	 * <b>Note:</b> An immutable list is returned by this method and thus its
	 * values cannot be changed. Instead use setValue() as this method is
	 * intended to be for read only purposes.
	 * 
	 * @return the array of values used to represent the
	 *         CategoricalDistribution.
	 * 
	 * @see CategoricalDistribution#setValue(int, ProbabilityNumber)
	 */
	List<ProbabilityNumber> getValues();

	/**
	 * Retrieve the index into the CategoricalDistribution for the provided
	 * array of values for the random variables comprising the distribution.
	 * 
	 * @param eventValues
	 *            the values for the random variables comprising the
	 *            distribution. (ordering and size of eventValues must be as
	 *            specified in the distribution and values must be valid in
	 *            their respective domains). Skipping or changing order of
	 *            values will lead to undefined behaviour.
	 * 
	 * @return the index within the distribution for the values specified.
	 */
	int getIndex(Object... eventValues);

	/**
	 * Retrieve the index into the CategoricalDistribution for the provided map
	 * of values for the random variables comprising the distribution.
	 * 
	 * @param event
	 *            a mapping of random variables comprising the distribution to
	 *            particular domain values. Order of random variables does not
	 *            matter, but the size of the event must equal the random
	 *            variables specified by the distribution.
	 * 
	 * @return the index within the distribution for the event specified.
	 */
	int getIndex(Map<RandomVariable, Object> event);

	/**
	 * Normalize the values comprising this distribution.
	 * 
	 * @return this instance with its values normalized.
	 */
	CategoricalDistribution normalize();

	/**
	 * Get the marginal distribution consisting of all the random variables that
	 * are NOT a part of the supplied list (vars). The returned distribution has
	 * the values updated with the summed out random variables.
	 * 
	 * @param varsToMarginalize
	 *            the random variables to marginalize/sum out. Ordering of
	 *            random variables does not matter.
	 * 
	 * @return a new distribution containing any remaining random variables not
	 *         summed out and a new set of values updated with the summed out
	 *         values.
	 */
	CategoricalDistribution marginalize(RandomVariable... varsToMarginalize);
}