package aima.search.framework;

import java.util.Comparator;

/**
 * Artificial Intelligence A Modern Approach (2nd Edition): page 94.
 * 
 * best-first search.
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
		return new Comparator<Node>() {
			public int compare(Node one, Node two) {
				Double f1 = evaluationFunction.getValue(p, one);
				Double f2 = evaluationFunction.getValue(p, two);

				return f1.compareTo(f2);
			}
		};
	}
}
