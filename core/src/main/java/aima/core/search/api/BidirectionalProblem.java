package aima.core.search.api;

/**
 * Created by manthan on 2/3/17.
 */

public interface BidirectionalProblem<A,S> {
    Problem<A,S> getOriginalProblem();

    Problem<A,S> getReverseProblem();
}
