package aima.core.probability.proposed.util;

import java.util.Map;

import aima.core.probability.proposed.CategoricalDistribution;
import aima.core.probability.proposed.RandomVariable;
import aima.core.probability.proposed.bayes.Node;
import aima.core.probability.proposed.domain.FiniteDomain;
import aima.core.probability.proposed.proposition.ConjunctiveProposition;
import aima.core.probability.proposed.proposition.Proposition;
import aima.core.util.Randomizer;
import aima.core.util.Util;

public class ProbUtil {

	/**
	 * Check if name provided is valid for use as the name of a RandomVariable.
	 * 
	 * @param name
	 *            proposed for the RandomVariable.
	 * @throws IllegalArgumentException
	 *             if not a valid RandomVariable name.
	 */
	public static void checkValidRandomVariableName(String name)
			throws IllegalArgumentException {
		if (null == name || name.trim().length() == 0
				|| name.trim().length() != name.length() || name.contains(" ")) {
			throw new IllegalArgumentException(
					"Name of RandomVariable must be specified and contain no leading, trailing or embedded spaces.");
		}
		if (name.substring(0, 1).toLowerCase().equals(name.substring(0, 1))) {
			throw new IllegalArgumentException(
					"Name must start with a leading upper case letter.");
		}
	}

	/**
	 * Calculated the expected size of a ProbabilityTable for the provided
	 * random variables.
	 * 
	 * @param vars
	 *            null, 0 or more random variables that are to be used to
	 *            construct a CategoricalDistribution.
	 * @return the size (i.e. getValues().length) that the
	 *         CategoricalDistribution will need to be in order to represent the
	 *         specified random variables.
	 * 
	 * @see CategoricalDistribution#getValues()
	 */
	public static int expectedSizeOfProbabilityTable(RandomVariable... vars) {
		// initially 1, as this will represent constant assignments
		// e.g. Dice1 = 1.
		int expectedSizeOfDistribution = 1;
		if (null != vars) {
			for (RandomVariable rv : vars) {
				// Create ordered domains for each variable
				if (!(rv.getDomain() instanceof FiniteDomain)) {
					throw new IllegalArgumentException(
							"Cannot have an infinite domain for a variable in this calculation:"
									+ rv);
				}
				FiniteDomain d = (FiniteDomain) rv.getDomain();
				expectedSizeOfDistribution *= d.size();
			}
		}

		return expectedSizeOfDistribution;
	}

	/**
	 * Calculated the expected size of a CategoricalDistribution for the
	 * provided random variables.
	 * 
	 * @param vars
	 *            null, 0 or more random variables that are to be used to
	 *            construct a CategoricalDistribution.
	 * @return the size (i.e. getValues().length) that the
	 *         CategoricalDistribution will need to be in order to represent the
	 *         specified random variables.
	 * 
	 * @see CategoricalDistribution#getValues()
	 */
	public static int expectedSizeOfCategoricalDistribution(
			RandomVariable... vars) {
		// Equivalent calculation
		return expectedSizeOfProbabilityTable(vars);
	}

	/**
	 * Convenience method for ensure a conjunction of probabilistic
	 * propositions.
	 * 
	 * @param props
	 *            propositions to be combined into a ConjunctiveProposition if
	 *            necessary.
	 * @return a ConjunctivePropositions if more than 1 proposition in 'props',
	 *         otherwise props[0].
	 */
	public static Proposition constructConjunction(Proposition[] props) {
		return constructConjunction(props, 0);
	}

	/**
	 * 
	 * @param probabilityChoice
	 *            a probability choice for the sample
	 * @param Xi
	 *            a Random Variable with a finite domain from which a random
	 *            sample is to be chosen based on the probability choice.
	 * @param distribution
	 *            Xi's distribution.
	 * @return a Random Sample from Xi's domain.
	 */
	public static Object sample(double probabilityChoice, RandomVariable Xi,
			double[] distribution) {
		FiniteDomain fd = (FiniteDomain) Xi.getDomain();
		if (fd.size() != distribution.length) {
			throw new IllegalArgumentException("Size of domain Xi " + fd.size()
					+ " is not equal to the size of the distribution "
					+ distribution.length);
		}
		int i = 0;
		double total = distribution[0];
		while (probabilityChoice > total) {
			i++;
			total += distribution[i];
		}
		return fd.getValueAt(i);
	}

	/**
	 * Get a random sample from <b>P</b>(X<sub>i</sub> | parents(X<sub>i</sub>))
	 * 
	 * @param Xi
	 *            a Node from a Bayesian network for the Random Variable
	 *            X<sub>i</sub>.
	 * @param event
	 *            comprising assignments for parents(X<sub>i</sub>)
	 * @param r
	 *            a Randomizer for generating a probability choice for the
	 *            sample.
	 * @return a random sample from <b>P</b>(X<sub>i</sub> |
	 *         parents(X<sub>i</sub>))
	 */
	public static Object randomSample(Node Xi,
			Map<RandomVariable, Object> event, Randomizer r) {
		return Xi.getCPD().getSample(r.nextDouble(),
				getEventValuesForParents(Xi, event));
	}

