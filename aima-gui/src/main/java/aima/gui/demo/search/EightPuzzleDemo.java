package aima.gui.demo.search;

import aima.core.agent.Action;
import aima.core.environment.eightpuzzle.*;
import aima.core.search.agent.SearchAgent;
import aima.core.search.framework.SearchForActions;
import aima.core.search.framework.problem.Problem;
import aima.core.search.framework.qsearch.GraphSearch;
import aima.core.search.informed.AStarSearch;
import aima.core.search.informed.GreedyBestFirstSearch;
import aima.core.search.local.SimulatedAnnealingSearch;
import aima.core.search.uninformed.DepthLimitedSearch;
import aima.core.search.uninformed.IterativeDeepeningSearch;

import java.util.List;
import java.util.Properties;

/**
 * @author Ravi Mohan
 * @author Ruediger Lunde
 * 
 */

public class EightPuzzleDemo {
	private static EightPuzzleBoard boardWithThreeMoveSolution =
			new EightPuzzleBoard(new int[] { 1, 2, 5, 3, 4, 0, 6, 7, 8 });

	private static EightPuzzleBoard random1 =
			new EightPuzzleBoard(new int[] { 1, 4, 2, 7, 5, 8, 3, 0, 6 });

//	private static EightPuzzleBoard extreme =
//			new EightPuzzleBoard(new int[] { 0, 8, 7, 6, 5, 4, 3, 2, 1 });

	public static void main(String[] args) {
		System.out.println("Initial State:\n" + boardWithThreeMoveSolution);
		eightPuzzleDLSDemo();
		eightPuzzleIDLSDemo();
		eightPuzzleGreedyBestFirstDemo();
		eightPuzzleGreedyBestFirstManhattanDemo();
		eightPuzzleAStarDemo();
		eightPuzzleAStarManhattanDemo();
		eightPuzzleSimulatedAnnealingDemo();
	}

	private static void eightPuzzleDLSDemo() {
		System.out.println("\nEightPuzzleDemo recursive DLS (9)");
		try {
			Problem<EightPuzzleBoard, Action> problem = new BidirectionalEightPuzzleProblem(boardWithThreeMoveSolution);
			SearchForActions<EightPuzzleBoard, Action> search = new DepthLimitedSearch<>(9);
			SearchAgent<Object, EightPuzzleBoard, Action> agent = new SearchAgent<>(problem, search);
			printActions(agent.getActions());
			printInstrumentation(agent.getInstrumentation());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void eightPuzzleIDLSDemo() {
		System.out.println("\nEightPuzzleDemo Iterative DLS");
		try {
			Problem<EightPuzzleBoard, Action> problem = new BidirectionalEightPuzzleProblem(random1);
			SearchForActions<EightPuzzleBoard, Action> search = new IterativeDeepeningSearch<>();
			SearchAgent<Object, EightPuzzleBoard, Action> agent = new SearchAgent<>(problem, search);
			printActions(agent.getActions());
			printInstrumentation(agent.getInstrumentation());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void eightPuzzleGreedyBestFirstDemo() {
		System.out.println("\nEightPuzzleDemo Greedy Best First Search (MisplacedTileHeursitic)");
		try {
			Problem<EightPuzzleBoard, Action> problem = new BidirectionalEightPuzzleProblem(boardWithThreeMoveSolution);
			SearchForActions<EightPuzzleBoard, Action> search = new GreedyBestFirstSearch<>
					(new GraphSearch<>(), EightPuzzleFunctions::getNumberOfMisplacedTiles);
			SearchAgent<Object, EightPuzzleBoard, Action> agent = new SearchAgent<>(problem, search);
			printActions(agent.getActions());
			printInstrumentation(agent.getInstrumentation());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void eightPuzzleGreedyBestFirstManhattanDemo() {
		System.out.println("\nEightPuzzleDemo Greedy Best First Search (ManhattanHeursitic)");
		try {
			Problem<EightPuzzleBoard, Action> problem = new BidirectionalEightPuzzleProblem(boardWithThreeMoveSolution);
			SearchForActions<EightPuzzleBoard, Action> search = new GreedyBestFirstSearch<>
					(new GraphSearch<>(), EightPuzzleFunctions::getManhattanDistance);
			SearchAgent<Object, EightPuzzleBoard, Action> agent = new SearchAgent<>(problem, search);
			printActions(agent.getActions());
			printInstrumentation(agent.getInstrumentation());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static void eightPuzzleAStarDemo() {
		System.out.println("\nEightPuzzleDemo AStar Search (MisplacedTileHeursitic)");
		try {
			Problem<EightPuzzleBoard, Action> problem = new BidirectionalEightPuzzleProblem(random1);
			SearchForActions<EightPuzzleBoard, Action> search = new AStarSearch<>
					(new GraphSearch<>(), EightPuzzleFunctions::getNumberOfMisplacedTiles);
			SearchAgent<Object, EightPuzzleBoard, Action> agent = new SearchAgent<>(problem, search);
			printActions(agent.getActions());
			printInstrumentation(agent.getInstrumentation());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void eightPuzzleSimulatedAnnealingDemo() {
		System.out.println("\nEightPuzzleDemo Simulated Annealing Search");
		try {
			Problem<EightPuzzleBoard, Action> problem = new BidirectionalEightPuzzleProblem(random1);
			SimulatedAnnealingSearch<EightPuzzleBoard, Action> search = new SimulatedAnnealingSearch<>
					(EightPuzzleFunctions::getManhattanDistance);
			SearchAgent<Object, EightPuzzleBoard, Action> agent = new SearchAgent<>(problem, search);
			printActions(agent.getActions());
			System.out.println("Final State:\n" + search.getLastState());
			printInstrumentation(agent.getInstrumentation());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void eightPuzzleAStarManhattanDemo() {
		System.out.println("\nEightPuzzleDemo AStar Search (ManhattanHeursitic)");
		try {
			Problem<EightPuzzleBoard, Action> problem = new BidirectionalEightPuzzleProblem(random1);
			SearchForActions<EightPuzzleBoard, Action> search = new AStarSearch<>
					(new GraphSearch<>(), EightPuzzleFunctions::getManhattanDistance);
			SearchAgent<Object, EightPuzzleBoard, Action> agent = new SearchAgent<>(problem, search);
			printActions(agent.getActions());
			printInstrumentation(agent.getInstrumentation());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void printInstrumentation(Properties properties) {
		properties.keySet().stream().map(key -> key + "=" + properties.get(key)).forEach(System.out::println);
	}

	private static void printActions(List<Action> actions) {
		actions.forEach(System.out::println);
	}
}