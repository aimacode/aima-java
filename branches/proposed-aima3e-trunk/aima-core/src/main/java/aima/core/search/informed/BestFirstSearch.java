package aima.core.search.informed;

import java.util.Comparator;

import aima.core.search.framework.EvaluationFunction;
import aima.core.search.framework.GraphSearch;
import aima.core.search.framework.Node;
import aima.core.search.framework.PrioritySearch;
import aima.core.search.framework.Problem;
import aima.core.search.framework.QueueSearch;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page ??.
 * 
 * Best-first search is an instance of the general TREE-SEARCH or GRAPH-SEARCH algorithm 
 * in which a node is selected for expansion based on an evaluation function, f(n). The 
 * evaluation function is construed as a cost estimate, so the node with the lowest evaluation 
 * is expanded first. The implementation of best-first graph search is identical to that for 
 * uniform-cost search (Figure 3.14), except for the use of f instead of g to order the 
 * priority queue.
 */

/**
 * @author Ciaran O'Reilly
 * 
 */
public class BestFirstSearch extends PrioritySearch {

	private final EvaluationFunction evaluationFunction;

	public BestFirstSearch(QueueSearch search, EvaluationFunction ef) {
		this.search = search;
		evaluationFunction = ef;
	}

	//
	// PROTECTED METHODS
	//
	@Override
	protected Comparator<Node> getComparator(final Problem p) {
		Comparator<Node> f = new Comparator<Node>() {
			public int compare(Node one, Node two) {
				Double f1 = evaluationFunction.getValue(p, one);
				Double f2 = evaluationFunction.getValue(p, two);

				return f1.compareTo(f2);
			}
		};
		
		if (this.search instanceof GraphSearch) {
			((GraphSearch)this.search).setReplaceFrontierNodeAtStateCostFunction(f);
		}
		
		return f;
	}
}
