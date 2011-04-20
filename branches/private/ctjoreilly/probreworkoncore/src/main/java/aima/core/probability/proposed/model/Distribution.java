package aima.core.probability.proposed.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import aima.core.probability.proposed.model.domain.FiniteDomain;
import aima.core.probability.proposed.model.proposition.AssignmentProposition;
import aima.core.util.SetOps;
import aima.core.util.math.MixedRadixNumber;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 487.<br>
 * <br>
 * A vector of numbers, where we assume a predefined ordering on the domain(s)
 * of the Random Variables used to create the distribution.<br>
 * <br>
 * This class is used to represent both Probability and Joint Probability
 * distributions for finite domains.
 * 
 * @author Ciaran O'Reilly
 */
public class Distribution {
	private double[] distribution = null;
	//
	private Map<RandomVariable, RVInfo> randomVarInfo = new LinkedHashMap<RandomVariable, RVInfo>();
	private int[] radixs = null;
	//
	private String toString = null;
	private double sum = -1;

	/**
	 * Interface to be implemented by an object/algorithm that wishes to iterate
	 * over the distribution.
	 * 
	 * @see Distribution#iterateDistribution(Iterator)
	 */
	public interface Iterator {
		/**
		 * Called for each possible world represented by the Distribution.
		 * 
		 * @param possibleWorld
		 *            a possible world, &omega;, of variable/value pairs.
		 * @param probability
		 *            the probability associated with &omega;
		 */
		void iterate(Map<RandomVariable, Object> possibleWorld,
				double probability);

		/**
		 * Can be called after iteration.
		 * 
		 * @return some value relevant to having iterated over all possible
		 *         world, for e.g. the sum of possible worlds matching a
		 *         particular criteria.
		 */
		Object getPostIterateValue();
	}

	/**
	 * Calculated the expected size of a Distribution for the provided random
	 * variables.
	 * 
	 * @param vars
	 *            null, 0 or more random variables that are to be used to
	 *            construct a Distribution.
	 * @return the size (i.e. getValues().length) that the Distribution will
	 *         need to be in order to represent the specified random variables.
	 * 
	 * @see Distribution#getValues()
	 */
	public static int expectedSizeOfDistribution(RandomVariable... vars) {
		// initially 1, as this will represent constant assignments
		// e.g. Dice1 = 1.
		int expectedSizeOfDistribution = 1;
		if (null != vars) {
			for (RandomVariable rv : vars) {
				// Create ordered domains for each variable
				if (!(rv.getDomain() instanceof FiniteDomain)) {
					throw new IllegalArgumentException(
							"Cannot have an infinite domain for a variable in a Distribution:"
									+ rv);
				}
				FiniteDomain d = (FiniteDomain) rv.getDomain();
				expectedSizeOfDistribution *= d.size();
			}
		}

		return expectedSizeOfDistribution;
	}

	public Distribution(Collection<RandomVariable> vars) {
		this(vars.toArray(new RandomVariable[vars.size()]));
	}

	public Distribution(RandomVariable... vars) {
		this(new double[expectedSizeOfDistribution(vars)], vars);
	}

	public Distribution(double[] values, RandomVariable... vars) {
		if (null == values) {
			throw new IllegalArgumentException(
					"Distribution values must be specified");
		}
		if (values.length != expectedSizeOfDistribution(vars)) {
			throw new IllegalArgumentException("Distribution of length "
					+ values.length + " is not the correct size, should be "
					+ expectedSizeOfDistribution(vars)
					+ " in order to represent all possible combinations.");
		}
		if (null != vars) {
			for (RandomVariable rv : vars) {
				// Track index information relevant to each variable.
				randomVarInfo.put(rv, new RVInfo(rv));
			}
		}

		distribution = new double[values.length];
		System.arraycopy(values, 0, distribution, 0, values.length);

		radixs = createRadixs(randomVarInfo);
	}

	public boolean contains(RandomVariable rv) {
		return randomVarInfo.keySet().contains(rv);
	}

	public Set<RandomVariable> getRepresentation() {
		return randomVarInfo.keySet();
	}

