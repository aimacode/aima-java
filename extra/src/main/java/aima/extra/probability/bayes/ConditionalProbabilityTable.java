package aima.extra.probability.bayes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.IntStream;

import aima.core.util.math.MixedRadixInterval;
import aima.extra.probability.ProbabilityNumber;
import aima.extra.probability.RandomVariable;
import aima.extra.probability.constructs.ProbabilityUtilities;
import aima.extra.probability.domain.FiniteDomain;
import aima.extra.util.ListOps;

/**
 * A conditional probability table, or CPT, can be used for representing
 * conditional probabilities for discrete (finite) random variables. Each row in
 * a CPT contains the conditional probability of each node value for a
 * <b>conditioning case</b>.
 * 
 * @author Ciaran O'Reilly
 * @author Nagaraj Poti
 */
public class ConditionalProbabilityTable extends AbstractProbabilityTable
		implements ConditionalProbabilityDistribution {

	// Internal fields

	/**
	 * The query random variable X in P(X|E) for which the conditional
	 * probability distribution is defined.
	 */
	private RandomVariable on;

	/**
	 * The list of all parent random variables (E<sub>1</sub>,
	 * E<sub>2</sub>,...) in P(X|E<sub>1</sub>,E<sub>2</sub>,...).
	 */
	private List<RandomVariable> parents;

	// Constructor

	/**
	 * Constructor to initialize the ConditionalProbabilityTable.
	 * 
	 * @param on
	 *            is the query random variable X in P(X|E).
	 * @param values
	 *            is the ordered list of probability values that form the CPT.
	 * @param conditionedOn
	 *            is the list of parent random variables.
	 * @param clazz
	 *            specifies the class type of the ProbabilityNumber
	 *            implementation to use.
	 */
	public ConditionalProbabilityTable(RandomVariable on, List<ProbabilityNumber> values,
			List<RandomVariable> conditionedOn, Class<? extends ProbabilityNumber> clazz) {
		// The list randomVariables in AbstractProbabilityTable class consists
		// of the conditioning variable first followed by the conditionedOn
		// variables.
		super(ListOps.union(Arrays.asList(on), conditionedOn), values, clazz);
		this.on = on;
		this.parents = new ArrayList<RandomVariable>(conditionedOn);
		boolean isValid = checkEachRowTotalsOne();
		if (!isValid) {
			throw new IllegalArgumentException("Probability values of each row of the CPT do not sum to 1.0");
		}
	}

	// Public methods

	// START-ProbabilityMass

	@Override
	public ConditionalProbabilityTable setValue(ProbabilityNumber value, Object... eventValues) {
		List<ProbabilityNumber> newValues = new ArrayList<ProbabilityNumber>(this.values);
		int idx = this.getIndex(eventValues);
		newValues.set(idx, value);
		ConditionalProbabilityTable newTable = new ConditionalProbabilityTable(this.on, newValues, this.randomVariables,
				this.clazz);
		return newTable;
	}

	@Override
	public ConditionalProbabilityTable setValue(ProbabilityNumber value, Map<RandomVariable, Object> event) {
		List<ProbabilityNumber> newValues = new ArrayList<ProbabilityNumber>(this.values);
		int idx = this.getIndex(event);
		newValues.set(idx, value);
		ConditionalProbabilityTable newTable = new ConditionalProbabilityTable(this.on, newValues, this.randomVariables,
				this.clazz);
		return newTable;
	}

	// END-ProbabilityMass

	// START-CategoricalDistribution

	/**
	 * Normalization of a conditional probability table is done independently
	 * for each row of the CPT, where each row corresponds to a conditioning
	 * case.
	 */
	@Override
	public ConditionalProbabilityTable normalize() {
		ProbabilityTable summedOut = this.marginalize(Arrays.asList(this.on));
		List<ProbabilityNumber> normalizedValues = new ArrayList<ProbabilityNumber>(this.values);
		this.queryMRI.stream().forEach(possibleWorldNumerals -> {
			int[] summedOutNumerals = IntStream.range(1, possibleWorldNumerals.length).toArray();
			int summedOutIdx = summedOut.queryMRI.getValueFor(summedOutNumerals).intValue();
			ProbabilityNumber sum = summedOut.values.get(summedOutIdx);
			int normalizedIdx = this.queryMRI.getValueFor(possibleWorldNumerals).intValue();
			ProbabilityNumber normalizedValue = this.values.get(normalizedIdx).divide(sum);
			normalizedValues.set(normalizedIdx, normalizedValue);
		});
		ConditionalProbabilityTable normalized = new ConditionalProbabilityTable(this.on, normalizedValues,
				this.parents, this.clazz);
		return normalized;
	}

	/**
	 * Marginalization of the query random variable alone results in a
	 * meaningful outcome. The operation is UNDEFINED on parent random
	 * variables.
	 * 
	 * @param varsToMarginalize
	 *            is the list of random variables to marginalize. Should only
	 *            contain the query variable.
	 */
	@Override
	public ProbabilityTable marginalize(List<RandomVariable> varsToMarginalize) {
		Objects.requireNonNull(varsToMarginalize,
				"varsToMarginalize must specify valid random variables to marginalize out");
		boolean isValid = varsToMarginalize.stream().allMatch(this.randomVariables::contains);
		if (!isValid) {
			throw new IllegalArgumentException(
					"varsToMarginalize must be a subset of randomVariables of the probability table.");
		}
		List<RandomVariable> remainingVars = ListOps.difference(this.randomVariables, varsToMarginalize);
		List<Integer> remainingVarIdx = ListOps.getIntersectionIdx(this.randomVariables, remainingVars);
		int[] marginalizedRadices = remainingVars.stream().mapToInt(var -> var.getDomain().size()).toArray();
		MixedRadixInterval marginalizedQueryMRI = new MixedRadixInterval(marginalizedRadices);
		int marginalizedValuesSize = ProbabilityUtilities.expectedSizeofProbabilityTable(remainingVars);
		List<ProbabilityNumber> marginalizedValues = new ArrayList<ProbabilityNumber>(
				Collections.nCopies(marginalizedValuesSize, this.probFactory.valueOf(BigDecimal.ZERO)));
		this.queryMRI.stream().forEach(possibleWorldNumerals -> {
			int[] summedOutNumerals = IntStream.range(0, possibleWorldNumerals.length).filter(remainingVarIdx::contains)
					.sorted().map(idx -> possibleWorldNumerals[idx]).toArray();
			int newValueIdx = marginalizedQueryMRI.getValueFor(summedOutNumerals).intValue();
			int addendIdx = queryMRI.getValueFor(possibleWorldNumerals).intValue();
			ProbabilityNumber augend = marginalizedValues.get(newValueIdx);
			ProbabilityNumber addend = this.values.get(addendIdx);
			marginalizedValues.set(newValueIdx, augend.add(addend));
		});
		ProbabilityTable marginalized = new ProbabilityTable(remainingVars, marginalizedValues, this.clazz);
		return marginalized;	
	}

	// END-CategoricalDistribution

	// START-ConditionalProbabilityDistribution

	@Override
	public RandomVariable getOn() {
		return this.on;
	}

	@Override
	public List<RandomVariable> getParents() {
		return this.parents;
	}

	@Override
	public ProbabilityTable getConditioningCase(Map<RandomVariable, Object> parentWorld) {
		int[] conditionedWorldNumerals = new int[this.randomVariables.size()];
		IntStream.range(1, this.randomVariables.size()).forEach(idx -> {
			RandomVariable var = this.randomVariables.get(idx);
			conditionedWorldNumerals[idx] = ((FiniteDomain) (var.getDomain())).getOffset(parentWorld.get(var));
		});
		int conditionedOnDomainSize = this.on.getDomain().size();
		ProbabilityNumber[] conditionedValues = new ProbabilityNumber[conditionedOnDomainSize];
		IntStream.range(0, this.randomVariables.size()).forEach(domainIdx -> {
			conditionedWorldNumerals[0] = domainIdx;
			conditionedValues[domainIdx] = this.values.get(this.queryMRI.getValueFor(conditionedWorldNumerals).intValue());
		});
		ProbabilityTable conditioned = new ProbabilityTable(new RandomVariable[]{this.on}, conditionedValues, this.clazz);
		return conditioned;
	}

	@Override
	public ProbabilityTable getConditioningCase(Predicate<Map<RandomVariable, Object>> parentWorldProposition) {
		int conditionedOnDomainSize = this.on.getDomain().size();
		ProbabilityNumber[] conditionedValues = new ProbabilityNumber[conditionedOnDomainSize];
		this.queryMRI.stream().forEach(possibleWorldNumerals -> {
			int[] parentWorldNumerals = Arrays.copyOfRange(possibleWorldNumerals, 1, possibleWorldNumerals.length);
			Map<RandomVariable, Object> conditionedWorld = this.mapNumeralsToProposition(parentWorldNumerals);
			int domainIdx = parentWorldNumerals[0];
			if (parentWorldProposition.test(conditionedWorld) == true) {
				conditionedValues[domainIdx] = this.values.get(this.queryMRI.getValueFor(possibleWorldNumerals).intValue());
			}
		});
		ProbabilityTable conditioned = new ProbabilityTable(new RandomVariable[]{this.on}, conditionedValues, this.clazz);
		return conditioned;
	}

	// END-ConditionalProbabilityDistribution

	// Private methods

	/**
	 * Check if each row of the CPT sums to one. Method returns true for any
	 * valid CPT.
	 * 
	 * @return true if each row adds upto one, false otherwise.
	 */
	private boolean checkEachRowTotalsOne() {
		ProbabilityTable summedOut = this.marginalize(Arrays.asList(this.on));
		boolean check = summedOut.values.stream().allMatch(value -> value.isOne());
		return check;
	}
}
