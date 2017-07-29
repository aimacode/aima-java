package aima.extra.probability.bayes;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
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

	// Constructors

	/**
	 * Constructor initializes ProbabilityTable with all values set to 0.
	 * 
	 * @param vars
	 *            is an ordered list of all random variables for which the
	 *            distribution is to be represented.
	 * @param clazz
	 *            specifies the class type of the ProbabilityNumber
	 *            implementation to use.
	 */
	public ProbabilityTable(List<RandomVariable> vars, Class<? extends ProbabilityNumber> clazz) {
		super(vars, null, clazz);
	}

	/**
	 * Constructor initializes ProbabilityTable with values if provided,
	 * else initializes it with zero probability values.
	 * 
	 * @param vars
	 *            is an ordered set of all random variables.
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
	public ProbabilityTable marginalize(List<RandomVariable> vars) {
		return sumOut(vars);
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

	public ProbabilityTable sumOut(List<RandomVariable> sumOutVars) {
		// TODO - Check if vars randomvariables exist
		List<RandomVariable> remainingVars = ListOps.difference(this.randomVariables, sumOutVars);
		List<Integer> remainingVarIdx = ListOps.getIntersectionIdx(this.randomVariables, remainingVars);
		ProbabilityTable summedOut = new ProbabilityTable(remainingVars, this.clazz);
		this.queryMRI.stream().forEach(possibleWorldNumerals -> {
			int[] summedOutNumerals = IntStream.range(0, possibleWorldNumerals.length).filter(remainingVarIdx::contains)
					.sorted().toArray();
			int newValueIdx = summedOut.queryMRI.getValueFor(summedOutNumerals).intValue();
			int addendIdx = queryMRI.getValueFor(possibleWorldNumerals).intValue();
			ProbabilityNumber augend = summedOut.getValues().get(newValueIdx);
			ProbabilityNumber addend = this.getValues().get(addendIdx);
			summedOut.setValue(newValueIdx, augend.add(addend));
		});
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
			ProbabilityNumber operand1 = this.getValues().get(term1ValueIdx);
			ProbabilityNumber operand2 = multiplier.getValues().get(term2ValueIdx);
			product.setValue(resultIdx, operand1.multiply(operand2));
		});
		return product;
	}
	
	// END-Factor

	/**
	 * String representation of ProbabilityTable.
	 */
	@Override
	public String toString() {
		return values.stream().map(value -> value.toString()).collect(Collectors.joining(", "));
	}

}