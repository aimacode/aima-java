/*
 * Created on Sep 12, 2004
 *
 */
package aima.search.informed;

import java.util.Comparator;

import aima.search.framework.Metrics;
import aima.search.framework.Node;
import aima.search.framework.PrioritySearch;
import aima.search.framework.Problem;
import aima.search.framework.QueueSearch;

/**
 * @author Ravi Mohan
 * 
 */

public class AStarSearch extends PrioritySearch {

	public AStarSearch(QueueSearch search) {
		this.search = search;
	}

	class NodeComparator implements Comparator {
		private Problem problem;

		NodeComparator(Problem problem) {
			this.problem = problem;
		}

		public int compare(Object aNode, Object anotherNode) {
			Node one = (Node) aNode;
			Node two = (Node) anotherNode;

			int h1 = problem.getHeuristicFunction().getHeuristicValue(
					one.getState());
			double g1 = one.getPathCost();
			int h2 = problem.getHeuristicFunction().getHeuristicValue(
					two.getState());
			double g2 = two.getPathCost();

			double s1 = g1 + h1;
			double s2 = g2 + h2;
			if (s1 == s2) {
				return 0;
			} else if (s1 < s2) {
				return -1;
			} else {
				return 1;//
			}
		}
	}

	public Metrics getMetrics() {
		return search.getMetrics();
	}

	protected Comparator getComparator(Problem p) {

		return new NodeComparator(p);
	}

}