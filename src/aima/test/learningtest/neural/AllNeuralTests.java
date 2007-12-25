package aima.test.learningtest.neural;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllNeuralTests {
	public static Test suite() {
		TestSuite suite = new TestSuite("All tests for NN Implementation");

		suite.addTest(new TestSuite(BackPropagationTests.class));
		suite.addTest(new TestSuite(LayerTests.class));
		suite.addTest(new TestSuite(DataSetTests.class));

		return suite;
	}
}
