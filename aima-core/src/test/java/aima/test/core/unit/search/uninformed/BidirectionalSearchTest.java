package aima.test.core.unit.search.uninformed;

import aima.core.agent.*;
import aima.core.environment.map.MoveToAction;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import aima.core.environment.map.ExtendableMap;
import aima.core.environment.map.MapEnvironment;
import aima.core.environment.map.SimpleMapAgent;
import aima.core.search.framework.SearchForActions;
import aima.core.search.framework.qsearch.BidirectionalSearch;
import aima.core.search.uninformed.BreadthFirstSearch;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class BidirectionalSearchTest {

	private StringBuffer envChanges;
	private SearchForActions<String, MoveToAction> search;

	@Before
	public void setUp() {

		envChanges = new StringBuffer();

		BidirectionalSearch<String, MoveToAction> bidirectionalSearch = new BidirectionalSearch<>();
		search = new BreadthFirstSearch<>(bidirectionalSearch);
	}

	//
	// Test IG(A)
	@Test
	public void test_A_StartingAtGoal() {
		ExtendableMap aMap = new ExtendableMap();

		MapEnvironment me = new MapEnvironment(aMap);
		SimpleMapAgent ma = new SimpleMapAgent(me.getMap(), search, "A").setNotifier(me);
		me.addAgent(ma, "A");
		me.addEnvironmentListener(new TestEnvironmentView());
		me.stepUntilDone();

		Assert.assertEquals(
				"CurrentLocation=In(A), Goal=In(A):Search{maxQueueSize=0, nodesExpanded=0, pathCost=0.0, queueSize=0}:",
				envChanges.toString());
	}

	//
	// Test IG(A)<->(B)<->(C)
	@Test
	public void test_ABC_StartingAtGoal() {
		ExtendableMap aMap = new ExtendableMap();
		aMap.addBidirectionalLink("A", "B", 5.0);
		aMap.addBidirectionalLink("B", "C", 5.0);

		MapEnvironment me = new MapEnvironment(aMap);
		SimpleMapAgent ma = new SimpleMapAgent(me.getMap(), search, "A").setNotifier(me);
		me.addAgent(ma, "A");
		me.addEnvironmentListener(new TestEnvironmentView());
		me.stepUntilDone();

		Assert.assertEquals(
				"CurrentLocation=In(A), Goal=In(A):Search{maxQueueSize=0, nodesExpanded=0, pathCost=0.0, queueSize=0}:",
				envChanges.toString());
	}

	//
	// Test I(A)<->G(B)
	@Test
	public void test_AB_BothWaysPath() {
		ExtendableMap aMap = new ExtendableMap();
		aMap.addBidirectionalLink("A", "B", 5.0);

		MapEnvironment me = new MapEnvironment(aMap);
		SimpleMapAgent ma = new SimpleMapAgent(me.getMap(), search, "B").setNotifier(me);
		me.addAgent(ma, "A");
		me.addEnvironmentListener(new TestEnvironmentView());
		me.stepUntilDone();

		Assert.assertEquals(
				"CurrentLocation=In(A), Goal=In(B):Search{maxQueueSize=2, nodesExpanded=1, pathCost=5.0, queueSize=1}:Action[name=moveTo, location=B]:",
				envChanges.toString());
	}

	//
	// Test I(A)<->(B)<->G(C)
	@Test
	public void test_ABC_BothWaysPath() {
		ExtendableMap aMap = new ExtendableMap();
		aMap.addBidirectionalLink("A", "B", 5.0);
		aMap.addBidirectionalLink("B", "C", 5.0);

		MapEnvironment me = new MapEnvironment(aMap);
		SimpleMapAgent ma = new SimpleMapAgent(me.getMap(), search, "C").setNotifier(me);
		me.addAgent(ma, "A");
		me.addEnvironmentListener(new TestEnvironmentView());
		me.stepUntilDone();

		Assert.assertEquals(
				"CurrentLocation=In(A), Goal=In(C):Search{maxQueueSize=2, nodesExpanded=3, pathCost=10.0, queueSize=1}:Action[name=moveTo, location=B]:Action[name=moveTo, location=C]:",
				envChanges.toString());
	}

	//
	// Test I(A)<->(B)<->(C)<->G(D)
	@Test
	public void test_ABCD_BothWaysPath() {
		ExtendableMap aMap = new ExtendableMap();
		aMap.addBidirectionalLink("A", "B", 5.0);
		aMap.addBidirectionalLink("B", "C", 5.0);
		aMap.addBidirectionalLink("C", "D", 5.0);

		MapEnvironment me = new MapEnvironment(aMap);
		SimpleMapAgent ma = new SimpleMapAgent(me.getMap(), search, "D").setNotifier(me);
		me.addAgent(ma, "A");
		me.addEnvironmentListener(new TestEnvironmentView());
		me.stepUntilDone();

		Assert.assertEquals(
				"CurrentLocation=In(A), Goal=In(D):Search{maxQueueSize=2, nodesExpanded=4, pathCost=15.0, queueSize=1}:Action[name=moveTo, location=B]:Action[name=moveTo, location=C]:Action[name=moveTo, location=D]:",
				envChanges.toString());
	}

	//
	// Test I(A)->G(B)
	@Test
	public void test_AB_OriginalOnlyPath() {
		ExtendableMap aMap = new ExtendableMap();
		aMap.addUnidirectionalLink("A", "B", 5.0);

		MapEnvironment me = new MapEnvironment(aMap);
		SimpleMapAgent ma = new SimpleMapAgent(me.getMap(), search, "B").setNotifier(me);
		me.addAgent(ma, "A");
		me.addEnvironmentListener(new TestEnvironmentView());
		me.stepUntilDone();

		Assert.assertEquals(
				"CurrentLocation=In(A), Goal=In(B):Search{maxQueueSize=2, nodesExpanded=1, pathCost=5.0, queueSize=1}:Action[name=moveTo, location=B]:",
				envChanges.toString());
	}

	//
	// Test I(A)->(B)->G(C)
	@Test
	public void test_ABC_OriginalOnlyPath() {
		ExtendableMap aMap = new ExtendableMap();
		aMap.addUnidirectionalLink("A", "B", 5.0);
		aMap.addUnidirectionalLink("B", "C", 5.0);

		MapEnvironment me = new MapEnvironment(aMap);
		SimpleMapAgent ma = new SimpleMapAgent(me.getMap(), search, "C").setNotifier(me);
		me.addAgent(ma, "A");
		me.addEnvironmentListener(new TestEnvironmentView());
		me.stepUntilDone();

		Assert.assertEquals(
				"CurrentLocation=In(A), Goal=In(C):Search{maxQueueSize=2, nodesExpanded=3, pathCost=10.0, queueSize=0}:Action[name=moveTo, location=B]:Action[name=moveTo, location=C]:",
				envChanges.toString());

	}

	//
	// Test I(A)->(B)->(C)<->(D)<->G(E)
	@Test
	public void test_ABCDE_OriginalOnlyPath() {
		ExtendableMap aMap = new ExtendableMap();
		aMap.addBidirectionalLink("A", "B", 5.0);
		aMap.addUnidirectionalLink("B", "C", 5.0);
		aMap.addBidirectionalLink("C", "D", 5.0);
		aMap.addBidirectionalLink("D", "E", 5.0);

		MapEnvironment me = new MapEnvironment(aMap);
		SimpleMapAgent ma = new SimpleMapAgent(me.getMap(), search, "E").setNotifier(me);
		me.addAgent(ma, "A");
		me.addEnvironmentListener(new TestEnvironmentView());
		me.stepUntilDone();

		Assert.assertEquals(
				"CurrentLocation=In(A), Goal=In(E):Search{maxQueueSize=2, nodesExpanded=5, pathCost=20.0, queueSize=1}:Action[name=moveTo, location=B]:Action[name=moveTo, location=C]:Action[name=moveTo, location=D]:Action[name=moveTo, location=E]:",
				envChanges.toString());
	}

	//
	// Test I(A)<-G(B)
	@Test
	public void test_AB_ReverseOnlyPath() {
		ExtendableMap aMap = new ExtendableMap();
		aMap.addUnidirectionalLink("B", "A", 5.0);

		MapEnvironment me = new MapEnvironment(aMap);
		SimpleMapAgent ma = new SimpleMapAgent(me.getMap(), search, "B").setNotifier(me);
		me.addAgent(ma, "A");
		me.addEnvironmentListener(new TestEnvironmentView());
		me.stepUntilDone();

		Assert.assertEquals(
				"CurrentLocation=In(A), Goal=In(B):Search{maxQueueSize=2, nodesExpanded=2, pathCost=0, queueSize=0}:",
				envChanges.toString());
	}

	//
	// Test I(A)<-(B)<-G(C)
	@Test
	public void test_ABC_ReverseOnlyPath() {
		ExtendableMap aMap = new ExtendableMap();
		aMap.addUnidirectionalLink("B", "A", 5.0);
		aMap.addUnidirectionalLink("C", "B", 5.0);

		MapEnvironment me = new MapEnvironment(aMap);
		SimpleMapAgent ma = new SimpleMapAgent(me.getMap(), search, "C").setNotifier(me);
		me.addAgent(ma, "A");
		me.addEnvironmentListener(new TestEnvironmentView());
		me.stepUntilDone();

		Assert.assertEquals(
				"CurrentLocation=In(A), Goal=In(C):Search{maxQueueSize=2, nodesExpanded=2, pathCost=0, queueSize=0}:",
				envChanges.toString());
	}

	// Test I(A)<->(B)<->(C)<-(D)<-G(E)
	@Test
	public void test_ABCDE_ReverseOnlyPath() {
		ExtendableMap aMap = new ExtendableMap();
		aMap.addBidirectionalLink("A", "B", 5.0);
		aMap.addBidirectionalLink("B", "C", 5.0);
		aMap.addUnidirectionalLink("D", "C", 5.0);
		aMap.addUnidirectionalLink("E", "D", 5.0);

		MapEnvironment me = new MapEnvironment(aMap);
		SimpleMapAgent ma = new SimpleMapAgent(me.getMap(), search, "E").setNotifier(me);
		me.addAgent(ma, "A");
		me.addEnvironmentListener(new TestEnvironmentView());
		me.stepUntilDone();

		Assert.assertEquals(
				"CurrentLocation=In(A), Goal=In(E):Search{maxQueueSize=2, nodesExpanded=4, pathCost=0, queueSize=0}:",
				envChanges.toString());
	}

	/**
	 * <code>
	 * Test I(A)<->(B)<->(C)<->(D)<->(E)<->(F)<->(G)<->G(H)
	 *              |                                    +
	 *              --------------------------------------
	 * </code>
	 */
	@Test
	public void test_ABCDEF_OriginalFirst() {
		ExtendableMap aMap = new ExtendableMap();
		aMap.addBidirectionalLink("A", "B", 5.0);
		aMap.addBidirectionalLink("B", "C", 5.0);
		aMap.addBidirectionalLink("C", "D", 5.0);
		aMap.addBidirectionalLink("D", "E", 5.0);
		aMap.addBidirectionalLink("E", "F", 5.0);
		aMap.addBidirectionalLink("F", "G", 5.0);
		aMap.addBidirectionalLink("G", "H", 5.0);
		aMap.addUnidirectionalLink("B", "H", 5.0);

		MapEnvironment me = new MapEnvironment(aMap);
		SimpleMapAgent ma = new SimpleMapAgent(me.getMap(), search, "H").setNotifier(me);
		me.addAgent(ma, "A");
		me.addEnvironmentListener(new TestEnvironmentView());
		me.stepUntilDone();

		Assert.assertEquals(
				"CurrentLocation=In(A), Goal=In(H):Search{maxQueueSize=2, nodesExpanded=3, pathCost=10.0, queueSize=2}:Action[name=moveTo, location=B]:Action[name=moveTo, location=H]:",
				envChanges.toString());
	}

	/**
	 * <code>
	 * Test I(A)<->(B)<->(C)<->(D)<->(E)<->G(F)
	 *        +                       |
	 *        -------------------------
	 * </code>
	 */
	@Test
	public void test_ABCDEF_ReverseFirstButNotFromOriginal() {
		ExtendableMap aMap = new ExtendableMap();
		aMap.addBidirectionalLink("A", "B", 5.0);
		aMap.addBidirectionalLink("B", "C", 5.0);
		aMap.addBidirectionalLink("C", "D", 5.0);
		aMap.addBidirectionalLink("D", "E", 5.0);
		aMap.addBidirectionalLink("E", "F", 5.0);
		aMap.addUnidirectionalLink("E", "A", 5.0);

		MapEnvironment me = new MapEnvironment(aMap);
		SimpleMapAgent ma = new SimpleMapAgent(me.getMap(), search, "F").setNotifier(me);
		me.addAgent(ma, "A");
		me.addEnvironmentListener(new TestEnvironmentView());
		me.stepUntilDone();

		Assert.assertEquals(
				"CurrentLocation=In(A), Goal=In(F):Search{maxQueueSize=2, nodesExpanded=6, pathCost=25.0, queueSize=1}:Action[name=moveTo, location=B]:Action[name=moveTo, location=C]:Action[name=moveTo, location=D]:Action[name=moveTo, location=E]:Action[name=moveTo, location=F]:",
				envChanges.toString());
	}

	/**
	 * <code>
	 *                          -------------
	 *                          +           +
	 * Test I(A)<->(B)<->(C)<->(D)<->(E)<-G(F)
	 *        +                       +
	 *        -------------------------
	 * </code>
	 */
	@Test
	public void test_ABCDEF_MoreComplexReverseFirstButNotFromOriginal() {
		ExtendableMap aMap = new ExtendableMap();
		aMap.addBidirectionalLink("A", "B", 5.0);
		aMap.addBidirectionalLink("B", "C", 5.0);
		aMap.addBidirectionalLink("C", "D", 5.0);
		aMap.addBidirectionalLink("D", "E", 5.0);
		aMap.addUnidirectionalLink("F", "E", 5.0);
		aMap.addBidirectionalLink("E", "A", 5.0);
		aMap.addBidirectionalLink("D", "F", 5.0);

		MapEnvironment me = new MapEnvironment(aMap);
		SimpleMapAgent ma = new SimpleMapAgent(me.getMap(), search, "F").setNotifier(me);
		me.addAgent(ma, "A");
		me.addEnvironmentListener(new TestEnvironmentView());
		me.stepUntilDone();

		Assert.assertEquals(
				"CurrentLocation=In(A), Goal=In(F):Search{maxQueueSize=3, nodesExpanded=5, pathCost=15.0, queueSize=3}:Action[name=moveTo, location=E]:Action[name=moveTo, location=D]:Action[name=moveTo, location=F]:",
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
