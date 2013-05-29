package aima.core.probability.bayes.model;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import aima.core.probability.CategoricalDistribution;
import aima.core.probability.FiniteProbabilityModel;
import aima.core.probability.ProbabilityModel;
import aima.core.probability.RandomVariable;
import aima.core.probability.bayes.BayesInference;
import aima.core.probability.bayes.BayesianNetwork;
import aima.core.probability.bayes.exact.EnumerationAsk;
import aima.core.probability.proposition.AssignmentProposition;
import aima.core.probability.proposition.ConjunctiveProposition;
import aima.core.probability.proposition.Proposition;
import aima.core.probability.util.ProbUtil;
import aima.core.probability.util.ProbabilityTable;

/**
 * Very simple implementation of the FiniteProbabilityModel API using a Bayesian
 * Network, consisting of FiniteNodes, to represent the underlying model.<br>
 * <br>
 * <b>Note:</b> The implementation currently doesn't take advantage of the use
 * of evidence values when calculating posterior values using the provided
 * Bayesian Inference implementation (e.g enumerationAsk). Instead it simply
 * creates a joint distribution over the scope of the propositions (i.e. using
 * the inference implementation with just query variables corresponding to the
 * scope of the propositions) and then iterates over this to get the appropriate
 * probability values. A smarter version, in the general case, would need to
 * dynamically create deterministic nodes to represent the outcome of logical
 * and derived propositions (e.g. conjuncts and summations over variables) in
 * order to be able to correctly calculate using evidence values.
 * 
 * @author Ciaran O'Reilly
 */
public class FiniteBayesModel implements FiniteProbabilityModel {

	private BayesianNetwork bayesNet = null;
	private Set<RandomVariable> representation = new LinkedHashSet<RandomVariable>();
	private BayesInference bayesInference = null;

	public FiniteBayesModel(BayesianNetwork bn) {
		this(bn, new EnumerationAsk());
	}

	public FiniteBayesModel(BayesianNetwork bn, BayesInference bi) {
		if (null == bn) {
			throw new IllegalArgumentException(
					"Bayesian Network describing the model must be specified.");
		}
		this.bayesNet = bn;
		this.representation.addAll(bn.getVariablesInTopologicalOrder());
		setBayesInference(bi);
	}

	public BayesInference getBayesInference() {
		return bayesInference;
	}

	public void setBayesInference(BayesInference bi) {
		this.bayesInference = bi;
	}

	//
	// START-ProbabilityModel
	public boolean isValid() {
		// Handle rounding
		return Math.abs(1 - prior(representation
				.toArray(new Proposition[representation.size()]))) <= ProbabilityModel.DEFAULT_ROUNDING_THRESHOLD;
	}

	public double prior(Proposition... phi) {
		// Calculating the prior, therefore no relevant evidence
		// just query over the scope of proposition phi in order
		// to get a joint distribution for these
		final Proposition conjunct = ProbUtil.constructConjunction(phi);
		RandomVariable[] X = conjunct.getScope().toArray(
				new RandomVariable[conjunct.getScope().size()]);
		CategoricalDistribution d = bayesInference.ask(X,
				new AssignmentProposition[0], bayesNet);

		// Then calculate the probability of the propositions phi
		// be seeing where they hold.
		final double[] probSum = new double[1];
		CategoricalDistribution.Iterator di = new CategoricalDistribution.Iterator() {
			public void iterate(Map<RandomVariable, Object> possibleWorld,
					double probability) {
				if (conjunct.holds(possibleWorld)) {
					probSum[0] += probability;
				}
			}
		};
		d.iterateOver(di);

		return probSum[0];
	}

	public double posterior(Proposition phi, Proposition... evidence) {

		Proposition conjEvidence = ProbUtil.constructConjunction(evidence);

		// P(A | B) = P(A AND B)/P(B) - (13.3 AIMA3e)
		Proposition aAndB = new ConjunctiveProposition(phi, conjEvidence);
		double probabilityOfEvidence = prior(conjEvidence);
		if (0 != probabilityOfEvidence) {
			return prior(aAndB) / probabilityOfEvidence;
		}

		return 0;
	}

	public Set<RandomVariable> getRepresentation() {
		return representation;
	}

	// END-ProbabilityModel
	//

	//
	// START-FiniteProbabilityModel
	public CategoricalDistribution priorDistribution(Proposition... phi) {
		return jointDistribution(phi);
	}

	public CategoricalDistribution posteriorDistribution(Proposition phi,
			Proposition... evidence) {

		Proposition conjEvidence = ProbUtil.constructConjunction(evidence);

		// P(A | B) = P(A AND B)/P(B) - (13.3 AIMA3e)
		CategoricalDistribution dAandB = jointDistribution(phi, conjEvidence);
		CategoricalDistribution dEvidence = jointDistribution(conjEvidence);

		CategoricalDistribution rVal = dAandB.divideBy(dEvidence);
		// Note: Need to ensure normalize() is called
		// in order to handle the case where an approximate
		// algorithm is used (i.e. won't evenly divide
		// as will have calculated on separate approximate
		// runs). However, this should only be done
		// if the all of the evidences scope are bound (if not
		// you are returning in essence a set of conditional
		// distributions, which you do not want normalized).
		boolean unboundEvidence = false;
		for (Proposition e : evidence) {
			if (e.getUnboundScope().size() > 0) {
				unboundEvidence = true;
				break;
			}
		}
		if (!unboundEvidence) {
			rVal.normalize();
		}

		return rVal;
	}

	public CategoricalDistribution jointDistribution(
			Proposition... propositions) {
		ProbabilityTable d = null;
		final Proposition conjProp = ProbUtil
				.constructConjunction(propositions);
		final LinkedHashSet<RandomVariable> vars = new LinkedHashSet<RandomVariable>(
				conjProp.getUnboundScope());

		if (vars.size() > 0) {
			RandomVariable[] distVars = new RandomVariable[vars.size()];
			int i = 0;
			for (RandomVariable rv : vars) {
				distVars[i] = rv;
				i++;
			}

			final ProbabilityTable ud = new ProbabilityTable(distVars);
			final Object[] values = new Object[vars.size()];

			CategoricalDistribution.Iterator di = new CategoricalDistribution.Iterator() {

				public void iterate(Map<RandomVariable, Object> possibleWorld,
						double probability) {
					if (conjProp.holds(possibleWorld)) {
						int i = 0;
						for (RandomVariable rv : vars) {
							values[i] = possibleWorld.get(rv);
							i++;
						}
						int dIdx = ud.getIndex(values);
						ud.setValue(dIdx, ud.getValues()[dIdx] + probability);
					}
				}
			};

			RandomVariable[] X = conjProp.getScope().toArray(
					new RandomVariable[conjProp.getScope().size()]);
			bayesInference.ask(X, new AssignmentProposition[0], bayesNet)
					.iterateOver(di);

			d = ud;
		} else {
			// No Unbound Variables, therefore just return
			// the singular probability related to the proposition.
			d = new ProbabilityTable();
			d.setValue(0, prior(propositions));
		}
		return d;
	}

	// END-FiniteProbabilityModel
	//
}