	/**
	 * <b>Note:</b> Do not modify the double[] returned by this method directly.
	 * Instead use setValue() to ensure values and related values are updated
	 * correctly.
	 * 
	 * @return the internal double[] used to represent this Distribution.
	 * 
	 * @see Distribution#setValue(int, double)
	 */
	public double[] getValues() {
		return distribution;
	}

	/**
	 * Retrieve the index into the Distribution for the provided set of values
	 * for the random variables comprising the Distribution.
	 * 
	 * @param values
	 *            an ordered set of values for the random variables comprising
	 *            the Distribution (<b>Node:</b> the order must match the order
	 *            of the random variables)
	 * @return the index with the Distribution for the values specified.
	 * 
	 * @see Distribution#getValues()
	 */
	public int getIndex(Object... values) {
		if (values.length != randomVarInfo.size()) {
			throw new IllegalArgumentException(
					"Values passed in is not the same size as variables making up distribution.");
		}
		int[] radixValues = new int[values.length];
		int i = 0;
		for (RVInfo rvInfo : randomVarInfo.values()) {
			radixValues[rvInfo.getRadixIdx()] = rvInfo
					.getIdxForDomain(values[i]);
			i++;
		}

		MixedRadixNumber mrn = new MixedRadixNumber(radixValues, radixs);
		return mrn.intValue();
	}

	/**
	 * Get the value for the provided set of AssignmentPropositions for the
	 * random variables comprising the Distribution (size of each must equal and
	 * their random variables must match).
	 * 
	 * @param values
	 *            the assignment propositions for the random variables
	 *            comprising the Distribution
	 * @return the value for the possible world associated with the assignments
	 *         for the random variables comprising the Distribution.
	 */
	public double getValueFor(AssignmentProposition... values) {
		if (values.length != randomVarInfo.size()) {
			throw new IllegalArgumentException(
					"Values passed in is not the same size as variables making up distribution.");
		}
		int[] radixValues = new int[values.length];
		for (AssignmentProposition ap : values) {
			RVInfo rvInfo = randomVarInfo.get(ap.getTermVariable());
			if (null == rvInfo) {
				throw new IllegalArgumentException(
						"Values passed for a variable that is not part of this distribution:"
								+ ap.getTermVariable());
			}
			radixValues[rvInfo.getRadixIdx()] = rvInfo.getIdxForDomain(ap
					.getValue());
		}
		MixedRadixNumber mrn = new MixedRadixNumber(radixValues, radixs);
		return distribution[mrn.intValue()];
	}

	/**
	 * Set the value at a specified index within the distribution.
	 * 
	 * @param idx
	 * @param value
	 */
	public void setValue(int idx, double value) {
		distribution[idx] = value;
		reinitLazyValues();
	}

	/**
	 * 
	 * @return the summation of all of the elements within the Distribution.
	 */
	public double getSum() {
		if (-1 == sum) {
			sum = 0;
			for (int i = 0; i < distribution.length; i++) {
				sum += distribution[i];
			}
		}
		return sum;
	}

	/**
	 * Sum out the provided variables from this Distribution creating a new
	 * Distribution of the remaining variables with their values updated with
	 * the summed out random variables.<br>
	 * <br>
	 * see: AIMA3e page 527.<br>
	 * <br>
	 * 
	 * @param vars
	 *            the random variables to sum out.
	 * @return a new Distribution containing any remaining random variables not
	 *         summed out and a new set of values updated with the summed out
	 *         values.
	 */
	public Distribution sumOut(RandomVariable... vars) {
		Set<RandomVariable> soutVars = new LinkedHashSet<RandomVariable>(
				this.randomVarInfo.keySet());
		for (RandomVariable rv : vars) {
			soutVars.remove(rv);
		}
		final Distribution summedOut = new Distribution(soutVars);
		if (1 == summedOut.getValues().length) {
			summedOut.getValues()[0] = getSum();
		} else {
			// Otherwise need to iterate through this distribution
			// to calculate the summed out distribution.
			final Object[] termValues = new Object[summedOut.randomVarInfo
					.size()];
			Distribution.Iterator di = new Distribution.Iterator() {
				public void iterate(Map<RandomVariable, Object> possibleWorld,
						double probability) {

					int i = 0;
					for (RandomVariable rv : summedOut.randomVarInfo.keySet()) {
						termValues[i] = possibleWorld.get(rv);
						i++;
					}
					summedOut.getValues()[summedOut.getIndex(termValues)] += probability;
				}

				public Object getPostIterateValue() {
					return null; // N/A
				}
			};
			iterateDistribution(di);
		}

		return summedOut;
	}

