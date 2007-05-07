package aima.test.probdecisiontest;

import junit.framework.Test;
import junit.framework.TestSuite;


public class ProbabilityDecisionTests {
    public static Test suite() {
	TestSuite suite = new TestSuite();
	suite.addTest(new TestSuite(CellWorldTest.class));
	//suite.addTest(new TestSuite(PolicyIterationTest.class));
	suite.addTest(new TestSuite(ValueIterationTest.class));
	return suite;
    }
}
