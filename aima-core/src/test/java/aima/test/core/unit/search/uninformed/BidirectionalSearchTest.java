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
		SimpleMapAgent ma = new SimpleMapAgent(me.getMap(), me, search, new String[] { "A" });
		me.addAgent(ma, "A");
		me.addEnvironmentView(new BDSEnvironmentView());
		me.stepUntilDone();

		Assert.assertEquals(
				"CurrentLocation=In(A), Goal=In(A):Action[name=NoOp]:METRIC[pathCost]=0.0:METRIC[maxQueueSize]=0:METRIC[queueSize]=0:METRIC[nodesExpanded]=0:Action[name=NoOp]:",
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
		SimpleMapAgent ma = new SimpleMapAgent(me.getMap(), me, search, new String[] { "A" });
		me.addAgent(ma, "A");
		me.addEnvironmentView(new BDSEnvironmentView());
		me.stepUntilDone();

		Assert.assertEquals(
				"CurrentLocation=In(A), Goal=In(A):Action[name=NoOp]:METRIC[pathCost]=0.0:METRIC[maxQueueSize]=0:METRIC[queueSize]=0:METRIC[nodesExpanded]=0:Action[name=NoOp]:",
				envChanges.toString());
	}

	//
	// Test I(A)<->G(B)
	@Test
	public void test_AB_BothWaysPath() {
		ExtendableMap aMap = new ExtendableMap();
		aMap.addBidirectionalLink("A", "B", 5.0);

		MapEnvironment me = new MapEnvironment(aMap);
		SimpleMapAgent ma = new SimpleMapAgent(me.getMap(), me, search, new String[] { "B" });
		me.addAgent(ma, "A");
		me.addEnvironmentView(new BDSEnvironmentView());
		me.stepUntilDone();

		Assert.assertEquals(
				"CurrentLocation=In(A), Goal=In(B):Action[name=moveTo, location=B]:METRIC[pathCost]=5.0:METRIC[maxQueueSize]=2:METRIC[queueSize]=1:METRIC[nodesExpanded]=1:Action[name=NoOp]:",
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
		SimpleMapAgent ma = new SimpleMapAgent(me.getMap(), me, search, new String[] { "C" });
		me.addAgent(ma, "A");
		me.addEnvironmentView(new BDSEnvironmentView());
		me.stepUntilDone();

		Assert.assertEquals(
				"CurrentLocation=In(A), Goal=In(C):Action[name=moveTo, location=B]:Action[name=moveTo, location=C]:METRIC[pathCost]=10.0:METRIC[maxQueueSize]=2:METRIC[queueSize]=1:METRIC[nodesExpanded]=3:Action[name=NoOp]:",
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
		SimpleMapAgent ma = new SimpleMapAgent(me.getMap(), me, search, new String[] { "D" });
		me.addAgent(ma, "A");
		me.addEnvironmentView(new BDSEnvironmentView());
		me.stepUntilDone();

		Assert.assertEquals(
				"CurrentLocation=In(A), Goal=In(D):Action[name=moveTo, location=B]:Action[name=moveTo, location=C]:Action[name=moveTo, location=D]:METRIC[pathCost]=15.0:METRIC[maxQueueSize]=2:METRIC[queueSize]=1:METRIC[nodesExpanded]=4:Action[name=NoOp]:",
				envChanges.toString());
	}

	//
	// Test I(A)->G(B)
	@Test
	public void test_AB_OriginalOnlyPath() {
		ExtendableMap aMap = new ExtendableMap();
		aMap.addUnidirectionalLink("A", "B", 5.0);

		MapEnvironment me = new MapEnvironment(aMap);
		SimpleMapAgent ma = new SimpleMapAgent(me.getMap(), me, search, new String[] { "B" });
		me.addAgent(ma, "A");
		me.addEnvironmentView(new BDSEnvironmentView());
		me.stepUntilDone();

		Assert.assertEquals(
				"CurrentLocation=In(A), Goal=In(B):Action[name=moveTo, location=B]:METRIC[pathCost]=5.0:METRIC[maxQueueSize]=2:METRIC[queueSize]=1:METRIC[nodesExpanded]=1:Action[name=NoOp]:",
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
		SimpleMapAgent ma = new SimpleMapAgent(me.getMap(), me, search, new String[] { "C" });
		me.addAgent(ma, "A");
		me.addEnvironmentView(new BDSEnvironmentView());
		me.stepUntilDone();

		Assert.assertEquals(
				"CurrentLocation=In(A), Goal=In(C):Action[name=moveTo, location=B]:Action[name=moveTo, location=C]:METRIC[pathCost]=10.0:METRIC[maxQueueSize]=2:METRIC[queueSize]=0:METRIC[nodesExpanded]=3:Action[name=NoOp]:",
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
		SimpleMapAgent ma = new SimpleMapAgent(me.getMap(), me, search, new String[] { "E" });
		me.addAgent(ma, "A");
		me.addEnvironmentView(new BDSEnvironmentView());
		me.stepUntilDone();

		Assert.assertEquals(
				"CurrentLocation=In(A), Goal=In(E):Action[name=moveTo, location=B]:Action[name=moveTo, location=C]:Action[name=moveTo, location=D]:Action[name=moveTo, location=E]:METRIC[pathCost]=20.0:METRIC[maxQueueSize]=2:METRIC[queueSize]=1:METRIC[nodesExpanded]=5:Action[name=NoOp]:",
				envChanges.toString());
	}

	//
	// Test I(A)<-G(B)
	@Test
	public void test_AB_ReverseOnlyPath() {
		ExtendableMap aMap = new ExtendableMap();
		aMap.addUnidirectionalLink("B", "A", 5.0);

		MapEnvironment me = new MapEnvironment(aMap);
		SimpleMapAgent ma = new SimpleMapAgent(me.getMap(), me, search, new String[] { "B" });
		me.addAgent(ma, "A");
		me.addEnvironmentView(new BDSEnvironmentView());
		me.stepUntilDone();

		Assert.assertEquals(
				"CurrentLocation=In(A), Goal=In(B):Action[name=NoOp]:METRIC[pathCost]=0:METRIC[maxQueueSize]=2:METRIC[queueSize]=0:METRIC[nodesExpanded]=2:Action[name=NoOp]:",
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
		SimpleMapAgent ma = new SimpleMapAgent(me.getMap(), me, search, new String[] { "C" });
		me.addAgent(ma, "A");
		me.addEnvironmentView(new BDSEnvironmentView());
		me.stepUntilDone();

		Assert.assertEquals(
				"CurrentLocation=In(A), Goal=In(C):Action[name=NoOp]:METRIC[pathCost]=0:METRIC[maxQueueSize]=2:METRIC[queueSize]=0:METRIC[nodesExpanded]=2:Action[name=NoOp]:",
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
		SimpleMapAgent ma = new SimpleMapAgent(me.getMap(), me, search, new String[] { "E" });
		me.addAgent(ma, "A");
		me.addEnvironmentView(new BDSEnvironmentView());
		me.stepUntilDone();

		Assert.assertEquals(
				"CurrentLocation=In(A), Goal=In(E):Action[name=NoOp]:METRIC[pathCost]=0:METRIC[maxQueueSize]=2:METRIC[queueSize]=0:METRIC[nodesExpanded]=4:Action[name=NoOp]:",
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
		SimpleMapAgent ma = new SimpleMapAgent(me.getMap(), me, search, new String[] { "H" });
		me.addAgent(ma, "A");
		me.addEnvironmentView(new BDSEnvironmentView());
		me.stepUntilDone();

		Assert.assertEquals(
				"CurrentLocation=In(A), Goal=In(H):Action[name=moveTo, location=B]:Action[name=moveTo, location=H]:METRIC[pathCost]=10.0:METRIC[maxQueueSize]=2:METRIC[queueSize]=2:METRIC[nodesExpanded]=3:Action[name=NoOp]:",
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
		SimpleMapAgent ma = new SimpleMapAgent(me.getMap(), me, search, new String[] { "F" });
		me.addAgent(ma, "A");
		me.addEnvironmentView(new BDSEnvironmentView());
		me.stepUntilDone();

		Assert.assertEquals(
				"CurrentLocation=In(A), Goal=In(F):Action[name=moveTo, location=B]:Action[name=moveTo, location=C]:Action[name=moveTo, location=D]:Action[name=moveTo, location=E]:Action[name=moveTo, location=F]:METRIC[pathCost]=25.0:METRIC[maxQueueSize]=2:METRIC[queueSize]=1:METRIC[nodesExpanded]=6:Action[name=NoOp]:",
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
		SimpleMapAgent ma = new SimpleMapAgent(me.getMap(), me, search, new String[] { "F" });
		me.addAgent(ma, "A");
		me.addEnvironmentView(new BDSEnvironmentView());
		me.stepUntilDone();

		Assert.assertEquals(
				"CurrentLocation=In(A), Goal=In(F):Action[name=moveTo, location=E]:Action[name=moveTo, location=D]:Action[name=moveTo, location=F]:METRIC[pathCost]=15.0:METRIC[maxQueueSize]=3:METRIC[queueSize]=3:METRIC[nodesExpanded]=5:Action[name=NoOp]:",
				envChanges.toString());
	}

	private class BDSEnvironmentView implements EnvironmentView {
		public void notify(String msg) {
			envChanges.append(msg).append(":");
		}

		public void agentAdded(Agent agent, Environment source) {
			// Nothing.
		}

		public void agentActed(Agent agent, Percept percept, Action action, Environment source) {
			envChanges.append(action).append(":");
		}
	}
}