	/**
	 * Divide the dividend (this) distribution by the divisor to create a new
	 * distribution representing the quotient. The variables comprising the
	 * divisor distribution must be a subset of the dividend. However, ordering
	 * of variables does not matter as the quotient contains the same ordered
	 * variables as the dividend and the internal logic handles iterating
	 * through the two distributions correctly, irrespective of the order of
	 * their variables.
	 * 
	 * @param divisor
	 * @return a new Distribution representing the quotient of the dividend
	 *         (this) divided by the divisor.
	 * @throws IllegalArgumentException
	 *             if the variables of the divisor distribution are not a subset
	 *             of the dividend.
	 */
	public Distribution divideBy(final Distribution divisor) {
		if (!randomVarInfo.keySet().containsAll(divisor.randomVarInfo.keySet())) {
			throw new IllegalArgumentException(
					"Divisor must be a subset of the dividend.");
		}

		final Distribution quotient = new Distribution(randomVarInfo.keySet());

		if (1 == divisor.getValues().length) {
			double d = divisor.getValues()[0];
			for (int i = 0; i < quotient.getValues().length; i++) {
				if (0 == d) {
					quotient.getValues()[i] = 0;
				} else {
					quotient.getValues()[i] = getValues()[i] / d;
				}
			}
		} else {
			Set<RandomVariable> dividendDivisorDiff = SetOps
					.difference(this.randomVarInfo.keySet(),
							divisor.randomVarInfo.keySet());
			Map<RandomVariable, RVInfo> tdiff = null;
			MixedRadixNumber tdMRN = null;
			if (dividendDivisorDiff.size() > 0) {
				tdiff = new LinkedHashMap<RandomVariable, RVInfo>();
				for (RandomVariable rv : dividendDivisorDiff) {
					tdiff.put(rv, new RVInfo(rv));
				}
				tdMRN = new MixedRadixNumber(0, createRadixs(tdiff));
			}
			final Map<RandomVariable, RVInfo> diff = tdiff;
			final MixedRadixNumber dMRN = tdMRN;
			final int[] qRVs = new int[quotient.radixs.length];
			final MixedRadixNumber qMRN = new MixedRadixNumber(0,
					quotient.radixs);
			Distribution.Iterator divisorIterator = new Distribution.Iterator() {
				public void iterate(Map<RandomVariable, Object> possibleWorld,
						double probability) {
					for (RandomVariable rv : possibleWorld.keySet()) {
						RVInfo rvInfo = quotient.randomVarInfo.get(rv);
						qRVs[rvInfo.getRadixIdx()] = rvInfo
								.getIdxForDomain(possibleWorld.get(rv));
					}
					if (null != diff) {
						// Start from 0 off the diff
						dMRN.setCurrentValueFor(new int[diff.size()]);
						do {
							for (RandomVariable rv : diff.keySet()) {
								RVInfo drvInfo = diff.get(rv);
								RVInfo qrvInfo = quotient.randomVarInfo.get(rv);
								qRVs[qrvInfo.getRadixIdx()] = dMRN
										.getCurrentNumeralValue(drvInfo
												.getRadixIdx());
							}
							updateQuotient(probability);
						} while (dMRN.increment());
					} else {
						updateQuotient(probability);
					}
				}

				public Object getPostIterateValue() {
					return null; // N/A
				}

				//
				//
				private void updateQuotient(double probability) {
					int offset = (int) qMRN.getCurrentValueFor(qRVs);
					if (0 == probability) {
						quotient.getValues()[offset] = 0;
					} else {
						quotient.getValues()[offset] += getValues()[offset]
								/ probability;
					}
				}
			};

			divisor.iterateDistribution(divisorIterator);
		}

		return quotient;
	}

