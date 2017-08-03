package aima.extra.probability.bayes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import aima.extra.probability.ProbabilityNumber;
import aima.extra.probability.RandomVariable;
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
public class ProbabilityTable extends AbstractProbabilityTable implements Factor {

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
	
	// Private constructor
	
	private ProbabilityTable(List<RandomVariable> vars, Class<? extends ProbabilityNumber> clazz) {
		super(vars, clazz);
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
	public ProbabilityTable marginalize(RandomVariable... varsToMarginalize) {
		return this.sumOut(varsToMarginalize);
	}

	// END-CategoricalDistribution

	/**
	 * @return size (number of values) of the probability table.
	 */
	public int size() {
		return this.values.size();
	}

	/**
	 * Lazy computation of sum (recompute sum only if any probability value is
	 * changed).
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

	// START-Factor

	@Override
	public List<RandomVariable> getArgumentVariables() {
		return this.randomVariables;
	}

	public ProbabilityTable sumOut(RandomVariable... sumOutVars) {
		// TODO - Check if vars randomvariables exist
		List<RandomVariable> sumOutVarsList = Arrays.asList(sumOutVars);
		List<RandomVariable> remainingVars = ListOps.difference(this.randomVariables, sumOutVarsList);
		List<Integer> remainingVarIdx = ListOps.getIntersectionIdx(this.randomVariables, remainingVars);
		ProbabilityTable summedOut = new ProbabilityTable(remainingVars, this.clazz);
		this.queryMRI.stream().forEach(possibleWorldNumerals -> {
			int[] summedOutNumerals = IntStream.range(0, possibleWorldNumerals.length).filter(remainingVarIdx::contains)
					.sorted().toArray();
			int newValueIdx = summedOut.queryMRI.getValueFor(summedOutNumerals).intValue();
			int addendIdx = queryMRI.getValueFor(possibleWorldNumerals).intValue();
			ProbabilityNumber augend = summedOut.values.get(newValueIdx);
			ProbabilityNumber addend = this.values.get(addendIdx);
			summedOut.setValue(newValueIdx, augend.add(addend));
		});
		summedOut.values = ListOps.protectListFromModification(summedOut.values);
		return summedOut;
	}

	public ProbabilityTable pointwiseProduct(Factor multiplier) {
		// TODO - Check if random variables are valid
		ProbabilityTable secondFactor = (ProbabilityTable) multiplier;
		List<RandomVariable> productRandomVariables = ListOps.union(this.randomVariables, secondFactor.randomVariables);
		return this.pointwiseProductPOS(multiplier, productRandomVariables);
	}

	@Override
	public ProbabilityTable pointwiseProductPOS(Factor multiplier, List<RandomVariable> prodVarOrder) {
		ProbabilityTable secondFactor = (ProbabilityTable) multiplier;
		ProbabilityTable product = new ProbabilityTable(prodVarOrder, this.clazz);
		List<Integer> term1Idx = ListOps.getIntersectionIdx(product.randomVariables, this.randomVariables);
		List<Integer> term2Idx = ListOps.getIntersectionIdx(product.randomVariables, secondFactor.randomVariables);
		product.queryMRI.stream().forEach(possibleWorldNumerals -> {
			int[] term1Numerals = IntStream.range(0, possibleWorldNumerals.length).filter(term1Idx::contains).sorted()
					.toArray();
			int[] term2Numerals = IntStream.range(0, possibleWorldNumerals.length).filter(term2Idx::contains).sorted()
					.toArray();
			int resultIdx = product.queryMRI.getValueFor(possibleWorldNumerals).intValue();
			int term1ValueIdx = this.queryMRI.getValueFor(term1Numerals).intValue();
			int term2ValueIdx = secondFactor.queryMRI.getValueFor(term2Numerals).intValue();
			ProbabilityNumber operand1 = this.values.get(term1ValueIdx);
			ProbabilityNumber operand2 = multiplier.getValues().get(term2ValueIdx);
			product.setValue(resultIdx, operand1.multiply(operand2));
		});
		product.values = ListOps.protectListFromModification(product.values);
		return product;
	}

	// END-Factor

	/**
	 * String representation of ProbabilityTable.
	 */
	@Override
	public String toString() {
		return Stream.of(this.values).map(value -> value.toString()).collect(Collectors.joining(", "));
	}
}