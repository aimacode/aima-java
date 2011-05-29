package aima.test.core.unit.probability.proposed.bayes.exact;

import org.junit.Before;

import aima.core.probability.proposed.bayes.exact.EnumerationAsk;

/**
 * 
 * @author Ciaran O'Reilly
 * @author Ravi Mohan
 */
public class EnumerationAskTest extends BayesianInferenceTest {
	
	@Before
	public void setUp() {
		bayesInference = new EnumerationAsk();
	}
}
