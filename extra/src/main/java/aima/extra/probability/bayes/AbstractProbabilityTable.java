package aima.extra.probability.bayes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import aima.core.util.math.MixedRadixInterval;
import aima.extra.probability.ProbabilityNumber;
import aima.extra.probability.RandomVariable;
import aima.extra.probability.ProbabilityComputation;
import aima.extra.probability.constructs.ProbabilityUtilities;
import aima.extra.probability.domain.FiniteDomain;
import aima.extra.probability.factory.ProbabilityFactory;
import aima.extra.probability.proposition.Proposition;

/**
 * Defines all common datastructures and probability table operations.
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

	// Constructor
	
	/**
	 * Constructor to initialize the probability table with values if provided,
	 * else initializes it with zero probability values.
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
		// TODO - Unmodifiable list ?
		int expectedSize = ProbabilityUtilities.expectedSizeofProbabilityTable(vars);
		this.clazz = clazz;
		this.probFactory = ProbabilityFactory.make(clazz);
		if (null == values) {
			this.values = new ArrayList<ProbabilityNumber>(
					Collections.nCopies(expectedSize, this.probFactory.valueOf(BigDecimal.ZERO)));
		} else {
			if (values.size() != expectedSize) {
				throw new IllegalArgumentException(
						"ProbabilityTable of length " + values.size() + " is not the correct size, should be "
								+ expectedSize + " in order to represent all possible combinations.");
			}
			this.values = values.stream().map(val -> this.probFactory.convert(val)).collect(Collectors.toList());
		}
		this.randomVariables = vars.stream().map(var -> (RandomVariable) var).collect(Collectors.toList());
		int[] radices = this.randomVariables.stream().mapToInt(var -> var.getDomain().size()).toArray();
		this.queryMRI = new MixedRadixInterval(radices);
	}

	// Public methods 
	
	// START-ProbabilityDistribution

	@Override
	public List<RandomVariable> getVariables() {
		return this.randomVariables;
	}

	@Override
	public boolean contains(RandomVariable var) {
		return this.randomVariables.contains(var);
	}

	@Override
	public ProbabilityNumber getValue(Predicate<List<Object>> eventProposition) {
		ProbabilityComputation adder = new ProbabilityComputation();
		ProbabilityNumber result = this.probFactory.valueOf(0.0);
		AtomicInteger counter = new AtomicInteger(0);
		this.queryMRI.stream().map(possibleWorld -> {
			List<Object> eventValues = Arrays.stream(possibleWorld)
					.mapToObj(e -> ((FiniteDomain) (this.randomVariables.get(counter.getAndIncrement()).getDomain()))
							.getValueAt(e))
					.collect(Collectors.toList());
			return eventValues;
		}).filter(eventProposition).map(eventValues -> this.getValue(eventValues)).reduce(result, adder::add);
		return null;
	}

	// END-ProbabilityDistribution

	// START-ProbabilityMass

	@Override
	public void setValue(List<Object> eventValues, ProbabilityNumber value) {
		int idx = this.getIndex(eventValues);
		this.setValue(idx, value);
	}

	@Override
	public void setValue(Map<RandomVariable, Object> event, ProbabilityNumber value) {
		List<Object> eventValues = this.randomVariables.stream().map(var -> event.get(var))
				.collect(Collectors.toList());
		int idx = this.getIndex(eventValues);
		this.setValue(idx, value);
	}

	@Override
	public void setValue(ProbabilityNumber value, Proposition... assignmentPropositions) {
		Map<RandomVariable, Object> event = new HashMap<RandomVariable, Object>();
		// TODO - Check if particular type of proposition ?
		Arrays.stream(assignmentPropositions).forEach(
				proposition -> event.put(proposition.getPropositionVariable(), proposition.getPropositionValue()));
		this.setValue(event, value);
	}

	@Override
	public ProbabilityNumber getValue(List<Object> eventValues) {
		int idx = this.getIndex(eventValues);
		return this.values.get(idx);
	}

	@Override
	public ProbabilityNumber getValue(Map<RandomVariable, Object> event) {
		List<Object> eventValues = this.randomVariables.stream().map(var -> event.get(var))
				.collect(Collectors.toList());
		return this.getValue(eventValues);
	}

	@Override
	public ProbabilityNumber getValue(Proposition... assignmentPropositions) {
		Map<RandomVariable, Object> event = new HashMap<RandomVariable, Object>();
		// TODO - Check if particular type of proposition ?
		Arrays.stream(assignmentPropositions).forEach(
				proposition -> event.put(proposition.getPropositionVariable(), proposition.getPropositionValue()));
		return this.getValue(event);
	}

	// END-ProbabilityMass

	// START-CategoricalDistribution

	@Override
	public void setValue(int idx, ProbabilityNumber value) {
		this.values.set(idx, value);
	}

	@Override
	public List<ProbabilityNumber> getValues() {
		return this.values;
	}

	@Override
	public int getIndex(List<Object> eventValues) {
		// TODO - Check list sizes equality
		int radixSize = this.randomVariables.size();
		int[] radixValues = new int[radixSize];
		for (int i = 0; i < radixSize; i++) {
			RandomVariable var = this.randomVariables.get(i);
			Object value = eventValues.get(i);
			radixValues[i] = ((FiniteDomain) (var.getDomain())).getOffset(value);
		}
		int idx = this.queryMRI.getValueFor(radixValues).intValue();
		return idx;
	}

	@Override
	public int getIndex(Map<RandomVariable, Object> event) {
		int radixSize = this.randomVariables.size();
		int[] radixValues = new int[radixSize];
		for (int i = 0; i < radixSize; i++) {
			RandomVariable var = this.randomVariables.get(i);
			Object value = event.get(var);
			radixValues[i] = ((FiniteDomain) (var.getDomain())).getOffset(value);
		}
		int idx = this.queryMRI.getValueFor(radixValues).intValue();
		return idx;
	}

	// END-CategoricalDistribution
}
