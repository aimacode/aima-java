package aima.test.learningtest;

import aima.learning.framework.DataSet;
import aima.learning.framework.DataSetFactory;
import aima.learning.learners.NeuralNetLearner;
import aima.learning.statistics.FeedForwardNetwork;
import aima.learning.statistics.IrisDataSetNumerizer;
import aima.learning.statistics.StandardBackPropogation;
import aima.probability.JavaRandomizer;
import aima.util.Util;
import junit.framework.TestCase;

/**
 * @author Ravi Mohan
 * 
 */
public class WeirdNanTest extends TestCase {
	public void testNan() {
		System.out.println(Util.ntimes("*", 100));
		System.out.println("BackPropogation  (Neural Net)");
		System.out.println(Util.ntimes("*", 100));
		System.out
				.println("Trying to run BackPropogation  Learning on the Iris DataSet");
		System.out
				.println("The Network weights and biases are set up at random .So you may get a different result on some runs");
		try {
			DataSet ds = DataSetFactory.getIrisDataSet();
			FeedForwardNetwork network = new FeedForwardNetwork(4, 4, 3,
					new JavaRandomizer());
			NeuralNetLearner learner = new NeuralNetLearner(network,
					new IrisDataSetNumerizer(), new StandardBackPropogation(),
					1000);
			learner.train(ds);
			int[] result = learner.test(ds);

			System.out
					.println("\nThis BackPropogation Network  classifies the data set with "
							+ result[0]
							+ " successes"
							+ " and "
							+ result[1]
							+ " failures");
			System.out.println("\n");
		} catch (Exception e) {
			System.out.println("exception");
			e.printStackTrace();
		}
	}
}
