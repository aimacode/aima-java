package aima.test.probreasoningtest;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Ravi Mohan
 * 
 */
public class ProbabilisticReasoningTests {
	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTest(new TestSuite(HMMAgentTest.class));
		suite.addTest(new TestSuite(HMMTest.class));
		suite.addTest(new TestSuite(ParticleFilterTest.class));
		suite.addTest(new TestSuite(RandomVariableTest.class));
		return suite;
	}

}
