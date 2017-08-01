package aima.extra.probability.bayes;

import java.util.Map;
import aima.extra.probability.ProbabilityNumber;
import aima.extra.probability.RandomVariable;

/**
 * ProbabilityMass is the probability distribution for discrete random
 * variables. Only a finite number of random variables is supported.
 * 
 * @author Ciaran O'Reilly
 * @author Nagaraj Poti
 */
public interface ProbabilityMass extends ProbabilityDistribution {

	/**
	 * Set the probability value for the provided array of event values for the
	 * random variables comprising the distribution.
	 * 
	 * @param value
	 *            to be assigned.
	 * @param eventValues
	 *            the values for the random variables comprising the
	 *            distribution. (ordering and size of eventValues must be as
	 *            specified in the distribution and values must be valid in
	 *            their respective domains). Skipping or changing order of
	 *            values will lead to undefined behaviour.
	 * 
	 * @return a new ProbabilityMass object with the updated probability value.
	 */
	ProbabilityMass setValue(ProbabilityNumber value, Object... eventValues);

	/**
	 * Provide a mapping of random variables to their domain values and set the
	 * probability value for such an event.
	 * 
	 * @param value
	 *            to be assigned.
	 * @param event
	 *            is a mapping of random variables to corresponding domain
	 *            values. Order of random variables does not matter, but the
	 *            size of the event must equal the random variables specified by
	 *            the distribution.
	 * 
	 * @return a new ProbabilityMass object with the updated probability value.
	 */
	ProbabilityMass setValue(ProbabilityNumber value, Map<RandomVariable, Object> event);

	/**
	 * Get the probability value for the provided array of event values for the
	 * random variables comprising the distribution.
	 * 
	 * @param eventValues
	 *            the values for the random variables comprising the
	 *            distribution. (ordering and size of eventValues must be as
	 *            specified in the distribution and values must be valid in
	 *            their respective domains). Skipping or changing order of
	 *            values will lead to undefined behaviour.
	 * 
	 * @return the probability value for the possible world associated with the
	 *         value assignments for the random variables comprising the
	 *         distribution.
	 */
	ProbabilityNumber getValue(Object... eventValues);

	/**
	 * Provide a mapping of random variables to their domain values and get the
	 * probability value for such an event.
	 * 
	 * @param event
	 *            is a mapping of random variables to corresponding domain
	 *            values. Order of random variables does not matter, but the
	 *            size of the event must equal the random variables specified by
	 *            the distribution.
	 * 
	 * @return the probability value for the possible world associated with the
	 *         value assignments for the random variables comprising the
	 *         distribution.
	 */
	ProbabilityNumber getValue(Map<RandomVariable, Object> event);
}