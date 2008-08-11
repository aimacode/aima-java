package aima.search.demos;

import aima.basic.BasicEnvironmentView;
import aima.search.framework.GraphSearch;
import aima.search.framework.HeuristicFunction;
import aima.search.framework.TreeSearch;
import aima.search.informed.AStarEvaluationFunction;
import aima.search.informed.RecursiveBestFirstSearch;
import aima.search.map.MapAgent;
import aima.search.map.MapEnvironment;
import aima.search.map.SimplifiedRoadMapOfPartOfRomania;
import aima.search.uninformed.BidirectionalSearch;
import aima.search.uninformed.BreadthFirstSearch;
import aima.search.uninformed.UniformCostSearch;
import aima.search.uninformed.DepthFirstSearch;
import aima.search.uninformed.DepthLimitedSearch;
import aima.search.uninformed.IterativeDeepeningSearch;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class MapDemo {

	public static void main(String[] args) {
		newMapDemo();
	}

	private static void newMapDemo() {
		mapWithBreadthFirstSearch();
		mapWithUniformCostSearch();
		mapWithDepthFirstSearch();
		mapWithRecursiveDLS();
		mapWithIterativeDeepeningSearch();
		mapWithBidrectionalSearch();
		mapWithRecursiveBestFirstSearch();
	}

	private static void mapWithBreadthFirstSearch() {
		System.out.println("\nMapDemo BFS -->");

		MapEnvironment me = new MapEnvironment(SimplifiedRoadMapOfPartOfRomania
				.getMapOfRomania());
		MapAgent ma = new MapAgent(me,
				new BreadthFirstSearch(new GraphSearch()), 2);
		me.addAgent(ma, SimplifiedRoadMapOfPartOfRomania.ARAD);
		me.registerView(new BasicEnvironmentView());
		me.stepUntilNoOp();
	}

	private static void mapWithUniformCostSearch() {
		// Both Tree and Graph Search should find the same route (for this
		// problem), however, the number
		// of nodes expanded etc... will more than likely differ
		System.out.println("\nMapDemo UCS (using a TreeSearch) -->");

		MapEnvironment me = new MapEnvironment(SimplifiedRoadMapOfPartOfRomania
				.getMapOfRomania());
		MapAgent ma = new MapAgent(me, new UniformCostSearch(new TreeSearch()),
				new String[] { SimplifiedRoadMapOfPartOfRomania.BUCHAREST });
		me.addAgent(ma, SimplifiedRoadMapOfPartOfRomania.ARAD);
		me.registerView(new BasicEnvironmentView());
		me.stepUntilNoOp();

		System.out.println("\nMapDemo UCS (using a GraphSearch) -->");

		me = new MapEnvironment(SimplifiedRoadMapOfPartOfRomania
				.getMapOfRomania());
		ma = new MapAgent(me, new UniformCostSearch(new GraphSearch()),
				new String[] { SimplifiedRoadMapOfPartOfRomania.BUCHAREST });
		me.addAgent(ma, SimplifiedRoadMapOfPartOfRomania.ARAD);
		me.registerView(new BasicEnvironmentView());
		me.stepUntilNoOp();
	}

	private static void mapWithDepthFirstSearch() {
		System.out.println("\nMapDemo DFS -->");

		MapEnvironment me = new MapEnvironment(SimplifiedRoadMapOfPartOfRomania
				.getMapOfRomania());
		MapAgent ma = new MapAgent(me, new DepthFirstSearch(new GraphSearch()),
				2);
		me.addAgent(ma, SimplifiedRoadMapOfPartOfRomania.ARAD);
		me.registerView(new BasicEnvironmentView());
		me.stepUntilNoOp();
	}

	private static void mapWithRecursiveDLS() {
		System.out.println("\nMapDemo recursive DLS -->");

		MapEnvironment me = new MapEnvironment(SimplifiedRoadMapOfPartOfRomania
				.getMapOfRomania());
		MapAgent ma = new MapAgent(me, new DepthLimitedSearch(8), 2);
		me.addAgent(ma, SimplifiedRoadMapOfPartOfRomania.ARAD);
		me.registerView(new BasicEnvironmentView());
		me.stepUntilNoOp();
	}

	private static void mapWithIterativeDeepeningSearch() {
		System.out.println("\nMapDemo Iterative DS  -->");

		MapEnvironment me = new MapEnvironment(SimplifiedRoadMapOfPartOfRomania
				.getMapOfRomania());
		MapAgent ma = new MapAgent(me, new IterativeDeepeningSearch(), 2);
		me.addAgent(ma, SimplifiedRoadMapOfPartOfRomania.ARAD);
		me.registerView(new BasicEnvironmentView());
		me.stepUntilNoOp();
	}

	private static void mapWithBidrectionalSearch() {
		System.out.println("\nMapDemo Bidirectional Search  -->");

		MapEnvironment me = new MapEnvironment(SimplifiedRoadMapOfPartOfRomania
				.getMapOfRomania());
		MapAgent ma = new MapAgent(me, new BidirectionalSearch(),
				new String[] { SimplifiedRoadMapOfPartOfRomania.BUCHAREST });
		me.addAgent(ma, SimplifiedRoadMapOfPartOfRomania.ORADEA);
		me.registerView(new BasicEnvironmentView());
		me.stepUntilNoOp();
	}

	private static void mapWithRecursiveBestFirstSearch() {
		System.out.println("\nMapDemo RecursiveBestFirstSearch Search  -->");

		MapEnvironment me = new MapEnvironment(SimplifiedRoadMapOfPartOfRomania
				.getMapOfRomania());
		MapAgent ma = new MapAgent(me, new RecursiveBestFirstSearch(
				new AStarEvaluationFunction()),
				new String[] { SimplifiedRoadMapOfPartOfRomania.BUCHAREST });
		ma.setHeuristicFunction(new HeuristicFunction() {
			public double getHeuristicValue(Object state) {
				return SimplifiedRoadMapOfPartOfRomania
						.getStraightLineDistancesToBucharest().getDistance(
								(String) state,
								SimplifiedRoadMapOfPartOfRomania.BUCHAREST);
			}
		});

		me.addAgent(ma, SimplifiedRoadMapOfPartOfRomania.ARAD);
		me.registerView(new BasicEnvironmentView());
		me.stepUntilNoOp();
	}
}