	/**
	 * Pointwise multiplication of this Distribution by a given multiplier,
	 * creating a new Distribution representing the product of the two.<br>
	 * <br>
	 * see: AIMA3e Figure 14.10 page 527.<br>
	 * <br>
	 * Note: Default Distribution multiplication is not commutative. The reason
	 * is because the order of the variables comprising a Distribution dictate
	 * the ordering of the values for that distribution. For example (the
	 * General case of Baye's rule, AIMA3e pg. 496), using this API method:<br>
	 * <br>
	 * <b>P</b>(Y | X) = (<b>P</b>(X | Y)<b>P</b>(Y))/<b>P</b>(X)<br>
	 * <br>
	 * is NOT true, due to multiplication of distributions not being
	 * commutative. However:<br>
	 * <br>
	 * <b>P</b>(Y | X) = (<b>P</b>(Y)<b>P</b>(X | Y))/<b>P</b>(X)<br>
	 * <br>
	 * is true, using this API.<br>
	 * <br>
	 * The default order of the variable of the Distribution returned is the
	 * order of the variables as they are seen, as read from the left to right
	 * term, for e.g.: <br>
	 * <br>
	 * <b>P</b>(Y)<b>P</b>(X | Y)<br>
	 * <br>
	 * would give a Distribution of the following form: <br>
	 * Y, X<br>
	 * <br>
	 * i.e. an ordered union of the variables from the two distributions. <br>
	 * To override the default order of the product use pointwiseProductPOS().
	 * 
	 * @param multiplier
	 * 
	 * @return a new Distribution representing the pointwise product of this and
	 *         the passed in multiplier. The order of the variables comprising
	 *         the product distribution is the ordered union of the left term
	 *         (this) and the right term (multiplier).
	 * 
	 * @see Distribution#pointwiseProductPOS(Distribution, RandomVariable...)
	 */
	public Distribution pointwiseProduct(final Distribution multiplier) {
		Set<RandomVariable> prodVars = SetOps.union(randomVarInfo.keySet(),
				multiplier.randomVarInfo.keySet());
		return pointwiseProductPOS(multiplier, prodVars
				.toArray(new RandomVariable[prodVars.size()]));
	}

	/**
	 * Pointwise Multiplication - Product Order Specified (POS).<br>
	 * <br>
	 * see: AIMA3e Figure 14.10 page 527.<br>
	 * <br>
	 * Pointwise multiplication of this Distribution by a given multiplier,
	 * creating a new Distribution representing the product of the two. The
	 * order of the variables comprising the product will match those specified.
	 * For example (the General case of Baye's rule, AIMA3e pg. 496), using this
	 * API method:<br>
	 * <br>
	 * <b>P</b>(Y | X) = (<b>P</b>(X | Y)<b>P</b>(Y), [Y, X])/<b>P</b>(X)<br>
	 * <br>
	 * is true when the correct product order is specified.
	 * 
	 * @param multiplier
	 * @param prodVarOrder
	 *            the order the variables comprising the product are to be in.
	 * 
	 * @return a new Distribution representing the pointwise product of this and
	 *         the passed in multiplier. The order of the variables comprising
	 *         the product distribution is the order specified.
	 * 
	 * @see Distribution#pointwiseProduct(Distribution)
	 */
	public Distribution pointwiseProductPOS(final Distribution multiplier,
			RandomVariable... prodVarOrder) {
		final Distribution product = new Distribution(prodVarOrder);
		if (!product.randomVarInfo.keySet().equals(
				SetOps.union(randomVarInfo.keySet(), multiplier.randomVarInfo
						.keySet()))) {
			throw new IllegalArgumentException(
					"Specified list deatailing order of mulitplier is inconsistent.");
		}

		// If no variables in the product
		if (1 == product.getValues().length) {
			product.getValues()[0] = getValues()[0] * multiplier.getValues()[0];
		} else {
			// Otherwise need to iterate through the product
			// to calculate its values based on the terms.
			final Object[] term1Values = new Object[randomVarInfo.size()];
			final Object[] term2Values = new Object[multiplier.randomVarInfo
					.size()];
			Distribution.Iterator di = new Distribution.Iterator() {
				private int idx = 0;

				public void iterate(Map<RandomVariable, Object> possibleWorld,
						double probability) {
					int term1Idx = termIdx(term1Values, Distribution.this,
							possibleWorld);
					int term2Idx = termIdx(term2Values, multiplier,
							possibleWorld);

					product.getValues()[idx] = getValues()[term1Idx]
							* multiplier.getValues()[term2Idx];

					idx++;
				}

				public Object getPostIterateValue() {
					return null; // N/A
				}

				private int termIdx(Object[] termValues, Distribution d,
						Map<RandomVariable, Object> possibleWorld) {
					if (0 == termValues.length) {
						// The term has no variables so always position 0.
						return 0;
					}

					int i = 0;
					for (RandomVariable rv : d.randomVarInfo.keySet()) {
						termValues[i] = possibleWorld.get(rv);
						i++;
					}

					return d.getIndex(termValues);
				}
			};
			product.iterateDistribution(di);
		}

		return product;
	}

