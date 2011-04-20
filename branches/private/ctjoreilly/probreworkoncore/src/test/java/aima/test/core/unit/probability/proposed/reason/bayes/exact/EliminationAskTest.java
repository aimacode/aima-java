package aima.test.core.unit.probability.proposed.reason.bayes.exact;

import org.junit.Before;

import aima.core.probability.proposed.reason.bayes.exact.EliminationAsk;

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
