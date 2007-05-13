/*
 * Created on Apr 13, 2005
 *
 */
package aima.test.learningtest;

import junit.framework.Test;
import junit.framework.TestSuite;

public class LearningTests {
	public static Test suite(){
		TestSuite suite = new TestSuite();
		suite.addTest(new TestSuite(DataSetTest.class));
		suite.addTest(new TestSuite(DLTestTestCase.class));
		suite.addTest(new TestSuite(DecisionListTest.class));
		suite.addTest(new TestSuite(DecisionTreeTest.class));
		suite.addTest(new TestSuite(EnsembleLearningTest.class));
		suite.addTest(new TestSuite(InformationAndGainTest.class));
		suite.addTest(new TestSuite(LearnerTests.class));
		suite.addTest(new TestSuite(NeuralNetworkTest.class));
		suite.addTest(new TestSuite(QTableTest.class));
		suite.addTest(new TestSuite(ReinforcementLearningTest.class));
		return suite;
	}
}
