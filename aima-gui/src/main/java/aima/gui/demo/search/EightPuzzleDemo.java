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
		System.out.println("Goal State:\n" + EightPuzzleFunctions.GOAL_STATE);
		
		// nullHeuristic		// h = 0
//		misplaced				// h1 -> wighted misplaced (2^i per misplaced tile)
//		manhattan				// h2 -> weighted manhattan
//		nonconsistentheuristic	// h3
//		epsilonManhattan		// (1+epsilon) * h2
		
		// Default
//		eightPuzzleDLSDemo();
//		eightPuzzleIDLSDemo();
//		eightPuzzleGreedyBestFirstDemo();
//		eightPuzzleGreedyBestFirstManhattanDemo();
//		eightPuzzleAStarDemo();
//		eightPuzzleAStarManhattanDemo();
//		eightPuzzleSimulatedAnnealingDemo();
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
			SearchForActions<EightPuzzleBoard, Action> search = new AStarSearch<> (new GraphSearch<>(), EightPuzzleFunctions::getManhattanDistance);
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
		System.out.println("Total actions: " + actions.size()); // Ejercicio 4
		actions.forEach(System.out::println);
	}
}


//Banco de ejemplos para el 8-puzzle. 
//Objetivo {1, 2, 3, 8, 0, 4, 7, 6, 5}
	//Situaciones iniciales:
//5 movimientos
//	{1, 0, 3, 8, 2, 5, 7, 4, 6}
//	{1, 4, 2, 0, 8, 3, 7, 6, 5} 
//	{1, 3, 4, 8, 6, 2, 7, 0, 5}
//	{1, 2, 3, 7, 8, 0, 6, 5, 4}
//	{1, 4, 2, 8, 3, 0, 7, 6, 5}
//	{1, 2, 3, 0, 8, 6, 7, 5, 4}
//	{1, 2, 3, 0, 4, 5, 8, 7, 6}
//	{1, 0, 2, 8, 6, 3, 7, 5, 4}
//	{2, 8, 3, 1, 6, 4, 7, 0, 5}
//10 movimientos	
//	{8, 2, 1, 7, 0, 3, 6, 5, 4}
//	{1, 4, 0, 8, 5, 2, 7, 3, 6}
//	{8, 1, 3, 7, 0, 5, 4, 2, 6}
//	{8, 1, 2, 4, 0, 6, 7, 5, 3}
//	{0, 1, 3, 7, 2, 5, 4, 8, 6}
//	{1, 4, 0, 7, 8, 2, 6, 5, 3}
//	{3, 8, 4, 1, 6, 2, 0, 7, 5}
//	{1, 3, 4, 6, 7, 2, 0, 8, 5}
//	{3, 2, 4, 1, 0, 5, 8, 7, 6}
//	{8, 1, 0, 2, 5, 3, 7, 4, 6}
//15 movimientos
//	{4, 8, 2, 6, 3, 5, 1, 0, 7}
//	{1, 4, 5, 2, 7, 0, 8, 6, 3}
//	{1, 3, 8, 6, 7, 4, 2, 0, 5}
//	{2, 0, 8, 7, 5, 3, 4, 1, 6}
//	{7, 1, 3, 4, 5, 0, 8, 2, 6}
//	{1, 3, 6, 7, 2, 0, 4, 5, 8}
//	{7, 0, 3, 5, 1, 8, 2, 6, 4}
//	{6, 3, 5, 2, 1, 0, 8, 4, 7}
//	{6, 0, 3, 8, 1, 5, 4, 2, 7}
//	{7, 8, 3, 1, 5, 0, 4, 2, 6}
//20 movimientos
//	{6, 2, 7, 4, 5, 1, 0, 8, 3}
//	{4, 7, 2, 1, 0, 6, 3, 5, 8}
//	{7, 1, 5, 4, 0, 8, 2, 6, 3}
//	{5, 1, 6, 4, 0, 3, 8, 7, 2}
//	{7, 1, 4, 5, 0, 6, 3, 2, 8}
//	{2, 4, 0, 6, 3, 1, 7, 8, 5}
//	{3, 5, 6, 2, 4, 7, 0, 1, 8}
//	{1, 4, 7, 6, 8, 5, 0, 3, 2}
//	{6, 4, 0, 2, 8, 1, 7, 3, 5}
//	{4, 1, 3, 7, 2, 8, 5, 6, 0}
//25 movimientos
//	{6, 7, 4, 0, 5, 1, 3, 2, 8}
//	{6, 0, 7, 5, 4, 1, 3, 8, 2}
//	{3, 4, 8, 5, 7, 1, 6, 0, 2}
//	{4, 5, 3, 7, 6, 2, 8, 0, 1}
//	{2, 7, 8, 5, 4, 0, 3, 1, 6}
//30 movimientos
//	{5, 6, 7, 2, 8, 4, 0, 3, 1}
//	{5, 6, 7, 4, 0, 8, 3, 2, 1}
//	{5, 4, 7, 6, 0, 3, 8, 2, 1} 
//	{3, 8, 7, 4, 0, 6, 5, 2, 1}
//	{5, 6, 3, 4, 0, 2, 7, 8, 1}
//Instancias especiales
//	{ 6, 3, 2, 5, 7, 8, 0, 4, 1 } 	// 24 movimientos
//	{ 6, 3, 2, 5, 8, 1, 4, 0, 7 } 	// 23 movimientos
//	{ 3, 5, 6, 4, 2, 7, 0, 8, 1 } 	// 24 movimientos
//	{ 7, 0, 2, 3, 6, 1, 5, 8, 4 } 	// 21 movimientos
