package aima.extra.probability.bayes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import aima.extra.probability.ProbabilityComputation;
import aima.extra.probability.ProbabilityNumber;
import aima.extra.probability.RandomVariable;
import aima.extra.probability.factory.ProbabilityFactory;
import aima.extra.probability.proposition.Proposition;
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
	public ProbabilityNumber prior(Proposition phi) {
		ProbabilityNumber result = this.jointDistribution.getValue(phi.getStatement());
		return result;
	}

	@Override
	public ProbabilityNumber posterior(Proposition phi, Proposition evidence) {
		ProbabilityNumber probabilityOfPhiAndEvidence = this.jointDistribution.getValue(phi.getStatement().and(evidence.getStatement()));
		ProbabilityNumber probabilityOfEvidence = this.jointDistribution.getValue(evidence.getStatement());
		ProbabilityComputation compute = new ProbabilityComputation();
		ProbabilityNumber result = compute.div(probabilityOfPhiAndEvidence, probabilityOfEvidence);
		return result;
	}

	@Override
	public List<RandomVariable> getRepresentation() {
		return this.jointDistribution.getVariables();
	}

	// END-ProbabilityModel

	// START-FiniteProbabilityModel

	@Override
	public ProbabilityTable priorDistribution(Proposition phi) {
		ProbabilityTable result = probabilityOf(phi);
		return result;
	}

	@Override
	public CategoricalDistribution posteriorDistribution(Proposition phi, Proposition evidence) {
		List<RandomVariable> dividendRandomVariables = this.jointDistribution.getVariables();
		List<ProbabilityNumber> dividendValues = new ArrayList<ProbabilityNumber>(
				Collections.nCopies(this.jointDistribution.size(), this.probFactory.valueOf(BigDecimal.ZERO)));
		jointDistribution.stream().filter(phi.getStatement().and(evidence.getStatement())).forEach(
				world -> dividendValues.set(jointDistribution.getIndex(world), jointDistribution.getValue(world)));
		ProbabilityTable dividend = new ProbabilityTable(dividendRandomVariables, dividendValues, this.clazz);
		ProbabilityTable divisor = priorDistribution(evidence);
		ProbabilityTable posterior = dividend.divideBy(divisor);
		List<RandomVariable> varsToMarginalize = ListOps.difference(dividendRandomVariables,
				ListOps.union(phi.getUnscopedTerms(), evidence.getUnscopedTerms()));
		ProbabilityTable result = posterior.marginalize(varsToMarginalize);
		return result;
	}

	@Override
	public CategoricalDistribution jointDistribution(Proposition phi) {
		ProbabilityTable result = probabilityOf(phi);
		return result;
	}

	// END-FiniteProbabilityModel

	// Private methods

	/**
	 * Returns ProbabilityTable that satisfies the specified proposition.
	 * 
	 * @param proposition
	 *            for which the probability distribution is to be returned.
	 * 
	 * @return ProbabilityTable corresponding to the specified proposition and
	 *         unscopedTerms.
	 */
	private ProbabilityTable probabilityOf(Proposition phi) {
		List<RandomVariable> randomVariables = this.jointDistribution.getVariables();
		List<ProbabilityNumber> values = new ArrayList<ProbabilityNumber>(
				Collections.nCopies(this.jointDistribution.size(), this.probFactory.valueOf(BigDecimal.ZERO)));
		jointDistribution.stream().filter(phi.getStatement())
				.forEach(world -> values.set(jointDistribution.getIndex(world), jointDistribution.getValue(world)));
		List<RandomVariable> varsToMarginalize = ListOps.difference(randomVariables, phi.getUnscopedTerms());
		ProbabilityTable newTable = new ProbabilityTable(randomVariables, values, clazz);
		ProbabilityTable result = newTable.marginalize(varsToMarginalize);
		return result;
	}
}
