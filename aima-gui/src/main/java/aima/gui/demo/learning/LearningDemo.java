package aima.gui.demo.learning;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import aima.core.environment.cellworld.Cell;
import aima.core.environment.cellworld.CellWorld;
import aima.core.environment.cellworld.CellWorldAction;
import aima.core.environment.cellworld.CellWorldFactory;
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
import aima.core.learning.reinforcement.agent.PassiveADPAgent;
import aima.core.learning.reinforcement.agent.PassiveTDAgent;
import aima.core.learning.reinforcement.agent.QLearningAgent;
import aima.core.learning.reinforcement.agent.ReinforcementAgent;
import aima.core.learning.reinforcement.example.CellWorldEnvironment;
import aima.core.probability.example.MDPFactory;
import aima.core.probability.mdp.impl.ModifiedPolicyEvaluation;
import aima.core.util.JavaRandomizer;
import aima.core.util.Util;

public class LearningDemo {
	public static void main(String[] args) {

		// Chapter 18
		decisionTreeDemo();
		decisionListDemo();
		ensembleLearningDemo();
		perceptronDemo();
		backPropogationDemo();

		// Chapter 21
		passiveADPAgentDemo();
		passiveTDAgentDemo();
		qLearningAgentDemo();
	}

	public static void decisionTreeDemo() {
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

	public static void decisionListDemo() {
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

	public static void ensembleLearningDemo() {
		System.out.println(Util.ntimes("*", 100));
		System.out
				.println("\n Ensemble Decision Demo - Weak Learners co operating to give Superior decisions ");
		System.out.println(Util.ntimes("*", 100));
		try {
			DataSet ds = DataSetFactory.getRestaurantDataSet();
			List<DecisionTree> stumps = DecisionTree.getStumpsFor(ds, "Yes",
					"No");
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
			e.printStackTrace();

		}
	}

	public static void perceptronDemo() {
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
			e.printStackTrace();
		}

	}

	public static void backPropogationDemo() {
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
			config.setConfig(FeedForwardNeuralNetwork.LOWER_LIMIT_WEIGHTS, -2.0);
			config.setConfig(FeedForwardNeuralNetwork.UPPER_LIMIT_WEIGHTS, 2.0);

			FeedForwardNeuralNetwork ffnn = new FeedForwardNeuralNetwork(config);
			ffnn.setTrainingScheme(new BackPropLearning(0.1, 0.9));

			ffnn.trainOn(innds, 10);

			innds.refreshDataset();
			int[] result = ffnn.testOnDataSet(innds);
			System.out.println(result[0] + " right, " + result[1] + " wrong");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void passiveADPAgentDemo() {
		System.out.println("=======================");
		System.out.println("DEMO: Passive-ADP-Agent");
		System.out.println("=======================");
		System.out.println("Figure 21.3");
		System.out.println("-----------");

		CellWorld<Double> cw = CellWorldFactory.createCellWorldForFig17_1();
		CellWorldEnvironment cwe = new CellWorldEnvironment(
				cw.getCellAt(1, 1),
				cw.getCells(),
				MDPFactory.createTransitionProbabilityFunctionForFigure17_1(cw),
				new JavaRandomizer());

		Map<Cell<Double>, CellWorldAction> fixedPolicy = new HashMap<Cell<Double>, CellWorldAction>();
		fixedPolicy.put(cw.getCellAt(1, 1), CellWorldAction.Up);
		fixedPolicy.put(cw.getCellAt(1, 2), CellWorldAction.Up);
		fixedPolicy.put(cw.getCellAt(1, 3), CellWorldAction.Right);
		fixedPolicy.put(cw.getCellAt(2, 1), CellWorldAction.Left);
		fixedPolicy.put(cw.getCellAt(2, 3), CellWorldAction.Right);
		fixedPolicy.put(cw.getCellAt(3, 1), CellWorldAction.Left);
		fixedPolicy.put(cw.getCellAt(3, 2), CellWorldAction.Up);
		fixedPolicy.put(cw.getCellAt(3, 3), CellWorldAction.Right);
		fixedPolicy.put(cw.getCellAt(4, 1), CellWorldAction.Left);

		PassiveADPAgent<Cell<Double>, CellWorldAction> padpa = new PassiveADPAgent<Cell<Double>, CellWorldAction>(
				fixedPolicy, cw.getCells(), cw.getCellAt(1, 1),
				MDPFactory.createActionsFunctionForFigure17_1(cw),
				new ModifiedPolicyEvaluation<Cell<Double>, CellWorldAction>(10,
						1.0));

		cwe.addAgent(padpa);

		output_utility_learning_rates(padpa, 20, 100, 100, 1);

		System.out.println("=========================");
	}

	public static void passiveTDAgentDemo() {
		System.out.println("======================");
		System.out.println("DEMO: Passive-TD-Agent");
		System.out.println("======================");
		System.out.println("Figure 21.5");
		System.out.println("-----------");

		CellWorld<Double> cw = CellWorldFactory.createCellWorldForFig17_1();
		CellWorldEnvironment cwe = new CellWorldEnvironment(
				cw.getCellAt(1, 1),
				cw.getCells(),
				MDPFactory.createTransitionProbabilityFunctionForFigure17_1(cw),
				new JavaRandomizer());

		Map<Cell<Double>, CellWorldAction> fixedPolicy = new HashMap<Cell<Double>, CellWorldAction>();
		fixedPolicy.put(cw.getCellAt(1, 1), CellWorldAction.Up);
		fixedPolicy.put(cw.getCellAt(1, 2), CellWorldAction.Up);
		fixedPolicy.put(cw.getCellAt(1, 3), CellWorldAction.Right);
		fixedPolicy.put(cw.getCellAt(2, 1), CellWorldAction.Left);
		fixedPolicy.put(cw.getCellAt(2, 3), CellWorldAction.Right);
		fixedPolicy.put(cw.getCellAt(3, 1), CellWorldAction.Left);
		fixedPolicy.put(cw.getCellAt(3, 2), CellWorldAction.Up);
		fixedPolicy.put(cw.getCellAt(3, 3), CellWorldAction.Right);
		fixedPolicy.put(cw.getCellAt(4, 1), CellWorldAction.Left);

		PassiveTDAgent<Cell<Double>, CellWorldAction> ptda = new PassiveTDAgent<Cell<Double>, CellWorldAction>(
				fixedPolicy, 0.2, 1.0);

		cwe.addAgent(ptda);

		output_utility_learning_rates(ptda, 20, 500, 100, 1);

		System.out.println("=========================");
	}

	public static void qLearningAgentDemo() {
		System.out.println("======================");
		System.out.println("DEMO: Q-Learning-Agent");
		System.out.println("======================");

		CellWorld<Double> cw = CellWorldFactory.createCellWorldForFig17_1();
		CellWorldEnvironment cwe = new CellWorldEnvironment(
				cw.getCellAt(1, 1),
				cw.getCells(),
				MDPFactory.createTransitionProbabilityFunctionForFigure17_1(cw),
				new JavaRandomizer());

		QLearningAgent<Cell<Double>, CellWorldAction> qla = new QLearningAgent<Cell<Double>, CellWorldAction>(
				MDPFactory.createActionsFunctionForFigure17_1(cw),
				CellWorldAction.None, 0.2, 1.0, 5,
				2.0);

		cwe.addAgent(qla);

		output_utility_learning_rates(qla, 20, 10000, 500, 20);

		System.out.println("=========================");
	}

	//
	// PRIVATE METHODS
	//
	private static void output_utility_learning_rates(
			ReinforcementAgent<Cell<Double>, CellWorldAction> reinforcementAgent,
			int numRuns, int numTrialsPerRun, int rmseTrialsToReport,
			int reportEveryN) {

		if (rmseTrialsToReport > (numTrialsPerRun / reportEveryN)) {
			throw new IllegalArgumentException(
					"Requesting to report too many RMSE trials, max allowed for args is "
							+ (numTrialsPerRun / reportEveryN));
		}

		CellWorld<Double> cw = CellWorldFactory.createCellWorldForFig17_1();
		CellWorldEnvironment cwe = new CellWorldEnvironment(
				cw.getCellAt(1, 1),
				cw.getCells(),
				MDPFactory.createTransitionProbabilityFunctionForFigure17_1(cw),
				new JavaRandomizer());

		cwe.addAgent(reinforcementAgent);

		Map<Integer, List<Map<Cell<Double>, Double>>> runs = new HashMap<Integer, List<Map<Cell<Double>, Double>>>();
		for (int r = 0; r < numRuns; r++) {
			reinforcementAgent.reset();
			List<Map<Cell<Double>, Double>> trials = new ArrayList<Map<Cell<Double>, Double>>();
			for (int t = 0; t < numTrialsPerRun; t++) {
				cwe.executeTrial();
				if (0 == t % reportEveryN) {
					Map<Cell<Double>, Double> u = reinforcementAgent
							.getUtility();
					if (null == u.get(cw.getCellAt(1, 1))) {
						throw new IllegalStateException(
								"Bad Utility State Encountered: r=" + r
										+ ", t=" + t + ", u=" + u);
					}
					trials.add(u);
				}
			}
			runs.put(r, trials);
		}

		StringBuilder v4_3 = new StringBuilder();
		StringBuilder v3_3 = new StringBuilder();
		StringBuilder v1_3 = new StringBuilder();
		StringBuilder v1_1 = new StringBuilder();
		StringBuilder v3_2 = new StringBuilder();
		StringBuilder v2_1 = new StringBuilder();
		for (int t = 0; t < (numTrialsPerRun / reportEveryN); t++) {
			// Use the last run
			Map<Cell<Double>, Double> u = runs.get(numRuns - 1).get(t);
			v4_3.append((u.containsKey(cw.getCellAt(4, 3)) ? u.get(cw
					.getCellAt(4, 3)) : 0.0) + "\t");
			v3_3.append((u.containsKey(cw.getCellAt(3, 3)) ? u.get(cw
					.getCellAt(3, 3)) : 0.0) + "\t");
			v1_3.append((u.containsKey(cw.getCellAt(1, 3)) ? u.get(cw
					.getCellAt(1, 3)) : 0.0) + "\t");
			v1_1.append((u.containsKey(cw.getCellAt(1, 1)) ? u.get(cw
					.getCellAt(1, 1)) : 0.0) + "\t");
			v3_2.append((u.containsKey(cw.getCellAt(3, 2)) ? u.get(cw
					.getCellAt(3, 2)) : 0.0) + "\t");
			v2_1.append((u.containsKey(cw.getCellAt(2, 1)) ? u.get(cw
					.getCellAt(2, 1)) : 0.0) + "\t");
		}

		StringBuilder rmseValues = new StringBuilder();
		for (int t = 0; t < rmseTrialsToReport; t++) {
			// Calculate the Root Mean Square Error for utility of 1,1
			// for this trial# across all runs
			double xSsquared = 0;
			for (int r = 0; r < numRuns; r++) {
				Map<Cell<Double>, Double> u = runs.get(r).get(t);
				Double val1_1 = u.get(cw.getCellAt(1, 1));
				if (null == val1_1) {
					throw new IllegalStateException(
							"U(1,1,) is not present: r=" + r + ", t=" + t
									+ ", runs.size=" + runs.size()
									+ ", runs(r).size()=" + runs.get(r).size()
									+ ", u=" + u);
				}
				xSsquared += Math.pow(0.705 - val1_1, 2);
			}
			double rmse = Math.sqrt(xSsquared / runs.size());
			rmseValues.append(rmse);
			rmseValues.append("\t");
		}

		System.out
				.println("Note: You may copy and paste the following lines into a spreadsheet to generate graphs of learning rate and RMS error in utility:");
		System.out.println("(4,3)" + "\t" + v4_3);
		System.out.println("(3,3)" + "\t" + v3_3);
		System.out.println("(1,3)" + "\t" + v1_3);
		System.out.println("(1,1)" + "\t" + v1_1);
		System.out.println("(3,2)" + "\t" + v3_2);
		System.out.println("(2,1)" + "\t" + v2_1);
		System.out.println("RMSeiu" + "\t" + rmseValues);
	}
}
