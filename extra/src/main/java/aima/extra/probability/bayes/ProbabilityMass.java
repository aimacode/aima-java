package aima.extra.probability.bayes;

import java.util.List;
import java.util.Map;
import aima.extra.probability.ProbabilityNumber;
import aima.extra.probability.RandomVariable;
import aima.extra.probability.proposition.Proposition;

/**
 * ProbabilityMass is the probability distribution for discrete random
 * variables. Only a finite number of random variables is supported.
 * 
 * @author Ciaran O'Reilly
 * @author Nagaraj Poti
 */
public interface ProbabilityMass extends ProbabilityDistribution {

	/**
	 * Set the probability value for the provided list of event values for the
	 * random variables comprising the distribution.
	 * 
	 * @param eventValues
	 *            the values for the random variables comprising the
	 *            distribution. (ordering and size of eventValues must be as
	 *            specified in the distribution and values must be valid in
	 *            their respective domains). Skipping or changing order of
	 *            values will lead to undefined behaviour.
	 * @param value
	 *            to be assigned.
	 */
	void setValue(List<Object> eventValues, ProbabilityNumber value);

	/**
	 * Provide a mapping of random variables to their domain values and set the
	 * probability value for such an event.
	 * 
	 * @param event
	 *            is a mapping of random variables to corresponding domain
	 *            values. Order of random variables does not matter, but the
	 *            size of the event must equal the random variables specified by
	 *            the distribution.
	 * @param value
	 *            to be assigned.
	 */
	void setValue(Map<RandomVariable, Object> event, ProbabilityNumber value);

	/**
	 * Set the probability value for a possible world specified by an array of
	 * propositions. The propositions are connected with implicit AND operators.
	 * 
	 * @param assignmentPropositions[]
	 *            is an array of propositions, where each proposition associates
	 *            a random variable with a domain value, comprising all random
	 *            variables in this distribution. (ordering and size of
	 *            assignmentPropositions must conform to the random variables
	 *            specified in the distribution and values must be valid in
	 *            their respective domains).
	 * @param value
	 *            to be assigned.
	 */
	void setValue(ProbabilityNumber value, Proposition... assignmentPropositions);

	/**
	 * Get the probability value for the provided list of event values for the
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
	ProbabilityNumber getValue(List<Object> eventValues);

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

	/**
	 * Get the probability value for a possible world specified by an array of
	 * propositions. The propositions are connected with implicit AND operators.
	 * 
	 * @param assignmentPropositions[]
	 *            is an array of propositions, where each proposition associates
	 *            a random variable with a domain value, comprising all random
	 *            variables in this distribution. (ordering and size of
	 *            assignmentPropositions must conform to the random variables
	 *            specified in the distribution and values must be valid in
	 *            their respective domains).
	 * 
	 * @return the probability value for the possible world associated with the
	 *         assignments for the random variables comprising the distribution.
	 */
	ProbabilityNumber getValue(Proposition... assignmentPropositions);
}
