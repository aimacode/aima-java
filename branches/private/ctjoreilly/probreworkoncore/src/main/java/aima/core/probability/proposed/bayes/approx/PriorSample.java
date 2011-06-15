package aima.core.probability.proposed.bayes.approx;

import java.util.LinkedHashMap;
import java.util.Map;

import aima.core.probability.proposed.RandomVariable;
import aima.core.probability.proposed.bayes.BayesianNetwork;
import aima.core.probability.proposed.bayes.Node;
import aima.core.util.JavaRandomizer;
import aima.core.util.Randomizer;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 531.<br>
 * <br>
 * 
 * <pre>
 * function PRIOR-SAMPLE(bn) returns an event sampled from the prior specified by bn
 *   inputs: bn, a Bayesian network specifying joint distribution <b>P</b>(X<sub>1</sub>,...,X<sub>n</sub>)
 * 
 *   x <- an event with n elements
 *   foreach variable X<sub>i</sub> in X<sub>1</sub>,...,X<sub>n</sub> do
 *      x[i] <- a random sample from <b>P</b>(X<sub>i</sub> | parents(X<sub>i</sub>))
 *   return x
 * </pre>
 * 
 * Figure 14.13 A sampling algorithm that generates events from a Bayesian
 * network. Each variable is sampled according to the conditional distribution
 * given the values already sampled for the variable's parents.
 * 
 * @author Ciaran O'Reilly
 * @author Ravi Mohan
 */
public class PriorSample {

	private Randomizer randomizer = null;

	public PriorSample() {
		this(new JavaRandomizer());
	}

	public PriorSample(Randomizer r) {
		this.randomizer = r;
	}

	// function PRIOR-SAMPLE(bn) returns an event sampled from the prior
	// specified by bn
	/**
	 * The PRIOR-SAMPLE algorithm in Figure 14.13. A sampling algorithm that
	 * generates events from a Bayesian network. Each variable is sampled
	 * according to the conditional distribution given the values already
	 * sampled for the variable's parents.
	 * 
	 * @param bn
	 *            a Bayesian network specifying joint distribution
	 *            <b>P</b>(X<sub>1</sub>,...,X<sub>n</sub>)
	 * @return an event sampled from the prior specified by bn
	 */
	public Map<RandomVariable, Object> priorSample(BayesianNetwork bn) {
		// x <- an event with n elements
		Map<RandomVariable, Object> x = new LinkedHashMap<RandomVariable, Object>();
		// foreach variable X<sub>i</sub> in X<sub>1</sub>,...,X<sub>n</sub> do
		for (RandomVariable Xi : bn.getVariablesInTopologicalOrder()) {
			// x[i] <- a random sample from
			// <b>P</b>(X<sub>i</sub> | parents(X<sub>i</sub>))
			x.put(Xi, randomSample(Xi, x, bn));
		}
		// return x
		return x;
	}

	//
	// PRIVATE METHODS
	//
	private Object randomSample(RandomVariable Xi,
			Map<RandomVariable, Object> eventx, BayesianNetwork bn) {
		Node n = bn.getNode(Xi);
		Object[] parentValues = new Object[n.getParents().size()];
		int i = 0;
		for (Node pn : n.getParents()) {
			parentValues[i] = eventx.get(pn.getRandomVariable());
			i++;
		}

		return n.getCPD().getSample(randomizer.nextDouble(), parentValues);
	}
}
