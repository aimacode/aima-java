package aima.core.probability.proposed.bayes;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import aima.core.probability.proposed.Distribution;
import aima.core.probability.proposed.FiniteProbabilityModel;
import aima.core.probability.proposed.ProbabilityModel;
import aima.core.probability.proposed.RandomVariable;
import aima.core.probability.proposed.bayes.exact.EnumerationAsk;
import aima.core.probability.proposed.proposition.AssignmentProposition;
import aima.core.probability.proposed.proposition.ConjunctiveProposition;
import aima.core.probability.proposed.proposition.Proposition;
import aima.core.probability.proposed.util.ProbUtil;

/**
 * Very simple implementation of the FiniteProbabilityModel API using a Bayesian
 * Network to represent the underlying model.<br>
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
		Distribution d = bayesInference.ask(X, new AssignmentProposition[0],
				bayesNet);

		// Then calculate the probability of the propositions phi
		// be seeing where they hold.
		Distribution.Iterator di = new Distribution.Iterator() {
			private double probSum = 0;

			public void iterate(Map<RandomVariable, Object> possibleWorld,
					double probability) {
				if (conjunct.holds(possibleWorld)) {
					probSum += probability;
				}
			}

			public Object getPostIterateValue() {
				return probSum;
			}
		};
		d.iterateDistribution(di);

		return ((Double) di.getPostIterateValue()).doubleValue();
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
	public Distribution priorDistribution(Proposition... phi) {
		return jointDistribution(phi);
	}

	public Distribution posteriorDistribution(Proposition phi,
			Proposition... evidence) {

		Proposition conjEvidence = ProbUtil.constructConjunction(evidence);

		// P(A | B) = P(A AND B)/P(B) - (13.3 AIMA3e)
		Distribution dAandB = jointDistribution(phi, conjEvidence);
		Distribution dEvidence = jointDistribution(conjEvidence);

		return dAandB.divideBy(dEvidence);
	}

	public Distribution jointDistribution(Proposition... propositions) {
		Distribution d = null;
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

			final Distribution ud = new Distribution(distVars);
			final Object[] values = new Object[vars.size()];

			Distribution.Iterator di = new Distribution.Iterator() {

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

				public Object getPostIterateValue() {
					return null; // N/A
				}
			};

			RandomVariable[] X = conjProp.getScope().toArray(
					new RandomVariable[conjProp.getScope().size()]);
			bayesInference.ask(X, new AssignmentProposition[0], bayesNet)
					.iterateDistribution(di);

			d = ud;
		} else {
			// No Unbound Variables, therefore just return
			// the singular probability related to the proposition.
			d = new Distribution();
			d.setValue(0, prior(propositions));
		}
		return d;
	}

	// END-FiniteProbabilityModel
	//
}
