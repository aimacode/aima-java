package aima.test.search.searches;

import junit.framework.TestCase;
import aima.basic.BasicEnvironmentView;
import aima.search.map.ExtendableMap;
import aima.search.map.MapAgent;
import aima.search.map.MapEnvironment;
import aima.search.uninformed.BidirectionalSearch;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class BidirectionalSearchTest extends TestCase {

	StringBuffer envChanges;

	BidirectionalSearch bidirectionalSearch;

	@Override
	public void setUp() {

		envChanges = new StringBuffer();

		bidirectionalSearch = new BidirectionalSearch();
	}

	//
	// Test IG(A)
	public void test_A_StartingAtGoal() {
		ExtendableMap aMap = new ExtendableMap();

		MapEnvironment me = new MapEnvironment(aMap);
		MapAgent ma = new MapAgent(me, bidirectionalSearch,
				new String[] { "A" });
		me.addAgent(ma, "A");
		me.registerView(new BasicEnvironmentView() {
			@Override
			public void envChanged(String command) {
				envChanges.append(command).append(":");
			}
		});
		me.stepUntilDone();

		assertEquals(
				"CurrentLocation=In(A), Goal=In(A):NoOP:METRIC[pathCost]=0.0:METRIC[maxQueueSize]=2:METRIC[queueSize]=0:METRIC[nodesExpanded]=2:NoOP:",
				envChanges.toString());

		assertEquals(
				BidirectionalSearch.SearchOutcome.PATH_FOUND_BETWEEN_PROBLEMS,
				bidirectionalSearch.getSearchOutcome());
	}

	//
	// Test IG(A)<->(B)<->(C)
	public void test_ABC_StartingAtGoal() {
		ExtendableMap aMap = new ExtendableMap();
		aMap.addBidirectionalLink("A", "B", 5.0);
		aMap.addBidirectionalLink("B", "C", 5.0);

		MapEnvironment me = new MapEnvironment(aMap);
		MapAgent ma = new MapAgent(me, bidirectionalSearch,
				new String[] { "A" });
		me.addAgent(ma, "A");
		me.registerView(new BasicEnvironmentView() {
			@Override
			public void envChanged(String command) {
				envChanges.append(command).append(":");
			}
		});
		me.stepUntilDone();

		assertEquals(
				"CurrentLocation=In(A), Goal=In(A):NoOP:METRIC[pathCost]=0.0:METRIC[maxQueueSize]=2:METRIC[queueSize]=2:METRIC[nodesExpanded]=2:NoOP:",
				envChanges.toString());

		assertEquals(
				BidirectionalSearch.SearchOutcome.PATH_FOUND_BETWEEN_PROBLEMS,
				bidirectionalSearch.getSearchOutcome());
	}

	//
	// Test I(A)<->G(B)
	public void test_AB_BothWaysPath() {
		ExtendableMap aMap = new ExtendableMap();
		aMap.addBidirectionalLink("A", "B", 5.0);

		MapEnvironment me = new MapEnvironment(aMap);
		MapAgent ma = new MapAgent(me, bidirectionalSearch,
				new String[] { "B" });
		me.addAgent(ma, "A");
		me.registerView(new BasicEnvironmentView() {
			@Override
			public void envChanged(String command) {
				envChanges.append(command).append(":");
			}
		});
		me.stepUntilDone();

		assertEquals(
				"CurrentLocation=In(A), Goal=In(B):B:METRIC[pathCost]=5.0:METRIC[maxQueueSize]=2:METRIC[queueSize]=2:METRIC[nodesExpanded]=2:NoOP:",
				envChanges.toString());

		assertEquals(
				BidirectionalSearch.SearchOutcome.PATH_FOUND_BETWEEN_PROBLEMS,
				bidirectionalSearch.getSearchOutcome());
	}

	//
	// Test I(A)<->(B)<->G(C)
	public void test_ABC_BothWaysPath() {
		ExtendableMap aMap = new ExtendableMap();
		aMap.addBidirectionalLink("A", "B", 5.0);
		aMap.addBidirectionalLink("B", "C", 5.0);

		MapEnvironment me = new MapEnvironment(aMap);
		MapAgent ma = new MapAgent(me, bidirectionalSearch,
				new String[] { "C" });
		me.addAgent(ma, "A");
		me.registerView(new BasicEnvironmentView() {
			@Override
			public void envChanged(String command) {
				envChanges.append(command).append(":");
			}
		});
		me.stepUntilDone();

		assertEquals(
				"CurrentLocation=In(A), Goal=In(C):B:C:METRIC[pathCost]=10.0:METRIC[maxQueueSize]=4:METRIC[queueSize]=4:METRIC[nodesExpanded]=4:NoOP:",
				envChanges.toString());

		assertEquals(
				BidirectionalSearch.SearchOutcome.PATH_FOUND_BETWEEN_PROBLEMS,
				bidirectionalSearch.getSearchOutcome());
	}

	//
	// Test I(A)<->(B)<->(C)<->(D)
	public void test_ABCD_BothWaysPath() {
		ExtendableMap aMap = new ExtendableMap();
		aMap.addBidirectionalLink("A", "B", 5.0);
		aMap.addBidirectionalLink("B", "C", 5.0);
		aMap.addBidirectionalLink("C", "D", 5.0);

		MapEnvironment me = new MapEnvironment(aMap);
		MapAgent ma = new MapAgent(me, bidirectionalSearch,
				new String[] { "D" });
		me.addAgent(ma, "A");
		me.registerView(new BasicEnvironmentView() {
			@Override
			public void envChanged(String command) {
				envChanges.append(command).append(":");
			}
		});
		me.stepUntilDone();

		assertEquals(
				"CurrentLocation=In(A), Goal=In(D):B:C:D:METRIC[pathCost]=15.0:METRIC[maxQueueSize]=4:METRIC[queueSize]=4:METRIC[nodesExpanded]=4:NoOP:",
				envChanges.toString());

		assertEquals(
				BidirectionalSearch.SearchOutcome.PATH_FOUND_BETWEEN_PROBLEMS,
				bidirectionalSearch.getSearchOutcome());
	}

	//
	// Test I(A)->G(B)
	public void test_AB_OriginalOnlyPath() {
		ExtendableMap aMap = new ExtendableMap();
		aMap.addUnidirectionalLink("A", "B", 5.0);

		MapEnvironment me = new MapEnvironment(aMap);
		MapAgent ma = new MapAgent(me, bidirectionalSearch,
				new String[] { "B" });
		me.addAgent(ma, "A");
		me.registerView(new BasicEnvironmentView() {
			@Override
			public void envChanged(String command) {
				envChanges.append(command).append(":");
			}
		});
		me.stepUntilDone();

		assertEquals(
				"CurrentLocation=In(A), Goal=In(B):B:METRIC[pathCost]=5.0:METRIC[maxQueueSize]=2:METRIC[queueSize]=1:METRIC[nodesExpanded]=2:NoOP:",
				envChanges.toString());

		assertEquals(
				BidirectionalSearch.SearchOutcome.PATH_FOUND_FROM_ORIGINAL_PROBLEM,
				bidirectionalSearch.getSearchOutcome());
	}

	//
	// Test I(A)->(B)->G(C)
	public void test_ABC_OriginalOnlyPath() {
		ExtendableMap aMap = new ExtendableMap();
		aMap.addUnidirectionalLink("A", "B", 5.0);
		aMap.addUnidirectionalLink("B", "C", 5.0);

		MapEnvironment me = new MapEnvironment(aMap);
		MapAgent ma = new MapAgent(me, bidirectionalSearch,
				new String[] { "C" });
		me.addAgent(ma, "A");
		me.registerView(new BasicEnvironmentView() {
			@Override
			public void envChanged(String command) {
				envChanges.append(command).append(":");
			}
		});
		me.stepUntilDone();

		assertEquals(
				"CurrentLocation=In(A), Goal=In(C):B:C:METRIC[pathCost]=10.0:METRIC[maxQueueSize]=2:METRIC[queueSize]=0:METRIC[nodesExpanded]=4:NoOP:",
				envChanges.toString());

		assertEquals(
				BidirectionalSearch.SearchOutcome.PATH_FOUND_FROM_ORIGINAL_PROBLEM,
				bidirectionalSearch.getSearchOutcome());
	}

	//
	// Test I(A)->(B)->(C)<->(D)<->G(E)
	public void test_ABCDE_OriginalOnlyPath() {
		ExtendableMap aMap = new ExtendableMap();
		aMap.addBidirectionalLink("A", "B", 5.0);
		aMap.addUnidirectionalLink("B", "C", 5.0);
		aMap.addBidirectionalLink("C", "D", 5.0);
		aMap.addBidirectionalLink("D", "E", 5.0);

		MapEnvironment me = new MapEnvironment(aMap);
		MapAgent ma = new MapAgent(me, bidirectionalSearch,
				new String[] { "E" });
		me.addAgent(ma, "A");
		me.registerView(new BasicEnvironmentView() {
			@Override
			public void envChanged(String command) {
				envChanges.append(command).append(":");
			}
		});
		me.stepUntilDone();

		assertEquals(
				"CurrentLocation=In(A), Goal=In(E):B:C:D:E:METRIC[pathCost]=20.0:METRIC[maxQueueSize]=4:METRIC[queueSize]=3:METRIC[nodesExpanded]=5:NoOP:",
				envChanges.toString());

		assertEquals(
				BidirectionalSearch.SearchOutcome.PATH_FOUND_FROM_ORIGINAL_PROBLEM,
				bidirectionalSearch.getSearchOutcome());
	}

	//
	// Test I(A)<-G(B)
	public void test_AB_ReverseOnlyPath() {
		ExtendableMap aMap = new ExtendableMap();
		aMap.addUnidirectionalLink("B", "A", 5.0);

		MapEnvironment me = new MapEnvironment(aMap);
		MapAgent ma = new MapAgent(me, bidirectionalSearch,
				new String[] { "B" });
		me.addAgent(ma, "A");
		me.registerView(new BasicEnvironmentView() {
			@Override
			public void envChanged(String command) {
				envChanges.append(command).append(":");
			}
		});
		me.stepUntilDone();

		assertEquals(
				"CurrentLocation=In(A), Goal=In(B):NoOP:METRIC[pathCost]=0.0:METRIC[maxQueueSize]=2:METRIC[queueSize]=0:METRIC[nodesExpanded]=3:NoOP:",
				envChanges.toString());

		assertEquals(BidirectionalSearch.SearchOutcome.PATH_NOT_FOUND,
				bidirectionalSearch.getSearchOutcome());
	}

	//
	// Test I(A)<-(B)<-G(C)
	public void test_ABC_ReverseOnlyPath() {
		ExtendableMap aMap = new ExtendableMap();
		aMap.addUnidirectionalLink("B", "A", 5.0);
		aMap.addUnidirectionalLink("C", "B", 5.0);

		MapEnvironment me = new MapEnvironment(aMap);
		MapAgent ma = new MapAgent(me, bidirectionalSearch,
				new String[] { "C" });
		me.addAgent(ma, "A");
		me.registerView(new BasicEnvironmentView() {
			@Override
			public void envChanged(String command) {
				envChanges.append(command).append(":");
			}
		});
		me.stepUntilDone();

		assertEquals(
				"CurrentLocation=In(A), Goal=In(C):NoOP:METRIC[pathCost]=0.0:METRIC[maxQueueSize]=2:METRIC[queueSize]=0:METRIC[nodesExpanded]=4:NoOP:",
				envChanges.toString());

		assertEquals(BidirectionalSearch.SearchOutcome.PATH_NOT_FOUND,
				bidirectionalSearch.getSearchOutcome());
	}

	// Test I(A)<->(B)<->(C)<-(D)<-G(E)
	public void test_ABCDE_ReverseOnlyPath() {
		ExtendableMap aMap = new ExtendableMap();
		aMap.addBidirectionalLink("A", "B", 5.0);
		aMap.addBidirectionalLink("B", "C", 5.0);
		aMap.addUnidirectionalLink("D", "C", 5.0);
		aMap.addUnidirectionalLink("E", "D", 5.0);

		MapEnvironment me = new MapEnvironment(aMap);
		MapAgent ma = new MapAgent(me, bidirectionalSearch,
				new String[] { "E" });
		me.addAgent(ma, "A");
		me.registerView(new BasicEnvironmentView() {
			@Override
			public void envChanged(String command) {
				envChanges.append(command).append(":");
			}
		});
		me.stepUntilDone();

		assertEquals(
				"CurrentLocation=In(A), Goal=In(E):NoOP:METRIC[pathCost]=0.0:METRIC[maxQueueSize]=3:METRIC[queueSize]=0:METRIC[nodesExpanded]=8:NoOP:",
				envChanges.toString());

		assertEquals(BidirectionalSearch.SearchOutcome.PATH_NOT_FOUND,
				bidirectionalSearch.getSearchOutcome());
	}

	// Test I(A)<->(B)<->(C)<->(D)<->(E)<->G(F)
	// | +
	// -------------------------
	public void test_ABCDEF_OriginalFirst() {
		ExtendableMap aMap = new ExtendableMap();
		aMap.addBidirectionalLink("A", "B", 5.0);
		aMap.addBidirectionalLink("B", "C", 5.0);
		aMap.addBidirectionalLink("C", "D", 5.0);
		aMap.addBidirectionalLink("D", "E", 5.0);
		aMap.addBidirectionalLink("E", "F", 5.0);
		aMap.addUnidirectionalLink("B", "F", 5.0);

		MapEnvironment me = new MapEnvironment(aMap);
		MapAgent ma = new MapAgent(me, bidirectionalSearch,
				new String[] { "F" });
		me.addAgent(ma, "A");
		me.registerView(new BasicEnvironmentView() {
			@Override
			public void envChanged(String command) {
				envChanges.append(command).append(":");
			}
		});
		me.stepUntilDone();

		assertEquals(
				"CurrentLocation=In(A), Goal=In(F):B:F:METRIC[pathCost]=10.0:METRIC[maxQueueSize]=5:METRIC[queueSize]=5:METRIC[nodesExpanded]=6:NoOP:",
				envChanges.toString());

		assertEquals(
				BidirectionalSearch.SearchOutcome.PATH_FOUND_FROM_ORIGINAL_PROBLEM,
				bidirectionalSearch.getSearchOutcome());
	}

	// Test I(A)<->(B)<->(C)<->(D)<->(E)<->G(F)
	// + |
	// -------------------------
	public void test_ABCDEF_ReverseFirstButNotFromOriginal() {
		ExtendableMap aMap = new ExtendableMap();
		aMap.addBidirectionalLink("A", "B", 5.0);
		aMap.addBidirectionalLink("B", "C", 5.0);
		aMap.addBidirectionalLink("C", "D", 5.0);
		aMap.addBidirectionalLink("D", "E", 5.0);
		aMap.addBidirectionalLink("E", "F", 5.0);
		aMap.addUnidirectionalLink("E", "A", 5.0);

		MapEnvironment me = new MapEnvironment(aMap);
		MapAgent ma = new MapAgent(me, bidirectionalSearch,
				new String[] { "F" });
		me.addAgent(ma, "A");
		me.registerView(new BasicEnvironmentView() {
			@Override
			public void envChanged(String command) {
				envChanges.append(command).append(":");
			}
		});
		me.stepUntilDone();

		assertEquals(
				"CurrentLocation=In(A), Goal=In(F):B:C:D:E:F:METRIC[pathCost]=25.0:METRIC[maxQueueSize]=6:METRIC[queueSize]=6:METRIC[nodesExpanded]=7:NoOP:",
				envChanges.toString());

		assertEquals(
				BidirectionalSearch.SearchOutcome.PATH_FOUND_BETWEEN_PROBLEMS,
				bidirectionalSearch.getSearchOutcome());
	}

	// -------------
	// + +
	// Test I(A)<->(B)<->(C)<->(D)<->(E)<-G(F)
	// + +
	// -------------------------
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
		MapAgent ma = new MapAgent(me, bidirectionalSearch,
				new String[] { "F" });
		me.addAgent(ma, "A");
		me.registerView(new BasicEnvironmentView() {
			@Override
			public void envChanged(String command) {
				envChanges.append(command).append(":");
			}
		});
		me.stepUntilDone();

		assertEquals(
				"CurrentLocation=In(A), Goal=In(F):B:C:D:F:METRIC[pathCost]=20.0:METRIC[maxQueueSize]=9:METRIC[queueSize]=9:METRIC[nodesExpanded]=7:NoOP:",
				envChanges.toString());

		assertEquals(
				BidirectionalSearch.SearchOutcome.PATH_FOUND_BETWEEN_PROBLEMS,
				bidirectionalSearch.getSearchOutcome());
	}
}
