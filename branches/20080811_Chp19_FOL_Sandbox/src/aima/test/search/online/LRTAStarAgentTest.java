package aima.test.search.online;

import junit.framework.TestCase;
import aima.basic.BasicEnvironmentView;
import aima.search.framework.HeuristicFunction;
import aima.search.map.BidirectionalMapProblem;
import aima.search.map.Map;
import aima.search.map.MapEnvironment;
import aima.search.online.LRTAStarAgent;

public class LRTAStarAgentTest extends TestCase {
	Map aMap;

	StringBuffer envChanges;

	HeuristicFunction hf;

	@Override
	public void setUp() {
		aMap = new Map(new String[] { "A", "B", "C", "D", "E", "F" });
		aMap.addBidirectionalLink("A", "B", 4);
		aMap.addBidirectionalLink("B", "C", 4);
		aMap.addBidirectionalLink("C", "D", 4);
		aMap.addBidirectionalLink("D", "E", 4);
		aMap.addBidirectionalLink("E", "F", 4);
		hf = new HeuristicFunction() {
			public double getHeuristicValue(Object state) {
				return 1;
			}
		};

		envChanges = new StringBuffer();
	}

	public void testAlreadyAtGoal() {
		MapEnvironment me = new MapEnvironment(aMap);
		LRTAStarAgent agent = new LRTAStarAgent(new BidirectionalMapProblem(me
				.getMap(), "A", "A", hf));
		me.addAgent(agent, "A");
		me.registerView(new BasicEnvironmentView() {
			@Override
			public void envChanged(String command) {
				envChanges.append(command).append("->");
			}
		});
		me.stepUntilNoOp();

		assertEquals("NoOP->", envChanges.toString());
	}

	public void testNormalSearch() {
		MapEnvironment me = new MapEnvironment(aMap);
		LRTAStarAgent agent = new LRTAStarAgent(new BidirectionalMapProblem(me
				.getMap(), "A", "F", hf));
		me.addAgent(agent, "A");
		me.registerView(new BasicEnvironmentView() {
			@Override
			public void envChanged(String command) {
				envChanges.append(command).append("->");
			}
		});
		me.stepUntilNoOp();

		assertEquals("B->A->B->C->B->C->D->C->D->E->D->E->F->NoOP->",
				envChanges.toString());
	}

	public void testNoPath() {
		// Note: Will search forever if no path is possible.
		// MapEnvironment me = new MapEnvironment(aMap);
		// LRTAStarAgent agent = new LRTAStarAgent(new
		// BidirectionalMapProblem(me
		// .getMap(), "A", "G", hf));
		// me.addAgent(agent, "A");
		// me.registerView(new BasicEnvironmentView() {
		// @Override
		// public void envChanged(String command) {
		// envChanges.append(command).append("->");
		// }
		// });
		// me.stepUntilNoOp();
		//
		// assertEquals("B->A->B->C->B->C->D->C->D->E->D->E->F->NoOP->",
		// envChanges.toString());
	}
}