	/**
	 * Normalize the values comprising this distribution.
	 * 
	 * @return this
	 */
	public Distribution normalize() {
		double s = getSum();
		if (s != 0) {
			for (int i = 0; i < distribution.length; i++) {
				distribution[i] = distribution[i] / s;
			}
			reinitLazyValues();
		}
		return this;
	}

	/**
	 * Iterate over all the possible worlds describing this Distribution.
	 * 
	 * @param di
	 *            the Distribution Iterator to iterate.
	 */
	public void iterateDistribution(Iterator di) {
		Map<RandomVariable, Object> possibleWorld = new LinkedHashMap<RandomVariable, Object>();
		MixedRadixNumber mrn = new MixedRadixNumber(0, radixs);
		do {
			for (RVInfo rvInfo : randomVarInfo.values()) {
				possibleWorld.put(rvInfo.getVariable(), rvInfo
						.getDomainValueAt(mrn.getCurrentNumeralValue(rvInfo
								.getRadixIdx())));
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

	//
	// PRIVATE METHODS
	//
	private void reinitLazyValues() {
		sum = -1;
		toString = null;
	}

	private int[] createRadixs(Map<RandomVariable, RVInfo> mapRtoInfo) {
		int[] r = new int[mapRtoInfo.size()];
		// Read in reverse order so that the enumeration
		// through the distributions is of the following
		// order using a MixedRadixNumber, e.g. for two Booleans:
		// X Y
		// true true
		// true false
		// false true
		// false false
		// which corresponds with how displayed in book.
		int x = mapRtoInfo.size() - 1;
		for (RVInfo rvInfo : mapRtoInfo.values()) {
			r[x] = rvInfo.getDomainSize();
			rvInfo.setRadixIdx(x);
			x--;
		}
		return r;
	}

	private class RVInfo {
		private RandomVariable variable;
		private Map<Integer, Object> idxDomainMap = new HashMap<Integer, Object>();
		private Map<Object, Integer> domainIdxMap = new HashMap<Object, Integer>();
		private int radixIdx = 0;

		public RVInfo(RandomVariable rv) {
			variable = rv;
			int idx = 0;
			for (Object pv : ((FiniteDomain) variable.getDomain())
					.getPossibleValues()) {
				domainIdxMap.put(pv, idx);
				idxDomainMap.put(idx, pv);
				idx++;
			}
		}

		public RandomVariable getVariable() {
			return variable;
		}

		public int getDomainSize() {
			return domainIdxMap.size();
		}

		public int getIdxForDomain(Object value) {
			return domainIdxMap.get(value);
		}

		public Object getDomainValueAt(int idx) {
			return idxDomainMap.get(idx);
		}

		public void setRadixIdx(int idx) {
			radixIdx = idx;
		}

		public int getRadixIdx() {
			return radixIdx;
		}
	}
}