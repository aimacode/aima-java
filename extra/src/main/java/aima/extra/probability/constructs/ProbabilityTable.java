package aima.extra.probability.constructs;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import aima.core.util.math.MixedRadixInterval;
import aima.extra.probability.ProbabilityNumber;
import aima.extra.probability.factory.ProbabilityFactory;
import aima.extra.util.ListOps;

/**
 * This class represents a ProbabilityTable that is indexed by the values
 * corresponding to random variables. A ProbabilityTable holds the values of the
 * joint distribution of a set of random variables.
 * 
 * @author Ciaran O'Reilly
 * @author Nagaraj Poti
 */
public class ProbabilityTable {

	// Internal fields

	/**
	 * Contains all values of the joint distribution.
	 */
	private final List<ProbabilityNumber> values;

	/**
	 * Ordered set of all random variables over which the joint distribution is
	 * represented.
	 */
	private final List<FiniteRandomVariable> randomVariables;

	/**
	 * Data structure for indexing into values to store and retrieve probability
	 * values. {@link MixedRadixInterval}
	 */
	private final MixedRadixInterval queryMRI;

	/**
	 * ProbabilityFactory instance.
	 */
	private final ProbabilityFactory<?> probFactory;

	/**
	 * Sum of all values.
	 */
	private ProbabilityNumber sumOfValues = null;

	/**
	 * Class type of the ProbabilityNumber used by values.
	 */
	private Class<? extends ProbabilityNumber> clazz;

	// Constructors

	/**
	 * Constructor initializes ProbabilityTable with all values set to 0.
	 * 
	 * @param vars
	 *            is an ordered set of all random variables for which the
	 *            distribution is to be represented.
	 * @param clazz
	 *            specifies the class type of the ProbabilityNumber
	 *            implementation to use.
	 */
	public ProbabilityTable(List<? extends RandomVariable> vars, Class<? extends ProbabilityNumber> clazz) {
		this(vars, null, clazz);
	}

	/**
	 * Constructor initializes ProbabilityTable with values if provided, else
	 * initializes it with zero probability values.
	 * 
	 * @param vars
	 *            is an ordered set of all random variables.
	 * @param values
	 *            is an ordered list of probability values that form the
	 *            probability table.
	 */
	public ProbabilityTable(List<? extends RandomVariable> vars, List<ProbabilityNumber> values,
			Class<? extends ProbabilityNumber> clazz) {
		int expectedSize = ProbabilityUtilities.expectedSizeofProbabilityTable(vars);
		this.clazz = clazz;
		probFactory = ProbabilityFactory.make(clazz);
		if (null == values) {
			this.values = new ArrayList<ProbabilityNumber>(
					Collections.nCopies(expectedSize, probFactory.valueOf(BigDecimal.ZERO)));
		} else {
			if (values.size() != expectedSize) {
				throw new IllegalArgumentException(
						"ProbabilityTable of length " + values.size() + " is not the correct size, should be "
								+ expectedSize + " in order to represent all possible combinations.");
			}
			this.values = values.stream().map(val -> probFactory.convert(val))
					.collect(Collectors.toCollection(ArrayList<ProbabilityNumber>::new));
		}
		this.randomVariables = vars.stream().map(var -> (FiniteRandomVariable) var)
				.collect(Collectors.toCollection(ArrayList<FiniteRandomVariable>::new));
		int[] radices = this.randomVariables.stream().mapToInt(var -> var.getDomain().size()).toArray();
		this.queryMRI = new MixedRadixInterval(radices);
	}

	// Public methods

	/**
	 * @return size (number of values) of the probability table.
	 */
	public int size() {
		return this.values.size();
	}

	/**
	 * @return list of ProbabilityTable values.
	 */
	public List<ProbabilityNumber> getValues() {
		return this.values;
	}

	/**
	 * Set a particular value of ProbabilityTable by providing its index and
	 * value.
	 * 
	 * @param idx
	 *            at which to store the value.
	 * @param value
	 */
	public void setValue(int idx, ProbabilityNumber value) {
		this.values.set(idx, value);
	}

