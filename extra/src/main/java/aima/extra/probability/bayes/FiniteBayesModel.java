package aima.extra.probability.bayes;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import aima.extra.probability.ProbabilityComputation;
import aima.extra.probability.ProbabilityNumber;
import aima.extra.probability.RandomVariable;
import aima.extra.probability.proposition.Proposition;

/**
 * Implementation of the FiniteProbabilityModel API using a Bayesian Network,
 * consisting of FiniteNodes, to represent the underlying model.
 * 
 * @author Ciaran O'Reilly
 * @author Nagaraj Poti
 */
public class FiniteBayesModel implements FiniteProbabilityModel {

	// Internal fields

	private final Predicate<Map<RandomVariable, Object>> tautology = mp -> true;
	
	private final Proposition tautologyProposition = new Proposition(tautology);
	
	private BayesianNetwork bayesNet;
	
	private BayesInference bayesInference;
	
	private List<RandomVariable> representation;

	// Constructors

    public FiniteBayesModel(BayesianNetwork bn, BayesInference bi) {
    	Objects.requireNonNull(bn, "Bayesian Network describing the model must be specified.");
    	Objects.requireNonNull(bi, "BayesInference model must be specified");
        this.bayesNet = bn;
        this.representation.addAll(bn.getVariablesInTopologicalOrder());
        setBayesInference(bi);
    }

	// START-ProbabilityModel
	
	@Override
	public boolean isValid() {
		boolean result = this.prior(tautologyProposition).isOne();
		return result;
	}

	@Override
	public ProbabilityNumber prior(Proposition phi) {
		CategoricalDistribution d = bayesInference.ask(phi, tautologyProposition, bayesNet);
		ProbabilityNumber result = d.getValue(tautology);
		return result;
	}

	@Override
	public ProbabilityNumber posterior(Proposition phi, Proposition evidence) {
		ProbabilityNumber probabilityOfPhiAndEvidence = this.prior(phi.and(evidence));
		ProbabilityNumber probabilityOfEvidence = this.prior(evidence);
		ProbabilityComputation compute = new ProbabilityComputation();
		ProbabilityNumber result = compute.div(probabilityOfPhiAndEvidence, probabilityOfEvidence);
		return result;
	}

	@Override
	public List<RandomVariable> getRepresentation() {
		return this.representation;
	}
	
	// END-ProbabilityModel
	
	// START-FiniteProbabilityModel

	@Override
	public ProbabilityTable priorDistribution(Proposition phi) {
		return jointDistribution(phi);
	}

	@Override
	public ProbabilityTable posteriorDistribution(Proposition phi, Proposition evidence) {
        ProbabilityTable dividend = jointDistribution(phi.and(evidence));
        ProbabilityTable divisor = jointDistribution(evidence);
        ProbabilityTable result = dividend.divideBy(divisor);
		return result;
	}

	@Override
	public ProbabilityTable jointDistribution(Proposition phi) {
		ProbabilityTable result = (ProbabilityTable) (bayesInference.ask(phi, tautologyProposition, bayesNet));
		return result;
	}
	
	// END-FiniteProbabilityModel
	
    public BayesInference getBayesInference() {
        return this.bayesInference;
    }

    public void setBayesInference(BayesInference bi) {
        this.bayesInference = bi;
    }

}
