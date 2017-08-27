package aima.extra.probability.bayes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import aima.extra.probability.ProbabilityComputation;
import aima.extra.probability.ProbabilityNumber;
import aima.extra.probability.RandomVariable;
import aima.extra.probability.factory.ProbabilityFactory;
import aima.extra.util.ListOps;

/**
 * An implementation of the FiniteProbabilityModel interface using a full joint
 * distribution as the underlying model.
 * 
 * @author Ciaran O'Reilly
 * @author Nagaraj Poti
 */
public class FiniteFullJointDistributionModel implements FiniteProbabilityModel {

	// Internal fields

	/**
	 * This is an underlying full joint distribution over a set of random
	 * variables.
	 */
	private ProbabilityTable jointDistribution;

	/**
	 * Class type of the ProbabilityNumber used by values.
	 */
	private Class<? extends ProbabilityNumber> clazz;

	/**
	 * ProbabilityFactory instance.
	 */
	private ProbabilityFactory<?> probFactory;

	// Constructor

	/**
	 * Constructor initializes FiniteFullJointDistributionModel with
	 * ProbabilityTable constructed from vars and values.
	 * 
	 * @param vars
	 *            is the list of random variables comprising the joint
	 *            distribution.
	 * @param values
	 *            is the list of probability values of the joint distribution.
	 * @param clazz
	 *            specifies the class type of the ProbabilityNumber
	 *            implementation to use.
	 */
	public FiniteFullJointDistributionModel(List<RandomVariable> vars, List<ProbabilityNumber> values,
			Class<? extends ProbabilityNumber> clazz) {
		this.jointDistribution = new ProbabilityTable(vars, values, clazz);
		this.clazz = clazz;
		probFactory = ProbabilityFactory.make(this.clazz);
	}

	// Public methods

	// START-ProbabilityModel

	@Override
	public boolean isValid() {
		boolean valid = this.jointDistribution.getSum().isOne();
		return valid;
	}

	@Override
	public ProbabilityNumber prior(Predicate<Map<RandomVariable, Object>> phi) {
		ProbabilityNumber result = this.jointDistribution.getValue(phi);
		return result;
	}

	@Override
	public ProbabilityNumber posterior(Predicate<Map<RandomVariable, Object>> phi,
			Predicate<Map<RandomVariable, Object>> evidence) {
		ProbabilityNumber probabilityOfPhi = this.jointDistribution.getValue(phi.and(evidence));
		ProbabilityNumber probabilityOfEvidence = this.jointDistribution.getValue(evidence);
		ProbabilityComputation compute = new ProbabilityComputation();
		ProbabilityNumber result = compute.div(probabilityOfPhi, probabilityOfEvidence);
		return result;
	}

	@Override
	public List<RandomVariable> getRepresentation() {
		return this.jointDistribution.getVariables();
	}

	// END-ProbabilityModel

	// START-FiniteProbabilityModel

	@Override
	public ProbabilityTable priorDistribution(Predicate<Map<RandomVariable, Object>> phi,
			List<RandomVariable> unscopedTerms) {
		ProbabilityTable result = probabilityOf(phi, unscopedTerms);
		return result;
	}

	@Override
	public CategoricalDistribution posteriorDistribution(Predicate<Map<RandomVariable, Object>> phi,
			List<RandomVariable> unscopedPhi, Predicate<Map<RandomVariable, Object>> evidence,
			List<RandomVariable> unscopedEvidence) {
		Objects.requireNonNull(unscopedPhi, "To specify 0 unscoped variables, pass a blank list (not null).");
		Objects.requireNonNull(unscopedEvidence, "To specify 0 unscoped variables, pass a blank list (not null).");
		List<RandomVariable> dividendRandomVariables = this.jointDistribution.getVariables();
		List<ProbabilityNumber> dividendValues = new ArrayList<ProbabilityNumber>(
				Collections.nCopies(this.jointDistribution.size(), this.probFactory.valueOf(BigDecimal.ZERO)));
		jointDistribution.stream().filter(phi.and(evidence)).forEach(
				world -> dividendValues.set(jointDistribution.getIndex(world), jointDistribution.getValue(world)));
		ProbabilityTable dividend = new ProbabilityTable(dividendRandomVariables, dividendValues, this.clazz);
		ProbabilityTable divisor = priorDistribution(evidence, unscopedEvidence);
		ProbabilityTable posterior = dividend.divideBy(divisor);
		List<RandomVariable> varsToMarginalize = ListOps.difference(dividendRandomVariables,
				ListOps.union(unscopedPhi, unscopedEvidence));
		ProbabilityTable result = posterior.marginalize(varsToMarginalize);
		return result;
	}

	@Override
	public CategoricalDistribution jointDistribution(Predicate<Map<RandomVariable, Object>> proposition,
			List<RandomVariable> unscopedTerms) {
		ProbabilityTable result = probabilityOf(proposition, unscopedTerms);
		return result;
	}

	// END-FiniteProbabilityModel

	// Private methods

	/**
	 * Returns ProbabilityTable that satisfies the specified proposition.
	 * 
	 * @param proposition
	 *            for which the probability distribution is to be returned.
	 * @param unscopedTerms
	 *            is the list of unscoped random variables.
	 * @return ProbabilityTable corresponding to the specified proposition and
	 *         unscopedTerms.
	 */
	private ProbabilityTable probabilityOf(Predicate<Map<RandomVariable, Object>> proposition,
			List<RandomVariable> unscopedTerms) {
		List<RandomVariable> randomVariables = this.jointDistribution.getVariables();
		List<ProbabilityNumber> values = new ArrayList<ProbabilityNumber>(
				Collections.nCopies(this.jointDistribution.size(), this.probFactory.valueOf(BigDecimal.ZERO)));
		jointDistribution.stream().filter(proposition)
				.forEach(world -> values.set(jointDistribution.getIndex(world), jointDistribution.getValue(world)));
		List<RandomVariable> varsToMarginalize = ListOps.difference(randomVariables, unscopedTerms);
		ProbabilityTable newTable = new ProbabilityTable(randomVariables, values, clazz);
		ProbabilityTable result = newTable.marginalize(varsToMarginalize);
		return result;
	}
}
