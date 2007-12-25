package aima.test.search.searches;

import junit.framework.TestCase;
import aima.basic.BasicEnvironmentView;
import aima.search.framework.HeuristicFunction;
import aima.search.informed.AStarEvaluationFunction;
import aima.search.informed.RecursiveBestFirstSearch;
import aima.search.map.Map;
import aima.search.map.MapAgent;
import aima.search.map.MapEnvironment;
import aima.search.map.SimplifiedRoadMapOfPartOfRomania;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class RecursiveBestFirstSearchTest extends TestCase {

	StringBuffer envChanges;

	Map aMap;

	RecursiveBestFirstSearch recursiveBestFirstSearch;

	HeuristicFunction heuristicFunction;

	@Override
	public void setUp() {
		envChanges = new StringBuffer();

		aMap = SimplifiedRoadMapOfPartOfRomania.getMapOfRomania();

		recursiveBestFirstSearch = new RecursiveBestFirstSearch(
				new AStarEvaluationFunction());

		heuristicFunction = new HeuristicFunction() {
			public double getHeuristicValue(Object state) {
				return SimplifiedRoadMapOfPartOfRomania
						.getStraightLineDistancesToBucharest().getDistance(
								(String) state,
								SimplifiedRoadMapOfPartOfRomania.BUCHAREST);
			}
		};
	}

	public void testStartingAtGoal() {
		MapEnvironment me = new MapEnvironment(aMap);
		MapAgent ma = new MapAgent(me, recursiveBestFirstSearch,
				new String[] { SimplifiedRoadMapOfPartOfRomania.BUCHAREST });
		ma.setHeuristicFunction(heuristicFunction);

		me.addAgent(ma, SimplifiedRoadMapOfPartOfRomania.BUCHAREST);
		me.registerView(new BasicEnvironmentView() {
			@Override
			public void envChanged(String command) {
				envChanges.append(command).append(":");
			}
		});
		me.stepUntilNoOp();

		assertEquals(
				"CurrentLocation=In(Bucharest), Goal=In(Bucharest):NoOP:METRIC[pathCost]=0.0:METRIC[maxRecursiveDepth]=0:METRIC[nodesExpanded]=0:NoOP:",
				envChanges.toString());
	}

	public void testExampleFromBookFigure4_6Page103() {
		MapEnvironment me = new MapEnvironment(aMap);
		MapAgent ma = new MapAgent(me, recursiveBestFirstSearch,
				new String[] { SimplifiedRoadMapOfPartOfRomania.BUCHAREST });
		ma.setHeuristicFunction(heuristicFunction);

		me.addAgent(ma, SimplifiedRoadMapOfPartOfRomania.ARAD);
		me.registerView(new BasicEnvironmentView() {
			@Override
			public void envChanged(String command) {
				envChanges.append(command).append(":");
			}
		});
		me.stepUntilNoOp();

		assertEquals(
				"CurrentLocation=In(Arad), Goal=In(Bucharest):Sibiu:Rimnicu Vilcea:Pitesti:Bucharest:METRIC[pathCost]=422.0:METRIC[maxRecursiveDepth]=4:METRIC[nodesExpanded]=6:NoOP:",
				envChanges.toString());
	}
}
