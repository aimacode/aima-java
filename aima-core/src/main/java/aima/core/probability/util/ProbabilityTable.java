package aima.core.probability.util;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import aima.core.probability.CategoricalDistribution;
import aima.core.probability.Factor;
import aima.core.probability.RandomVariable;
import aima.core.probability.domain.FiniteDomain;
import aima.core.probability.proposition.AssignmentProposition;
import aima.core.util.SetOps;
import aima.core.util.math.MixedRadixNumber;

/**
 * A Utility Class for associating values with a set of finite Random Variables.
 * This is also the default implementation of the CategoricalDistribution and
 * Factor interfaces (as they are essentially dependent on the same underlying
 * data structures).
 * 
 * @author Ciaran O'Reilly
 */
public class ProbabilityTable implements CategoricalDistribution, Factor {
	private double[] values = null;
	//
	private Map<RandomVariable, RVInfo> randomVarInfo = new LinkedHashMap<RandomVariable, RVInfo>();
	private int[] radices = null;
	private MixedRadixNumber queryMRN = null;
	//
	private String toString = null;
	private double sum = -1;

	/**
	 * Interface to be implemented by an object/algorithm that wishes to iterate
	 * over the possible assignments for the random variables comprising this
	 * table.
	 * 
	 * @see ProbabilityTable#iterateOverTable(Iterator)
	 * @see ProbabilityTable#iterateOverTable(Iterator,
	 *      AssignmentProposition...)
	 */
	public interface Iterator {
		/**
		 * Called for each possible assignment for the Random Variables
		 * comprising this ProbabilityTable.
		 * 
		 * @param possibleAssignment
		 *            a possible assignment, &omega;, of variable/value pairs.
		 * @param probability
		 *            the probability associated with &omega;
		 */
		void iterate(Map<RandomVariable, Object> possibleAssignment,
				double probability);
	}

	public ProbabilityTable(Collection<RandomVariable> vars) {
		this(vars.toArray(new RandomVariable[vars.size()]));
	}

	public ProbabilityTable(RandomVariable... vars) {
		this(new double[ProbUtil.expectedSizeOfProbabilityTable(vars)], vars);
	}

	public ProbabilityTable(double[] vals, RandomVariable... vars) {
		if (null == vals) {
			throw new IllegalArgumentException("Values must be specified");
		}
		if (vals.length != ProbUtil.expectedSizeOfProbabilityTable(vars)) {
			throw new IllegalArgumentException("ProbabilityTable of length "
					+ vals.length + " is not the correct size, should be "
					+ ProbUtil.expectedSizeOfProbabilityTable(vars)
					+ " in order to represent all possible combinations.");
		}
		if (null != vars) {
			for (RandomVariable rv : vars) {
				// Track index information relevant to each variable.
				randomVarInfo.put(rv, new RVInfo(rv));
			}
		}

		values = new double[vals.length];
		System.arraycopy(vals, 0, values, 0, vals.length);

		radices = createRadixs(randomVarInfo);

		if (radices.length > 0) {
			queryMRN = new MixedRadixNumber(0, radices);
		}
	}

	public int size() {
		return values.length;
	}

	//
	// START-ProbabilityDistribution
	@Override
	public Set<RandomVariable> getFor() {
		return randomVarInfo.keySet();
	}

	@Override
	public boolean contains(RandomVariable rv) {
		return randomVarInfo.keySet().contains(rv);
	}

	@Override
	public double getValue(Object... assignments) {
		return values[getIndex(assignments)];
	}

	@Override
	public double getValue(AssignmentProposition... assignments) {
		if (assignments.length != randomVarInfo.size()) {
			throw new IllegalArgumentException(
					"Assignments passed in is not the same size as variables making up probability table.");
		}
		int[] radixValues = new int[assignments.length];
		for (AssignmentProposition ap : assignments) {
			RVInfo rvInfo = randomVarInfo.get(ap.getTermVariable());
			if (null == rvInfo) {
				throw new IllegalArgumentException(
						"Assignment passed for a variable that is not part of this probability table:"
								+ ap.getTermVariable());
			}
			radixValues[rvInfo.getRadixIdx()] = rvInfo.getIdxForDomain(ap
					.getValue());
		}
		return values[(int) queryMRN.getCurrentValueFor(radixValues)];
	}

