package aima.core.probability.proposed;

import aima.core.probability.proposed.model.JointProbabilityDistribution;
import aima.core.probability.proposed.model.RandomVariable;
import aima.core.probability.proposed.model.domain.BooleanDomain;

/**
 * 
 * @author oreilly
 */
public class ProbabilityDemo {

	public static void main(String[] args) {
		JointProbabilityDistribution jpd = new JointProbabilityDistribution(new RandomVariable("X", new BooleanDomain()));
		jpd.addEntry(1.0, Boolean.TRUE);
	}
}
