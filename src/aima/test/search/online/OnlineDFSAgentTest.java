package aima.test.search.online;

import junit.framework.TestCase;
import aima.basic.BasicEnvironmentView;
import aima.search.map.BidirectionalMapProblem;
import aima.search.map.Map;
import aima.search.map.MapEnvironment;
import aima.search.online.OnlineDFSAgent;

public class OnlineDFSAgentTest extends TestCase {

	Map aMap;

	StringBuffer envChanges;

	@Override
	public void setUp() {
		aMap = new Map(new String[] { "A", "B", "C", "D", "E", "F", "G" });
		aMap.addBidirectionalLink("A", "B", 5);
		aMap.addBidirectionalLink("A", "C", 6);
		aMap.addBidirectionalLink("B", "D", 4);
		aMap.addBidirectionalLink("B", "E", 7);
		aMap.addBidirectionalLink("D", "F", 4);
		aMap.addBidirectionalLink("D", "G", 8);

		envChanges = new StringBuffer();
	}

	public void testAlreadyAtGoal() {
		MapEnvironment me = new MapEnvironment(aMap);
		OnlineDFSAgent agent = new OnlineDFSAgent(new BidirectionalMapProblem(
				me.getMap(), "A", "A"));
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
		OnlineDFSAgent agent = new OnlineDFSAgent(new BidirectionalMapProblem(
				me.getMap(), "A", "G"));
		me.addAgent(agent, "A");
		me.registerView(new BasicEnvironmentView() {
			@Override
			public void envChanged(String command) {
				envChanges.append(command).append("->");
			}
		});
		me.stepUntilNoOp();

		assertEquals("C->A->B->E->B->D->G->NoOP->", envChanges.toString());
	}

	public void testNoPath() {
		// TODO - The OnlineDFSAgent as it is currently written
		// goes into a never ending loop if there is not goal!
		// Need to fix.
		// aMap = new Map(new String[] { "A", "B" });
		// aMap.addBidirectionalLink("A", "B", 1);
		// MapEnvironment me = new MapEnvironment(aMap);
		// OnlineDFSAgent agent = new OnlineDFSAgent(new
		// BidirectionalMapProblem(
		// me.getMap(), "A", "X"));
		// me.addAgent(agent, "A");
		// me.registerView(new BasicEnvironmentView() {
		// @Override
		// public void envChanged(String command) {
		// envChanges.append(command).append("->");
		// }
		// });
		// me.stepUntilNoOp();
		//
		// assertEquals("B->A->", envChanges.toString());
	}

	public void testFig4_18() {
		aMap = new Map(new String[] { "1,1", "1,2", "1,3", "2,1", "2,2", "2,3",
				"3,1", "3,2", "3,3" });
		aMap.addBidirectionalLink("1,1", "1,2", 1);
		aMap.addBidirectionalLink("1,1", "2,1", 1);
		aMap.addBidirectionalLink("1,2", "1,3", 1);
		aMap.addBidirectionalLink("1,2", "2,2", 1);
		aMap.addBidirectionalLink("1,3", "2,3", 1);
		aMap.addBidirectionalLink("2,2", "3,2", 1);
		aMap.addBidirectionalLink("2,3", "3,3", 1);
		aMap.addBidirectionalLink("3,1", "3,2", 1);

		MapEnvironment me = new MapEnvironment(aMap);
		OnlineDFSAgent agent = new OnlineDFSAgent(new BidirectionalMapProblem(
				me.getMap(), "1,1", "3,3"));
		me.addAgent(agent, "1,1");
		me.registerView(new BasicEnvironmentView() {
			@Override
			public void envChanged(String command) {
				envChanges.append(command).append("->");
			}
		});
		me.stepUntilNoOp();

		assertEquals(
				"2,1->1,1->1,2->2,2->3,2->3,1->3,2->2,2->1,2->1,3->2,3->3,3->NoOP->",
				envChanges.toString());
	}
}
