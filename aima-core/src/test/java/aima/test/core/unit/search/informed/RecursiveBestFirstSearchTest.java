package aima.test.core.unit.search.informed;

import aima.core.agent.*;
import aima.core.environment.map.*;
import aima.core.search.framework.Node;
import aima.core.search.informed.AStarSearch;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import aima.core.search.informed.RecursiveBestFirstSearch;
import aima.core.util.math.geom.shapes.Point2D;

import java.util.function.ToDoubleFunction;

/**
 * @author Ciaran O'Reilly
 * @author Ruediger Lunde
 * 
 */
public class RecursiveBestFirstSearchTest {

	private StringBuffer envChanges;

	private Map aMap;

	private RecursiveBestFirstSearch<String, MoveToAction> recursiveBestFirstSearch;
	private RecursiveBestFirstSearch<String, MoveToAction> recursiveBestFirstSearchAvoidingLoops;

	@Before
	public void setUp() {
		envChanges = new StringBuffer();
		aMap = new SimplifiedRoadMapOfRomania();

		ToDoubleFunction<Node<String, MoveToAction>> heuristicFunction = (node) -> {
				Point2D pt1 = aMap.getPosition((String) node.getState());
				Point2D pt2 = aMap.getPosition(SimplifiedRoadMapOfRomania.BUCHAREST);
				return pt1.distance(pt2);
		};

		recursiveBestFirstSearch = new RecursiveBestFirstSearch<>
				(AStarSearch.createEvalFn(heuristicFunction));
		recursiveBestFirstSearchAvoidingLoops = new RecursiveBestFirstSearch<>
				(AStarSearch.createEvalFn(heuristicFunction), true);
	}

	@Test
	public void testStartingAtGoal() {
		MapEnvironment me = new MapEnvironment(aMap);
		SimpleMapAgent ma = new SimpleMapAgent(me.getMap(), recursiveBestFirstSearch,
				SimplifiedRoadMapOfRomania.BUCHAREST).setNotifier(me);

		me.addAgent(ma, SimplifiedRoadMapOfRomania.BUCHAREST);
		me.addEnvironmentListener(new TestEnvironmentView());
		me.stepUntilDone();

		Assert.assertEquals(
				"CurrentLocation=In(Bucharest), Goal=In(Bucharest):Search{maxRecursiveDepth=0, nodesExpanded=0, pathCost=0.0}:",
				envChanges.toString());
	}

	@Test
	public void testAIMA3eFigure3_27() {
		MapEnvironment me = new MapEnvironment(aMap);
		SimpleMapAgent ma = new SimpleMapAgent(me.getMap(), recursiveBestFirstSearch,
				SimplifiedRoadMapOfRomania.BUCHAREST).setNotifier(me);

		me.addAgent(ma, SimplifiedRoadMapOfRomania.ARAD);
		me.addEnvironmentListener(new TestEnvironmentView());
		me.stepUntilDone();

		Assert.assertEquals(
				"CurrentLocation=In(Arad), Goal=In(Bucharest):Search{maxRecursiveDepth=4, nodesExpanded=6, pathCost=418.0}:Action[name=moveTo, location=Sibiu]:Action[name=moveTo, location=RimnicuVilcea]:Action[name=moveTo, location=Pitesti]:Action[name=moveTo, location=Bucharest]:",
				envChanges.toString());
	}

	@Test
	public void testAIMA3eAradNeamtA() {
		MapEnvironment me = new MapEnvironment(aMap);
		SimpleMapAgent ma = new SimpleMapAgent(me.getMap(), recursiveBestFirstSearch,
				SimplifiedRoadMapOfRomania.NEAMT).setNotifier(me);

		me.addAgent(ma, SimplifiedRoadMapOfRomania.ARAD);
		me.addEnvironmentListener(new TestEnvironmentView());
		me.stepUntilDone();

		Assert.assertEquals(
				"CurrentLocation=In(Arad), Goal=In(Neamt):Search{maxRecursiveDepth=12, nodesExpanded=340208, pathCost=824.0}:Action[name=moveTo, location=Sibiu]:Action[name=moveTo, location=RimnicuVilcea]:Action[name=moveTo, location=Pitesti]:Action[name=moveTo, location=Bucharest]:Action[name=moveTo, location=Urziceni]:Action[name=moveTo, location=Vaslui]:Action[name=moveTo, location=Iasi]:Action[name=moveTo, location=Neamt]:",
				envChanges.toString());
	}

	@Test
	public void testAIMA3eAradNeamtB() {
		MapEnvironment me = new MapEnvironment(aMap);
		SimpleMapAgent ma = new SimpleMapAgent(me.getMap(), recursiveBestFirstSearchAvoidingLoops,
				SimplifiedRoadMapOfRomania.NEAMT).setNotifier(me);

		me.addAgent(ma, SimplifiedRoadMapOfRomania.ARAD);
		me.addEnvironmentListener(new TestEnvironmentView());
		me.stepUntilDone();

		// loops avoided, now much less number of expanded nodes ...
		Assert.assertEquals(
				"CurrentLocation=In(Arad), Goal=In(Neamt):Search{maxRecursiveDepth=9, nodesExpanded=1367, pathCost=824.0}:Action[name=moveTo, location=Sibiu]:Action[name=moveTo, location=RimnicuVilcea]:Action[name=moveTo, location=Pitesti]:Action[name=moveTo, location=Bucharest]:Action[name=moveTo, location=Urziceni]:Action[name=moveTo, location=Vaslui]:Action[name=moveTo, location=Iasi]:Action[name=moveTo, location=Neamt]:",
				envChanges.toString());
	}

	private class TestEnvironmentView implements EnvironmentListener<Object, Object> {
		public void notify(String msg) {
			envChanges.append(msg).append(":");
		}

		public void agentAdded(Agent agent, Environment source) {
			// Nothing
		}

		public void agentActed(Agent agent, Object percept, Object action, Environment source) {
			envChanges.append(action).append(":");
		}
	}
}
