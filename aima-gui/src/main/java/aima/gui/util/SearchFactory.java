package aima.gui.util;

import aima.core.search.framework.SearchForActions;
import aima.core.search.framework.evalfunc.HeuristicFunction;
import aima.core.search.framework.qsearch.BidirectionalSearch;
import aima.core.search.framework.qsearch.GraphSearch;
import aima.core.search.framework.qsearch.GraphSearchBFS;
import aima.core.search.framework.qsearch.QueueSearch;
import aima.core.search.framework.qsearch.TreeSearch;
import aima.core.search.informed.AStarEvaluationFunction;
import aima.core.search.informed.AStarSearch;
import aima.core.search.informed.GreedyBestFirstSearch;
import aima.core.search.informed.RecursiveBestFirstSearch;
import aima.core.search.local.HillClimbingSearch;
import aima.core.search.uninformed.BreadthFirstSearch;
import aima.core.search.uninformed.DepthFirstSearch;
import aima.core.search.uninformed.IterativeDeepeningSearch;
import aima.core.search.uninformed.UniformCostSearch;

/**
 * Useful factory for configuring search objects. Implemented as a singleton.
 * @author Ruediger Lunde
 */
public class SearchFactory {

	/** Search strategy: Depth first search. */
	public final static int DF_SEARCH = 0;
	/** Search strategy: Depth first search. */
	public final static int BF_SEARCH = 1;
	/** Search strategy: Iterative deepening search. */
	public final static int ID_SEARCH = 2;
	/** Search strategy: Uniform cost search. */
	public final static int UC_SEARCH = 3;
	/** Search strategy: Greedy best first search. */
	public final static int GBF_SEARCH = 4;
	/** Search strategy: A* search. */
	public final static int ASTAR_SEARCH = 5;
	/** Search strategy: Recursive best first search. */
	public final static int RBF_SEARCH = 6;
	/** Search strategy: Recursive best first search avoiding loops. */
	public final static int RBF_AL_SEARCH = 7;
	/** Search strategy: Hill climbing search. */
	public final static int HILL_SEARCH = 8;

	/** Queue search implementation: tree search. */
	public final static int TREE_SEARCH = 0;
	/** Queue search implementation: graph search. */
	public final static int GRAPH_SEARCH = 1;
	/** Queue search implementation: graph search for breadth first search. */
	public final static int GRAPH_SEARCH_BFS = 2;
	/** Queue search implementation: bidirectional search. */
	public final static int BIDIRECTIONAL_SEARCH = 3;

	/** Contains the only existing instance. */
	private static SearchFactory instance;

	/** Invisible constructor. */
	private SearchFactory() {
	};

	/** Provides access to the factory. Implemented with lazy instantiation. */
	public static SearchFactory getInstance() {
		if (instance == null)
			instance = new SearchFactory();
		return instance;
	}

	/**
	 * Returns the names of all search strategies, which are supported by this
	 * factory. The indices correspond to the parameter values of method
	 * {@link #createSearch(int, int, HeuristicFunction)}.
	 */
	public String[] getSearchStrategyNames() {
		return new String[] { "Depth First", "Breadth First",
				"Iterative Deepening", "Uniform Cost", "Greedy Best First",
				"A*", "Recursive Best First", "Recursive Best First No Loops", "Hill Climbing" };
	}

	/**
	 * Returns the names of all queue search implementation names, which are supported by this
	 * factory. The indices correspond to the parameter values of method
	 * {@link #createSearch(int, int, HeuristicFunction)}.
	 */
	public String[] getQSearchImplNames() {
		return new String[] { "Tree Search", "Graph Search", "Graph Search BFS", "Bidirectional Search" };
	}

	/**
	 * Creates a search instance.
	 * 
	 * @param strategy
	 *            search strategy. See static constants.
	 * @param qSearchImpl
	 *            queue search implementation: e.g. {@link #TREE_SEARCH}, {@link #GRAPH_SEARCH}
	 * 
	 */
	public SearchForActions createSearch(int strategy, int qSearchImpl, HeuristicFunction hf) {
		QueueSearch qs = null;
		SearchForActions result = null;
		switch (qSearchImpl) {
		case TREE_SEARCH:
			qs = new TreeSearch();
			break;
		case GRAPH_SEARCH:
			qs = new GraphSearch();
			break;
		case GRAPH_SEARCH_BFS:
			qs = new GraphSearchBFS();
			break;
		case BIDIRECTIONAL_SEARCH:
			qs = new BidirectionalSearch();
		}
		switch (strategy) {
		case DF_SEARCH:
			result = new DepthFirstSearch(qs);
			break;
		case BF_SEARCH:
			result = new BreadthFirstSearch(qs);
			break;
		case ID_SEARCH:
			result = new IterativeDeepeningSearch();
			break;
		case UC_SEARCH:
			result = new UniformCostSearch(qs);
			break;
		case GBF_SEARCH:
			result = new GreedyBestFirstSearch(qs, hf);
			break;
		case ASTAR_SEARCH:
			result = new AStarSearch(qs, hf);
			break;
		case RBF_SEARCH:
			result = new RecursiveBestFirstSearch(new AStarEvaluationFunction(
					hf));
			break;
		case RBF_AL_SEARCH:
			result = new RecursiveBestFirstSearch(new AStarEvaluationFunction(
					hf), true);
			break;
		case HILL_SEARCH:
			result = new HillClimbingSearch(hf);
			break;
		}
		return result;
	}
}
