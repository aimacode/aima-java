package aima.gui.demo.learning;

import java.util.ArrayList;
import java.util.List;

import aima.core.learning.framework.DataSet;
import aima.core.learning.framework.DataSetFactory;
import aima.core.learning.framework.Learner;
import aima.core.learning.inductive.DLTestFactory;
import aima.core.learning.inductive.DecisionTree;
import aima.core.learning.learners.AdaBoostLearner;
import aima.core.learning.learners.DecisionListLearner;
import aima.core.learning.learners.DecisionTreeLearner;
import aima.core.learning.learners.StumpLearner;
import aima.core.learning.neural.BackPropLearning;
import aima.core.learning.neural.FeedForwardNeuralNetwork;
import aima.core.learning.neural.IrisDataSetNumerizer;
import aima.core.learning.neural.IrisNNDataSet;
import aima.core.learning.neural.NNConfig;
import aima.core.learning.neural.NNDataSet;
import aima.core.learning.neural.Numerizer;
import aima.core.learning.neural.Perceptron;
import aima.core.util.Util;

public class LearningDemo {
	public static void main(String[] args) {

		// For Reinforcement Learning Demos see Probability Demo
		decisionTreeDemo();
		decisionListDemo();
		ensembleLearningDemo();
		perceptronDemo();
		backPropogationDemo();
	}

	private static void decisionTreeDemo() {
		System.out.println(Util.ntimes("*", 100));
		System.out
				.println("\nDecisionTree Demo - Inducing a DecisionList from the Restaurant DataSet\n ");
		System.out.println(Util.ntimes("*", 100));
		try {
			DataSet ds = DataSetFactory.getRestaurantDataSet();
			DecisionTreeLearner learner = new DecisionTreeLearner();
			learner.train(ds);
			System.out.println("The Induced Decision Tree is ");
			System.out.println(learner.getDecisionTree());
			int[] result = learner.test(ds);

			System.out
					.println("\nThis Decision Tree classifies the data set with "
							+ result[0]
							+ " successes"
							+ " and "
							+ result[1]
							+ " failures");
			System.out.println("\n");
		} catch (Exception e) {
			System.out.println("Decision Tree Demo Failed  ");
			e.printStackTrace();
		}

	}

	private static void decisionListDemo() {
		try {
			System.out.println(Util.ntimes("*", 100));
			System.out
					.println("DecisionList Demo - Inducing a DecisionList from the Restaurant DataSet\n ");
			System.out.println(Util.ntimes("*", 100));
			DataSet ds = DataSetFactory.getRestaurantDataSet();
			DecisionListLearner learner = new DecisionListLearner("Yes", "No",
					new DLTestFactory());
			learner.train(ds);
			System.out.println("The Induced DecisionList is");
			System.out.println(learner.getDecisionList());
			int[] result = learner.test(ds);

			System.out
					.println("\nThis Decision List classifies the data set with "
							+ result[0]
							+ " successes"
							+ " and "
							+ result[1]
							+ " failures");
			System.out.println("\n");

		} catch (Exception e) {
			System.out.println("Decision ListDemo Failed");
		}
	}

	private static void ensembleLearningDemo() {
		System.out.println(Util.ntimes("*", 100));
		System.out
				.println("\n Ensemble Decision Demo - Weak Learners co operating to give Superior decisions ");
		System.out.println(Util.ntimes("*", 100));
		try {
			DataSet ds = DataSetFactory.getRestaurantDataSet();
			List<DecisionTree> stumps = DecisionTree.getStumpsFor(ds, "Yes", "No");
			List<Learner> learners = new ArrayList<Learner>();

			System.out
					.println("\nStump Learners vote to decide in this algorithm");
			for (Object stump : stumps) {
				DecisionTree sl = (DecisionTree) stump;
				StumpLearner stumpLearner = new StumpLearner(sl, "No");
				learners.add(stumpLearner);
			}
			AdaBoostLearner learner = new AdaBoostLearner(learners, ds);
			learner.train(ds);
			int[] result = learner.test(ds);
			System.out
					.println("\nThis Ensemble Learner  classifies the data set with "
							+ result[0]
							+ " successes"
							+ " and "
							+ result[1]
							+ " failures");
			System.out.println("\n");

		} catch (Exception e) {

		}

	}

	private static void perceptronDemo() {
		try {
			System.out.println(Util.ntimes("*", 100));
			System.out
					.println("\n Perceptron Demo - Running Perceptron on Iris data Set with 10 epochs of learning ");
			System.out.println(Util.ntimes("*", 100));
			DataSet irisDataSet = DataSetFactory.getIrisDataSet();
			Numerizer numerizer = new IrisDataSetNumerizer();
			NNDataSet innds = new IrisNNDataSet();

			innds.createExamplesFromDataSet(irisDataSet, numerizer);

			Perceptron perc = new Perceptron(3, 4);

			perc.trainOn(innds, 10);

			innds.refreshDataset();
			int[] result = perc.testOnDataSet(innds);
			System.out.println(result[0] + " right, " + result[1] + " wrong");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static void backPropogationDemo() {
		try {
			System.out.println(Util.ntimes("*", 100));
			System.out
					.println("\n BackpropagationDemo  - Running BackProp on Iris data Set with 10 epochs of learning ");
			System.out.println(Util.ntimes("*", 100));

			DataSet irisDataSet = DataSetFactory.getIrisDataSet();
			Numerizer numerizer = new IrisDataSetNumerizer();
			NNDataSet innds = new IrisNNDataSet();

			innds.createExamplesFromDataSet(irisDataSet, numerizer);

			NNConfig config = new NNConfig();
			config.setConfig(FeedForwardNeuralNetwork.NUMBER_OF_INPUTS, 4);
			config.setConfig(FeedForwardNeuralNetwork.NUMBER_OF_OUTPUTS, 3);
			config.setConfig(FeedForwardNeuralNetwork.NUMBER_OF_HIDDEN_NEURONS,
					6);
			config
					.setConfig(FeedForwardNeuralNetwork.LOWER_LIMIT_WEIGHTS,
							-2.0);
			config.setConfig(FeedForwardNeuralNetwork.UPPER_LIMIT_WEIGHTS, 2.0);

			FeedForwardNeuralNetwork ffnn = new FeedForwardNeuralNetwork(config);
			ffnn.setTrainingScheme(new BackPropLearning(0.1, 0.9));

			ffnn.trainOn(innds, 10);

			innds.refreshDataset();
			int[] result = ffnn.testOnDataSet(innds);
			System.out.println(result[0] + " right, " + result[1] + " wrong");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
