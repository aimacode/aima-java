package aima.core.search.basic.uninformed;

import java.util.List;

import aima.core.search.api.DepthLimitedSearch;
import aima.core.search.api.Problem;
import aima.core.search.api.SearchForActionsFunction;

/**
 * Artificial Intelligence A Modern Approach (4th Edition): Figure ??, page ??.
 * <br>
 * <br>
 *
 * <pre>
 * function ITERATIVE-DEEPENING-SEARCH(problem) returns a solution, or failure
 *   for depth = 0 to infinity  do
 *     result &larr; DEPTH-LIMITED-SEARCH(problem, depth)
 *     if result != cutoff then return result
 * </pre>
 *
 * Figure ?? The iterative deepening search algorithm, which repeatedly applies
 * depth-limited search with increasing limits. It terminates when a solution is
 * found or if the depth-limited search returns failure, meaning that no
 * solution exists.
 *
 * @author Ciaran O'Reilly
 * @author Ravi Mohan
 * @author Ruediger Lunde
 */
public class IterativeDeepeningSearch<A, S> implements SearchForActionsFunction<A, S> {
	// function ITERATIVE-DEEPENING-SEARCH(problem) returns a solution, or
	// failure
	@Override
	public List<A> apply(Problem<A, S> problem) {
		// for depth = 0 to infinity do
		for (int depth = 0; true; depth++) {
			// result <- DEPTH-LIMITED-SEARCH(problem, depth)
			List<A> result = dls.depthLimitedSearch(problem, depth);
			// if result != cutoff then return result
			if (result != dls.getCutoff()) {
				return result;
			}
		}
	}
	
	//
	// Supporting Code

	private DepthLimitedSearch<A, S> dls;

	public IterativeDeepeningSearch() {
		this(new DepthLimitedTreeSearch<>(0));
	}

	public IterativeDeepeningSearch(DepthLimitedSearch<A, S> dls) {
		this.dls = dls;
	}
}