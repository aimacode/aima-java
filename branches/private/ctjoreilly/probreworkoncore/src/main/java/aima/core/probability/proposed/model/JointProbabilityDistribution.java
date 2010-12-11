package aima.core.probability.proposed.model;

import java.util.LinkedHashSet;

// TODO - document pg. 487
public class JointProbabilityDistribution {
	private LinkedHashSet<RandomVariable> variables = new LinkedHashSet<RandomVariable>();

	public JointProbabilityDistribution(RandomVariable... vars) {
		if (null == vars) {
			throw new IllegalArgumentException("Variables must be specified.");
		}
		for (RandomVariable v : vars) {
			if (v.getDomain().isInfinite()) {
				throw new IllegalArgumentException(
						"Cannot have an infinite domain for a variable in a Joint Probability Distribution:"
								+ v);
			}
			if (variables.contains(v)) {
				throw new IllegalArgumentException(
						"Duplicate Variable definitions not allowed in Joing distribution:"
								+ v);
			}
			variables.add(v);
		}
	}
	
	public void addEntry(double probability, Object... values) {
		
	}
}