	/**
	 * Get a random sample from <b>P</b>(X<sub>i</sub> | mb(X<sub>i</sub>)),
	 * where mb(X<sub>i</sub>) is the Markov Blanket of X<sub>i</sub>. The
	 * probability of a variable given its Markov blanket is proportional to the
	 * probability of the variable given its parents times the probability of
	 * each child given its respective parents (see equation 14.12 pg. 538
	 * AIMA3e):<br>
	 * <br>
	 * P(x'<sub>i</sub>|mb(Xi)) =
	 * &alpha;P(x'<sub>i</sub>|parents(X<sub>i</sub>)) *
	 * &prod;<sub>Y<sub>j</sub> &isin; Children(X<sub>i</sub>)</sub>
	 * P(y<sub>j</sub>|parents(Y<sub>j</sub>))
	 * 
	 * @param Xi
	 *            a Node from a Bayesian network for the Random Variable
	 *            X<sub>i</sub>.
	 * @param event
	 *            comprising assignments for the Markov Blanket X<sub>i</sub>.
	 * @param r
	 *            a Randomizer for generating a probability choice for the
	 *            sample.
	 * @return a random sample from <b>P</b>(X<sub>i</sub> | mb(X<sub>i</sub>))
	 */
	public static Object mbRandomSample(Node Xi,
			Map<RandomVariable, Object> event, Randomizer r) {
		return sample(r.nextDouble(), Xi.getRandomVariable(),
				mbDistribution(Xi, event));
	}

	/**
	 * Calculate the probability distribution for <b>P</b>(X<sub>i</sub> |
	 * mb(X<sub>i</sub>)), where mb(X<sub>i</sub>) is the Markov Blanket of
	 * X<sub>i</sub>. The probability of a variable given its Markov blanket is
	 * proportional to the probability of the variable given its parents times
	 * the probability of each child given its respective parents (see equation
	 * 14.12 pg. 538 AIMA3e):<br>
	 * <br>
	 * P(x'<sub>i</sub>|mb(Xi)) =
	 * &alpha;P(x'<sub>i</sub>|parents(X<sub>i</sub>)) *
	 * &prod;<sub>Y<sub>j</sub> &isin; Children(X<sub>i</sub>)</sub>
	 * P(y<sub>j</sub>|parents(Y<sub>j</sub>))
	 * 
	 * @param Xi
	 *            a Node from a Bayesian network for the Random Variable
	 *            X<sub>i</sub>.
	 * @param event
	 *            comprising assignments for the Markov Blanket X<sub>i</sub>.
	 * @param r
	 *            a Randomizer for generating a probability choice for the
	 *            sample.
	 * @return a random sample from <b>P</b>(X<sub>i</sub> | mb(X<sub>i</sub>))
	 */
	public static double[] mbDistribution(Node Xi,
			Map<RandomVariable, Object> event) {
		FiniteDomain fd = (FiniteDomain) Xi.getRandomVariable().getDomain();
		double[] X = new double[fd.size()];

		for (int i = 0; i < fd.size(); i++) {
			// P(x'<sub>i</sub>|mb(Xi)) =
			// &alpha;P(x'<sub>i</sub>|parents(X<sub>i</sub>)) *
			// &prod;<sub>Y<sub>j</sub> &isin; Children(X<sub>i</sub>)</sub>
			// P(y<sub>j</sub>|parents(Y<sub>j</sub>))
			double cprob = 1.0;
			for (Node Yj : Xi.getChildren()) {
				cprob *= Yj.getCPD().getValue(
						getEventValuesForXiGivenParents(Yj, event));
			}
			X[i] = Xi.getCPD()
					.getValue(
							getEventValuesForXiGivenParents(Xi,
									fd.getValueAt(i), event))
					* cprob;
		}

		return Util.normalize(X);
	}

	/**
	 * Get the parent values for the Random Variable Xi from the provided event.
	 * 
	 * @param Xi
	 *            a Node for the Random Variable Xi whose parent values are to
	 *            be extracted from the provided event in the correct order.
	 * @param event
	 *            an event containing assignments for Xi's parents.
	 * @return an ordered set of values for the parents of Xi from the provided
	 *         event.
	 */
	public static Object[] getEventValuesForParents(Node Xi,
			Map<RandomVariable, Object> event) {
		Object[] parentValues = new Object[Xi.getParents().size()];
		int i = 0;
		for (Node pn : Xi.getParents()) {
			parentValues[i] = event.get(pn.getRandomVariable());
			i++;
		}
		return parentValues;
	}

	/**
	 * Get the values for the Random Variable Xi's parents and its own value
	 * from the provided event.
	 * 
	 * @param Xi
	 *            a Node for the Random Variable Xi whose parent values and
	 *            value are to be extracted from the provided event in the
	 *            correct order.
	 * @param event
	 *            an event containing assignments for Xi's parents and its own
	 *            value.
	 * @return an ordered set of values for the parents of Xi and its value from
	 *         the provided event.
	 */
	public static Object[] getEventValuesForXiGivenParents(Node Xi,
			Map<RandomVariable, Object> event) {
		return getEventValuesForXiGivenParents(Xi,
				event.get(Xi.getRandomVariable()), event);
	}

	/**
	 * Get the values for the Random Variable Xi's parents and its own value
	 * from the provided event.
	 * 
	 * @param Xi
	 *            a Node for the Random Variable Xi whose parent values are to
	 *            be extracted from the provided event in the correct order.
	 * @param xDelta
	 *            the value for the Random Variable Xi to be assigned to the
	 *            values returned.
	 * @param event
	 *            an event containing assignments for Xi's parents and its own
	 *            value.
	 * @return an ordered set of values for the parents of Xi and its value from
	 *         the provided event.
	 */
	public static Object[] getEventValuesForXiGivenParents(Node Xi,
			Object xDelta, Map<RandomVariable, Object> event) {
		Object[] values = new Object[Xi.getParents().size() + 1];

		int idx = 0;
		for (Node pn : Xi.getParents()) {
			values[idx] = event.get(pn.getRandomVariable());
			idx++;
		}
		values[idx] = xDelta;
		return values;
	}

	//
	// PRIVATE METHODS
	//

	private static Proposition constructConjunction(Proposition[] props, int idx) {
		if ((idx + 1) == props.length) {
			return props[idx];
		}

		return new ConjunctiveProposition(props[idx], constructConjunction(
				props, idx + 1));
	}
}
