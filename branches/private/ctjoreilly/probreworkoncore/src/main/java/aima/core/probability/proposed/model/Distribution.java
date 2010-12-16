package aima.core.probability.proposed.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import aima.core.probability.proposed.model.domain.FiniteDiscreteDomain;
import aima.core.util.math.MixedRadixNumber;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 487.
 * 
 * A vector of numbers, where we assume a predefined ordering on the domain(s)
 * of the Random Variables used to create the distribution.
 * 
 * This class is used to represent both Probability and Joint Probability
 * distributions for finite domains.
 * 
 * @author Ciaran O'Reilly
 */
public class Distribution {
	private double[] distribution = null;
	//
	private String toString = null;
	private double sum = -1;
	private List<RandomVariable> randomVars = new ArrayList<RandomVariable>();
	private List<List<Object>> domains = new ArrayList<List<Object>>();
	private int[] radixs = null;

	public interface Iterator {
		void iterate(Map<RandomVariable, Object> possibleWorld,
				double probability);

		Object getPostIterateValue();
	}

	public Distribution(double[] values, RandomVariable... vars) {
		if (null == values) {
			throw new IllegalArgumentException(
					"Distribution values must be specified");
		}
		// initially 1, as this will represent constant assignments
		// e.g. Dice1 = 1.
		int expectedSizeOfDistribution = 1;
		if (null != vars) {
			for (RandomVariable rv : vars) {
				// Create ordered domains for each variable
				if (!(rv.getDomain() instanceof FiniteDiscreteDomain)) {
					throw new IllegalArgumentException(
							"Cannot have an infinite domain for a variable in a Distribution:"
									+ rv);
				}
				FiniteDiscreteDomain d = (FiniteDiscreteDomain) rv.getDomain();
				expectedSizeOfDistribution *= d.size();
				randomVars.add(rv);
				domains.add(new ArrayList<Object>(d.getPossibleValues()));
			}
		}

		if (values.length != expectedSizeOfDistribution) {
			throw new IllegalArgumentException("Distribution of length "
					+ distribution.length
					+ " is not the correct size, should be "
					+ expectedSizeOfDistribution
					+ " in order to represent all possible combinations.");
		}

		distribution = new double[values.length];
		System.arraycopy(values, 0, distribution, 0, values.length);

		radixs = new int[domains.size()];
		// Read in reverse order so that the enumeration
		// through the distributions is of the following
		// order using a MixedRadixNumber, e.g. for two Booleans:
		// X     Y
		// true  true
		// true  false
		// false true
		// false false
		for (int i = domains.size() - 1; i >= 0; i--) {
			radixs[i] = domains.get(i).size();
		}
	}

	public double[] getValues() {
		return distribution;
	}

	public double getSum() {
		if (-1 == sum) {
			sum = 0;
			for (int i = 0; i < distribution.length; i++) {
				sum += distribution[i];
			}
		}
		return sum;
	}

	public void iterateDistribution(Iterator di) {
		Map<RandomVariable, Object> possibleWorld = new LinkedHashMap<RandomVariable, Object>();
		MixedRadixNumber mrn = new MixedRadixNumber(0, radixs);
		do {
			// Note: Have to index the MixedRadixNumber in reverse order
			// in order to comply with the fixed ordering of distributions
			// with respect to their variables.
			for (int i = 0, x = randomVars.size() - 1; i < randomVars.size(); i++, x--) {
				Object val = domains.get(i).get(mrn.getCurrentNumeralValue(x));
				possibleWorld.put(randomVars.get(i), val);
			}
			di.iterate(possibleWorld, distribution[mrn.intValue()]);

		} while (mrn.increment());
	}

	@Override
	public String toString() {
		if (null == toString) {
			StringBuilder sb = new StringBuilder();
			sb.append("<");
			for (int i = 0; i < distribution.length; i++) {
				if (i > 0) {
					sb.append(", ");
				}
				sb.append(distribution[i]);
			}
			sb.append(">");

			toString = sb.toString();
		}
		return toString;
	}
}