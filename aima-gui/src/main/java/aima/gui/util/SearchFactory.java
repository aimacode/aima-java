package aima.gui.util;

import aima.core.search.framework.Node;
import aima.core.search.framework.SearchForActions;
import aima.core.search.framework.qsearch.*;
import aima.core.search.informed.AStarSearch;
import aima.core.search.informed.GreedyBestFirstSearch;
import aima.core.search.informed.RecursiveBestFirstSearch;
import aima.core.search.local.HillClimbingSearch;
import aima.core.search.uninformed.BreadthFirstSearch;
import aima.core.search.uninformed.DepthFirstSearch;
import aima.core.search.uninformed.IterativeDeepeningSearch;
import aima.core.search.uninformed.UniformCostSearch;

import java.util.function.ToDoubleFunction;

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
	/** Queue search implementation: graph search with reduced frontier. */
	public final static int GRAPH_SEARCH_RED_FRONTIER = 2;
	/** Queue search implementation: graph search for breadth first search. */
	public final static int GRAPH_SEARCH_BFS = 3;
	/** Queue search implementation: bidirectional search. */
	public final static int BIDIRECTIONAL_SEARCH = 4;

	/** Contains the only existing instance. */
	private static SearchFactory instance;

	/** Invisible constructor. */
	private SearchFactory() {}

	/** Provides access to the factory. Implemented with lazy instantiation. */
	public static SearchFactory getInstance() {
		if (instance == null)
			instance = new SearchFactory();
		return instance;
	}

	/**
	 * Returns the names of all search strategies, which are supported by this
	 * factory. The indices correspond to the parameter values of method
	 * {@link #createSearch(int, int, ToDoubleFunction)}.
	 */
	public String[] getSearchStrategyNames() {
		return new String[] { "Depth First", "Breadth First",
				"Iterative Deepening", "Uniform Cost", "Greedy Best First",
				"A*", "Recursive Best First", "Recursive Best First No Loops", "Hill Climbing" };
	}

	/**
	 * Returns the names of all queue search implementation names, which are supported by this
	 * factory. The indices correspond to the parameter values of method
	 * {@link #createSearch(int, int, ToDoubleFunction)}.
	 */
	public String[] getQSearchImplNames() {
		return new String[] { "Tree Search", "Graph Search", "Graph Search red Fr.",
				"Graph Search BFS", "Bidirectional Search" };
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
	public <S, A> SearchForActions<S, A> createSearch(int strategy, int qSearchImpl, ToDoubleFunction<Node<S, A>> h) {
		QueueSearch<S, A> qs = null;
		SearchForActions<S, A> result = null;
		switch (qSearchImpl) {
		case TREE_SEARCH:
			qs = new TreeSearch<>();
			break;
		case GRAPH_SEARCH:
			qs = new GraphSearch<>();
			break;
		case GRAPH_SEARCH_RED_FRONTIER:
			qs = new GraphSearchReducedFrontier<>();
			break;
		case GRAPH_SEARCH_BFS:
			qs = new GraphSearchBFS<>();
			break;
		case BIDIRECTIONAL_SEARCH:
			qs = new BidirectionalSearch<>();
		}
		switch (strategy) {
		case DF_SEARCH:
			result = new DepthFirstSearch<>(qs);
			break;
		case BF_SEARCH:
			result = new BreadthFirstSearch<>(qs);
			break;
		case ID_SEARCH:
			result = new IterativeDeepeningSearch<>();
			break;
		case UC_SEARCH:
			result = new UniformCostSearch<>(qs);
			break;
		case GBF_SEARCH:
			result = new GreedyBestFirstSearch<>(qs, h);
			break;
		case ASTAR_SEARCH:
			result = new AStarSearch<>(qs, h);
			break;
		case RBF_SEARCH:
			result = new RecursiveBestFirstSearch<>(new AStarSearch.EvalFunction<>(h));
			break;
		case RBF_AL_SEARCH:
			result = new RecursiveBestFirstSearch<>(new AStarSearch.EvalFunction<>(h), true);
			break;
		case HILL_SEARCH:
			result = new HillClimbingSearch<>(h);
			break;
		}
		return result;
	}
}
