package aima.core.probability.util;

import java.util.Map;

import aima.core.probability.CategoricalDistribution;
import aima.core.probability.RandomVariable;
import aima.core.probability.bayes.Node;
import aima.core.probability.domain.FiniteDomain;
import aima.core.probability.proposition.ConjunctiveProposition;
import aima.core.probability.proposition.Proposition;
import aima.core.util.Randomizer;
import aima.core.util.Util;
import aima.core.util.math.MixedRadixNumber;

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

	/**
	 * Calculate the index into a vector representing the enumeration of the
	 * value assignments for the variables X and their corresponding assignment
	 * in x. For example the Random Variables:<br>
	 * Q::{true, false}, R::{'A', 'B','C'}, and T::{true, false}, would be
	 * enumerated in a Vector as follows:
	 * 
	 * <pre>
	 * Index  Q      R  T
	 * -----  -      -  -
	 * 00:    true,  A, true
	 * 01:    true,  A, false
	 * 02:    true,  B, true
	 * 03:    true,  B, false
	 * 04:    true,  C, true
	 * 05:    true,  C, false
	 * 06:    false, A, true
	 * 07:    false, A, false
	 * 08:    false, B, true
	 * 09:    false, B, false
	 * 10:    false, C, true
	 * 11:    false, C, false
	 * </pre>
	 * 
	 * if x = {Q=true, R='C', T=false} the index returned would be 5.
	 * 
	 * @param X
	 *            a list of the Random Variables that would comprise the vector.
	 * @param x
	 *            an assignment for the Random Variables in X.
	 * @return an index into a vector that would represent the enumeration of
	 *         the values for X.
	 */
	public static int indexOf(RandomVariable[] X, Map<RandomVariable, Object> x) {
		if (0 == X.length) {
			return ((FiniteDomain) X[0].getDomain()).getOffset(x.get(X[0]));
		}
		// X.length > 1 then calculate using a mixed radix number
		//
		// Note: Create radices in reverse order so that the enumeration
		// through the distributions is of the following
		// order using a MixedRadixNumber, e.g. for two Booleans:
		// X Y
		// true true
		// true false
		// false true
		// false false
		// which corresponds with how displayed in book.
		int[] radixValues = new int[X.length];
		int[] radices = new int[X.length];
		int j = X.length - 1;
		for (int i = 0; i < X.length; i++) {
			FiniteDomain fd = (FiniteDomain) X[i].getDomain();
			radixValues[j] = fd.getOffset(x.get(X[i]));
			radices[j] = fd.size();
			j--;
		}

		return new MixedRadixNumber(radixValues, radices).intValue();
	}

	/**
	 * Calculate the indexes for X[i] into a vector representing the enumeration
	 * of the value assignments for the variables X and their corresponding
	 * assignment in x. For example the Random Variables:<br>
	 * Q::{true, false}, R::{'A', 'B','C'}, and T::{true, false}, would be
	 * enumerated in a Vector as follows:
	 * 
	 * <pre>
	 * Index  Q      R  T
	 * -----  -      -  -
	 * 00:    true,  A, true
	 * 01:    true,  A, false
	 * 02:    true,  B, true
	 * 03:    true,  B, false
	 * 04:    true,  C, true
	 * 05:    true,  C, false
	 * 06:    false, A, true
	 * 07:    false, A, false
	 * 08:    false, B, true
	 * 09:    false, B, false
	 * 10:    false, C, true
	 * 11:    false, C, false
	 * </pre>
	 * 
	 * if X[i] = R and x = {..., R='C', ...} then the indexes returned would be
	 * [4, 5, 10, 11].
	 * 
	 * @param X
	 *            a list of the Random Variables that would comprise the vector.
	 * @param idx
	 *            the index into X for the Random Variable whose assignment we
	 *            wish to retrieve its indexes for.
	 * @param x
	 *            an assignment for the Random Variables in X.
	 * @return the indexes into a vector that would represent the enumeration of
	 *         the values for X[i] in x.
	 */
	public static int[] indexesOfValue(RandomVariable[] X, int idx,
			Map<RandomVariable, Object> x) {
		int csize = ProbUtil.expectedSizeOfCategoricalDistribution(X);

		FiniteDomain fd = (FiniteDomain) X[idx].getDomain();
		int vdoffset = fd.getOffset(x.get(X[idx]));
		int vdosize = fd.size();
		int[] indexes = new int[csize / vdosize];

		int blocksize = csize;
		for (int i = 0; i < X.length; i++) {
			blocksize = blocksize / X[i].getDomain().size();
			if (i == idx) {
				break;
			}
		}

		for (int i = 0; i < indexes.length; i += blocksize) {
			int offset = ((i / blocksize) * vdosize * blocksize)
					+ (blocksize * vdoffset);
			for (int b = 0; b < blocksize; b++) {
				indexes[i + b] = offset + b;
			}
		}

		return indexes;
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
