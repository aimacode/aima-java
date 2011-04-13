package aima.core.probability.proposed.model.bayes;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import aima.core.probability.proposed.model.Distribution;
import aima.core.probability.proposed.model.FiniteProbabilityModel;
import aima.core.probability.proposed.model.ProbabilityModel;
import aima.core.probability.proposed.model.RandomVariable;
import aima.core.probability.proposed.model.proposition.AssignmentProposition;
import aima.core.probability.proposed.model.proposition.ConjunctiveProposition;
import aima.core.probability.proposed.model.proposition.Proposition;
import aima.core.probability.proposed.reason.bayes.exact.EnumerationAsk;
import aima.core.probability.proposed.util.ProbUtil;

public class FiniteBayesModel implements FiniteProbabilityModel {

	private BayesianNetwork bayesNet = null;
	private Set<RandomVariable> representation = new LinkedHashSet<RandomVariable>();
	private BayesInference bayesInference = new EnumerationAsk();

	public FiniteBayesModel(BayesianNetwork bn) {
		if (null == bn) {
			throw new IllegalArgumentException(
					"Bayesian Network describing the model must be specified.");
		}
		this.bayesNet = bn;
		this.representation.addAll(bn.getVariablesInTopologicalOrder());
	}

	//
	// START-ProbabilityModel
	public boolean isValid() {
		// Handle rounding
		return Math.abs(1 - prior(representation
				.toArray(new Proposition[representation.size()]))) <= ProbabilityModel.DEFAULT_ROUNDING_THRESHOLD;
	}

	public double prior(Proposition... phi) {
		return probabilityOf(ProbUtil.constructConjunction(phi)).getSum();
	}

	public double posterior(Proposition phi, Proposition... evidence) {

		Proposition conjEvidence = ProbUtil.constructConjunction(evidence);

		// P(A | B) = P(A AND B)/P(B) - (13.3 AIMA3e)
		Proposition aAndB = new ConjunctiveProposition(phi, conjEvidence);
		double probabilityOfEvidence = probabilityOf(conjEvidence).getSum();
		if (0 != probabilityOfEvidence) {
			return probabilityOf(aAndB).getSum() / probabilityOfEvidence;
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
		return probabilityOf(ProbUtil.constructConjunction(propositions));
	}

	// END-FiniteProbabilityModel
	//

	//
	// PRIVATE METHODS
	//
	private Distribution probabilityOf(final Proposition phi) {
		// TODO - construct deterministic nodes
		BayesianNetwork bn = bayesNet;

		RandomVariable[] X = phi.getUnboundScope().toArray(
				new RandomVariable[phi.getUnboundScope().size()]);

		// TODO - construct
		AssignmentProposition[] observedEvidence = new AssignmentProposition[0];

		return bayesInference.ask(X, observedEvidence, bn);
	}
}