	// END-ProbabilityDistribution
	//

	//
	// START-CategoricalDistribution
	public double[] getValues() {
		return values;
	}

	@Override
	public void setValue(int idx, double value) {
		values[idx] = value;
		reinitLazyValues();
	}

	@Override
	public double getSum() {
		if (-1 == sum) {
			sum = 0;
			for (int i = 0; i < values.length; i++) {
				sum += values[i];
			}
		}
		return sum;
	}

	@Override
	public ProbabilityTable normalize() {
		double s = getSum();
		if (s != 0 && s != 1.0) {
			for (int i = 0; i < values.length; i++) {
				values[i] = values[i] / s;
			}
			reinitLazyValues();
		}
		return this;
	}

	@Override
	public int getIndex(Object... assignments) {
		if (assignments.length != randomVarInfo.size()) {
			throw new IllegalArgumentException(
					"Assignments passed in is not the same size as variables making up the table.");
		}
		int[] radixValues = new int[assignments.length];
		int i = 0;
		for (RVInfo rvInfo : randomVarInfo.values()) {
			radixValues[rvInfo.getRadixIdx()] = rvInfo
					.getIdxForDomain(assignments[i]);
			i++;
		}

		return (int) queryMRN.getCurrentValueFor(radixValues);
	}

	@Override
	public CategoricalDistribution marginal(RandomVariable... vars) {
		return sumOut(vars);
	}

	@Override
	public CategoricalDistribution divideBy(CategoricalDistribution divisor) {
		return divideBy((ProbabilityTable) divisor);
	}

	@Override
	public CategoricalDistribution multiplyBy(CategoricalDistribution multiplier) {
		return pointwiseProduct((ProbabilityTable) multiplier);
	}

	@Override
	public CategoricalDistribution multiplyByPOS(
			CategoricalDistribution multiplier, RandomVariable... prodVarOrder) {
		return pointwiseProductPOS((ProbabilityTable) multiplier, prodVarOrder);
	}

	@Override
	public void iterateOver(CategoricalDistribution.Iterator cdi) {
		iterateOverTable(new CategoricalDistributionIteratorAdapter(cdi));
	}

	@Override
	public void iterateOver(CategoricalDistribution.Iterator cdi,
			AssignmentProposition... fixedValues) {
		iterateOverTable(new CategoricalDistributionIteratorAdapter(cdi),
				fixedValues);
	}

	// END-CategoricalDistribution
	//

	//
	// START-Factor
	@Override
	public Set<RandomVariable> getArgumentVariables() {
		return randomVarInfo.keySet();
	}

	@Override
	public ProbabilityTable sumOut(RandomVariable... vars) {
		Set<RandomVariable> soutVars = new LinkedHashSet<RandomVariable>(
				this.randomVarInfo.keySet());
		for (RandomVariable rv : vars) {
			soutVars.remove(rv);
		}
		final ProbabilityTable summedOut = new ProbabilityTable(soutVars);
		if (1 == summedOut.getValues().length) {
			summedOut.getValues()[0] = getSum();
		} else {
			// Otherwise need to iterate through this distribution
			// to calculate the summed out distribution.
			final Object[] termValues = new Object[summedOut.randomVarInfo
					.size()];
			ProbabilityTable.Iterator di = new ProbabilityTable.Iterator() {
				public void iterate(Map<RandomVariable, Object> possibleWorld,
						double probability) {

					int i = 0;
					for (RandomVariable rv : summedOut.randomVarInfo.keySet()) {
						termValues[i] = possibleWorld.get(rv);
						i++;
					}
					summedOut.getValues()[summedOut.getIndex(termValues)] += probability;
				}
			};
			iterateOverTable(di);
		}

		return summedOut;
	}

	@Override
	public Factor pointwiseProduct(Factor multiplier) {
		return pointwiseProduct((ProbabilityTable) multiplier);
	}

