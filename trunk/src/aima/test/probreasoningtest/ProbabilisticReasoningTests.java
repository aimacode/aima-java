package aima.test.probreasoningtest;

import junit.framework.Test;
import junit.framework.TestSuite;

public class ProbabilisticReasoningTests {
	public static Test suite(){
		TestSuite suite = new TestSuite();
		suite.addTest(new TestSuite(HMMTest.class));
		return suite;
	}

}
