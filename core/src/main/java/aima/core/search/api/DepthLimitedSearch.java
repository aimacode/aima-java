package aima.core.search.api;

import java.util.List;

/**
 * Description of a Depth Limited Search Function.
 * 
 * @param <A>
 *            the type of the actions that can be performed.
 * @param <S>
 *            the type of the state space
 *
 * @author Ciaran O'Reilly
 */
public interface DepthLimitedSearch<A, S> {
	List<A> depthLimitedSearch(Problem<A, S> problem, int limit);

	int getLimit();

	List<A> getCutoff();
}