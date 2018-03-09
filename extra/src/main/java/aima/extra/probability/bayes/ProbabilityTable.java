package aima.extra.probability.bayes;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import aima.extra.probability.ProbabilityNumber;
import aima.extra.probability.RandomVariable;
import aima.extra.probability.constructs.ProbabilityUtilities;
import aima.core.util.math.MixedRadixInterval;
import aima.extra.probability.ProbabilityComputation;
import aima.extra.util.ListOps;

/**
 * This class represents a probability table that is indexed by domain value
 * assignments to finite random variables. The values comprising the
 * ProbabilityTable correspond to the various assignments to all the random
 * variables constituting the distribution. This ProbabilityTable could
 * represent a joint distribution or a conditional probability distribution for
 * finite random variables, although more specific and legal operations for CPDs
 * are defined in {@link ConditionalProbabilityTable}.
 * 
 * @author Ciaran O'Reilly
 * @author Nagaraj Poti
 */
public class ProbabilityTable extends AbstractProbabilityTable
		implements Factor, Iterable<Map<RandomVariable, Object>> {

	// Internal fields

	/**
	 * Sum of all values.
	 */
	private ProbabilityNumber sumOfValues = null;

	// Constructor

	/**
	 * Constructor initializes ProbabilityTable with values.
	 * 
	 * @param vars
	 *            is an ordered array of all random variables.
	 * @param values
	 *            is an ordered array of probability values that form the
	 *            probability table.
	 * @param clazz
	 *            specifies the class type of the ProbabilityNumber
	 *            implementation to use.
	 * 
	 * @see MixedRadixInterval class to understand how indexes correspond to
	 *      assignments of values to random variables.
	 */
	public ProbabilityTable(RandomVariable[] vars, ProbabilityNumber[] values,
			Class<? extends ProbabilityNumber> clazz) {
		super(vars, values, clazz);
	}

	/**
	 * Constructor initializes ProbabilityTable with values.
	 * 
	 * @param vars
	 *            is an ordered list of all random variables.
	 * @param values
	 *            is an ordered list of probability values that form the
	 *            probability table.
	 * @param clazz
	 *            specifies the class type of the ProbabilityNumber
	 *            implementation to use.
	 * 
	 * @see MixedRadixInterval class to understand how indexes correspond to
	 *      assignments of values to random variables.
	 */
	public ProbabilityTable(List<RandomVariable> vars, List<ProbabilityNumber> values,
			Class<? extends ProbabilityNumber> clazz) {
		super(vars, values, clazz);
	}

	// Public methods

	// START-ProbabilityMass

	@Override
	public ProbabilityTable setValue(ProbabilityNumber value, Object... eventValues) {
		List<ProbabilityNumber> newValues = new ArrayList<ProbabilityNumber>(this.values);
		int idx = this.getIndex(eventValues);
		newValues.set(idx, value);
		ProbabilityTable newTable = new ProbabilityTable(this.randomVariables, newValues, this.clazz);
		return newTable;
	}

	@Override
	public ProbabilityTable setValue(ProbabilityNumber value, Map<RandomVariable, Object> event) {
		List<ProbabilityNumber> newValues = new ArrayList<ProbabilityNumber>(this.values);
		int idx = this.getIndex(event);
		newValues.set(idx, value);
		ProbabilityTable newTable = new ProbabilityTable(this.randomVariables, newValues, this.clazz);
		return newTable;
	}

	// END-ProbabilityMass

	// START-CategoricalDistribution

	@Override
	public ProbabilityTable normalize() {
		ProbabilityNumber sum = this.getSum();
		List<ProbabilityNumber> normalizedValues = this.values.stream().map(value -> value.divide(sum))
				.collect(Collectors.toList());
		ProbabilityTable result = new ProbabilityTable(this.randomVariables, normalizedValues, this.clazz);
		return result;
	}

	@Override
	public ProbabilityTable marginalize(List<RandomVariable> varsToMarginalize) {
		Objects.requireNonNull(varsToMarginalize,
				"varsToMarginalize must specify valid random variables to marginalize out");
		boolean isValid = varsToMarginalize.stream().allMatch(this.randomVariables::contains);
		if (!isValid) {
			throw new IllegalArgumentException(
					"varsToMarginalize must be a subset of randomVariables of the probability table.");
		}
		if (ListOps.difference(this.randomVariables, varsToMarginalize).size() == 0) {
			return new ProbabilityTable(new RandomVariable[] {}, new ProbabilityNumber[] { this.getSum() }, this.clazz);
		}
		List<RandomVariable> remainingVars = ListOps.difference(this.randomVariables, varsToMarginalize);
		List<Integer> remainingVarIdx = ListOps.getIntersectionIdxInSource(this.randomVariables, remainingVars);
		int[] marginalizedRadices = remainingVars.stream().mapToInt(var -> var.getDomain().size()).toArray();
		MixedRadixInterval marginalizedQueryMRI = new MixedRadixInterval(marginalizedRadices);
		int marginalizedValuesSize = ProbabilityUtilities.expectedSizeofProbabilityTable(remainingVars);
		List<ProbabilityNumber> marginalizedValues = new ArrayList<ProbabilityNumber>(
				Collections.nCopies(marginalizedValuesSize, this.probFactory.valueOf(BigDecimal.ZERO)));
		ProbabilityComputation adder = new ProbabilityComputation();
		adder.overrideComputationPrecision(MathContext.UNLIMITED);
		this.queryMRI.stream().forEach(possibleWorldNumerals -> {
			int[] summedOutNumerals = IntStream.range(0, possibleWorldNumerals.length).filter(remainingVarIdx::contains)
					.sorted().map(idx -> possibleWorldNumerals[idx]).toArray();
			int newValueIdx = marginalizedQueryMRI.getValueFor(summedOutNumerals).intValue();
			int addendIdx = queryMRI.getValueFor(possibleWorldNumerals).intValue();
			ProbabilityNumber augend = marginalizedValues.get(newValueIdx);
			ProbabilityNumber addend = this.values.get(addendIdx);
			marginalizedValues.set(newValueIdx, adder.add(augend, addend));
		});
		ProbabilityTable marginalized = new ProbabilityTable(remainingVars, marginalizedValues, this.clazz);
		return marginalized;
	}

	// END-CategoricalDistribution

	// START-Factor

	@Override
	public List<RandomVariable> getArgumentVariables() {
		return this.randomVariables;
	}

	public ProbabilityTable sumOut(List<RandomVariable> sumOutVars) {
		return this.marginalize(sumOutVars);
	}

	public ProbabilityTable pointwiseProduct(Factor multiplier) {
		return this.pointwiseProductPOS(multiplier, null);
	}

	@Override
	public ProbabilityTable pointwiseProductPOS(Factor multiplier, List<RandomVariable> prodVarOrder) {
		ProbabilityTable secondFactor = (ProbabilityTable) multiplier;
		if (null == prodVarOrder) {
			prodVarOrder = ListOps.union(this.randomVariables, secondFactor.randomVariables);
		} else {
			List<RandomVariable> vars = ListOps.union(this.randomVariables, secondFactor.randomVariables);
			boolean isValid = prodVarOrder.stream().allMatch(vars::contains);
			if (!isValid) {
				throw new IllegalArgumentException(
						"prodVarOrder must contain random variables present in either of the factors.");
			}
		}
		List<Integer> term1Idx = ListOps.getIntersectionIdxInTarget(this.randomVariables, prodVarOrder);
		List<Integer> term2Idx = ListOps.getIntersectionIdxInTarget(secondFactor.randomVariables, prodVarOrder);
		int[] productRadices = prodVarOrder.stream().mapToInt(var -> var.getDomain().size()).toArray();
		// Check productRadices for empty
		MixedRadixInterval productQueryMRI = new MixedRadixInterval(productRadices);
		int productValuesSize = ProbabilityUtilities.expectedSizeofProbabilityTable(prodVarOrder);
		List<ProbabilityNumber> productValues = new ArrayList<ProbabilityNumber>(
				Collections.nCopies(productValuesSize, this.probFactory.valueOf(BigDecimal.ZERO)));
		ProbabilityComputation compute = new ProbabilityComputation();
		productQueryMRI.stream().forEach(possibleWorldNumerals -> {
			int[] term1Numerals = term1Idx.stream().mapToInt(idx -> possibleWorldNumerals[idx]).toArray();
			int[] term2Numerals = term2Idx.stream().mapToInt(idx -> possibleWorldNumerals[idx]).toArray();
			int resultIdx = productQueryMRI.getValueFor(possibleWorldNumerals).intValue();
			int term1ValueIdx = this.queryMRI.getValueFor(term1Numerals).intValue();
			int term2ValueIdx = secondFactor.queryMRI.getValueFor(term2Numerals).intValue();
			ProbabilityNumber operand1 = this.values.get(term1ValueIdx);
			ProbabilityNumber operand2 = multiplier.getValues().get(term2ValueIdx);
			productValues.set(resultIdx, compute.mul(operand1, operand2));
		});
		ProbabilityTable product = new ProbabilityTable(prodVarOrder, productValues, this.clazz);
		return product;
	}

	// END-Factor

	/**
	 * @return size (number of values) of the probability table.
	 */
	public int size() {
		return this.values.size();
	}

	/**
	 * Compute sum only the first time getSum is called (due to immutability of
	 * ProbabilityTable).
	 * 
	 * @return sum of all ProbabilityTable values.
	 */
	public ProbabilityNumber getSum() {
		if (null == this.sumOfValues) {
			ProbabilityComputation adder = new ProbabilityComputation();
			ProbabilityNumber initValue = this.probFactory.valueOf(BigDecimal.ZERO);
			this.sumOfValues = this.values.stream().reduce(initValue, adder::add);
		}
		return this.sumOfValues;
	}

	/**
	 * Divide this ProbabilityTable (dividend) with another ProbabilityTable
	 * (divisor) whose random variables form a subset of the dividend.
	 * 
	 * @param divisor
	 *            a ProbabilityTable whose random variables form a subset of
	 *            this.randomVariables.
	 * 
	 * @return a new ProbabilityTable formed by dividing the values in the
	 *         dividend with corresponding values in the divisor.
	 */
	public ProbabilityTable divideBy(ProbabilityTable divisor) {
		Objects.requireNonNull(divisor, "Divisor ProbabilityTable must be specified.");
		if (!this.randomVariables.containsAll(divisor.randomVariables)) {
			throw new IllegalArgumentException("Divisor must be a subset of the dividend.");
		}
		ProbabilityComputation compute = new ProbabilityComputation();
		List<ProbabilityNumber> newValues;
		if (divisor.randomVariables.size() == 0) {
			ProbabilityNumber divisorValue = divisor.values.get(0);
			newValues = this.values.stream().map(value -> compute.div(value, divisorValue))
					.collect(Collectors.toList());
		} else {
			List<Integer> divisorIdx = ListOps.getIntersectionIdxInTarget(divisor.randomVariables,
					this.randomVariables);
			newValues = this.queryMRI.stream().map(possibleWorldNumerals -> {
				int[] divisorNumerals = divisorIdx.stream().mapToInt(idx -> possibleWorldNumerals[idx]).toArray();
				int dividendValuesIdx = this.queryMRI.getValueFor(possibleWorldNumerals).intValue();
				int divisorValuesIdx = divisor.queryMRI.getValueFor(divisorNumerals).intValue();
				ProbabilityNumber operand1 = this.values.get(dividendValuesIdx);
				ProbabilityNumber operand2 = divisor.values.get(divisorValuesIdx);
				return compute.div(operand1, operand2);
			}).collect(Collectors.toList());
		}
		ProbabilityTable quotient = new ProbabilityTable(this.randomVariables, newValues, this.clazz);
		return quotient;
	}

	/**
	 * String representation of ProbabilityTable.
	 */
	@Override
	public String toString() {
		return this.values.stream().map(value -> value.toString()).collect(Collectors.joining(", "));
	}
}