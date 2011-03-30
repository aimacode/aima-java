package aima.core.probability.proposed.model.full;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import aima.core.probability.proposed.model.Distribution;
import aima.core.probability.proposed.model.FiniteProbabilityModel;
import aima.core.probability.proposed.model.ProbabilityModel;
import aima.core.probability.proposed.model.RandomVariable;
import aima.core.probability.proposed.model.proposition.ConjunctiveProposition;
import aima.core.probability.proposed.model.proposition.Proposition;
import aima.core.probability.proposed.model.proposition.TermProposition;

public class FullJointDistributionModel implements FiniteProbabilityModel {

	private Distribution distribution = null;
	private Set<RandomVariable> representation = null;
	//
	private List<RandomVariable> randomVars = new ArrayList<RandomVariable>();

	public FullJointDistributionModel(double[] values, RandomVariable... vars) {
		if (null == vars) {
			throw new IllegalArgumentException(
					"Random Variables describing the model's representation of the World need to be specified.");
		}

		distribution = new Distribution(values, vars);

		representation = new LinkedHashSet<RandomVariable>();
		for (int i = 0; i < vars.length; i++) {
			representation.add(vars[i]);
			randomVars.add(vars[i]);
		}
		representation = Collections.unmodifiableSet(representation);
	}

	//
	// START-ProbabilityModel
	public boolean isValid() {
		// Handle rounding
		return Math.abs(1 - distribution.getSum()) <= ProbabilityModel.DEFAULT_ROUNDING_THRESHOLD;
	}

	public double prior(Proposition... phi) {
		return probabilityOf(constructConjunction(phi, 0));
	}

	public double posterior(Proposition phi, Proposition... evidence) {

		Proposition conjEvidence = constructConjunction(evidence, 0);

		// P(X | Y) = P(A AND B)/P(B) - (13.3 AIMA3e)
		Proposition aAndB = new ConjunctiveProposition(phi, conjEvidence);
		double probabilityAandB = probabilityOf(aAndB);
		if (0 != probabilityAandB) {
			return probabilityAandB / probabilityOf(conjEvidence);
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
	public Distribution priorDistribution(TermProposition... phi) {
		return jointDistribution(phi);
	}

	public Distribution posteriorDistribution(TermProposition phi,
			TermProposition... evidence) {
		return null; // TODO
	}

	public Distribution jointDistribution(TermProposition... propositions) {
		return null; // TODO
	}

	// END-FiniteProbabilityModel
	//

	//
	// PRIVATE METHODS
	//
	private double probabilityOf(final Proposition phi) {
		Distribution.Iterator di = new Distribution.Iterator() {
			private double probSum = 0;

			public void iterate(Map<RandomVariable, Object> possibleWorld,
					double probability) {
				if (phi.holds(possibleWorld)) {
					probSum += probability;
				}
			}

			public Object getPostIterateValue() {
				return probSum;
			}
		};

		distribution.iterateDistribution(di);

		return ((Double) di.getPostIterateValue()).doubleValue();
	}

	private Proposition constructConjunction(Proposition[] props, int idx) {
		if ((idx + 1) == props.length) {
			return props[idx];
		}

		return new ConjunctiveProposition(props[idx], constructConjunction(
				props, idx + 1));
	}
}
