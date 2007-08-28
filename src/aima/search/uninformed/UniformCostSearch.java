package aima.search.uninformed;

import java.util.Comparator;

import aima.search.framework.Node;
import aima.search.framework.PrioritySearch;
import aima.search.framework.Problem;
import aima.search.framework.QueueSearch;

/**
 * Artificial Intelligence A Modern Approach (2nd Edition): page 75.
 * 
 * Uniform-cost search.
 */

/**
 * @author Ciaran O'Reilly
 * 
 */
public class UniformCostSearch extends PrioritySearch {

	public UniformCostSearch(QueueSearch search) {
		this.search = search;
	}

	//
	// PROTECTED METHODS
	//
	@Override
	protected Comparator<Node> getComparator(Problem p) {
		return new Comparator<Node>() {
			public int compare(Node node1, Node node2) {
				return (new Double(node1.getPathCost()).compareTo(new Double(
						node2.getPathCost())));
			}
		};
	}
}