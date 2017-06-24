package aima.gui.demo.search;

import aima.core.agent.Action;
import aima.core.environment.nqueens.NQueensBoard;
import aima.core.environment.nqueens.NQueensBoard.Config;
import aima.core.environment.nqueens.NQueensFunctions;
import aima.core.environment.nqueens.NQueensGenAlgoUtil;
import aima.core.environment.nqueens.QueenAction;
import aima.core.search.agent.SearchAgent;
import aima.core.search.framework.SearchForActions;
import aima.core.search.framework.problem.GoalTest;
import aima.core.search.framework.problem.Problem;
import aima.core.search.framework.qsearch.GraphSearch;
import aima.core.search.framework.qsearch.TreeSearch;
import aima.core.search.local.*;
import aima.core.search.uninformed.BreadthFirstSearch;
import aima.core.search.uninformed.DepthFirstSearch;
import aima.core.search.uninformed.DepthLimitedSearch;
import aima.core.search.uninformed.IterativeDeepeningSearch;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 * @author Ravi Mohan
 * @author Ruediger Lunde
 * 
 */

public class NQueensDemo {

	private static final int boardSize = 8;

	public static void main(String[] args) {

		newNQueensDemo();
	}

	private static void newNQueensDemo() {

		nQueensWithDepthFirstSearch();
		nQueensWithBreadthFirstSearch();
		nQueensWithRecursiveDLS();
		nQueensWithIterativeDeepeningSearch();
		nQueensSimulatedAnnealingSearch();
		nQueensHillClimbingSearch();
		nQueensGeneticAlgorithmSearch();
	}