	/**
	 * Lazy computation of sum (recompute sum only if any probability value is
	 * changed).
	 * 
	 * @return sum of all ProbabilityTable values.
	 */
	public ProbabilityNumber getSum() {
		if (null == this.sumOfValues) {
			final ProbabilityComputation adder = new ProbabilityComputation();
			ProbabilityNumber initValue = probFactory.valueOf(BigDecimal.ZERO);
			sumOfValues = values.stream().reduce(initValue, adder::add);
		}
		return sumOfValues;
	}

	/**
	 * The task of extracting the distribution over some subset of variables or
	 * a single variable is called marginalization or summing out. The
	 * probabilities for each possible value of the remaining variables are
	 * summed, thereby taking them out of the equation.
	 * 
	 * @param vars
	 *            is the set of random variables to be summed out (need NOT be
	 *            in order).
	 * 
	 * @return a new ProbabilityTable with updated probability values, excluding
	 *         the summed out random variables. The random variables retain
	 *         their original order.
	 */
	public ProbabilityTable sumOut(List<? extends RandomVariable> vars) {
		// Check if vars randomvariables exist
		List<FiniteRandomVariable> sumOutVars = vars.stream().map(var -> (FiniteRandomVariable) var)
				.collect(Collectors.toList());
		List<FiniteRandomVariable> remainingVars = ListOps.difference(this.randomVariables, sumOutVars);
		final List<Integer> remainingVarIdx = ListOps.getIntersectionIdx(this.randomVariables, remainingVars);
		final ProbabilityTable summedOut = new ProbabilityTable(remainingVars, this.clazz);
		this.queryMRI.stream().map(possibleWorldNumerals -> {
			int[] summedOutNumerals = IntStream.range(0, possibleWorldNumerals.length).filter(remainingVarIdx::contains)
					.sorted().toArray();
			int newValueIdx = summedOut.queryMRI.getValueFor(summedOutNumerals).intValue();
			int addendIdx = queryMRI.getValueFor(possibleWorldNumerals).intValue();
			ProbabilityNumber augend = summedOut.getValues().get(newValueIdx);
			ProbabilityNumber addend = this.getValues().get(addendIdx);
			summedOut.getValues().set(newValueIdx, augend.add(addend));
			return possibleWorldNumerals;
		});
		return summedOut;
	}

	/**
	 * The pointwise product of two factors f<sub>1</sub> and f<sub>2</sub>
	 * yields a new factor f whose variables are the union of the variables in
	 * f<sub>1</sub> and f<sub>2</sub> and whose elements are given by the
	 * product of the corresponding elements in the two factors.
	 * 
	 * @param multiplier
	 *            is the second factor operand.
	 * 
	 * @return the resultant factor of applying pointwise product operation on
	 *         operand factors.
	 */
	public ProbabilityTable pointwiseProduct(final ProbabilityTable multiplier) {
		List<FiniteRandomVariable> productRandomVariables = ListOps.union(this.randomVariables,
				multiplier.randomVariables);
		final ProbabilityTable product = new ProbabilityTable(productRandomVariables, this.clazz);
		final List<Integer> term1Idx = ListOps.getIntersectionIdx(product.randomVariables, this.randomVariables);
		final List<Integer> term2Idx = ListOps.getIntersectionIdx(product.randomVariables, multiplier.randomVariables);
		product.queryMRI.stream().map(possibleWorldNumerals -> {
			int[] term1Numerals = IntStream.range(0, possibleWorldNumerals.length).filter(term1Idx::contains)
					.sorted().toArray();
			int[] term2Numerals = IntStream.range(0, possibleWorldNumerals.length).filter(term2Idx::contains)
					.sorted().toArray();
			int resultIdx = product.queryMRI.getValueFor(possibleWorldNumerals).intValue();
			int term1ValueIdx = this.queryMRI.getValueFor(term1Numerals).intValue();
			int term2ValueIdx = multiplier.queryMRI.getValueFor(term2Numerals).intValue();
			ProbabilityNumber operand1 = this.getValues().get(term1ValueIdx);
			ProbabilityNumber operand2 = multiplier.getValues().get(term2ValueIdx);
			product.getValues().set(resultIdx, operand1.multiply(operand2));
			return possibleWorldNumerals;
		});
		return product;
	}

	/**
	 * String representation of ProbabilityTable.
	 */
	@Override
	public String toString() {
		return values.stream().map(value -> value.toString()).collect(Collectors.joining(", "));
	}

}