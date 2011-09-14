package aima.test.core.unit.learning.reinforcement.agent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import aima.core.environment.cellworld.Cell;
import aima.core.environment.cellworld.CellWorld;
import aima.core.environment.cellworld.CellWorldAction;
import aima.core.environment.cellworld.CellWorldFactory;
import aima.core.learning.reinforcement.agent.ReinforcementAgent;
import aima.core.learning.reinforcement.example.CellWorldEnvironment;
import aima.core.probability.example.MDPFactory;
import aima.core.util.JavaRandomizer;

public abstract class ReinforcementLearningAgentTest {

	public static void test_utility_learning_rates(
			ReinforcementAgent<Cell<Double>, CellWorldAction> reinforcementAgent,
			int numRuns, int numTrialsPerRun, int rmseTrialsToReport,
			int reportEveryN) {

		CellWorld<Double> cw = CellWorldFactory.createCellWorldForFig17_1();
		CellWorldEnvironment cwe = new CellWorldEnvironment(
				cw.getCellAt(1, 1),
				MDPFactory
						.createCellWorldPossibleOutcomesFunctionForFig17_1(cw),
				MDPFactory.createTransitionProbabilityFunctionForFigure17_1(cw),
				new JavaRandomizer());

		cwe.addAgent(reinforcementAgent);

		Map<Integer, List<Map<Cell<Double>, Double>>> runs = new HashMap<Integer, List<Map<Cell<Double>, Double>>>();
		for (int r = 0; r < numRuns; r++) {
			List<Map<Cell<Double>, Double>> trials = new ArrayList<Map<Cell<Double>, Double>>();
			for (int t = 0; t < numTrialsPerRun; t++) {
				cwe.executeTrial();
				trials.add(reinforcementAgent.getUtility());
			}
			runs.put(r, trials);
			reinforcementAgent.reset();
		}

		StringBuilder v4_3 = new StringBuilder();
		StringBuilder v3_3 = new StringBuilder();
		StringBuilder v1_3 = new StringBuilder();
		StringBuilder v1_1 = new StringBuilder();
		StringBuilder v3_2 = new StringBuilder();
		StringBuilder v2_1 = new StringBuilder();
		for (int t = 0; t < numTrialsPerRun; t++) {
			// Use the last run
			Map<Cell<Double>, Double> u = runs.get(numRuns - 1).get(t);
			v4_3.append((u.containsKey(cw.getCellAt(4, 3)) ? u.get(cw
					.getCellAt(4, 3)) : 0.0)
					+ "\t");
			v3_3.append((u.containsKey(cw.getCellAt(3, 3)) ? u.get(cw
					.getCellAt(3, 3)) : 0.0)
					+ "\t");
			v1_3.append((u.containsKey(cw.getCellAt(1, 3)) ? u.get(cw
					.getCellAt(1, 3)) : 0.0)
					+ "\t");
			v1_1.append((u.containsKey(cw.getCellAt(1, 1)) ? u.get(cw
					.getCellAt(1, 1)) : 0.0)
					+ "\t");
			v3_2.append((u.containsKey(cw.getCellAt(3, 2)) ? u.get(cw
					.getCellAt(3, 2)) : 0.0)
					+ "\t");
			v2_1.append((u.containsKey(cw.getCellAt(2, 1)) ? u.get(cw
					.getCellAt(2, 1)) : 0.0)
					+ "\t");
		}
		System.out.println("(4,3)" + "\t" + v4_3);
		System.out.println("(3,3)" + "\t" + v3_3);
		System.out.println("(1,3)" + "\t" + v1_3);
		System.out.println("(1,1)" + "\t" + v1_1);
		System.out.println("(3,2)" + "\t" + v3_2);
		System.out.println("(2,1)" + "\t" + v2_1);

		StringBuilder rmseValues = new StringBuilder();
		for (int t = 0; t < rmseTrialsToReport; t++) {
			// Calculate the Root Mean Square Error for utility of 1,1
			// for this trial# across all runs
			double xSsquared = 0;
			for (int r = 0; r < numRuns; r++) {
				Map<Cell<Double>, Double> u = runs.get(r).get(t);
				xSsquared += Math.pow(0.705 - u.get(cw.getCellAt(1, 1)), 2);
			}
			double rmse = Math.sqrt(xSsquared / runs.size());
			rmseValues.append(rmse);
			rmseValues.append("\t");
		}
		System.out.println("RMSeiu" + "\t" + rmseValues);
	}
}
