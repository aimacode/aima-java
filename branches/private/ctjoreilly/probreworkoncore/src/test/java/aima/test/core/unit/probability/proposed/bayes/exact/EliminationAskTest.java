package aima.test.core.unit.probability.proposed.bayes.exact;

import org.junit.Before;

import aima.core.probability.proposed.bayes.exact.EliminationAsk;

/**
 * 
 * @author Ciaran O'Reilly
 */
public class EliminationAskTest extends BayesianInferenceTest {
	
	@Before
	public void setUp() {
		bayesInference = new EliminationAsk();
	}
}