	private static void nQueensWithRecursiveDLS() {
		System.out.println("\nNQueensDemo recursive DLS -->");
		try {
			Problem<NQueensBoard, QueenAction> problem =
					NQueensFunctions.createIncrementalFormulationProblem(boardSize);
			SearchForActions<NQueensBoard, QueenAction> search = new DepthLimitedSearch<>(boardSize);
			SearchAgent<NQueensBoard, QueenAction> agent = new SearchAgent<>(problem, search);
			printActions(agent.getActions());
			printInstrumentation(agent.getInstrumentation());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static void nQueensWithBreadthFirstSearch() {
		try {
			System.out.println("\nNQueensDemo BFS -->");
			Problem<NQueensBoard, QueenAction> problem =
					NQueensFunctions.createIncrementalFormulationProblem(boardSize);
			SearchForActions<NQueensBoard, QueenAction> search = new BreadthFirstSearch<>(new TreeSearch<>());
			SearchAgent<NQueensBoard, QueenAction> agent = new SearchAgent<>(problem, search);
			printActions(agent.getActions());
			printInstrumentation(agent.getInstrumentation());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void nQueensWithDepthFirstSearch() {
		System.out.println("\nNQueensDemo DFS -->");
		try {
			Problem<NQueensBoard, QueenAction> problem =
					NQueensFunctions.createIncrementalFormulationProblem(boardSize);
			SearchForActions<NQueensBoard, QueenAction> search = new DepthFirstSearch<>(new GraphSearch<>());
			SearchAgent<NQueensBoard, QueenAction> agent = new SearchAgent<>(problem, search);
			printActions(agent.getActions());
			printInstrumentation(agent.getInstrumentation());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void nQueensWithIterativeDeepeningSearch() {
		System.out.println("\nNQueensDemo Iterative DS  -->");
		try {
			Problem<NQueensBoard, QueenAction> problem =
					NQueensFunctions.createIncrementalFormulationProblem(boardSize);
			SearchForActions<NQueensBoard, QueenAction> search = new IterativeDeepeningSearch<>();
			SearchAgent<NQueensBoard, QueenAction> agent = new SearchAgent<>(problem, search);

			System.out.println();
			printActions(agent.getActions());
			printInstrumentation(agent.getInstrumentation());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void nQueensSimulatedAnnealingSearch() {
		System.out.println("\nNQueensDemo Simulated Annealing  -->");
		try {
			Problem<NQueensBoard, QueenAction> problem =
					NQueensFunctions.createCompleteStateFormulationProblem(boardSize, Config.QUEENS_IN_FIRST_ROW);

			SimulatedAnnealingSearch<NQueensBoard, QueenAction> search =
					new SimulatedAnnealingSearch<>(NQueensFunctions.createAttackingPairsHeuristicFunction(),
					new Scheduler(20, 0.045, 100));
			SearchAgent<NQueensBoard, QueenAction> agent = new SearchAgent<>(problem, search);

			System.out.println();
			printActions(agent.getActions());
			System.out.println("Search Outcome=" + search.getOutcome());
			System.out.println("Final State=\n" + search.getLastSearchState());
			printInstrumentation(agent.getInstrumentation());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void nQueensHillClimbingSearch() {
		System.out.println("\nNQueensDemo HillClimbing  -->");
		try {
			Problem<NQueensBoard, QueenAction> problem =
					NQueensFunctions.createCompleteStateFormulationProblem(boardSize, Config.QUEENS_IN_FIRST_ROW);
			HillClimbingSearch<NQueensBoard, QueenAction> search = new HillClimbingSearch<>
					(NQueensFunctions.createAttackingPairsHeuristicFunction());
			SearchAgent<NQueensBoard, QueenAction> agent = new SearchAgent<>(problem, search);

			System.out.println();
			printActions(agent.getActions());
			System.out.println("Search Outcome=" + search.getOutcome());
			System.out.println("Final State=\n" + search.getLastSearchState());
			printInstrumentation(agent.getInstrumentation());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void nQueensGeneticAlgorithmSearch() {
		System.out.println("\nNQueensDemo GeneticAlgorithm  -->");
		try {
			FitnessFunction<Integer> fitnessFunction = NQueensGenAlgoUtil.getFitnessFunction();
			GoalTest<Individual<Integer>> goalTest = NQueensGenAlgoUtil.getGoalTest();
			// Generate an initial population
			Set<Individual<Integer>> population = new HashSet<>();
			for (int i = 0; i < 50; i++) {
				population.add(NQueensGenAlgoUtil.generateRandomIndividual(boardSize));
			}

			GeneticAlgorithm<Integer> ga = new GeneticAlgorithm<>(boardSize,
					NQueensGenAlgoUtil.getFiniteAlphabetForBoardOfSize(boardSize), 0.15);

			// Run for a set amount of time
			Individual<Integer> bestIndividual = ga.geneticAlgorithm(population, fitnessFunction, goalTest, 1000L);

			System.out.println("Max Time (1 second) Best Individual=\n"
					+ NQueensGenAlgoUtil.getBoardForIndividual(bestIndividual));
			System.out.println("Board Size      = " + boardSize);
			System.out.println("# Board Layouts = " + (new BigDecimal(boardSize)).pow(boardSize));
			System.out.println("Fitness         = " + fitnessFunction.apply(bestIndividual));
			System.out.println("Is Goal         = " + goalTest.test(bestIndividual));
			System.out.println("Population Size = " + ga.getPopulationSize());
			System.out.println("Iterations      = " + ga.getIterations());
			System.out.println("Took            = " + ga.getTimeInMilliseconds() + "ms.");

			// Run till goal is achieved
			bestIndividual = ga.geneticAlgorithm(population, fitnessFunction, goalTest, 0L);

			System.out.println("");
			System.out
					.println("Goal Test Best Individual=\n" + NQueensGenAlgoUtil.getBoardForIndividual(bestIndividual));
			System.out.println("Board Size      = " + boardSize);
			System.out.println("# Board Layouts = " + (new BigDecimal(boardSize)).pow(boardSize));
			System.out.println("Fitness         = " + fitnessFunction.apply(bestIndividual));
			System.out.println("Is Goal         = " + goalTest.test(bestIndividual));
			System.out.println("Population Size = " + ga.getPopulationSize());
			System.out.println("Itertions       = " + ga.getIterations());
			System.out.println("Took            = " + ga.getTimeInMilliseconds() + "ms.");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void printInstrumentation(Properties properties) {
		for (Object o : properties.keySet()) {
			String key = (String) o;
			String property = properties.getProperty(key);
			System.out.println(key + " : " + property);
		}

	}

	private static void printActions(List<Action> actions) {
		for (Action action : actions) {
			System.out.println(action.toString());
		}
	}

}