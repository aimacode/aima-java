package aima.core.probability.proposed.model.impl.full;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import aima.core.probability.proposed.model.Distribution;
import aima.core.probability.proposed.model.FiniteProbabilityModel;
import aima.core.probability.proposed.model.RandomVariable;
import aima.core.probability.proposed.model.domain.FiniteDiscreteDomain;
import aima.core.probability.proposed.model.proposition.ConjunctiveProposition;
import aima.core.probability.proposed.model.proposition.Proposition;
import aima.core.probability.proposed.model.proposition.RandomVariableProposition;
import aima.core.probability.proposed.model.proposition.TermProposition;
import aima.core.util.math.MixedRadixNumber;

public class FullJointDistribution implements FiniteProbabilityModel {
	
	private Distribution distribution = null;
	private Set<RandomVariable> representation = null;
	//
	private List<RandomVariable> randomVars = new ArrayList<RandomVariable>();
	private List<List<Object>> domains = new ArrayList<List<Object>>();
	private int[] radixs = null;
	
	public FullJointDistribution(double[] values, RandomVariable... vars) {
		if (null == vars) {
			throw new IllegalArgumentException("Random Variables describing the model's representation of the World need to be specified.");
		}
		TermProposition[] tvars = new TermProposition[vars.length];
		for (int i = 0; i < vars.length; i++) {
			tvars[i] = new RandomVariableProposition(vars[i]);
		}
		distribution = new Distribution(values, tvars);
		
		representation = new LinkedHashSet<RandomVariable>();
		for (int i = 0; i < vars.length; i++) {
			representation.add(vars[i]);
			randomVars.add(vars[i]);
			domains.add(new ArrayList<Object>(((FiniteDiscreteDomain)vars[i].getDomain()).getPossibleValues()));
		}
		representation = Collections.unmodifiableSet(representation);
		
		radixs = new int[domains.size()];
		for (int i = 0; i < domains.size(); i++) {
			radixs[i] = domains.get(i).size();
		}
	}

	//
	// START-ProbabilityModel
	public boolean isValid() {
		// Handle rounding
		return Math.abs(1 - distribution.getSum()) < 1e-8;
	}
	
	public double prior(Proposition phi) {
		return probabilityOf(phi);
	}
	
	public double posterior(Proposition phi, Proposition... evidence) {
		
		Proposition conjEvidence = constructConjunction(evidence, 0);
		
		// P(X | Y) = P(A AND B)/P(B)
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
	public Distribution priorDistribution(TermProposition phi) {
		return jointProbabilityDistribution(phi);
	}
	
	public Distribution posteriorDistribution(TermProposition phi,
			TermProposition... evidence) {
		return null; // TODO
	}
	
	public Distribution jointProbabilityDistribution(TermProposition... propositions) {
		return null; // TODO
	}
	// END-FiniteProbabilityModel
	//
	
	//
	// PRIVATE METHODS
	//
	private double probabilityOf(Proposition phi) {
		double prob = 0;
		
		Map<RandomVariable, Object> possibleWorld = new LinkedHashMap<RandomVariable, Object>();
		Object[] values = new Object[randomVars.size()];
		MixedRadixNumber mrn = new MixedRadixNumber(0, radixs);
		do {
			for (int i = 0; i < randomVars.size(); i++) {
				Object val = domains.get(i).get(mrn.getCurrentNumeralValue(i));
				possibleWorld.put(randomVars.get(i), val);
				values[i] = val;
 			}
			if (phi.matches(possibleWorld)) {
				prob += distribution.getValues()[mrn.intValue()];
			}
		} while (mrn.increment());
		
		return prob;
	}
	
	private Proposition constructConjunction(Proposition[] props, int idx) {
		if ((idx+1) == props.length) {
			return props[idx];
		}
		
		return new ConjunctiveProposition(props[idx], constructConjunction(props, idx+1));
	}
}
