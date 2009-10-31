package aima.test.core.unit.search.informed;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import aima.core.agent.Action;
import aima.core.agent.Agent;
import aima.core.agent.EnvironmentState;
import aima.core.agent.EnvironmentView;
import aima.core.search.framework.HeuristicFunction;
import aima.core.search.informed.AStarEvaluationFunction;
import aima.core.search.informed.RecursiveBestFirstSearch;
import aima.core.search.map.Map;
import aima.core.search.map.MapAgent;
import aima.core.search.map.MapEnvironment;
import aima.core.search.map.SimplifiedRoadMapOfPartOfRomania;
import aima.core.util.datastructure.Point2D;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class RecursiveBestFirstSearchTest {

	StringBuffer envChanges;

	Map aMap;

	RecursiveBestFirstSearch recursiveBestFirstSearch;

	HeuristicFunction heuristicFunction;

	@Before
	public void setUp() {
		envChanges = new StringBuffer();

		aMap = new SimplifiedRoadMapOfPartOfRomania();

		recursiveBestFirstSearch = new RecursiveBestFirstSearch(
				new AStarEvaluationFunction());

		heuristicFunction = new HeuristicFunction() {
			public double getHeuristicValue(Object state) {
				Point2D pt1 = aMap.getPosition((String) state);
				Point2D pt2 = aMap
						.getPosition(SimplifiedRoadMapOfPartOfRomania.BUCHAREST);
				return pt1.distance(pt2);
			}
		};
	}

	@Test
	public void testStartingAtGoal() {
		MapEnvironment me = new MapEnvironment(aMap);
		MapAgent ma = new MapAgent(me, recursiveBestFirstSearch,
				new String[] { SimplifiedRoadMapOfPartOfRomania.BUCHAREST });
		ma.setHeuristicFunction(heuristicFunction);

		me.addAgent(ma, SimplifiedRoadMapOfPartOfRomania.BUCHAREST);
		me.addEnvironmentView(new EnvironmentView() {
			public void notify(String msg) {
				envChanges.append(msg).append(":");
			}
			
			public void envChanged(Agent agent, Action action, EnvironmentState state) {
				envChanges.append(action).append(":");
			}
		});
		me.stepUntilDone();

		Assert
				.assertEquals(
						"CurrentLocation=In(Bucharest), Goal=In(Bucharest):Action[name==NoOp]:METRIC[pathCost]=0.0:METRIC[maxRecursiveDepth]=0:METRIC[nodesExpanded]=0:Action[name==NoOp]:",
						envChanges.toString());
	}

	@Test
	public void testExampleFromBookFigure4_6Page103() {
		MapEnvironment me = new MapEnvironment(aMap);
		MapAgent ma = new MapAgent(me, recursiveBestFirstSearch,
				new String[] { SimplifiedRoadMapOfPartOfRomania.BUCHAREST });
		ma.setHeuristicFunction(heuristicFunction);

		me.addAgent(ma, SimplifiedRoadMapOfPartOfRomania.ARAD);
		me.addEnvironmentView(new EnvironmentView() {
			public void notify(String msg) {
				envChanges.append(msg).append(":");
			}
			
			public void envChanged(Agent agent, Action action, EnvironmentState state) {
				envChanges.append(action).append(":");
			}
		});
		me.stepUntilDone();

		Assert
				.assertEquals(
						"CurrentLocation=In(Arad), Goal=In(Bucharest):Action[name==moveTo, location==Sibiu]:Action[name==moveTo, location==RimnicuVilcea]:Action[name==moveTo, location==Pitesti]:Action[name==moveTo, location==Bucharest]:METRIC[pathCost]=418.0:METRIC[maxRecursiveDepth]=4:METRIC[nodesExpanded]=6:Action[name==NoOp]:",
						envChanges.toString());
	}
}
