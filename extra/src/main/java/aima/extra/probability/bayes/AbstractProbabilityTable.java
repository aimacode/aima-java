package aima.extra.probability.bayes;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import aima.core.util.math.MixedRadixInterval;
import aima.extra.probability.ProbabilityNumber;
import aima.extra.probability.RandomVariable;
import aima.extra.probability.ProbabilityComputation;
import aima.extra.probability.constructs.ProbabilityUtilities;
import aima.extra.probability.domain.FiniteDomain;
import aima.extra.probability.factory.ProbabilityFactory;
import aima.extra.util.ListOps;

/**
 * Defines all common datastructures and probability table operations. The class
 * is
 * 
 * @author Nagaraj Poti
 */
public abstract class AbstractProbabilityTable implements CategoricalDistribution {

	// Internal fields

	/**
	 * Contains all values of the distribution.
	 */
	protected List<ProbabilityNumber> values;

	/**
	 * Ordered set of all random variables over which the distribution is on.
	 */
	protected List<RandomVariable> randomVariables;

	/**
	 * Data structure for indexing into values to store and retrieve probability
	 * values. {@link MixedRadixInterval}
	 */
	protected MixedRadixInterval queryMRI;

	/**
	 * ProbabilityFactory instance.
	 */
	protected ProbabilityFactory<?> probFactory;

	/**
	 * Class type of the ProbabilityNumber used by values.
	 */
	protected Class<? extends ProbabilityNumber> clazz;

	// Public constructors

	/**
	 * Constructor to initialize the probability table with explicitly specified
	 * values.
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
	public AbstractProbabilityTable(RandomVariable[] vars, ProbabilityNumber[] values,
			Class<? extends ProbabilityNumber> clazz) {
		this(Arrays.asList(vars), Arrays.asList(values), clazz);
	}

	/**
	 * Constructor to initialize the probability table with explicitly specified
	 * values.
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
	public AbstractProbabilityTable(List<RandomVariable> vars, List<ProbabilityNumber> values,
			Class<? extends ProbabilityNumber> clazz) {
		init(vars, values, clazz);
	}

	// Public methods

	// START-ProbabilityDistribution

	@Override
	public List<RandomVariable> getVariables() {
		return this.randomVariables;
	}

	@Override
	public boolean contains(RandomVariable var) {
		boolean contains = this.randomVariables.contains(var);
		return contains;
	}

	@Override
	public ProbabilityNumber getValue(Predicate<Map<RandomVariable, Object>> eventProposition) {
		ProbabilityComputation adder = new ProbabilityComputation();
		ProbabilityNumber result = this.probFactory.valueOf(0.0);
		result = this.queryMRI.stream().map(possibleWorld -> mapNumeralsToProposition(possibleWorld))
				.filter(eventProposition).map(event -> this.values.get(this.getIndex(event)))
				.reduce(result, adder::add);
		return result;
	}

	// END-ProbabilityDistribution

	// START-ProbabilityMass

	@Override
	public ProbabilityNumber getValue(Object... eventValues) {
		int idx = this.getIndex(eventValues);
		return this.values.get(idx);
	}

	@Override
	public ProbabilityNumber getValue(Map<RandomVariable, Object> event) {
		Object[] eventValues = this.randomVariables.stream().map(var -> event.get(var)).toArray();
		return this.getValue(eventValues);
	}

	// END-ProbabilityMass

	// START-CategoricalDistribution

	@Override
	public List<ProbabilityNumber> getValues() {
		return this.values;
	}

	@Override
	public int getIndex(Object... eventValues) {
		if (this.randomVariables.size() != eventValues.length) {
			throw new IllegalArgumentException("Size of eventValues does not match size of randomVariables.");
		}
		int radixSize = this.randomVariables.size();
		int[] radixValues = new int[radixSize];
		for (int idx = 0; idx < radixSize; idx++) {
			RandomVariable var = this.randomVariables.get(idx);
			Object value = eventValues[idx];
			radixValues[idx] = ((FiniteDomain) (var.getDomain())).getOffset(value);
		}
		int idx = this.queryMRI.getValueFor(radixValues).intValue();
		return idx;
	}

	@Override
	public int getIndex(Map<RandomVariable, Object> event) {
		Object[] eventValues = this.randomVariables.stream().map(var -> event.get(var)).toArray();
		return this.getIndex(eventValues);
	}

	// END-CategoricalDistribution

	// Protected methods

	/**
	 * Set the value at a specifed index within the distribution.
	 * 
	 * @param idx
	 * @param value
	 */
	protected void setValue(int idx, ProbabilityNumber value) {
		this.values.set(idx, value);
	}

	/**
	 * Convert radix numeral values to a mapping of random variables and their
	 * corresponding domain values.
	 * 
	 * @param possibleWorldNumerals
	 *            is an array of numeral values for the radices.
	 * 
	 * @return a mapping of random variables to their domain values given by
	 *         possibleWorldNumerals.
	 */
	protected Map<RandomVariable, Object> mapNumeralsToProposition(int[] possibleWorldNumerals) {
		Map<RandomVariable, Object> proposition = new HashMap<RandomVariable, Object>();
		for (int idx = 0; idx < possibleWorldNumerals.length; idx++) {
			RandomVariable var = this.randomVariables.get(idx);
			Object value = ((FiniteDomain) (this.randomVariables.get(idx).getDomain()))
					.getValueAt(possibleWorldNumerals[idx]);
			proposition.put(var, value);
		}
		return proposition;
	}

	// Private methods

	/**
	 * Initialization method called by the constructor.
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
	private void init(List<RandomVariable> vars, List<ProbabilityNumber> values,
			Class<? extends ProbabilityNumber> clazz) {
		Objects.requireNonNull(vars,
				"RandomVariable list cannot be null. To specify zero random variables, create an empty list.");
		Objects.requireNonNull(values, "Values must be specified.");
		boolean isValid = ProbabilityUtilities.checkIfRandomVariablesAreUnique(vars);
		if (!isValid) {
			throw new IllegalArgumentException("Random variables must have unique names.");
		}
		int expectedSize = ProbabilityUtilities.expectedSizeofProbabilityTable(vars);
		if (values.size() != expectedSize) {
			throw new IllegalArgumentException(
					"ProbabilityTable of length " + values.size() + " is not the correct size, should be "
							+ expectedSize + " in order to represent all possible combinations.");
		}
		this.clazz = Objects.requireNonNull(clazz, "ProbabilityNumber class type must be specified.");
		this.probFactory = ProbabilityFactory.make(this.clazz);
		this.randomVariables = ListOps.protectListFromModification(vars);
		this.values = values.stream().map(val -> this.probFactory.convert(val)).collect(Collectors.toList());
		this.values = ListOps.protectListFromModification(this.values);
		int[] radices = this.randomVariables.stream().mapToInt(var -> var.getDomain().size()).toArray();
		if (radices.length == 0) {
			radices = new int[] { 1 };
		}
		this.queryMRI = new MixedRadixInterval(radices);
	}
}
