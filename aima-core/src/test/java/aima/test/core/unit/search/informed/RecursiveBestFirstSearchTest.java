package aima.test.core.unit.search.informed;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import aima.core.agent.Action;
import aima.core.agent.Agent;
import aima.core.agent.Environment;
import aima.core.agent.EnvironmentView;
import aima.core.environment.map.Map;
import aima.core.environment.map.SimpleMapAgent;
import aima.core.environment.map.MapEnvironment;
import aima.core.environment.map.SimplifiedRoadMapOfPartOfRomania;
import aima.core.search.framework.evalfunc.HeuristicFunction;
import aima.core.search.informed.AStarEvaluationFunction;
import aima.core.search.informed.RecursiveBestFirstSearch;
import aima.core.util.math.geom.shapes.Point2D;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class RecursiveBestFirstSearchTest {

	StringBuffer envChanges;

	Map aMap;

	RecursiveBestFirstSearch recursiveBestFirstSearch;
	RecursiveBestFirstSearch recursiveBestFirstSearchAvoidingLoops;

	@Before
	public void setUp() {
		envChanges = new StringBuffer();

		aMap = new SimplifiedRoadMapOfPartOfRomania();

		HeuristicFunction heuristicFunction = new HeuristicFunction() {
			public double h(Object state) {
				Point2D pt1 = aMap.getPosition((String) state);
				Point2D pt2 = aMap.getPosition(SimplifiedRoadMapOfPartOfRomania.BUCHAREST);
				return pt1.distance(pt2);
			}
		};

		recursiveBestFirstSearch = new RecursiveBestFirstSearch(new AStarEvaluationFunction(heuristicFunction));
		recursiveBestFirstSearchAvoidingLoops = new RecursiveBestFirstSearch(
				new AStarEvaluationFunction(heuristicFunction), true);
	}

	@Test
	public void testStartingAtGoal() {
		MapEnvironment me = new MapEnvironment(aMap);
		SimpleMapAgent ma = new SimpleMapAgent(me.getMap(), me, recursiveBestFirstSearch,
				new String[] { SimplifiedRoadMapOfPartOfRomania.BUCHAREST });

		me.addAgent(ma, SimplifiedRoadMapOfPartOfRomania.BUCHAREST);
		me.addEnvironmentView(new TestEnvironmentView());
		me.stepUntilDone();

		Assert.assertEquals(
				"CurrentLocation=In(Bucharest), Goal=In(Bucharest):Action[name==NoOp]:METRIC[pathCost]=0.0:METRIC[maxRecursiveDepth]=0:METRIC[nodesExpanded]=0:Action[name==NoOp]:",
				envChanges.toString());
	}

	@Test
	public void testAIMA3eFigure3_27() {
		MapEnvironment me = new MapEnvironment(aMap);
		SimpleMapAgent ma = new SimpleMapAgent(me.getMap(), me, recursiveBestFirstSearch,
				new String[] { SimplifiedRoadMapOfPartOfRomania.BUCHAREST });

		me.addAgent(ma, SimplifiedRoadMapOfPartOfRomania.ARAD);
		me.addEnvironmentView(new TestEnvironmentView());
		me.stepUntilDone();

		Assert.assertEquals(
				"CurrentLocation=In(Arad), Goal=In(Bucharest):Action[name==moveTo, location==Sibiu]:Action[name==moveTo, location==RimnicuVilcea]:Action[name==moveTo, location==Pitesti]:Action[name==moveTo, location==Bucharest]:METRIC[pathCost]=418.0:METRIC[maxRecursiveDepth]=4:METRIC[nodesExpanded]=6:Action[name==NoOp]:",
				envChanges.toString());
	}

	@Test
	public void testAIMA3eAradNeamtA() {
		MapEnvironment me = new MapEnvironment(aMap);
		SimpleMapAgent ma = new SimpleMapAgent(me.getMap(), me, recursiveBestFirstSearch,
				new String[] { SimplifiedRoadMapOfPartOfRomania.NEAMT });

		me.addAgent(ma, SimplifiedRoadMapOfPartOfRomania.ARAD);
		me.addEnvironmentView(new TestEnvironmentView());
		me.stepUntilDone();

		Assert.assertEquals(
				"CurrentLocation=In(Arad), Goal=In(Neamt):Action[name==moveTo, location==Sibiu]:Action[name==moveTo, location==RimnicuVilcea]:Action[name==moveTo, location==Pitesti]:Action[name==moveTo, location==Bucharest]:Action[name==moveTo, location==Urziceni]:Action[name==moveTo, location==Vaslui]:Action[name==moveTo, location==Iasi]:Action[name==moveTo, location==Neamt]:METRIC[pathCost]=824.0:METRIC[maxRecursiveDepth]=12:METRIC[nodesExpanded]=340208:Action[name==NoOp]:",
				envChanges.toString());
	}

	@Test
	public void testAIMA3eAradNeamtB() {
		MapEnvironment me = new MapEnvironment(aMap);
		SimpleMapAgent ma = new SimpleMapAgent(me.getMap(), me, recursiveBestFirstSearchAvoidingLoops,
				new String[] { SimplifiedRoadMapOfPartOfRomania.NEAMT });

		me.addAgent(ma, SimplifiedRoadMapOfPartOfRomania.ARAD);
		me.addEnvironmentView(new TestEnvironmentView());
		me.stepUntilDone();

		// loops avoided, now much less number of expanded nodes ...
		Assert.assertEquals(
				"CurrentLocation=In(Arad), Goal=In(Neamt):Action[name==moveTo, location==Sibiu]:Action[name==moveTo, location==RimnicuVilcea]:Action[name==moveTo, location==Pitesti]:Action[name==moveTo, location==Bucharest]:Action[name==moveTo, location==Urziceni]:Action[name==moveTo, location==Vaslui]:Action[name==moveTo, location==Iasi]:Action[name==moveTo, location==Neamt]:METRIC[pathCost]=824.0:METRIC[maxRecursiveDepth]=9:METRIC[nodesExpanded]=1367:Action[name==NoOp]:",
				envChanges.toString());
	}

	private class TestEnvironmentView implements EnvironmentView {
		public void notify(String msg) {
			envChanges.append(msg).append(":");
		}

		public void agentAdded(Agent agent, Environment source) {
			// Nothing.
		}

		public void agentActed(Agent agent, Action action, Environment source) {
			envChanges.append(action).append(":");
		}
	}
}
