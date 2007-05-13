/*
 * Created on Aug 6, 2005
 *
 */
package aima.learning.demos;

import java.util.ArrayList;
import java.util.List;

import aima.learning.framework.DataSet;
import aima.learning.framework.DataSetFactory;
import aima.learning.framework.Learner;
import aima.learning.inductive.DLTestFactory;
import aima.learning.inductive.DecisionTree;
import aima.learning.learners.AdaBoostLearner;
import aima.learning.learners.DecisionListLearner;
import aima.learning.learners.DecisionTreeLearner;
import aima.learning.learners.NeuralNetLearner;
import aima.learning.learners.StumpLearner;
import aima.learning.statistics.FeedForwardNetwork;
import aima.learning.statistics.IrisDataSetNumerizer;
import aima.learning.statistics.PerceptronLearning;
import aima.learning.statistics.StandardBackPropogation;
import aima.probability.JavaRandomizer;
import aima.util.Util;

public class LearningDemo {
	public static void main(String[] args) {
		
		// For  Reinforcement Learning Demos see Probability Demo 
		decisionTreeDemo();
		decisionListDemo();
		ensembleLearningDemo();
		perceptronDemo();
		backPropogationDemo();
	}

	private static void decisionTreeDemo() {
		System.out.println(Util.ntimes("*",100));
		System.out
				.println("\nDecisionTree Demo - Inducing a DecisionList from the Restaurant DataSet\n ");
		System.out.println(Util.ntimes("*",100));
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
			System.out.println(Util.ntimes("*",100));
			System.out
					.println("DecisionList Demo - Inducing a DecisionList from the Restaurant DataSet\n ");
			System.out.println(Util.ntimes("*",100));
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
		System.out.println(Util.ntimes("*",100));
		System.out
				.println("\n Ensemble Decision Demo - Weak Learners co operating to give Superior decisions ");
		System.out.println(Util.ntimes("*",100));
		try {
			DataSet ds = DataSetFactory.getRestaurantDataSet();
			List stumps = DecisionTree.getStumpsFor(ds,"Yes",
					"No");
			List<Learner> learners = new ArrayList<Learner>();
			
			System.out.println("\nStump Learners vote to decide in this algorithm");
			for (Object stump: stumps){
				DecisionTree sl = (DecisionTree)stump;
				StumpLearner stumpLearner =  new StumpLearner(sl,"No");
				learners.add(stumpLearner);
			}
			AdaBoostLearner learner = new AdaBoostLearner(learners,ds);
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
		System.out.println(Util.ntimes("*",100));
		System.out.println("Perceptron Demo (Neural Net)");
		System.out.println(Util.ntimes("*",100));
		System.out.println("Trying to run Perception Learning on the Iris DataSet");
		System.out.println("The Network weights and biases are set up at random .So you may get a different result n some runs");
		try{
			DataSet ds = DataSetFactory.getIrisDataSet();
			FeedForwardNetwork network = new FeedForwardNetwork(4,3,new JavaRandomizer());
			NeuralNetLearner learner= new NeuralNetLearner(network,new IrisDataSetNumerizer(),new PerceptronLearning(),10);
			learner.train(ds);
			int[] result = learner.test(ds);
			
			System.out
			.println("\nThis Perceptron  classifies the data set with "
					+ result[0]
					+ " successes"
					+ " and "
					+ result[1]
					+ " failures");
			System.out.println("\n");
		}catch(Exception e){
			
		}
		
		

	}

	private static void backPropogationDemo() {
		System.out.println(Util.ntimes("*",100));
		System.out.println("BackPropogation  (Neural Net)");
		System.out.println(Util.ntimes("*",100));
		System.out.println("Trying to run BackPropogation  Learning on the Iris DataSet");
		System.out.println("The Network weights and biases are set up at random .So you may get a different result on some runs");
		try{
			DataSet ds = DataSetFactory.getIrisDataSet();
			FeedForwardNetwork network = new FeedForwardNetwork(4,4,3,new JavaRandomizer());
			NeuralNetLearner learner= new NeuralNetLearner(network,new IrisDataSetNumerizer(),new StandardBackPropogation(),1000);
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
		}catch(Exception e){
			System.out.println("exception");
			e.printStackTrace();
		}
		

	}

}
