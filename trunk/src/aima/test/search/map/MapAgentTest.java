package aima.test.search.map;

import junit.framework.TestCase;
import aima.basic.BasicEnvironmentView;
import aima.search.framework.GraphSearch;
import aima.search.map.ExtendableMap;
import aima.search.map.MapAgent;
import aima.search.map.MapEnvironment;
import aima.search.uninformed.UniformCostSearch;

/**
 * @author Ciaran O'Reilly
 * 
 */

public class MapAgentTest extends TestCase {

	ExtendableMap aMap;

	StringBuffer envChanges;

	@Override
	public void setUp() {
		aMap = new ExtendableMap();
		aMap.addBidirectionalLink("A", "B", 5.0);
		aMap.addBidirectionalLink("A", "C", 6.0);
		aMap.addBidirectionalLink("B", "C", 4.0);
		aMap.addBidirectionalLink("C", "D", 7.0);
		aMap.addUnidirectionalLink("B", "E", 14.0);

		envChanges = new StringBuffer();
	}

	public void testAlreadyAtGoal() {
		MapEnvironment me = new MapEnvironment(aMap);
		MapAgent ma = new MapAgent(me,
				new UniformCostSearch(new GraphSearch()), new String[] { "A" });
		me.addAgent(ma, "A");
		me.registerView(new BasicEnvironmentView() {
			@Override
			public void envChanged(String command) {
				envChanges.append(command).append(":");
			}
		});
		me.stepUntilNoOp();

		assertEquals(
				"CurrentLocation=In(A), Goal=In(A):NoOP:METRIC[pathCost]=0.0:METRIC[maxQueueSize]=1:METRIC[queueSize]=0:METRIC[nodesExpanded]=0:NoOP:",
				envChanges.toString());
	}

	public void testNormalSearch() {
		MapEnvironment me = new MapEnvironment(aMap);
		MapAgent ma = new MapAgent(me,
				new UniformCostSearch(new GraphSearch()), new String[] { "D" });
		me.addAgent(ma, "A");
		me.registerView(new BasicEnvironmentView() {
			@Override
			public void envChanged(String command) {
				envChanges.append(command).append(":");
			}
		});
		me.stepUntilNoOp();

		assertEquals(
				"CurrentLocation=In(A), Goal=In(D):C:D:METRIC[pathCost]=15.0:METRIC[maxQueueSize]=6:METRIC[queueSize]=1:METRIC[nodesExpanded]=3:NoOP:",
				envChanges.toString());
	}

	public void testNoPath() {
		MapEnvironment me = new MapEnvironment(aMap);
		MapAgent ma = new MapAgent(me,
				new UniformCostSearch(new GraphSearch()), new String[] { "A" });
		me.addAgent(ma, "E");
		me.registerView(new BasicEnvironmentView() {
			@Override
			public void envChanged(String command) {
				envChanges.append(command).append(":");
			}
		});
		me.stepUntilNoOp();

		assertEquals(
				"CurrentLocation=In(E), Goal=In(A):NoOP:METRIC[pathCost]=0:METRIC[maxQueueSize]=1:METRIC[queueSize]=0:METRIC[nodesExpanded]=1:NoOP:",
				envChanges.toString());
	}
}
