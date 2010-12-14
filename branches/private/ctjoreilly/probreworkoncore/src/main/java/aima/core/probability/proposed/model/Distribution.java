package aima.core.probability.proposed.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import aima.core.probability.proposed.model.domain.BooleanDomain;
import aima.core.probability.proposed.model.domain.FiniteDiscreteDomain;
import aima.core.probability.proposed.model.proposition.AssignmentProposition;
import aima.core.probability.proposed.model.proposition.RandomVariableProposition;
import aima.core.probability.proposed.model.proposition.TermProposition;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 487.
 * 
 * A vector of numbers, where we assume a predefined ordering on the domain(s)
 * of the Term Proposition(s) used to create the distribution.
 * 
 * This class is used to represent both Probability and Joint Probability
 * distributions for finite domains.
 * 
 * @author oreilly
 */
public class Distribution {
	private double[] distribution = null;
	private List<Map<Object, Integer>> domainIndexes = new ArrayList<Map<Object, Integer>>();
	//
	private String toString = null;
	private double sum = -1;

	public Distribution(double[] values, TermProposition... props) {
		if (null == values) {
			throw new IllegalArgumentException(
					"Distribution values must be specified");
		}
		if (null == props) {
			throw new IllegalArgumentException(
					"Term propositions must be specified.");
		}
		// initially 1, as this will represent constant assignments 
		// e.g. Dice1 = 1.
		int expectedSizeOfDistribution = 1;
		for (TermProposition t : props) {
			for (RandomVariable rvt : t.getScope()) {
				if (!(rvt.getDomain() instanceof FiniteDiscreteDomain)) {
					throw new IllegalArgumentException(
							"Cannot have an infinite domain for a variable in a Distribution:"
									+ rvt);
				}
			}

			// Create ordered domains for each proposition that is not a
			// singular assignment
			if (t instanceof RandomVariableProposition) {
				RandomVariableProposition rvp = (RandomVariableProposition) t;
				FiniteDiscreteDomain d = (FiniteDiscreteDomain) rvp.getScope()
						.iterator().next().getDomain();
				expectedSizeOfDistribution *= d.size();
				addToDomainIndexes(d);
			} else if (t instanceof AssignmentProposition) {
				AssignmentProposition ap = (AssignmentProposition) t;
				// Note: We skip Assignment propositions whose scope.size = 1 as
				// this is taken as a fixed value and we don't iterate over it
				// to create a distribution.
				if (ap.getScope().size() > 1) {
					// A Scope > 1 indicates a derived value based on 2 or more
					// Variables from the Domain. In this case we treat it as a
					// Boolean domain, indicating whether it held or not. For
					// e.g. the variable Doubles is a derived value based on
					// Dice1 + Dice2.
					expectedSizeOfDistribution *= 2;
					addToDomainIndexes(new BooleanDomain());
				}
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

	//
	// PRIVATE METHODS
	//
	private void addToDomainIndexes(FiniteDiscreteDomain d) {
		Map<Object, Integer> index = new LinkedHashMap<Object, Integer>();
		int idx = 1; // Start at 1 as easier to calculate index offsets this way
		for (Object v : d.getPossibleValues()) {
			index.put(v, new Integer(idx));
			idx++;
		}
		domainIndexes.add(index);
	}
}