	@Override
	public Factor pointwiseProductPOS(Factor multiplier,
			RandomVariable... prodVarOrder) {
		return pointwiseProductPOS((ProbabilityTable) multiplier, prodVarOrder);
	}

	@Override
	public void iterateOver(Factor.Iterator fi) {
		iterateOverTable(new FactorIteratorAdapter(fi));
	}

	@Override
	public void iterateOver(Factor.Iterator fi,
			AssignmentProposition... fixedValues) {
		iterateOverTable(new FactorIteratorAdapter(fi), fixedValues);
	}

	// END-Factor
	//

	/**
	 * Iterate over all the possible value assignments for the Random Variables
	 * comprising this ProbabilityTable.
	 * 
	 * @param pti
	 *            the ProbabilityTable Iterator to iterate.
	 */
	public void iterateOverTable(Iterator pti) {
		Map<RandomVariable, Object> possibleWorld = new LinkedHashMap<RandomVariable, Object>();
		MixedRadixNumber mrn = new MixedRadixNumber(0, radices);
		do {
			for (RVInfo rvInfo : randomVarInfo.values()) {
				possibleWorld.put(rvInfo.getVariable(), rvInfo
						.getDomainValueAt(mrn.getCurrentNumeralValue(rvInfo
								.getRadixIdx())));
			}
			pti.iterate(possibleWorld, values[mrn.intValue()]);

		} while (mrn.increment());
	}

	/**
	 * Iterate over all possible values assignments for the Random Variables
	 * comprising this ProbabilityTable that are not in the fixed set of values.
	 * This allows you to iterate over a subset of possible combinations.
	 * 
	 * @param pti
	 *            the ProbabilityTable Iterator to iterate
	 * @param fixedValues
	 *            Fixed values for a subset of the Random Variables comprising
	 *            this Probability Table.
	 */
	public void iterateOverTable(Iterator pti,
			AssignmentProposition... fixedValues) {
		Map<RandomVariable, Object> possibleWorld = new LinkedHashMap<RandomVariable, Object>();
		MixedRadixNumber tableMRN = new MixedRadixNumber(0, radices);
		int[] tableRadixValues = new int[radices.length];

		// Assert that the Random Variables for the fixed values
		// are part of this probability table and assign
		// all the fixed values to the possible world.
		for (AssignmentProposition ap : fixedValues) {
			if (!randomVarInfo.containsKey(ap.getTermVariable())) {
				throw new IllegalArgumentException("Assignment proposition ["
						+ ap + "] does not belong to this probability table.");
			}
			possibleWorld.put(ap.getTermVariable(), ap.getValue());
			RVInfo fixedRVI = randomVarInfo.get(ap.getTermVariable());
			tableRadixValues[fixedRVI.getRadixIdx()] = fixedRVI
					.getIdxForDomain(ap.getValue());
		}
		// If have assignments for all the random variables
		// in this probability table
		if (fixedValues.length == randomVarInfo.size()) {
			// Then only 1 iteration call is required.
			pti.iterate(possibleWorld, getValue(fixedValues));
		} else {
			// Else iterate over the non-fixed values
			Set<RandomVariable> freeVariables = SetOps.difference(
					this.randomVarInfo.keySet(), possibleWorld.keySet());
			Map<RandomVariable, RVInfo> freeVarInfo = new LinkedHashMap<RandomVariable, RVInfo>();
			// Remove the fixed Variables
			for (RandomVariable fv : freeVariables) {
				freeVarInfo.put(fv, new RVInfo(fv));
			}
			int[] freeRadixValues = createRadixs(freeVarInfo);
			MixedRadixNumber freeMRN = new MixedRadixNumber(0, freeRadixValues);
			Object fval = null;
			// Iterate through all combinations of the free variables
			do {
				// Put the current assignments for the free variables
				// into the possible world and update
				// the current index in the table MRN
				for (RVInfo freeRVI : freeVarInfo.values()) {
					fval = freeRVI.getDomainValueAt(freeMRN
							.getCurrentNumeralValue(freeRVI.getRadixIdx()));
					possibleWorld.put(freeRVI.getVariable(), fval);

					tableRadixValues[randomVarInfo.get(freeRVI.getVariable())
							.getRadixIdx()] = freeRVI.getIdxForDomain(fval);
				}
				pti.iterate(possibleWorld, values[(int) tableMRN
						.getCurrentValueFor(tableRadixValues)]);

			} while (freeMRN.increment());
		}
	}

