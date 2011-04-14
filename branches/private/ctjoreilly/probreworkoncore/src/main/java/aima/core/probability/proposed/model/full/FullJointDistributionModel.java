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
import aima.core.probability.proposed.util.ProbUtil;

/**
 * An implementation of the FiniteProbabilityModel API using a full joint
 * distribution as the underlying model.
 * 
 * @author Ciaran O'Reilly
 */
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
		return probabilityOf(ProbUtil.constructConjunction(phi));
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

			distribution.iterateDistribution(di);

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
}
