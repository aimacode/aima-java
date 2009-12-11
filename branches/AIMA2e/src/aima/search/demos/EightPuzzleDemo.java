/*
 * Created on Sep 12, 2004
 *
 */
package aima.search.demos;

import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import aima.search.eightpuzzle.EightPuzzleBoard;
import aima.search.eightpuzzle.EightPuzzleGoalTest;
import aima.search.eightpuzzle.EightPuzzleSuccessorFunction;
import aima.search.eightpuzzle.ManhattanHeuristicFunction;
import aima.search.eightpuzzle.MisplacedTilleHeuristicFunction;
import aima.search.framework.GraphSearch;
import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchAgent;
import aima.search.informed.AStarSearch;
import aima.search.informed.GreedyBestFirstSearch;
import aima.search.informed.SimulatedAnnealingSearch;
import aima.search.uninformed.DepthLimitedSearch;
import aima.search.uninformed.IterativeDeepeningSearch;

/**
 * @author Ravi Mohan
 * 
 */

public class EightPuzzleDemo {
	static EightPuzzleBoard boardWithThreeMoveSolution = new EightPuzzleBoard(
			new int[] { 1, 2, 5, 3, 4, 0, 6, 7, 8 });;

	static EightPuzzleBoard random1 = new EightPuzzleBoard(new int[] { 1, 4, 2,
			7, 5, 8, 3, 0, 6 });

	static EightPuzzleBoard extreme = new EightPuzzleBoard(new int[] { 0, 8, 7,
			6, 5, 4, 3, 2, 1 });

	public static void main(String[] args) {
		eightPuzzleDLSDemo();
		eightPuzzleIDLSDemo();
		eightPuzzleGreedyBestFirstDemo();
		eightPuzzleGreedyBestFirstManhattanDemo();
		eightPuzzleAStarDemo();
		eightPuzzleAStarManhattanDemo();
		eightPuzzleSimulatedAnnealingDemo();
	}

	private static void eightPuzzleDLSDemo() {
		System.out.println("\nEightPuzzleDemo recursive DLS -->");
		try {
			Problem problem = new Problem(random1,
					new EightPuzzleSuccessorFunction(),
					new EightPuzzleGoalTest());
			Search search = new DepthLimitedSearch(9);
			SearchAgent agent = new SearchAgent(problem, search);
			printActions(agent.getActions());
			printInstrumentation(agent.getInstrumentation());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static void eightPuzzleIDLSDemo() {
		System.out.println("\nEightPuzzleDemo Iterative DLS -->");
		try {
			Problem problem = new Problem(random1,
					new EightPuzzleSuccessorFunction(),
					new EightPuzzleGoalTest());
			Search search = new IterativeDeepeningSearch();
			SearchAgent agent = new SearchAgent(problem, search);
			printActions(agent.getActions());
			printInstrumentation(agent.getInstrumentation());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static void eightPuzzleGreedyBestFirstDemo() {
		System.out
				.println("\nEightPuzzleDemo Greedy Best First Search (MisplacedTileHeursitic)-->");
		try {
			Problem problem = new Problem(boardWithThreeMoveSolution,
					new EightPuzzleSuccessorFunction(),
					new EightPuzzleGoalTest(),
					new MisplacedTilleHeuristicFunction());
			Search search = new GreedyBestFirstSearch(new GraphSearch());
			SearchAgent agent = new SearchAgent(problem, search);
			printActions(agent.getActions());
			printInstrumentation(agent.getInstrumentation());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static void eightPuzzleGreedyBestFirstManhattanDemo() {
		System.out
				.println("\nEightPuzzleDemo Greedy Best First Search (ManhattanHeursitic)-->");
		try {
			Problem problem = new Problem(boardWithThreeMoveSolution,
					new EightPuzzleSuccessorFunction(),
					new EightPuzzleGoalTest(), new ManhattanHeuristicFunction());
			Search search = new GreedyBestFirstSearch(new GraphSearch());
			SearchAgent agent = new SearchAgent(problem, search);
			printActions(agent.getActions());
			printInstrumentation(agent.getInstrumentation());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static void eightPuzzleAStarDemo() {
		System.out
				.println("\nEightPuzzleDemo AStar Search (MisplacedTileHeursitic)-->");
		try {
			Problem problem = new Problem(random1,
					new EightPuzzleSuccessorFunction(),
					new EightPuzzleGoalTest(),
					new MisplacedTilleHeuristicFunction());
			Search search = new AStarSearch(new GraphSearch());
			SearchAgent agent = new SearchAgent(problem, search);
			printActions(agent.getActions());
			printInstrumentation(agent.getInstrumentation());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static void eightPuzzleSimulatedAnnealingDemo() {
		System.out.println("\nEightPuzzleDemo Simulated Annealing  Search -->");
		try {
			Problem problem = new Problem(random1,
					new EightPuzzleSuccessorFunction(),
					new EightPuzzleGoalTest(), new ManhattanHeuristicFunction());
			SimulatedAnnealingSearch search = new SimulatedAnnealingSearch();
			SearchAgent agent = new SearchAgent(problem, search);
			printActions(agent.getActions());
			System.out.println("Search Outcome=" + search.getOutcome());
			System.out.println("Final State=\n" + search.getLastSearchState());
			printInstrumentation(agent.getInstrumentation());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void eightPuzzleAStarManhattanDemo() {
		System.out
				.println("\nEightPuzzleDemo AStar Search (ManhattanHeursitic)-->");
		try {
			Problem problem = new Problem(random1,
					new EightPuzzleSuccessorFunction(),
					new EightPuzzleGoalTest(), new ManhattanHeuristicFunction());
			Search search = new AStarSearch(new GraphSearch());
			SearchAgent agent = new SearchAgent(problem, search);
			printActions(agent.getActions());
			printInstrumentation(agent.getInstrumentation());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static void printInstrumentation(Properties properties) {
		Iterator keys = properties.keySet().iterator();
		while (keys.hasNext()) {
			String key = (String) keys.next();
			String property = properties.getProperty(key);
			System.out.println(key + " : " + property);
		}

	}

	private static void printActions(List actions) {
		for (int i = 0; i < actions.size(); i++) {
			String action = (String) actions.get(i);
			System.out.println(action);
		}
	}

}