	public ProbabilityTable divideBy(ProbabilityTable divisor) {
		if (!randomVarInfo.keySet().containsAll(divisor.randomVarInfo.keySet())) {
			throw new IllegalArgumentException(
					"Divisor must be a subset of the dividend.");
		}

		final ProbabilityTable quotient = new ProbabilityTable(randomVarInfo
				.keySet());

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
			final int[] qRVs = new int[quotient.radices.length];
			final MixedRadixNumber qMRN = new MixedRadixNumber(0,
					quotient.radices);
			ProbabilityTable.Iterator divisorIterator = new ProbabilityTable.Iterator() {
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

			divisor.iterateOverTable(divisorIterator);
		}

		return quotient;
	}

	public ProbabilityTable pointwiseProduct(final ProbabilityTable multiplier) {
		Set<RandomVariable> prodVars = SetOps.union(randomVarInfo.keySet(),
				multiplier.randomVarInfo.keySet());
		return pointwiseProductPOS(multiplier, prodVars
				.toArray(new RandomVariable[prodVars.size()]));
	}

	public ProbabilityTable pointwiseProductPOS(
			final ProbabilityTable multiplier, RandomVariable... prodVarOrder) {
		final ProbabilityTable product = new ProbabilityTable(prodVarOrder);
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
			ProbabilityTable.Iterator di = new ProbabilityTable.Iterator() {
				private int idx = 0;

				public void iterate(Map<RandomVariable, Object> possibleWorld,
						double probability) {
					int term1Idx = termIdx(term1Values, ProbabilityTable.this,
							possibleWorld);
					int term2Idx = termIdx(term2Values, multiplier,
							possibleWorld);

					product.getValues()[idx] = getValues()[term1Idx]
							* multiplier.getValues()[term2Idx];

					idx++;
				}

				private int termIdx(Object[] termValues, ProbabilityTable d,
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
			product.iterateOverTable(di);
		}

		return product;
	}

	@Override
	public String toString() {
		if (null == toString) {
			StringBuilder sb = new StringBuilder();
			sb.append("<");
			for (int i = 0; i < values.length; i++) {
				if (i > 0) {
					sb.append(", ");
				}
				sb.append(values[i]);
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
		private FiniteDomain varDomain;
		private int radixIdx = 0;

		public RVInfo(RandomVariable rv) {
			variable = rv;
			varDomain = (FiniteDomain) variable.getDomain();
		}

		public RandomVariable getVariable() {
			return variable;
		}

		public int getDomainSize() {
			return varDomain.size();
		}

		public int getIdxForDomain(Object value) {
			return varDomain.getOffset(value);
		}

		public Object getDomainValueAt(int idx) {
			return varDomain.getValueAt(idx);
		}

		public void setRadixIdx(int idx) {
			radixIdx = idx;
		}

		public int getRadixIdx() {
			return radixIdx;
		}
	}

	private class CategoricalDistributionIteratorAdapter implements Iterator {
		private CategoricalDistribution.Iterator cdi = null;

		public CategoricalDistributionIteratorAdapter(
				CategoricalDistribution.Iterator cdi) {
			this.cdi = cdi;
		}

		public void iterate(Map<RandomVariable, Object> possibleAssignment,
				double probability) {
			cdi.iterate(possibleAssignment, probability);
		}
	}

	private class FactorIteratorAdapter implements Iterator {
		private Factor.Iterator fi = null;

		public FactorIteratorAdapter(Factor.Iterator fi) {
			this.fi = fi;
		}

		public void iterate(Map<RandomVariable, Object> possibleAssignment,
				double probability) {
			fi.iterate(possibleAssignment, probability);
		}
	}
}