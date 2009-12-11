/*
 * Created on Jan 6, 2005
 *
 */
package aima.test.probabilitytest;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Ravi Mohan
 * 
 */

public class ProbabilityTests {
	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTest(new TestSuite(BayesNetTest.class));
		suite.addTest(new TestSuite(BayesNetNodeTest.class));
		suite.addTest(new TestSuite(EnumerationAskTest.class));
		suite.addTest(new TestSuite(EnumerationJointAskTest.class));
		suite.addTest(new TestSuite(ProbabilitySamplingTest.class));
		return suite;
	}

}
